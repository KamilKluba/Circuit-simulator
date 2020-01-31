package main;

import controllers.MainWindowController;
import controllers.StartWindowController;
import data.Names;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class Main extends Application {
    private StartWindowController startWindowController;
    private FlowPane flowPane;
    private MainWindowController mainWindowController;
    private BorderPane borderPane;
    private Scene scene;
    private Stage primaryStage;
    private boolean unsavedChanges = false;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loaderStartWindow = new FXMLLoader(getClass().getResource("/fxml/StartWindow.fxml"));
        flowPane = loaderStartWindow.load();
        startWindowController = loaderStartWindow.getController();

        FXMLLoader loaderMainWindow = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        borderPane = loaderMainWindow.load();
        mainWindowController = loaderMainWindow.getController();
        Pane pane = mainWindowController.getPaneWorkspace();
        Canvas canvas = mainWindowController.getCanvas();

        scene = new Scene(flowPane);

        startWindowController.myInitialize(this);
        mainWindowController.myInitialize(this);

        this.primaryStage = primaryStage;
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Symulator układów cyfrowych");
        primaryStage.getIcons().add(new Image(getClass().getResource("/graphics/and/and2_gate_off.png").toExternalForm()));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(unsavedChanges){
                    ButtonType confirmClose = new ButtonType("Tak", ButtonBar.ButtonData.OK_DONE);
                    ButtonType discardClose = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", confirmClose, discardClose);
                    alert.setTitle(Names.unsavedChangesTitle);
                    alert.setHeaderText(Names.unsavedChangesHeader);
                    Optional<ButtonType> answer = alert.showAndWait();
                    if(answer.get().getButtonData().isDefaultButton()){
                        System.exit(0);
                    }
                    else{
                        event.consume();
                    }
                }
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

    public BorderPane getBorderPane() {
        return borderPane;
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

    public void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }
}
