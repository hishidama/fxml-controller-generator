package jp.hishidama.javafx.fxml;

import java.lang.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

@SuppressWarnings("unused")
public class SuperGenMainController implements javafx.fxml.Initializable {

	public TextField fxmlFile;

	public TextField fxmlEncoding;

	public TextField controllerDir;

	public TextField controllerPrefix;

	public TextField controllerSuffix;

	public TextField controllerEncoding;

	public TextField messageLabel;

	@Override public void initialize(java.net.URL location,  java.util.ResourceBundle resources) {}

	public void handleDragDropped(javafx.scene.input.DragEvent event) {}

	public void handleDragOver(javafx.scene.input.DragEvent event) {}

	public void handleFxmlFile(javafx.event.ActionEvent event) {}

	public void handleControllerDirDropped(javafx.scene.input.DragEvent event) {}

	public void handleControllerDirOver(javafx.scene.input.DragEvent event) {}

	public void handleControllerDir(javafx.event.ActionEvent event) {}

	public void handleSaveSettings(javafx.event.ActionEvent event) {}

	public void handleGenerate(javafx.event.ActionEvent event) {}
}
