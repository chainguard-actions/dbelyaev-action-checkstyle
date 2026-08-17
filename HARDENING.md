<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v3.10.0

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `2`

Action **dbelyaev--action-checkstyle/v3.10.0** was hardened automatically. 2 finding(s) were identified and resolved across 2 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Rule (a) violation: A `${{ ... }}` expression is directly interpolated inside a `run:` shell command string. In trivy.yml, the step 'Build Docker image for scanning' uses `run: docker build -t docker-image:${{ github.sha }} .` — the `github.sha` context value is substituted by the Actions template engine before the shell ever sees it, making this a script-injection risk. Even though `github.sha` is not directly attacker-controlled, any `${{ ... }}` expression inside a `run:` block is a violation of this check.

Locations:

- `.github/workflows/trivy.yml:57`

### script-injection (severity: high)

Rule (a) violation: Two `run:` blocks in test-other.yml directly interpolate `${{ steps.*.outcome }}` expressions inside shell commands. The 'Verify failure' step in the `test-invalid-config` job uses `echo "Expected failure but got: ${{ steps.invalid_config.outcome }}"` and the same pattern appears in the `test-invalid-version` job with `${{ steps.invalid_version.outcome }}`. These expressions are substituted by the Actions template engine before the shell executes, which is a script-injection risk.

Locations:

- `.github/workflows/test-other.yml:131`
- `.github/workflows/test-other.yml:157`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed 3 script injection violations across 2 files:
1. hardened/action/.github/workflows/trivy.yml (line 57): Moved `${{ github.sha }}` into an `env:` block as `GIT_SHA`; the run command now uses `"docker-image:$GIT_SHA"`.
2. hardened/action/.github/workflows/test-other.yml (line 131): Moved `${{ steps.invalid_config.outcome }}` into an `env:` block as `STEP_OUTCOME`; the run block now uses `$STEP_OUTCOME`.
3. hardened/action/.github/workflows/test-other.yml (line 157): Moved `${{ steps.invalid_version.outcome }}` into an `env:` block as `STEP_OUTCOME`; the run block now uses `$STEP_OUTCOME`.
All remaining `${{ }}` expressions in these files are in `with:`, `if:`, `env:`, or `concurrency:` blocks — not in `run:` shell strings — so they are not script injection risks.

### Iteration 2

**Fixes applied:** script-injection

**Notes:**

Fixed the unquoted expansion of `${INPUT_REVIEWDOG_FLAGS}` in entrypoint.sh. The original code passed this user-controlled variable directly and unquoted to the reviewdog command, enabling shell metacharacter injection. The fix: (1) uses `set -f` to disable glob expansion, (2) uses `set -- ${INPUT_REVIEWDOG_FLAGS}` to split the flags into positional parameters via word-splitting only (variable expansion does not execute command substitutions or interpret shell metacharacters like `;`, `|`, `&`), (3) restores glob expansion with `set +f`, and (4) passes `"$@"` (properly quoted positional parameters) to reviewdog. This eliminates the injection vector while preserving the legitimate functionality of passing additional reviewdog flags.

