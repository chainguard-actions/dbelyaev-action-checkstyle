<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v3.13.0

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `2`

Action **dbelyaev--action-checkstyle/v3.13.0** was hardened automatically. 1 finding(s) were identified and resolved across 1 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Rule (b) violation: The shell variable `${INPUT_REVIEWDOG_FLAGS}` — which holds the user-controlled `reviewdog_flags` action input — is expanded **unquoted** in the `reviewdog` command invocation in `entrypoint.sh`. An attacker (or a calling workflow) can supply shell metacharacters (`;`, `|`, `&`, `$(...)`, backticks, etc.) in the `reviewdog_flags` input, causing arbitrary command injection. The `# shellcheck disable=SC2086` comment on the preceding line explicitly suppresses the shellcheck warning about this unquoted expansion. The fix is to either quote the variable (`"${INPUT_REVIEWDOG_FLAGS}"`) or use `eval` with careful sanitization, though the safest approach is to pass flags via an array or accept only a whitelist of known-safe flag values. Offending line: `    ${INPUT_REVIEWDOG_FLAGS} < "$cs_output" || rd_exit=$?`

Locations:

- `entrypoint.sh:162`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed unquoted expansion of `${INPUT_REVIEWDOG_FLAGS}` in entrypoint.sh. The fix uses `set -f` (disable glob expansion) + `set -- ${INPUT_REVIEWDOG_FLAGS}` (tokenize into positional params) + `set +f` (re-enable glob expansion), then passes `"$@"` to reviewdog. This is the correct POSIX sh approach for a script running on Alpine ash (no bash arrays available) where the command reads from a file redirect (so xargs cannot be piped to it). The `shellcheck disable=SC2086` comment is retained only for the `set --` line where it's needed.

