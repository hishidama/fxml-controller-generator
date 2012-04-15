@echo off
set BATDIR=%~dp0

set MAIN_JAR=%BATDIR%\fxml-controller-generator.jar
set JAVAFX_JAR=C:\Program Files (x86)\Oracle\JavaFX Scene Builder 1.0\bin\lib\jfxrt.jar

scala -cp "%MAIN_JAR%;%JAVAFX_JAR%"  jp.hishidama.javafx.fxml.FxmlControllerGenerator
