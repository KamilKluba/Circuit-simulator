package controllers;

import data.Main;
import data.Sizes;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

public class StartWindowController {
    private Main main;
    private GraphicsContext gc;
    private Image imageRight;
    private Image imageLeft;

    @FXML private Button buttonNewCircuit;
    @FXML private Button buttonLoadCircuit;
    @FXML private Button buttonExit;
    @FXML private Label labelError;
    @FXML private Canvas canvas;

    @FXML
    private void initialize(){
        imageRight = new Image(getClass().getResource("/graphics/or2_gate_off.png").toExternalForm(), Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false);
        ImageView imageView = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_off.png").toExternalForm(), Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false));
        imageView.setRotate(180);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        imageLeft = imageView.snapshot(snapshotParameters, null);

        gc = canvas.getGraphicsContext2D();
    }

    public void myInitialize(Main main){
        this.main = main;
    }

    public void actionNewCircuit(){
        main.changeScene();
    }

    public void actionLoadCircuit(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik do załadowania");
            File file = fileChooser.showOpenDialog(main.getPrimaryStage());
            main.getMainWindowController().loadCircuit(file);
        } catch(Exception e){
            e.printStackTrace();
            labelError.setText("Błąd podczas ładowania obwodu z pliku!");
        }
    }

    public void actionEnterNewCircuitButton(){
        gc.drawImage(imageRight, 0, 12.5);
        gc.drawImage(imageLeft, 225, 12.5);
    }

    public void actionEnterLoadCircuitButton(){
        gc.drawImage(imageRight, 0, 47.5);
        gc.drawImage(imageLeft, 225, 47.5);
    }

    public void actionEnterExitButton(){
        gc.drawImage(imageRight, 0, 82.5);
        gc.drawImage(imageLeft, 225, 82.5);
    }

    public void actionExitButtons(){
        gc.clearRect(0, 0, 300, 150);
    }

    public void actionExit(){
        System.exit(0);
    }
}
