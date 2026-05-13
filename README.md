# JetMeme

このプラグインは、GoLandをやかましくします。

## 使い方

GoLandの設定から、JetMemeを有効にしてください。
`.wav`ファイルを含むディレクトリを選択すると、GoLandのイベントに合わせて音が鳴ります。

対応イベント:

- エディタ上のエラー
- Run Configurationの失敗
- Terminalコマンドの失敗

サイドバーのJetMemeタブから、音のテスト再生、停止、イベントごとのON/OFF切り替えができます。


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
