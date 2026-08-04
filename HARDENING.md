<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v3.13.0

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `2`

Action **dbelyaev--action-checkstyle/v3.13.0** was hardened automatically. 1 finding(s) were identified and resolved across 2 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Rule (b) violation: The shell variable `${INPUT_REVIEWDOG_FLAGS}` — which holds the workflow-controlled `reviewdog_flags` input — is expanded **unquoted** in the `reviewdog` command invocation in `entrypoint.sh`. An unquoted shell expansion allows the shell to perform word-splitting and glob expansion on the value, enabling an attacker who controls the `reviewdog_flags` input to inject additional shell arguments or exploit glob patterns. The `# shellcheck disable=SC2086` comment on the preceding line explicitly suppresses the shellcheck warning about this unquoted variable. The fix is to either quote the variable (`"${INPUT_REVIEWDOG_FLAGS}"`) or use `eval` with proper sanitization if multi-word splitting is intentional and the input is trusted. Offending line: `    ${INPUT_REVIEWDOG_FLAGS} < "$cs_output"`

Locations:

- `entrypoint.sh:137`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed the unquoted ${INPUT_REVIEWDOG_FLAGS} expansion in entrypoint.sh at line 137. Replaced the bare unquoted variable expansion (with its shellcheck disable comment) with a safe tokenization approach: (1) use `xargs printf '%s\0'` to tokenize the flags value in a quote-aware manner into a temp file with null-delimited tokens, (2) use `while IFS= read -r -d '' token; do set -- "$@" "$token"; done` to rebuild positional parameters from those tokens, (3) pass `"$@"` (properly double-quoted) to reviewdog. This prevents word-splitting/glob injection while correctly handling multi-word flag lists. The approach is compatible with Alpine ash (busybox) used in the Docker container.

### Iteration 2

**Fixes applied:** script-injection

**Notes:**

Fixed script injection in .github/workflows/trivy.yml at line 74. Moved `${{ github.sha }}` out of the `run:` shell command and into an `env:` block as `GITHUB_SHA_VALUE`. The shell command now uses `"$GITHUB_SHA_VALUE"` instead of the direct template expression. The `${{ github.sha }}` usage in the `with: image-ref:` block of the subsequent step was left unchanged as it is a YAML input value (not a shell command) and is not subject to shell injection.

