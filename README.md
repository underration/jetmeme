# JetMeme

JetMeme is an unofficial JetBrains IDE plugin that plays a user-configured local sound when errors appear.

The repository intentionally does not include meme sounds or third-party audio files.

## Current status

This project is at the initial implementation stage.

Implemented:

- Detect editor error highlights after IDE analysis finishes.
- Let users choose a local sound file from `Settings | Tools | JetMeme`.
- Play a configured local WAV file when errors appear.
- Add an enable toggle and cooldown to avoid repeated playback spam.

Planned:

- Add broader audio format support.
- Refine error detection behavior after real-world GoLand testing.

## Sound files

JetMeme does not include meme sounds or third-party audio files.

Users can configure their own local sound files. Make sure you have the necessary rights or permissions for any audio file you use.

The first implementation is intended for WAV files. Other formats may depend on runtime audio codec support.

## Development

Requirements:

- JDK 21
- GoLand or IntelliJ IDEA

Run the plugin in a sandbox IDE:

```bash
./gradlew runIde
```

Build a plugin ZIP:

```bash
./gradlew buildPlugin
```
