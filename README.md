# OpenCode Mobile APK Builder

Apple Design se banaya gaya OpenCode mobile APK builder. Termux (Android) pe chalta hai — bina PC ke APK banao.

## Features

- ✅ **Apple Design** — Dark mode, SF Pro typography, rounded corners
- ✅ **Official OpenCode Web UI** — Desktop feel mobile pe
- ✅ **ADB Pairing** — In-app wireless debugging (koi external app nahi chahiye)
- ✅ **All Files Access** — Poora storage access
- ✅ **proot Debian** — apt se kuch bhi install (python, node, gcc...)
- ✅ **One command install** — Termux pe run karo, APK banao

## Quick Start

```bash
# Termux mein
pkg install git openjdk-17 android-tools aapt2 d8 zip apksigner
git clone https://github.com/YOUR_USERNAME/opencode-apk-builder.git
cd opencode-apk-builder
chmod +x build.sh
./build.sh
```

APK mil jayega: `~/OpenCode.apk` ya `/sdcard/Download/OpenCode.apk`

## Install APK pe

1. Download folder se `OpenCode.apk` install karo
2. "Install unknown apps" enable karo apne file manager ke liye
3. App kholo → API key paste karo → Start coding!

## Architecture

```
APK
├── lib/arm64-v8a/
│   ├── libopencode.so    (OpenCode V2 musl binary)
│   ├── libldmusl.so      (musl loader)
│   ├── libstdc++.so.6    (C++ runtime)
│   └── libgcc_s.so.1
├── assets/www/
│   └── index.html        (Apple Design web UI)
├── classes.dex           (Java app shell)
└── AndroidManifest.xml
```

## Requirements

- Termux (Android)
- openjdk-17
- android-tools (aapt2, d8, apksigner)
- zip
- ~250MB free space

## License

MIT