package jp.hishidama.javafx.fxml

import java.io._
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import scala.collection.JavaConverters._
import scala.io.Source
import scala.xml.factory.XMLLoader
import scala.xml.Elem
import scala.xml.Node
import scala.xml.ProcInstr

object FxmlControllerGenerator {
  def main(args: Array[String]) {
    Application.launch(classOf[Main], args: _*)
  }

  class Main extends Application {
    override def start(stage: Stage): Unit = {
      val root: Parent = FXMLLoader.load(getClass.getResource("controller-gen-main.fxml"))
      stage.setTitle("fxml to controller")
      stage.setScene(new Scene(root))
      stage.show()
    }
  }
}

class FxmlControllerGenerator(root: Elem, imports: Seq[String]) {
  val rootAttrMap = root.attributes.asAttrMap
  val controllerName = rootAttrMap.getOrElse("fx:controller", "")
  val (packName, controllerClassName) = splitName(controllerName)

  private def splitName(fullName: String) = {
    val n = fullName.lastIndexOf('.')
    if (n >= 0) {
      (fullName.substring(0, n), fullName.substring(n + 1))
    } else {
      ("", fullName)
    }
  }

  def writeJava(w: Writer, className: String): Unit = {
    //パッケージ宣言
    if (packName.nonEmpty) {
      w.write("package " + packName + ";\n\n")
    }

    //インポート宣言
    imports.foreach { i =>
      w.write("import " + i + ";\n")
    }
    w.write("\n")

    //クラス
    w.write("@SuppressWarnings(\"unused\")\n")
    w.write("public class " + className + " implements javafx.fxml.Initializable {\n")
    writeJavaFields(w, root)
    writeJavaInitialize(w)
    writeJavaHandlers(w, root, scala.collection.mutable.Set.empty)
    w.write("}\n")
  }

  def writeJavaFields(w: Writer, elem: Node): Unit = {
    val amap = elem.attributes.asAttrMap
    amap.get("fx:id").foreach { id =>
      val label = elem.label
      w.write("\n\tpublic " + label + " " + id + ";\n")
    }

    elem.child.foreach { writeJavaFields(w, _) }
  }

  def writeJavaInitialize(w: Writer): Unit = {
    w.write("\n\t@Override public void initialize(java.net.URL location, java.util.ResourceBundle resources) {}\n")
  }

  def writeJavaHandlers(w: Writer, elem: Node, set: scala.collection.mutable.Set[String]): Unit = {
    val amap = elem.attributes.asAttrMap
    amap.foreach {
      case (k, v) if v.startsWith("#") =>
        val hname = v.substring(1)
        val ename = eventMap(k.toLowerCase).getName
        val key = hname + " " + ename
        if (!set.contains(key)) {
          set += key
          w.write("\n\tpublic void " + hname + "(" + ename + " event) {}\n")
        }
      case _ =>
    }

    elem.child.foreach { writeJavaHandlers(w, _, set) }
  }

  lazy val eventMap: Map[String, Class[_]] = {
    import javafx.event._
    import javafx.scene.input._
    import javafx.stage._
    val list = Seq(
      classOf[ActionEvent],
      classOf[InputEvent],
      classOf[ContextMenuEvent],
      classOf[DragEvent],
      classOf[InputMethodEvent],
      classOf[KeyEvent],
      classOf[MouseEvent], classOf[MouseDragEvent],
      classOf[ScrollEvent],
      classOf[WindowEvent])

    var map = Map.empty[String, Class[_]]
    list.foreach { c =>
      c.getDeclaredFields.
        collect { case f if f.getType == classOf[EventType[_]] => f.get(null).asInstanceOf[EventType[_]] }.
        foreach { n =>
          map += (("on" + n.getName.replaceAll("[\\_\\-]", "")).toLowerCase -> c)
        }
    }
    //println(map)
    map.withDefaultValue(classOf[Event])
  }
}
