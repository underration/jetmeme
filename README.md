# JetMeme

JetMeme is an unofficial JetBrains IDE plugin that plays a user-configured local sound when errors appear.

The repository intentionally does not include meme sounds or third-party audio files.

## Current status

This project is at the initial scaffold stage.

Planned first features:

- Detect IDE/editor error events.
- Let users choose a local `.wav`, `.mp3`, or `.aiff` file.
- Play the configured sound when an error appears.
- Add a mute toggle and basic cooldown to avoid repeated playback spam.

## Sound files

JetMeme does not include meme sounds or third-party audio files.

Users can configure their own local sound files. Make sure you have the necessary rights or permissions for any audio file you use.

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
