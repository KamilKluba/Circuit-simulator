package main;

import components.ZoomableScrollPane;
import controllers.MainWindowController;
import controllers.StartWindowController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {
    private StartWindowController startWindowController;
    private FlowPane flowPane;
    private MainWindowController mainWindowController;
    private BorderPane borderPane;
    private Scene scene;
    private Stage primaryStage;
    private ZoomableScrollPane zsp;

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
        borderPane = loaderMainWindow.load();
        mainWindowController = loaderMainWindow.getController();
        Pane pane = mainWindowController.getPaneWorkspace();
        Canvas canvas = mainWindowController.getCanvas();
        zsp = new ZoomableScrollPane(pane);
        borderPane.setCenter(zsp);

        scene = new Scene(flowPane);

        startWindowController.myInitialize(this);
        mainWindowController.myInitialize(this);

        this.primaryStage = primaryStage;
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Symulator układów cyfrowych");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage.show();
    }

    public void changeScene(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setX(0.5 * (screenSize.width - 1024));
        primaryStage.setY(0.5 * (screenSize.height - 768));
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        primaryStage.show();
        scene.setRoot(borderPane);
        borderPane.requestFocus();
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

    public ZoomableScrollPane getZsp() {
        return zsp;
    }
}
