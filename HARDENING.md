<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v3.8.2

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `1`

Action **dbelyaev--action-checkstyle/v3.8.2** was hardened automatically. 1 finding(s) were identified and resolved across 1 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Rule (b) violation: The shell variable `${INPUT_REVIEWDOG_FLAGS}` — which holds the value of the `inputs.reviewdog_flags` action input (a workflow-controllable value) — is expanded **unquoted** on the `reviewdog` command line in `entrypoint.sh`. An unquoted expansion allows the shell to parse metacharacters (`;`, `|`, `&`, `$(...)`, backticks, glob characters, whitespace word-splitting) out of the value before passing it to the command, enabling command injection. The `# shellcheck disable=SC2086` comment immediately above confirms the author is aware of the word-splitting but does not address the injection risk. The offending line is:
```
    ${INPUT_REVIEWDOG_FLAGS} < "$cs_output" || rd_exit=$?
```
Fix: use `"${INPUT_REVIEWDOG_FLAGS}"` (double-quoted) or, if multi-word flag splitting is required, use an array (`eval` with sanitization or `read -ra FLAGS <<< "$INPUT_REVIEWDOG_FLAGS"`) and expand as `"${FLAGS[@]}"`

Locations:

- `entrypoint.sh:155`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed unquoted expansion of ${INPUT_REVIEWDOG_FLAGS} in entrypoint.sh line 155. Replaced `${INPUT_REVIEWDOG_FLAGS}` (unquoted, with shellcheck SC2086 suppression) with `${INPUT_REVIEWDOG_FLAGS:+"${INPUT_REVIEWDOG_FLAGS}"}` which: drops the argument when empty (no spurious empty-string arg) and expands as a double-quoted word when non-empty (prevents word-splitting and shell metacharacter injection). Removed the `# shellcheck disable=SC2086` comment that was suppressing the warning about the unquoted expansion.

