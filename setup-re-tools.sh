#!/usr/bin/env bash
# ============================================================
# Termux Reverse Engineering Setup Script
# Installs: binutils, radare2, frida, apktool, jadx, python
# ============================================================
set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

info()  { echo -e "${BLUE}[RE-Setup]${NC} $*"; }
ok()    { echo -e "${GREEN}[RE-Setup]${NC} $*"; }
warn()  { echo -e "${YELLOW}[RE-Setup]${NC} $*"; }
error() { echo -e "${RED}[RE-Setup]${NC} $*" >&2; exit 1; }

echo ""
echo "============================================================"
ok "Termux Reverse Engineering Setup"
echo "============================================================"

# 1. Update packages
info "Updating packages..."
pkg update -y
pkg upgrade -y

# 2. Install core RE tools
info "Installing binutils (readelf, nm, objdump, strings)..."
pkg install -y binutils

info "Installing radare2 (advanced RE)..."
pkg install -y radare2

info "Installing python + pip..."
pkg install -y python

info "Installing frida (runtime instrumentation)..."
pip install frida frida-tools 2>/dev/null || warn "frida install failed — optional"

info "Installing apktool + jadx (APK reverse engineering)..."
pkg install -y apktool jadx 2>/dev/null || warn "apktool/jadx not available via pkg"

# 3. Create working directory
RE_DIR="$HOME/re-tools"
mkdir -p "$RE_DIR"

# 4. Create helper scripts

# --- Helper: nm offsets ---
cat > "$RE_DIR/offsets.sh" << 'OFFSET_EOF'
#!/usr/bin/env bash
# Usage: ./offsets.sh <lib.so> [function_name]
if [ -z "$1" ]; then
    echo "Usage: $0 <lib.so> [function_name]"
    exit 1
fi
LIB="$1"
if [ ! -f "$LIB" ]; then
    echo "File not found: $LIB"
    exit 1
fi
echo "=== Symbols in $LIB ==="
nm -D "$LIB" 2>/dev/null || nm "$LIB"
if [ -n "$2" ]; then
    echo ""
    echo "=== Searching for: $2 ==="
    nm -D "$LIB" 2>/dev/null | grep -i "$2" || nm "$LIB" | grep -i "$2"
fi
OFFSET_EOF

# --- Helper: strings ---
cat > "$RE_DIR/strings.sh" << 'STRINGS_EOF'
#!/usr/bin/env bash
# Usage: ./strings.sh <lib.so> [search_term]
if [ -z "$1" ]; then
    echo "Usage: $0 <lib.so> [search_term]"
    exit 1
fi
LIB="$1"
if [ ! -f "$LIB" ]; then
    echo "File not found: $LIB"
    exit 1
fi
echo "=== Strings in $LIB ==="
strings "$LIB" | head -100
if [ -n "$2" ]; then
    echo ""
    echo "=== Searching for: $2 ==="
    strings "$LIB" | grep -i "$2"
fi
STRINGS_EOF

# --- Helper: disassemble ---
cat > "$RE_DIR/disasm.sh" << 'DISASM_EOF'
#!/usr/bin/env bash
# Usage: ./disasm.sh <lib.so> [function_name]
if [ -z "$1" ]; then
    echo "Usage: $0 <lib.so> [function_name]"
    exit 1
fi
LIB="$1"
if [ ! -f "$LIB" ]; then
    echo "File not found: $LIB"
    exit 1
fi
echo "=== Disassembly of $LIB ==="
objdump -d "$LIB" | head -200
if [ -n "$2" ]; then
    echo ""
    echo "=== Around function: $2 ==="
    objdump -d "$LIB" | grep -A 50 "<$2>" || objdump -d "$LIB" | grep -i -A 50 "$2"
fi
DISASM_EOF

# --- Helper: readelf info ---
cat > "$RE_DIR/info.sh" << 'INFO_EOF'
#!/usr/bin/env bash
# Usage: ./info.sh <lib.so>
if [ -z "$1" ]; then
    echo "Usage: $0 <lib.so>"
    exit 1
fi
LIB="$1"
if [ ! -f "$LIB" ]; then
    echo "File not found: $LIB"
    exit 1
fi
echo "=== ELF Header ==="
readelf -h "$LIB"
echo ""
echo "=== Sections ==="
readelf -S "$LIB"
echo ""
echo "=== Dynamic Symbols ==="
readelf --dyn-syms "$LIB" | head -50
INFO_EOF

# Make helpers executable
chmod +x "$RE_DIR"/*.sh

# 5. Add to PATH
RE_BIN="$RE_DIR"
if [[ ":$PATH:" != *":$RE_BIN:"* ]]; then
    echo "" >> ~/.bashrc
    echo "# RE Tools" >> ~/.bashrc
    echo "export PATH=\"$RE_BIN:\$PATH\"" >> ~/.bashrc
    ok "Added $RE_BIN to PATH in ~/.bashrc"
fi

# 6. Verify installations
echo ""
ok "Installation complete!"
echo ""
echo "=== Installed Tools ==="
command -v readelf && echo "  ✓ readelf"
command -v nm && echo "  ✓ nm"
command -v objdump && echo "  ✓ objdump"
command -v strings && echo "  ✓ strings"
command -v r2 && echo "  ✓ radare2 (r2)"
command -v apktool && echo "  ✓ apktool"
command -v jadx && echo "  ✓ jadx"
command -v frida && echo "  ✓ frida"

echo ""
echo "============================================================"
ok "Setup Done! Next steps:"
echo "============================================================"
echo ""
echo "1. Restart Termux OR run: source ~/.bashrc"
echo ""
echo "2. Copy your .so file to phone, then run:"
echo "   offsets.sh /sdcard/Download/libyourfile.so"
echo ""
echo "3. Search specific function:"
echo "   offsets.sh /sdcard/Download/libyourfile.so function_name"
echo ""
echo "4. See strings:"
echo "   strings.sh /sdcard/Download/libyourfile.so"
echo ""
echo "5. Disassemble:"
echo "   disasm.sh /sdcard/Download/libyourfile.so"
echo ""
echo "6. Full ELF info:"
echo "   info.sh /sdcard/Download/libyourfile.so"
echo ""
echo "7. Radare2 (interactive):"
echo "   r2 -A /sdcard/Download/libyourfile.so"
echo ""
echo "============================================================"
