package controllers;

import components.Component;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ChangeCanvasSizeController {
    @FXML private Stage stage;
    @FXML private Label labelCurrentSize;
    @FXML private Label labelNewSize;
    @FXML private Label labelAreaQuotient;
    @FXML private Slider sliderHorizontalSize;
    @FXML private Slider sliderVerticalSize;
    @FXML private Label labelError;

    private ArrayList<Component> arrayListComponents;
    private Canvas canvas;
    private Pane paneWorkspace;

    private double horizontalValue;
    private double verticalValue;

    public void initialize(){
        sliderHorizontalSize.valueProperty().addListener(e -> actionScrollHorizontalChanged());
        sliderVerticalSize.valueProperty().addListener(e -> actionScrollVerticalChanged());

        horizontalValue = sliderHorizontalSize.getValue() - sliderHorizontalSize.getValue() % 5;
        verticalValue = sliderVerticalSize.getValue() - sliderVerticalSize.getValue() % 5;
    }

    public void myInitialize(){
        labelCurrentSize.setText(canvas.getWidth() + " x " + canvas.getHeight());
        sliderVerticalSize.setValue(canvas.getHeight());
        sliderHorizontalSize.setValue(canvas.getWidth());
    }

    public void actionScrollHorizontalChanged(){
        horizontalValue = sliderHorizontalSize.getValue() - sliderHorizontalSize.getValue() % 5;
        labelNewSize.setText(horizontalValue + " x " + verticalValue);
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        labelAreaQuotient.setText(format.format((sliderHorizontalSize.getValue() * sliderVerticalSize.getValue()) /
                (canvas.getWidth() * canvas.getHeight())));
    }

    public void actionScrollVerticalChanged(){
        verticalValue = sliderVerticalSize.getValue() - sliderVerticalSize.getValue() % 5;
        labelNewSize.setText(horizontalValue + " x " + verticalValue);
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        labelAreaQuotient.setText(format.format((sliderHorizontalSize.getValue() * sliderVerticalSize.getValue()) /
                (canvas.getWidth() * canvas.getHeight())));
    }

    public void actionCancel(){
        stage.close();
    }

    public void actionChangeSize(){
        double minX = 1000000;
        double maxX = 0;
        double minY = 1000000;
        double maxY = 0;

        for(Component c : arrayListComponents){
            if(c.getPointCenter().getX() < minX){
                minX = c.getPointCenter().getX();
            }
            if(c.getPointCenter().getX() > maxX){
                maxX = c.getPointCenter().getX();
            }
            if(c.getPointCenter().getY() < minY){
                minY = c.getPointCenter().getY();
            }
            if(c.getPointCenter().getY() > maxY){
                maxY = c.getPointCenter().getY();
            }
        }

        if(maxX - minX > horizontalValue - 200 || maxY - minY > verticalValue - 200){
            Executors.newFixedThreadPool(1).execute(() -> {
                Platform.runLater(() -> {
                    labelError.setText("Za male pole robocze!");
                });
                try {
                    Thread.sleep(3000);
                    Platform.runLater(() -> {
                        labelError.setText("");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        else {
            canvas.setWidth(horizontalValue);
            canvas.setHeight(verticalValue);
            paneWorkspace.setPrefWidth(horizontalValue);
            paneWorkspace.setPrefHeight(verticalValue);

            for (Component c : arrayListComponents){
                c.getPointCenter().setX(c.getPointCenter().getX() - minX + 200);
                c.getPointCenter().setY(c.getPointCenter().getY() - minY + 200);

            }
            stage.close();
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }


    public void setPaneWorkspace(Pane paneWorkspace) {
        this.paneWorkspace = paneWorkspace;
    }

    public void setArrayListComponents(ArrayList<Component> arrayListComponents) {
        this.arrayListComponents = arrayListComponents;
    }
}
