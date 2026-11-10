#!/usr/bin/env bash
#
# opencode-termux-installer.sh
# One-command OpenCode installer for Termux (android/arm64).
#
# OpenCode is a Go CLI. The official prebuilt release tarball is a glibc Linux
# binary that crashes on Termux (SIGSYS on faccessat2), so we build it from
# source using Termux's native android/arm64 Go toolchain.
#
# Usage (one command):
#   bash <(curl -fsSL https://raw.githubusercontent.com/<you>/<repo>/main/install-opencode-termux.sh)
#   # or, if you saved this file locally:
#   bash install-opencode-termux.sh
#
set -euo pipefail

APP=opencode
PREFIX="${PREFIX:-/data/data/com.termux/files/usr}"
BIN_DIR="$PREFIX/bin"

# -----------------------------------------------------------------------------
# 0. Confirm we are on Termux + arm64/aarch64
# -----------------------------------------------------------------------------
if [ -z "${TERMUX_APP__PACKAGE_NAME:-}" ] && [ ! -d "$PREFIX" ]; then
  echo "ERROR: this script is for Termux only." >&2
  exit 1
fi

arch=$(uname -m)
case "$arch" in
  aarch64) arch=arm64 ;;
esac
if [ "$arch" != "arm64" ]; then
  echo "ERROR: only arm64 (aarch64) Termux is supported, got $arch" >&2
  exit 1
fi

echo "[opencode-termux] Detected Termux $arch"

# -----------------------------------------------------------------------------
# 1. Ensure Go is installed (Termux ships golang via pkg)
# -----------------------------------------------------------------------------
if ! command -v go >/dev/null 2>&1; then
  echo "[opencode-termux] Installing golang via pkg..."
  pkg install golang -y
fi

export GOOS=android GOARCH=arm64
echo "[opencode-termux] Using $(go version) ($GOOS/$GOARCH)"

# -----------------------------------------------------------------------------
# 2. Build OpenCode from source
#    NOTE: the prebuilt opencode-linux-*.tar.gz is a glibc Linux binary and
#    will SIGSYS-crash on Termux. Building from source targets android and works.
# -----------------------------------------------------------------------------
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

echo "[opencode-termux] Downloading & building github.com/opencode-ai/opencode@latest ..."
cd "$TMP"
go install github.com/opencode-ai/opencode@latest

BIN_SRC="$(go env GOPATH)/bin/$APP"
if [ ! -x "$BIN_SRC" ]; then
  echo "ERROR: expected built binary at $BIN_SRC but it is missing" >&2
  exit 1
fi

# -----------------------------------------------------------------------------
# 3. Install into Termux PATH ($PREFIX/bin is on PATH by default)
# -----------------------------------------------------------------------------
mkdir -p "$BIN_DIR"
cp -f "$BIN_SRC" "$BIN_DIR/$APP"
chmod +x "$BIN_DIR/$APP"

echo "[opencode-termux] Installed $APP to $BIN_DIR/$APP"
echo "[opencode-termux] Version: $($BIN_DIR/$APP --version 2>/dev/null | tail -n1)"
echo
echo "[opencode-termux] Done! Run:  opencode"
echo "[opencode-termux] Tip: set an API key first, e.g.  export GEMINI_API_KEY=...  (or ANTHROPIC_API_KEY / OPENAI_API_KEY ...)"
echo "[opencode-termux] Optional: pkg install fzf ripgrep  (enables fuzzy search & ripgrep tools)"
