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

## インストール

1. [Releases](https://github.com/underration/jetmeme/releases) から最新版の `jetmeme-*.zip` をダウンロード
2. GoLandを開く
3. `Settings | Plugins` を開く
4. 歯車アイコンから `Install Plugin from Disk...` を選択
5. ダウンロードしたZIPを選択
6. GoLandを再起動
7. `Settings | Tools | JetMeme` で `.wav` ファイル、または `.wav` を含むディレクトリを指定

## GitHub Releasesで配布する手順

プラグインZIPを作成します。

```bash
JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home ./gradlew buildPlugin
```

生成されるファイル:

```text
build/distributions/jetmeme-0.1.0.zip
```

GitHubの [Releases](https://github.com/underration/jetmeme/releases) で `Draft a new release` を押し、タグとタイトルを設定します。

例:

```text
v0.1.0
```

リリース説明の例:

```markdown
# JetMeme v0.1.0

初回リリースです。

## 機能

- GoLandのエディタエラー時にサウンド再生
- Run Configurationの失敗時にサウンド再生
- Terminalコマンド失敗時にサウンド再生
- サイドバーのJetMemeタブ
- サウンドのテスト再生
- 再生停止
- `.wav`ファイルまたは`.wav`を含むディレクトリ指定
- ディレクトリ指定時はランダム再生

## 注意

JetMemeはmeme音源や第三者音源を同梱していません。
利用者自身が権利を持つ音声ファイルを指定してください。
```

最後に `build/distributions/jetmeme-0.1.0.zip` を添付して `Publish release` を押します。

## 音源について

JetMemeはmeme音源や第三者音源を同梱しません。
利用者自身が権利を持つローカル音声ファイルを指定してください。

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
