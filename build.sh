#!/bin/bash
# OpenCode Mobile APK Builder — Proper aapt2 build with framework-res
set -euo pipefail

ROOT="$HOME/.opencode-build"
OUT="/sdcard/Download/OpenCode.apk"
KEYSTORE="$HOME/.debug.keystore"

echo "[1/8] Cleaning..."
rm -rf "$ROOT/out" "$ROOT/stubs-out" "$ROOT/apk" "$ROOT/res_compiled" "$ROOT/unsigned.apk" "$OUT"
mkdir -p "$ROOT/out" "$ROOT/stubs-out" "$ROOT/apk/lib/arm64-v8a" "$ROOT/apk/assets/www" "$ROOT/apk/META-INF"

echo "[2/8] Compiling Android stubs..."
javac --release 8 -d "$ROOT/stubs-out" $(find "$ROOT/stubs" -name "*.java") 2>&1 || true

echo "[3/8] Compiling app Java..."
javac --release 8 -cp "$ROOT/stubs-out" -d "$ROOT/out" "$ROOT"/src/com/opencode/mobile/*.java 2>&1 || true

echo "[4/8] DEX conversion (d8)..."
d8 --release --output "$ROOT" "$ROOT"/out/com/opencode/mobile/*.class 2>&1 || true

echo "[5/8] Copy native libs..."
cp "$ROOT"/lib/arm64-v8a/libopencode.so "$ROOT/apk/lib/arm64-v8a/"
cp "$ROOT"/lib/arm64-v8a/libldmusl.so "$ROOT/apk/lib/arm64-v8a/"
cp "$ROOT"/lib/arm64-v8a/libstdc++.so.6 "$ROOT/apk/lib/arm64-v8a/"
cp "$ROOT"/lib/arm64-v8a/libstdc++.so "$ROOT/apk/lib/arm64-v8a/"
cp "$ROOT"/lib/arm64-v8a/libgcc_s.so.1 "$ROOT/apk/lib/arm64-v8a/"

echo "[6/8] Copy assets..."
cp "$ROOT"/assets/www/index.html "$ROOT/apk/assets/www/"

echo "[7/8] Build APK with aapt2 + framework-res..."
rm -rf "$ROOT/res_compiled"
mkdir -p "$ROOT/res_compiled"
aapt2 compile -o "$ROOT/res_compiled" \
  "$ROOT"/res/values/colors.xml \
  "$ROOT"/res/values-night/colors.xml \
  "$ROOT"/res/values/strings.xml \
  "$ROOT"/res/values/dimens.xml \
  "$ROOT"/res/drawable/*.xml \
  "$ROOT"/res/layout/*.xml \
  "$ROOT"/res/mipmap-anydpi-v26/*.xml 2>&1 || true

aapt2 link -o "$ROOT/apk/base.apk" \
  --manifest "$ROOT/res/AndroidManifest.xml" \
  -R "$ROOT"/res_compiled/*.flat \
  -I /system/framework/framework-res.apk \
  --min-sdk-version 24 \
  --auto-add-overlay 2>&1 || true

# Add classes.dex, assets, lib to base.apk
cd "$ROOT/apk"
cp base.apk unsigned.apk
cp "$ROOT/classes.dex" .
zip -r -X unsigned.apk classes.dex assets/ lib/ 2>/dev/null || true

echo "[8/8] Sign APK..."
if [ -f "$KEYSTORE" ]; then
  apksigner sign --ks "$KEYSTORE" --ks-key-alias androiddebugkey --ks-pass pass:android --min-sdk-version 24 --out "$OUT" "$ROOT/apk/unsigned.apk" 2>&1 || {
    echo "Signing failed, outputting unsigned.apk"
    cp "$ROOT/apk/unsigned.apk" "$OUT"
  }
else
  echo "No keystore — outputting unsigned APK"
  cp "$ROOT/apk/unsigned.apk" "$OUT"
fi

echo ""
echo "============================"
echo "APK: $OUT"
ls -la "$OUT" 2>/dev/null || echo "Build incomplete"
echo "============================"
