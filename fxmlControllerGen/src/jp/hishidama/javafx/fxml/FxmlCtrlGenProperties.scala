package jp.hishidama.javafx.fxml
import java.io._
import java.util.Properties
import javafx.scene.control.TextField

object FxmlCtrlGenProperties {
  lazy val propertyFile = new File(System.getProperty("user.home"), ".fxml-controller-generator.properties")
  val ENCODING = "UTF-8"

  val KEY_FXMLFILE_ENCODING = "fxmlfile.prefix"
  val KEY_CONTROLLER_PREFIX = "controller.prefix"
  val KEY_CONTROLLER_SUFFX = "controller.suffix"
  val KEY_CONTROLLER_ENCODING = "controller.encoding"
  val KEY_FILECHOOSER_DIR = "filechooser.dir"

  def load(c: GenMainController): Unit = {
    val p = new Properties

    val r = new BufferedReader(new InputStreamReader(new FileInputStream(propertyFile), ENCODING))
    try {
      p.load(r)
    } finally {
      r.close()
    }

    def set(t: TextField, key: String) {
      val v = p.getProperty(key, "")
      t.setText(v)
    }
    set(c.fxmlEncoding, KEY_FXMLFILE_ENCODING)
    set(c.controllerPrefix, KEY_CONTROLLER_PREFIX)
    set(c.controllerSuffix, KEY_CONTROLLER_SUFFX)
    set(c.controllerEncoding, KEY_CONTROLLER_ENCODING)

    val v = p.getProperty(KEY_FILECHOOSER_DIR, "")
    if (v.nonEmpty) {
      val f = getDir(v)
      if (f.exists) {
        c.ffc.setInitialDirectory(f)
        c.dfc.setInitialDirectory(f)
      }
    }
  }

  def save(c: GenMainController): Unit = {
    val p = new Properties

    def set(t: TextField, key: String) {
      p.setProperty(key, t.getText)
    }
    set(c.fxmlEncoding, KEY_FXMLFILE_ENCODING)
    set(c.controllerPrefix, KEY_CONTROLLER_PREFIX)
    set(c.controllerSuffix, KEY_CONTROLLER_SUFFX)
    set(c.controllerEncoding, KEY_CONTROLLER_ENCODING)

    set(c.fxmlFile, KEY_FILECHOOSER_DIR)

    val w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(propertyFile), ENCODING))
    try {
      p.store(w, "")
    } finally {
      w.close()
    }
  }

  def getDir(f: String): File = {
    val f1 = new File(f)
    val f2 = if (f1.isFile) f1.getParentFile else f1
    f2
  }
}
