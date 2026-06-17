<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v3.9.0

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `1`

Action **dbelyaev--action-checkstyle/v3.9.0** was hardened automatically. 1 finding(s) were identified and resolved across 1 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Rule (b) violation: The shell variable `${INPUT_REVIEWDOG_FLAGS}` — which holds the value of the user-controlled `inputs.reviewdog_flags` action input — is expanded **unquoted** in the `reviewdog` command in `entrypoint.sh`. An unquoted expansion allows the shell to perform word-splitting and glob expansion on the value, meaning an attacker who controls this input can inject shell metacharacters (`;`, `|`, `&`, `$(...)`, backticks, etc.) to execute arbitrary commands inside the container. The `# shellcheck disable=SC2086` comment confirms the developer intentionally left it unquoted for multi-flag word-splitting, but this does not mitigate the injection risk. The offending line is: `    ${INPUT_REVIEWDOG_FLAGS} < "$cs_output" || rd_exit=$?`

Locations:

- `entrypoint.sh:148`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed script injection in entrypoint.sh at line 148. The unquoted `${INPUT_REVIEWDOG_FLAGS}` expansion was replaced with a safe pattern: (1) `set -f` disables glob expansion, (2) `set -- $(printf '%s\n' "${INPUT_REVIEWDOG_FLAGS}")` splits the double-quoted variable value into positional parameters via IFS word-splitting only (no glob expansion, no command substitution on the value), (3) `set +f` restores glob expansion, (4) `"$@"` passes each flag token as a separate quoted argument to reviewdog. Shell metacharacters in the input (`;`, `|`, `&`, `$(...)`, backticks) are never interpreted as shell syntax — they become literal string arguments that reviewdog rejects as invalid flags.

