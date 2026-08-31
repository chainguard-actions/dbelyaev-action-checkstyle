<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v4.0.1

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `2`

Action **dbelyaev--action-checkstyle/v4.0.1** was hardened automatically. 1 finding(s) were identified and resolved across 1 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Sub-rule (a): A GitHub Actions expression `${{ github.sha }}` is directly interpolated inside a `run:` shell command string. The offending line is: `run: docker build -t docker-image:${{ github.sha }} .` — the `github.*` context value is substituted into the shell command before the shell ever sees it, allowing YAML template injection. Even though `github.sha` is not attacker-controlled, any `${{ ... }}` expression directly in a `run:` block is a script-injection finding per the check rules. The fix is to pass the value via an `env:` variable and reference it as `"$DOCKER_TAG"` in the shell command.

Locations:

- `.github/workflows/trivy.yml:74`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed script injection in hardened/action/.github/workflows/trivy.yml at line 74. Moved `${{ github.sha }}` out of the `run:` shell command into an `env:` block as `DOCKER_TAG: ${{ github.sha }}`, and updated the shell command to use `"$DOCKER_TAG"` instead. The `image-ref` input in the subsequent trivy-action step uses the same expression but as a YAML `with:` input (not a shell command), so it is not a script injection issue.

