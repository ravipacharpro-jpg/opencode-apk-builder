#!/usr/bin/env bash
# ============================================================
# OpenCode Termux Installer — One Command Setup
# Direct install, bina kisi API key setup ke
# ============================================================
set -euo pipefail

APP=opencode
PREFIX="${PREFIX:-/data/data/com.termux/files/usr}"
BIN_DIR="$PREFIX/bin"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

info()   { echo -e "${BLUE}[opencode]${NC} $*"; }
ok()     { echo -e "${GREEN}[opencode]${NC} $*"; }
warn()   { echo -e "${YELLOW}[opencode]${NC} $*"; }
error()  { echo -e "${RED}[opencode]${NC} $*" >&2; exit 1; }

# -----------------------------------------------------------
# 0. Confirm Termux
# -----------------------------------------------------------
if [ -z "${TERMUX_APP__PACKAGE_NAME:-}" ] && [ ! -d "$PREFIX" ]; then
  error "This script is for Termux only."
fi

arch=$(uname -m)
case "$arch" in
  aarch64) arch=arm64 ;;
esac
if [ "$arch" != "arm64" ]; then
  error "Only arm64 (aarch64) Termux is supported, got $arch"
fi

ok "Detected Termux $arch"

# -----------------------------------------------------------
# 1. Install golang if missing
# -----------------------------------------------------------
if ! command -v go >/dev/null 2>&1; then
  info "Installing golang via pkg (one-time, ~40MB)..."
  pkg update -y
  pkg install golang -y
fi

export GOOS=android GOARCH=arm64
ok "Using $(go version) ($GOOS/$GOARCH)"

# -----------------------------------------------------------
# 2. Build OpenCode from source
#    Prebuilt opencode-linux-arm64.tar.gz is glibc Linux
#    and crashes on Termux (SIGSYS). Building from source
#    targets Android natively.
# -----------------------------------------------------------
info "Building github.com/opencode-ai/opencode@latest from source..."
go install github.com/opencode-ai/opencode@latest

BIN_SRC="$(go env GOPATH)/bin/$APP"
if [ ! -x "$BIN_SRC" ]; then
  error "Expected binary at $BIN_SRC but it is missing"
fi

# -----------------------------------------------------------
# 3. Install into Termux PATH
# -----------------------------------------------------------
mkdir -p "$BIN_DIR"
cp -f "$BIN_SRC" "$BIN_DIR/$APP"
chmod +x "$BIN_DIR/$APP"

ok "Installed $APP to $BIN_DIR/$APP"
ok "Version: $($BIN_DIR/$APP --version 2>/dev/null | tail -n1)"

# -----------------------------------------------------------
# 4. Add to PATH in shell config (if not already there)
# -----------------------------------------------------------
if [[ ":$PATH:" != *":$BIN_DIR:"* ]]; then
  warn "$BIN_DIR is not in PATH. Adding to shell config..."
  case "$(basename "$SHELL")" in
    bash) echo "export PATH=$BIN_DIR:\$PATH" >> ~/.bashrc ;;
    zsh)  echo "export PATH=$BIN_DIR:\$PATH" >> ~/.zshrc ;;
    ash)  echo "export PATH=$BIN_DIR:\$PATH" >> ~/.profile ;;
    sh)   echo "export PATH=$BIN_DIR:\$PATH" >> ~/.profile ;;
    *)    echo "export PATH=$BIN_DIR:\$PATH" >> ~/.bashrc ;;
  esac
  ok "Added to PATH. Run: source ~/.bashrc (or restart Termux)"
fi

# -----------------------------------------------------------
# 5. Done
# -----------------------------------------------------------
echo ""
echo "============================================================"
ok "OpenCode installed successfully!"
echo "============================================================"
echo ""
echo "Run directly:"
echo "  opencode"
echo ""
echo "Pehli baar run karne par app khud API key mang lega"
echo "(Anthropic / OpenAI / Gemini / Groq kisi bhi ek ka use kar sakte ho)"
echo ""
echo "Optional: better search ke liye"
echo "  pkg install fzf ripgrep"
echo ""
echo "============================================================"
