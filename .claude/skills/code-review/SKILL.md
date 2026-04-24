---
name: code-review
description: Shared review criteria and output format for code review agents
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

# Code Review Skill

Shared review criteria and output format for code review agents.

## Review Criteria

### Coding Conventions (docs/coding-conventions.md)

- **Naming**: Check if ViewModel, Composable, and variable names follow conventions
- **Package Structure**: Verify feature module directory structure
- **Compose Patterns**: Screen/Content separation, Modifier usage, Preview implementation
- **Data Class**: `@Immutable` annotation, `ImmutableList` usage
- **Navigation**: Extension function patterns, Uri encoding
- **Security**: No hardcoded API keys, access tokens, or secrets

### Architecture (docs/architecture.md)

- **Module Dependencies**: feature depends only on `data/repository`, not directly on `data/api` or
  `data/db`
- **Implementation Patterns**: ViewModel, Repository, Screen patterns

### General Code Quality

- **Bugs/Logic Errors**: Null safety, boundary conditions, exception handling
- **Security**: Hardcoded API keys, input validation
- **Performance**: Unnecessary recomposition, N+1 queries
- **Readability**: Overly complex logic, unclear naming

## Output Format

Always categorize findings into the following 3 categories:

### Critical issues (must fix)

Issues that must be fixed. Bugs, security risks, build errors, etc.

### Warnings (should fix)

Issues that should be fixed. Convention violations, potential bugs, performance issues, etc.

### Suggestions (consider improving)

Points to consider improving. Refactoring suggestions, readability improvements, etc.

## Output Example

```
## Critical issues (must fix)

- `feature/auth/LoginViewModel.kt:42` - API key is hardcoded. Please retrieve it from `local.properties`.

## Warnings (should fix)

- `feature/album/ui/screen/AlbumScreen.kt:15` - Screen and Content are not separated. Please separate them according to conventions.
- `core/entity/Track.kt:8` - Missing `@Immutable` annotation.

## Suggestions (consider improving)

- `feature/home/ui/viewmodel/HomeViewModel.kt:30` - Extracting this logic into a separate function would improve readability.

---

If a category has no findings, write "None".
```

## Notes

- Always include file path and line number for each finding
- Provide specific remediation suggestions
- Avoid overly minor issues and focus on important problems
