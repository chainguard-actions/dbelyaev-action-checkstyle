<!-- markdownlint-disable -->

# Hardening Report: dbelyaev--action-checkstyle/v4.0.0

> This file was generated automatically by the hardening agent.

**Policy SHA:** `d636be7e43ef829af6e853da6b3c7566db9f72fe`

**Test Policy SHA:** `843adf9e4b8f85d0c08b27b9d0b09dd094b54702`

**Harden Agent Version:** `2`

Action **dbelyaev--action-checkstyle/v4.0.0** was hardened automatically. 1 finding(s) were identified and resolved across 1 iteration(s).

## Findings Fixed

### script-injection (severity: high)

Sub-rule (a): A ${{ }} expression is directly interpolated inside a run: shell command string. In the trivy-image-scan job, the step 'Build Docker image for scanning' uses: `run: docker build -t docker-image:${{ github.sha }} .` — the expression ${{ github.sha }} is substituted by the Actions template engine before the shell ever sees the command, making this a script-injection pattern. The safe alternative is to pass the value via an env: variable and reference it as $GITHUB_SHA (the pre-set environment variable) or as "$SHA" after setting `env: SHA: ${{ github.sha }}`

Locations:

- `.github/workflows/trivy.yml:62`

## Iteration Notes

### Iteration 1

**Fixes applied:** script-injection

**Notes:**

Fixed script injection in hardened/action/.github/workflows/trivy.yml line 62: replaced `docker build -t docker-image:${{ github.sha }} .` with `docker build -t docker-image:"$GITHUB_SHA" .`. The GITHUB_SHA environment variable is pre-set by GitHub Actions and holds the same commit SHA value, avoiding template interpolation in the shell command.

