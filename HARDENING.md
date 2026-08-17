<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v3.11.0

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `2`

Action **dbelyaev--action-checkstyle/v3.11.0** was hardened automatically. 1 finding(s) were identified and resolved across 2 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Sub-rule (a): A ${{ }} expression is directly interpolated inside a run: shell command string. The step `docker build -t docker-image:${{ github.sha }} .` embeds `${{ github.sha }}` directly in the shell command. Although `github.sha` is not attacker-controlled via PR content, any `${{ ... }}` expression interpolated directly into a run: block is a script-injection finding per the check rules — the value flows through YAML template substitution before the shell ever sees it. The fix is to pass it via an env: variable and reference `$GITHUB_SHA` (the pre-set env var) or a quoted `"$SHA"` shell variable instead.

Locations:

- `.github/workflows/trivy.yml:74`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed script injection in hardened/action/.github/workflows/trivy.yml at line 74. Moved `${{ github.sha }}` out of the `run:` shell command into an `env:` block as `SHA: ${{ github.sha }}`, then referenced it as `"$SHA"` in the docker build command. The `image-ref` in the trivy-action `with:` block was left unchanged as it is a YAML input to an action, not a shell command, and does not have the same injection risk.

### Iteration 2

**Fixes applied:** script-injection

**Notes:**

Fixed unquoted expansion of ${INPUT_REVIEWDOG_FLAGS} in entrypoint.sh. Replaced the `# shellcheck disable=SC2086` + unquoted `${INPUT_REVIEWDOG_FLAGS}` pattern with a safe approach: (1) build fixed reviewdog args as positional parameters with `set --` and proper double-quoting, (2) parse user-supplied flags through `xargs -n1` into a temp file (xargs splits on shell quoting rules but does not invoke a shell, so metacharacters are literal), (3) append each parsed token to `$@` with `set -- "$@" "$_flag"`, (4) invoke `reviewdog "$@"`. This prevents shell metacharacter injection while preserving multi-flag support and remaining compatible with POSIX sh/Alpine ash.

