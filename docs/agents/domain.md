# Domain Docs

How engineering skills should consume this repository’s domain documentation.

## Before exploring, read these

- `CONTEXT.md` at the repository root.
- Relevant decisions under `docs/adr/`.

If these files do not exist, proceed silently. The domain-modeling workflow creates them when terminology or architectural decisions are resolved.

## File structure

This repository uses a single-context layout:

```text
/
├── CONTEXT.md
├── docs/adr/
└── src/
```

## Use the glossary’s vocabulary

Use terms as defined in `CONTEXT.md`. Avoid synonyms that the glossary explicitly rejects. If a needed concept is absent, reconsider the language or note the gap for domain modeling.

## Flag ADR conflicts

Explicitly identify output that contradicts an existing architectural decision instead of silently overriding it.
