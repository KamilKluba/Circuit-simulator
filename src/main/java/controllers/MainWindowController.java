package controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;

public class MainWindowController {
    private GraphicsContext graphicsContext;

    @FXML private Canvas canvas;

    @FXML
    public void initialize(){
        graphicsContext = canvas.getGraphicsContext2D();

        Image image = new Image(getClass().getResource("/graphics/and_gate_right.png").toExternalForm(), 200, 200, false, false);

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(5);
        graphicsContext.strokeLine(200, 200, 700, 400);
        graphicsContext.drawImage(image, 500, 500);
    }

    public void loadCircuit(File file){

    }
}
