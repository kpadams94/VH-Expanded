// Static handoff validation. Does not install recipes or execute manifest notes.
const fs = require('node:fs');
const path = require('node:path');
const crypto = require('node:crypto');
const root = path.resolve(__dirname, '../..');
const source = process.argv[2];
if (!source) throw new Error('Provide the recipe export path');
const bytes = fs.readFileSync(source);
const manifest = JSON.parse(bytes);
const read = p => JSON.parse(fs.readFileSync(path.join(root, p), 'utf8'));
const catalogs = ['bhc', 'vault', 'minecraft'].flatMap(n => read(`recipe-tweaker/dist/data/${n}-items.json`));
const known = new Map(catalogs.map(i => [i.id, i]));
const originals = read('recipe-tweaker/dist/data/bhc-recipes.json');
const originalIds = new Set(originals.map(r => r.id));
const errors = [], warnings = [], rows = [], ids = new Set(), usedItems = new Set(), usedTags = new Set();
const check = (ok, message) => { if (!ok) errors.push(message); };
const resource = /^[a-z0-9_.-]+:[a-z0-9_./-]+$/;
check(manifest.schema === 'vault-hunters-expanded/recipe-tweaker' && manifest.schemaVersion === 1, 'Unsupported manifest schema');
check(manifest.minecraftVersion === '1.18.2', 'Wrong Minecraft version');
check(Array.isArray(manifest.recipes), 'Missing recipe array');
function ingredient(i, id) {
  check(i && typeof i === 'object' && (Boolean(i.item) !== Boolean(i.tag)), `${id}: ingredient must name exactly one item or tag`);
  if (i?.item) { usedItems.add(i.item); check(resource.test(i.item) && known.has(i.item), `${id}: unknown item ${i.item}`); }
  if (i?.tag) { usedTags.add(i.tag); check(resource.test(i.tag), `${id}: invalid tag ${i.tag}`); }
}
for (const entry of manifest.recipes || []) {
  const {id, recipe: r} = entry;
  check(resource.test(id) && !id.split(':')[1]?.split('/').some(p => p === '.' || p === '..'), `Invalid ID ${id}`);
  check(!ids.has(id), `Duplicate ID ${id}`); ids.add(id);
  const [namespace, name] = id.split(':');
  const expected = `data/${namespace}/recipes/${name}.json`;
  check(entry.path === expected, `${id}: expected path ${expected}`);
  check(['add', 'replace'].includes(entry.action), `${id}: unsupported action`);
  check(entry.action === 'replace' ? originalIds.has(entry.sourceRecipeId) && entry.sourceRecipeId === id : !originalIds.has(id) && entry.sourceRecipeId === null, `${id}: action does not match original recipe inventory`);
  check(r?.result?.item === entry.outputItemId && known.has(entry.outputItemId), `${id}: invalid output`);
  check(entry.itemRegistrationRequired === false, `${id}: unexpected item registration`);
  check(Number.isInteger(r?.result?.count) && r.result.count >= 1 && r.result.count <= 64, `${id}: invalid output count`);
  usedItems.add(entry.outputItemId);
  let inputs = [];
  if (r?.type === 'minecraft:crafting_shaped') {
    check(Array.isArray(r.pattern) && r.pattern.length >= 1 && r.pattern.length <= 3, `${id}: invalid pattern height`);
    check(r.pattern.every(p => typeof p === 'string' && p.length >= 1 && p.length <= 3 && p.length === r.pattern[0].length), `${id}: invalid pattern width`);
    const symbols = new Set(r.pattern.join('').replaceAll(' ', ''));
    check(symbols.size > 0 && [...symbols].every(s => r.key[s]) && Object.keys(r.key).every(s => symbols.has(s) && s.length === 1), `${id}: inconsistent pattern keys`);
    inputs = r.pattern.join('').split('').filter(s => s !== ' ').map(s => r.key[s]);
    const grid = r.pattern.flatMap(row => row.split('').map(s => s === ' ' ? null : r.key[s]));
    check(JSON.stringify(grid) === JSON.stringify(entry.editor.slots), `${id}: editor grid differs from recipe`);
  } else if (r?.type === 'minecraft:crafting_shapeless') {
    inputs = r.ingredients || [];
    check(inputs.length >= 1 && inputs.length <= 9, `${id}: invalid shapeless input count`);
    check(JSON.stringify(inputs) === JSON.stringify(entry.editor.slots.filter(Boolean)), `${id}: editor inputs differ from recipe`);
  } else errors.push(`${id}: unsupported recipe type`);
  inputs.forEach(i => ingredient(i, id));
  check(entry.editor.output === r.result.item && entry.editor.count === r.result.count, `${id}: editor output differs`);
  const counts = new Map();
  for (const i of inputs) { const key = i.item || `#${i.tag}`; counts.set(key, (counts.get(key) || 0) + 1); }
  rows.push({id, action: entry.action, path: expected, output: r.result, ingredients: Object.fromEntries(counts), pattern: r.pattern || null});
}
const missingReplacements = [...originalIds].filter(id => !ids.has(id));
check(missingReplacements.length === 0, `Unreplaced native recipes: ${missingReplacements.join(', ')}`);
const produced = new Set(rows.map(r => r.output.item));
const bhcInputs = [...usedItems].filter(id => id.startsWith('bhc:') && !produced.has(id));
check(bhcInputs.length === 0, `Uncraftable BHC intermediates: ${bhcInputs.join(', ')}`);
const sources = read('recipe-tweaker/dist/data/pack-sources.json');
const relevantSources = sources.filter(s => /bhc:|enchanted_golden_apple|bhc["']/.test(s.content)).map(s => s.path);
warnings.push('Static catalogs corroborate IDs; runtime registries, tag membership, research gating and final recipe-manager load order still require an in-game check.');
const report = {sourceFilename: path.basename(source), sha256: crypto.createHash('sha256').update(bytes).digest('hex'), recipeCount: rows.length, replacements: rows.filter(r => r.action === 'replace').length, additions: rows.filter(r => r.action === 'add').length, errors, warnings, usedItems: [...usedItems].sort(), usedTags: [...usedTags].sort(), relevantPackSources: relevantSources, rows};
fs.writeFileSync(path.join(__dirname, 'heart-canisters-validation.json'), JSON.stringify(report, null, 2) + '\n');
console.log(JSON.stringify({recipeCount: rows.length, replacements: report.replacements, additions: report.additions, errors, usedTags: report.usedTags, relevantPackSources: relevantSources, sha256: report.sha256}, null, 2));
if (errors.length) process.exitCode = 1;
