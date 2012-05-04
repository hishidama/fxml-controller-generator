=========================
fxml-controller-generator
=========================
`JavaFX <http://www.ne.jp/asahi/hishidama/home/tech/java/fx/index.html>`_ の
`fxmlファイル <http://www.ne.jp/asahi/hishidama/home/tech/java/fx/fxml/index.html>`_
（ `Scene Builder <http://www.ne.jp/asahi/hishidama/home/tech/java/fx/sb/index.html>`_ で作成する想定）から
`コントローラークラス <http://www.ne.jp/asahi/hishidama/home/tech/java/fx/fxml/controller.html>`_ のJavaソースを生成するツールです。

--------
動作環境
--------
* `Java <http://www.ne.jp/asahi/hishidama/home/tech/java/index.html>`_ （JDK1.6以降）
* `Scala <http://www.ne.jp/asahi/hishidama/home/tech/scala/index.html>`_ 2.9
* `JavaFX <http://www.ne.jp/asahi/hishidama/home/tech/java/fx/index.html>`_ 2.1（2.0は非対応）


--------------------------
インストールおよび実行方法
--------------------------
fxml-controller-generatorのjarファイルをダウンロードして適当な場所に配置し、scalaコマンドで実行するだけです。

→ `fxml-controller-generator.jar <https://github.com/hishidama/fxml-controller-generator/tree/master/fxmlControllerGen/release>`_

| Windowsの場合は、fxml-controller-generator.jarと同じ場所にrun.batを配置し、それを実行すればいいです。
| ただし、run.bat内部のクラスパスをご自身の環境に合わせて変えて下さい。（主にJavaFXのjfxrt.jarのパス）
* run.batを実行した場合、コマンドプロンプトが開きます。何か例外（エラー）が発生すると、ここにスタックトレースが出力されます。


--------------------
アンインストール方法
--------------------
配置したjarファイル類を手動で削除して下さい。

また、ユーザーのホームディレクトリー（WindowsだとC:\\users\\hishidamaといった感じ）にプロパティーファイル（.fxml-controller-generator.properties）が作られますので、それも削除して下さい。


--------
使用方法
--------
fxml-controller-generatorを実行すると、ウィンドウが開きます。

fxml file
  | 読み込むfxmlファイルを指定します。
  | 指定した際に「controller write directory」項目が空だと、そちらにもfxmlファイルと同じディレクトリーが入力されます。
  | なお、ファイルをドラッグしてウィンドウへドロップすると、「fxml file」項目への入力になります。
fxml encoding
  | fxmlファイルのエンコーディングを指定します。普通はUTF-8なので何も入力しないで構いません。
  | （WindowsのScene Builder 1.0のバグで、日本語を使った場合にShift_JISでfxmlファイルが生成されます。それを読み込む為にMS932を指定します）
controller write directory
  生成するJavaソースを書き込むディレクトリーを指定します。
controller class name prefix
  | 生成するコントローラークラス名に付ける接頭辞を指定します。
  | （Superと入力すると、fxmlファイル内のコントローラークラス名がMyControllerなら、生成されるのはSuperMyControllerになります）
controller class name suffix
  | 生成するコントローラークラス名に付ける接尾辞を指定します。
controller encoding
  | 生成するJavaソースのエンコーディングを指定します。
generate
  | このボタンを押すと、コントローラークラスのJavaソースを生成します。
  | 生成が成功すると、ボタンの右側に「success クラス名」というメッセージが表示されます。
  | （失敗した場合はエラーメッセージが表示されます。例外が発生している場合は、実行元のコンソールにスタックトレースが出力されます）
save settings
  | このボタンを押すと、現在入力されている設定（ファイル名を除く）をプロパティーファイルに保存します。
  | プロパティーファイルは、ユーザーのホームディレクトリーの下に.fxml-controller-generator.propertiesという名前で作られます。


------
作成者
------
`ひしだま <http://www.ne.jp/asahi/hishidama/home/tech/soft/index.html>`_

