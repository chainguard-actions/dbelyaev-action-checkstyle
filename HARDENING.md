<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v3.15.0

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `2`

Action **dbelyaev--action-checkstyle/v3.15.0** was hardened automatically. 2 finding(s) were identified and resolved across 1 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Rule (a) violation: A `${{ github.sha }}` expression is directly interpolated inside a `run:` shell command string: `run: docker build -t docker-image:${{ github.sha }} .`. Any `${{ ... }}` expression inside a `run:` block is a script-injection risk because the value is substituted by the YAML template engine before the shell ever sees it. The fix is to pass the value via an `env:` variable and reference it as `"$ENV_VAR"` in the shell command.

Locations:

- `.github/workflows/trivy.yml:63`

### script-injection (severity: high)

Rule (b) violation: The shell variable `${INPUT_REVIEWDOG_FLAGS}` — which holds the user-controlled `inputs.reviewdog_flags` action input — is expanded **unquoted** in the `reviewdog` command: `${INPUT_REVIEWDOG_FLAGS} < "$cs_output"`. The developer explicitly suppressed the shellcheck warning with `# shellcheck disable=SC2086`, confirming the unquoted word-splitting is intentional, but this still allows an attacker-supplied value containing shell metacharacters (`;`, `|`, `&`, `$(...)`, backticks, etc.) to inject arbitrary shell commands. The fix is to either double-quote the variable (`"${INPUT_REVIEWDOG_FLAGS}"`) or use an array to safely pass multiple flags.

Locations:

- `entrypoint.sh:185`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed two script-injection findings:

1. `.github/workflows/trivy.yml` line 63: Moved `${{ github.sha }}` out of the `run:` shell command into an `env:` block as `IMAGE_TAG`, then referenced it as `"$IMAGE_TAG"` in the shell command. The `image-ref:` field in the subsequent `with:` block is a YAML value passed to an action (not a shell command), so it was left as-is.

2. `entrypoint.sh` line 185: Replaced the unquoted `${INPUT_REVIEWDOG_FLAGS}` expansion with a safe `xargs`-based approach. The flags are piped through `printf '%s' "$INPUT_REVIEWDOG_FLAGS" | xargs reviewdog ...` which tokenizes the input (honoring quotes/backslashes) without interpreting shell metacharacters. Stdin for reviewdog is supplied via file descriptor 3 (`exec 3< "$cs_output"` / `<&3`) so xargs can use its own stdin for the flag list. This is POSIX sh / Alpine ash compatible.

