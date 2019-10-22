package main;

import controllers.MainWindowController;
import controllers.StartWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {
    private StartWindowController startWindowController;
    private FlowPane flowPane;
    private MainWindowController mainWindowController;
    private BorderPane anchorPane;
    private Scene scene;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        javafx.scene.text.Font.loadFont(Main.class.getResourceAsStream("/fonts/Bank Gothic Medium BT.ttf"), 0);

        FXMLLoader loaderStartWindow = new FXMLLoader(getClass().getResource("/fxml/StartWindow.fxml"));
        flowPane = loaderStartWindow.load();
        startWindowController = loaderStartWindow.getController();

        FXMLLoader loaderMainWindow = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        anchorPane = loaderMainWindow.load();
        mainWindowController = loaderMainWindow.getController();

        scene = new Scene(flowPane);

        startWindowController.myInitialize(this);
        mainWindowController.myInitialize(this);

        this.primaryStage = primaryStage;
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Symulator układów cyfrowych");
        primaryStage.show();
    }

    public void changeScene(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setX((screenSize.width - 1024) / 2);
        primaryStage.setY((screenSize.height - 768) / 2);
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        primaryStage.show();
        scene.setRoot(anchorPane);
    }

    public StartWindowController getStartWindowController() {
        return startWindowController;
    }

    public FlowPane getFlowPane() {
        return flowPane;
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
