# Issue tracker: GitHub

Issues and specs for this repo live in GitHub at `kpadams94/VH-Expanded`. Use the `gh` CLI with `--repo kpadams94/VH-Expanded` for all operations.

## Conventions

- **Create an issue**: `gh issue create --repo kpadams94/VH-Expanded --title "..." --body "..."`
- **Read an issue**: `gh issue view <number> --repo kpadams94/VH-Expanded --comments`
- **List issues**: `gh issue list --repo kpadams94/VH-Expanded --state open --json number,title,body,labels,comments`
- **Comment**: `gh issue comment <number> --repo kpadams94/VH-Expanded --body "..."`
- **Apply or remove labels**: use `gh issue edit` with `--add-label` or `--remove-label`
- **Close**: `gh issue close <number> --repo kpadams94/VH-Expanded --comment "..."`

## Pull requests as a triage surface

**PRs as a request surface: no.**

## When a skill says “publish to the issue tracker”

Create a GitHub issue in `kpadams94/VH-Expanded`.

## When a skill says “fetch the relevant ticket”

Run `gh issue view <number> --repo kpadams94/VH-Expanded --comments`.

## Wayfinding operations

The map is a single issue labeled `wayfinder:map`, with child issues serving as tickets.

- Child labels use `wayfinder:<type>`: `research`, `prototype`, `grilling`, or `task`.
- Use native GitHub sub-issues and issue dependencies when available.
- If those features are unavailable, link children through task lists and `Part of #<map>` or `Blocked by: #<number>` lines.
- Claim a ticket with `gh issue edit <number> --repo kpadams94/VH-Expanded --add-assignee @me`.
- Resolve it by commenting with the answer and then closing it.
