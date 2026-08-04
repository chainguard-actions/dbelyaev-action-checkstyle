<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v3.14.0

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `2`

Action **dbelyaev--action-checkstyle/v3.14.0** was hardened automatically. 1 finding(s) were identified and resolved across 2 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Sub-rule (b): The shell variable `${INPUT_REVIEWDOG_FLAGS}` — sourced from the workflow-controllable input `inputs.reviewdog_flags` — is expanded **unquoted** in the `reviewdog` command invocation. The script even carries a `# shellcheck disable=SC2086` comment acknowledging the intentional word-splitting. Because the value is not double-quoted, the shell parses any metacharacters (`;`, `|`, `&`, `$(...)`, backticks, etc.) embedded in the input before passing arguments to reviewdog, enabling command injection by whoever controls the calling workflow. Offending line: `    ${INPUT_REVIEWDOG_FLAGS} < "$cs_output" || rd_exit=$?`

Locations:

- `entrypoint.sh:163`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed the unquoted expansion of ${INPUT_REVIEWDOG_FLAGS} in entrypoint.sh (line 163). Replaced the unsafe `${INPUT_REVIEWDOG_FLAGS}` unquoted expansion (which had a `# shellcheck disable=SC2086` comment) with a safe xargs-based tokenization approach. The fix uses `printf '%s' "${INPUT_REVIEWDOG_FLAGS}" | xargs sh -c 'reviewdog "$@" < "$CS_OUT"' _ <fixed_flags>` pattern: xargs tokenizes the user input respecting quotes/backslashes but without evaluating shell metacharacters (;, |, &&, $(...), backticks), preventing command injection. The checkstyle XML is read from the exported $CS_OUT environment variable so xargs can own stdin. A direct reviewdog invocation handles the empty-flags case. The solution is POSIX sh / Alpine ash compatible.

### Iteration 2

**Fixes applied:** script-injection

**Notes:**

Replaced `${{ github.sha }}` with `$GITHUB_SHA` in the `run:` step of the `trivy-image-scan` job in `.github/workflows/trivy.yml`. The pre-set `GITHUB_SHA` environment variable is always available in GitHub Actions runners and avoids YAML template substitution before the shell processes the command.

