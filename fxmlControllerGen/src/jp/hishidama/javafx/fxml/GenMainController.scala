package jp.hishidama.javafx.fxml

import java.io._
import java.net.URL
import java.util.ResourceBundle
import javafx.scene.input.DragEvent
import javafx.event.ActionEvent
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.DirectoryChooser
import javafx.fxml.Initializable
import scala.io.Source
import scala.xml.factory.XMLLoader
import scala.xml.Elem
import scala.xml.ProcInstr

class GenMainController extends SuperGenMainController with Initializable {

  override def initialize(url: URL, resourcebundle: ResourceBundle): Unit = {
    try {
      FxmlCtrlGenProperties.load(this)
    } catch { case _ => }
  }

  override def handleSaveSettings(event: ActionEvent) {
    FxmlCtrlGenProperties.save(this)
  }

  override def handleDrop(event: DragEvent) {
    println(event)
  }

  lazy val ffc = {
    val fc = new FileChooser
    fc.setTitle("fxmlファイル")
    fc.getExtensionFilters.addAll(new ExtensionFilter("FXML", "*.fxml"), new ExtensionFilter("ALL", "*.*"))
    fc
  }

  override def handleFxmlFile(event: ActionEvent) {
    val d = FxmlCtrlGenProperties.getDir(fxmlFile.getText)
    if (d.exists) ffc.setInitialDirectory(d)
    val f = ffc.showOpenDialog(null)
    if (f != null) {
      fxmlFile.setText(f.getCanonicalPath)
      if (controllerDir.getText().isEmpty) {
        controllerDir.setText(f.getParent)
      }
    }
  }

  lazy val dfc = {
    val fc = new DirectoryChooser
    fc.setTitle("出力先ディレクトリー")
    fc
  }

  override def handleControllerDir(event: ActionEvent) {
    val d = FxmlCtrlGenProperties.getDir(controllerDir.getText)
    if (d.exists) dfc.setInitialDirectory(d)
    val f = dfc.showDialog(null)
    if (f != null) {
      controllerDir.setText(f.getCanonicalPath)
    }
  }

  override def handleGenerate(event: ActionEvent) {
    try {
      generate()
    } catch {
      case e =>
        def getCause(e: Throwable): Throwable = {
          val c = e.getCause
          if (c != null) getCause(c) else e
        }
        messageLabel.setText(getCause(e).toString)
        throw e
    }
  }

  def generate(): Unit = {
    val f = new File(fxmlFile.getText)
    val fis = new FileInputStream(f)
    try {
      val enc = fxmlEncoding.getText
      val reader = if (enc.isEmpty) new InputStreamReader(fis) else new InputStreamReader(fis, enc)
      try {
        generate(reader)
      } finally {
        reader.close()
      }
    } finally {
      fis.close()
    }
  }

  def generate(r: Reader): Unit = {
    val loader = new XMLLoader[Elem] {
      override lazy val adapter = super.adapter
    }
    val xml = loader.load(r)
    val seq = loader.adapter.hStack.toSeq
    val pis = seq.collect { case pi: ProcInstr => pi }.reverse
    val imports = pis.collect { case p: ProcInstr if p.target == "import" => p.proctext }

    val gen = new FxmlControllerGenerator(xml, imports)
    if (gen.controllerClassName.isEmpty) {
      messageLabel.setText("fx:controller not found")
    } else {
      val wname = controllerPrefix.getText + gen.controllerClassName + controllerSuffix.getText
      val wf = new File(controllerDir.getText, wname + ".java")
      val fos = new FileOutputStream(wf)
      try {
        val enc = controllerEncoding.getText
        val ww = new BufferedWriter(
          if (enc.isEmpty) new OutputStreamWriter(fos) else new OutputStreamWriter(fos, enc))
        try {
          gen.writeJava(ww, wname)
        } finally {
          ww.close()
        }
      } finally {
        fos.close()
      }
      messageLabel.setText("success " + wname)
    }
  }
}
