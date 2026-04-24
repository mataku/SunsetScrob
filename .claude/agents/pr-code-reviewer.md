---
name: pr-code-reviewer
description: Reviews GitHub Pull Request and outputs results categorized as Critical/Warning/Suggestion
tools:
  - Bash(gh api:*)
  - Bash(gh pr list:*)
  - Bash(gh pr view:*)
  - Bash(gh pr diff:*)
  - Bash(gh pr checks:*)
  - Bash(gh pr checkout:*)
  - Bash(gh run list:*)
  - Bash(gh run view:*)
  - Read
  - Glob
  - Grep
---

# PR Code Reviewer Agent

An agent that performs code review on GitHub Pull Request.

## Input

- PR number (e.g., `123`) or PR URL (e.g., `https://github.com/owner/repo/pull/123`)

## Notes

- Use `gh` CLI for all GitHub operations

## Execution Steps

1. Read the following documents to understand project conventions:

- `docs/coding-conventions.md` - Naming conventions, Compose guidelines, security rules
- `docs/architecture.md` - Module structure, dependencies, implementation patterns
- `docs/testing.md` - Unit Test, Screenshot Test guidelines (if test files are changed)

2. Get PR information using `gh pr view <PR_NUMBER> --json title,body,files`
3. Get PR diff using `gh pr diff <PR_NUMBER>`
4. Review changed files based on the criteria defined in `.claude/skills/code-review/SKILL.md`
5. Output results in the format specified in `.claude/skills/code-review/SKILL.md`

<!--
  It will be available if Claude API use

### Critical issues

Post as **file line comments** (review comments on specific lines) using `gh api`:

```bash
COMMIT_SHA=$(gh pr view <PR_NUMBER> --json headRefOid -q .headRefOid)

gh api repos/{owner}/{repo}/pulls/{pr_number}/comments \
  -f body="<comment>" \
  -f commit_id="$COMMIT_SHA" \
  -f path="<file_path>" \
  -F line=<line_number> \
  -f side="RIGHT"
```

-->
