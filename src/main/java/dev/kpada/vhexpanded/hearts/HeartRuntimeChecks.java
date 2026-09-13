package dev.kpada.vhexpanded.hearts;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.traverse.bhc.common.BaubleyHeartCanisters;
import com.traverse.bhc.common.config.ConfigHandler;
import com.traverse.bhc.common.init.RegistryHandler;
import com.traverse.bhc.common.util.HealthModifier;
import dev.kpada.vhexpanded.Expanded;
import io.netty.buffer.Unpooled;
import iskallia.vault.config.Config;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.research.Restrictions;
import iskallia.vault.world.data.PlayerResearchesData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.util.FakePlayer;
import top.theillusivec4.curios.api.CuriosApi;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Explicit disposable-world checks using loaded pack classes, with no real player's data changed. */
public final class HeartRuntimeChecks {
    private static int count;
    private static final List<String> failures = new ArrayList<>();
    private HeartRuntimeChecks() {}
    private static void check(boolean value, String label) {
        if (!value) {
            failures.add(label);
            Expanded.LOGGER.error("Heart Canisters runtime FAIL: {}", label);
            return;
        }
        count++;
        Expanded.LOGGER.info("Heart Canisters runtime PASS: {}", label);
    }
    private static final class TestPlayer extends FakePlayer {
        final List<ItemStack> delivered = new ArrayList<>();
        TestPlayer(ServerLevel level) { super(level, new GameProfile(UUID.randomUUID(), "BHC-test")); }
        @Override public ItemEntity drop(ItemStack stack, boolean scatter, boolean trace) {
            delivered.add(stack.copy());
            return null;
        }
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static byte[] recipeBytes(Recipe recipe) {
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            recipe.getSerializer().toNetwork(buffer, recipe);
            byte[] bytes = new byte[buffer.readableBytes()];
            buffer.readBytes(bytes);
            return bytes;
        } finally { buffer.release(); }
    }
    public static int run(MinecraftServer server) throws Exception {
        count = 0;
        failures.clear();
        var level = server.overworld();
        var player = new TestPlayer(level);
        var tree = PlayerResearchesData.get(level).getResearches(player);
        var research = ModConfigs.RESEARCHES.getByName(HeartResearch.NAME);
        var poolBefore = Config.GSON.toJsonTree(ModConfigs.MOD_BOX);
        var researchBefore = Config.GSON.toJsonTree(ModConfigs.RESEARCHES);
        var groupsBefore = Config.GSON.toJsonTree(ModConfigs.RESEARCH_GROUPS);
        var vaultRulesBefore = Config.GSON.toJsonTree(ModConfigs.VAULT_GENERAL);
        HeartResearch.merge(); HeartResearch.merge();
        check(vaultRulesBefore.equals(Config.GSON.toJsonTree(ModConfigs.VAULT_GENERAL)), "vault blacklist merge preserves existing rules and does not duplicate hearts");
        for (String id : HeartVaultRules.CONSUMABLE_HEARTS) {
            var item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
            check(ModConfigs.VAULT_GENERAL.isBlacklisted(item), "native vault interaction blacklist blocks " + id);
            player.setHealth(10);
            var eaten = new ItemStack(item, 2);
            item.finishUsingItem(eaten, level, player);
            check(player.getHealth() > 10 && eaten.getCount() == 1, "native heart consumption and healing retained outside vault " + id);
        }
        check(!ModConfigs.VAULT_GENERAL.isBlacklisted(RegistryHandler.RELIC_APPLE.get()), "relic apples remain usable in vaults");
        check(!ModConfigs.VAULT_GENERAL.isBlacklisted(RegistryHandler.HEART_AMULET.get()), "equipped amulet is not vault-blacklisted");
        check(ModConfigs.VAULT_GENERAL.isBlacklisted(Items.CHORUS_FRUIT), "existing native chorus-fruit vault restriction retained");
        check(poolBefore.equals(Config.GSON.toJsonTree(ModConfigs.MOD_BOX)), "pool merge twice preserves every pool and fallback");
        check(researchBefore.equals(Config.GSON.toJsonTree(ModConfigs.RESEARCHES)) && groupsBefore.equals(Config.GSON.toJsonTree(ModConfigs.RESEARCH_GROUPS)), "research and group merges are idempotent");
        check(ModConfigs.RESEARCHES.getAll().stream().filter(r -> HeartResearch.NAME.equals(r.getName())).count() == 1, "one research entry");
        check("QoL".equals(ModConfigs.RESEARCH_GROUPS.getResearchGroupId(ModConfigs.RESEARCH_GROUPS.getResearchGroup(research))), "native QoL membership");
        var style = ModConfigs.RESEARCHES_GUI.getStyles().get(HeartResearch.NAME);
        var styles = ModConfigs.RESEARCHES_GUI.getStyles();
        var topRow = List.of("Waystones", "Torch Master", "Trashcans");
        var bottomRow = List.of("Elevators", "Altar Automation", HeartResearch.NAME);
        for (int column = 0; column < 3; column++) {
            var top = styles.get(topRow.get(column));
            var bottom = styles.get(bottomRow.get(column));
            check(top.x == bottom.x && top.x == 390 + column * 50 && top.y == 120 && bottom.y == 160,
                    "QoL research column " + column + " aligns in an evenly spaced two-row grid");
        }
        var groupStyle = ModConfigs.RESEARCH_GROUP_STYLES.getStyle("QoL");
        check(style.x >= groupStyle.getX() && style.x + 30 <= groupStyle.getX() + groupStyle.getBoxWidth()
                && style.y >= groupStyle.getY() + 20 && style.y + 30 <= groupStyle.getY() + groupStyle.getBoxHeight(), "research frame fits native QoL box");
        check(ModConfigs.RESEARCHES_GUI.getStyles().entrySet().stream().filter(e -> !e.getKey().equals(HeartResearch.NAME))
                .noneMatch(e -> Math.abs(e.getValue().x - style.x) < 30 && Math.abs(e.getValue().y - style.y) < 30), "research frame does not overlap existing nodes");
        var pricing = ResearchTree.empty();
        check(pricing.getResearchCost(research) == 2 && !research.isGated(), "early solo research costs two");
        for (var other : ModConfigs.RESEARCHES.getAll()) if (!other.getName().equals(HeartResearch.NAME)) pricing.research(other);
        check(pricing.getResearchCost(research) == 2, "all other research leaves solo cost at two");
        var result = new ResultContainer();
        check(server.getPackRepository().getSelectedIds().contains(HeartRecipePack.ID), "required approved recipe pack is selected");
        var ids = List.of("canister", "heart_amulet", "red_heart", "yellow_heart", "green_heart", "blue_heart",
                "red_heart_canister", "yellow_heart_canister", "green_heart_canister", "blue_heart_canister", "relic_apple", "god_apple");
        check(server.getRecipeManager().getRecipes().stream().filter(r -> r.getId().getNamespace().equals("bhc")).count() == 12, "exactly twelve loaded BHC recipe IDs");
        for (String id : ids) {
            var location = new ResourceLocation("bhc", id);
            Recipe<?> actual = server.getRecipeManager().byKey(location).orElseThrow();
            try (var reader = new InputStreamReader(Objects.requireNonNull(HeartRuntimeChecks.class.getResourceAsStream("/vhexpanded/hearts/recipes/" + id + ".json")), StandardCharsets.UTF_8)) {
                var approved = RecipeManager.fromJson(location, JsonParser.parseReader(reader).getAsJsonObject());
                if (!Arrays.equals(recipeBytes(actual), recipeBytes(approved))) {
                    Expanded.LOGGER.error("BHC recipe mismatch {}: actual={} expected={}", id,
                            actual.getIngredients().stream().map(i -> Arrays.toString(i.getItems())).toList(),
                            approved.getIngredients().stream().map(i -> Arrays.toString(i.getItems())).toList());
                }
                check(Arrays.equals(recipeBytes(actual), recipeBytes(approved)), "loaded approved ingredients/pattern/output " + id);
            }
            check(actual.getIngredients().stream().filter(i -> i != net.minecraft.world.item.crafting.Ingredient.EMPTY).allMatch(i -> i.getItems().length > 0), "loaded ingredients and tags resolve " + id);
            check(HeartResearch.NAME.equals(tree.restrictedBy(actual.getResultItem(), Restrictions.Type.CRAFTABILITY)), "native output gate covers manual and supported automation " + id);
            check(!result.setRecipeUsed(level, player, actual), "locked recipe denied before result " + id);
            tree.research(research);
            check(result.setRecipeUsed(level, player, actual), "unlocked recipe permitted " + id);
            tree.removeResearch(research);
        }
        check(tree.restrictedBy(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), Restrictions.Type.USABILITY) == null, "unrelated vanilla apple use stays available");
        check(tree.restrictedBy(new ItemStack(RegistryHandler.RED_HEART.get()), Restrictions.Type.USABILITY) == null, "native miniature-heart healing is not restricted");
        for (String color : List.of("red", "yellow", "green", "blue"))
            check(BaubleyHeartCanisters.config.getHeartTypeEntries(color) != null && BaubleyHeartCanisters.config.getHeartTypeEntries(color).values().stream().allMatch(v -> v == 0), "ordinary " + color + " drops disabled");
        check(ConfigHandler.general.heartStackSize.get() == 10, "native capacity ten per color");
        check(!ConfigHandler.server.allowStartingHeathTweaks.get(), "native starting-health override remains disabled");
        check(ConfigHandler.general.boneDropRate.get() == .15, "native wither-bone drops retained");

        var pool = ModConfigs.MOD_BOX.POOL.get(HeartResearch.NAME);
        check(pool.weight == 1 && pool.entries.getTotalWeight() == 1201, "pool and total item weights 1 / 1201");
        check(ModConfigs.MOD_BOX.collectPools(tree.getResearchesDone()).isEmpty(), "locked player's eligible set excludes BHC");
        Map<String, Integer> outcomes = new HashMap<>();
        for (int roll = 0; roll < 1201; roll++) {
            final int boundary = roll;
            var product = pool.entries.getRandom(new Random() { @Override public int nextInt(int bound) {
                if (bound != 1201) throw new IllegalStateException("wrong native weight bound");
                return boundary;
            }});
            checkProduct(product.generateItemStack());
            outcomes.merge(product.id, 1, Integer::sum);
        }
        check(outcomes.equals(Map.of("bhc:heart_amulet", 100, "bhc:red_heart", 1000, "bhc:yellow_heart", 100, "bhc:green_heart", 1)), "all 1201 native weighted boundaries produce exact approved distribution");
        tree.research(research);
        check(ModConfigs.MOD_BOX.collectPools(tree.getResearchesDone()).getTotalWeight() == 1, "BHC-only eligible pool has total weight one");
        var nonPool = ModConfigs.RESEARCHES.getAll().stream().filter(r -> !ModConfigs.MOD_BOX.POOL.containsKey(r.getName())).findFirst().orElseThrow();
        tree.research(nonPool);
        check(ModConfigs.MOD_BOX.collectPools(tree.getResearchesDone()).getTotalWeight() == 1, "research with no pool does not dilute rewards");
        tree.removeResearch(nonPool);
        var names = ModConfigs.MOD_BOX.POOL.keySet().stream().filter(n -> !n.equals("None") && !n.equals(HeartResearch.NAME)).limit(7).toList();
        var eligible = new ArrayList<>(names); eligible.add(HeartResearch.NAME);
        check(ModConfigs.MOD_BOX.collectPools(eligible).getTotalWeight() == 8, "eight equal eligible pools give BHC one-eighth share");
        // Seed only this disposable world's random stream; invoke native use and capture native delivery.
        long seed = 0;
        for (;; seed++) { var random = new Random(seed); random.nextInt(1); if (random.nextInt(1201) == 1200) break; }
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.MOD_BOX, 2));
        level.random.setSeed(seed);
        ModItems.MOD_BOX.use(level, player, InteractionHand.MAIN_HAND);
        check(player.getMainHandItem().getCount() == 1 && player.delivered.size() == 1
                && player.delivered.get(0).is(RegistryHandler.GREEN_HEART.get()) && player.delivered.get(0).getCount() == 1,
                "forced native green opening delivers exactly one heart and consumes exactly one box");
        tree.removeResearch(research);
        player.delivered.clear();
        ModItems.MOD_BOX.use(level, player, InteractionHand.MAIN_HAND);
        check(player.delivered.stream().noneMatch(s -> s.getItem().getRegistryName().getNamespace().equals("bhc")), "locked native opening delivers no BHC reward");

        var amulet = new ItemStack(RegistryHandler.HEART_AMULET.get());
        amulet.getOrCreateTag().putIntArray("heart_amount", new int[]{20,20,20,20});
        var health = player.getAttribute(Attributes.MAX_HEALTH);
        health.setBaseValue(20); player.setHealth(20);
        HealthModifier.updatePlayerHealth(player, amulet, true);
        check(player.getMaxHealth() == 20, "received fully loaded amulet gives locked player no benefit");
        tree.research(research);
        for (int color = 0; color < 4; color++) {
            int[] values = new int[4]; values[color] = 20;
            amulet.getOrCreateTag().putIntArray("heart_amount", values);
            HealthModifier.updatePlayerHealth(player, amulet, true);
            check(player.getMaxHealth() == 40, "ten canisters of color " + color + " add ten full hearts");
        }
        amulet.getOrCreateTag().putIntArray("heart_amount", new int[]{20,20,20,20});
        HealthModifier.updatePlayerHealth(player, amulet, true);
        check(player.getMaxHealth() == 100, "four full colors add forty full hearts");
        amulet.getOrCreateTag().putIntArray("heart_amount", new int[]{200,200,200,200});
        HealthModifier.updatePlayerHealth(player, amulet, true);
        check(player.getMaxHealth() == 100, "native per-color clamp rejects over-cap health");
        amulet.getOrCreateTag().putIntArray("heart_amount", new int[]{20,20,20,20});
        HealthModifier.updatePlayerHealth(player, amulet, true);
        check(player.getMaxHealth() == 100, "repeated native update does not duplicate health");
        player.setHealth(95);
        HealthModifier.updatePlayerHealth(player, ItemStack.EMPTY, false);
        check(player.getMaxHealth() == 20 && player.getHealth() == 15, "wounded unequip retains five missing health points");
        var gear = new AttributeModifier(UUID.randomUUID(), "test additive gear", 10, AttributeModifier.Operation.ADDITION);
        health.addTransientModifier(gear);
        HealthModifier.updatePlayerHealth(player, amulet, true);
        check(player.getMaxHealth() == 110, "amulet stacks with other additive max-health modifiers");
        health.removeModifier(gear);
        var curios = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElseThrow(() -> new IllegalStateException("Missing Curios capability"));
        var slot = curios.getStacksHandler("heartamulet").orElseThrow().getStacks();
        slot.setStackInSlot(0, amulet);
        HeartEquipment.reconcile(player);
        check(player.getMaxHealth() == 100, "native Curios amulet slot supplies equipped benefit");
        var saved = curios.saveInventory(false);
        slot.setStackInSlot(0, ItemStack.EMPTY); HeartEquipment.reconcile(player);
        check(player.getMaxHealth() == 20, "empty Curios slot removes bonus");
        curios.loadInventory(saved); HeartEquipment.reconcile(player);
        check(player.getMaxHealth() == 100, "Curios inventory save/restore restores one bonus");
        tree.removeResearch(research); HeartEquipment.reconcile(player);
        check(player.getMaxHealth() == 20, "research removal reconciles an already equipped amulet");
        slot.setStackInSlot(0, ItemStack.EMPTY);
        tree.resetResearches();
        if (!failures.isEmpty()) throw new IllegalStateException(String.join("; ", failures));
        return count;
    }
    private static void checkProduct(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() != 1 || stack.hasTag())
            throw new IllegalStateException("Mod Box product must be one empty/plain item");
    }
}
