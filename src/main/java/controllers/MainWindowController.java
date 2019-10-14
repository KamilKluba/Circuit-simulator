package controllers;

import data.Line;
import data.Main;
import data.Sizes;
import gates.And.And2;
import gates.And.Or2;
import gates.Gate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;

public class MainWindowController {
    private Main main;
    private ArrayList<Gate> arrayListCreatedGates = new ArrayList<>();
    private ArrayList<Line> arrayListCreatedLines = new ArrayList<>();
    private GraphicsContext graphicsContext;
    private ArrayList<Gate> arrayListPossibleGates = new ArrayList<>();
    private boolean coveredError = false;

    @FXML private Canvas canvas;
    @FXML private TextField textFieldFilterGate;
    @FXML private TableView<Gate> tableViewComponents;
    @FXML private TableColumn<Gate, ImageView> tableColumnComponentsPictures;
    @FXML private TableColumn<Gate, Integer> tableColumnComponentnputsNumber;
    @FXML private Button buttonAddGate;
    @FXML private Button buttonDeleteGate;

    @FXML
    public void initialize(){
        arrayListPossibleGates.add(new And2());
        arrayListPossibleGates.add(new Or2());

        ObservableList<Gate> ol = FXCollections.observableList(arrayListPossibleGates);
        tableViewComponents.setItems(ol);

        tableColumnComponentsPictures.setCellValueFactory(new PropertyValueFactory<>("image"));
        tableColumnComponentnputsNumber.setCellValueFactory(new PropertyValueFactory<>("arrayListInputsSize"));

        graphicsContext = canvas.getGraphicsContext2D();

        Image image = new Image(getClass().getResource("/graphics/or2_gate_right.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false);

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(Sizes.baseLineWidth);
        graphicsContext.strokeLine(200, 200, 700, 400);
        graphicsContext.drawImage(image, 500, 500);

        canvas.setOnMouseClicked(e -> actionCanvasMouseClicked(e.getX(), e.getY()));
        canvas.setOnMouseMoved(e -> actionCanvasMouseMoved(e.getX(), e.getY()));
    }

    public void myInitialize(Main main){
        this.main = main;
        main.getScene().setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));
    }

    public void loadCircuit(File file){

    }

    public void actionAddGate(){

    }

    public void actionDeleteGate(){

    }

    public void actionFilterGate(){

    }

    private void actionCanvasMouseMoved(double x, double y){
        if(coveredError) {
            repaint();

            graphicsContext.setStroke(Color.RED);
            graphicsContext.setLineWidth(1);
            double shiftX = Sizes.baseGateXShift;
            double shiftY = Sizes.baseGateYShift;
            for (Gate g : arrayListCreatedGates) {
                graphicsContext.strokeLine(g.getX() - shiftX, g.getY() - shiftY, g.getX() - shiftX, g.getY() + shiftY);
                graphicsContext.strokeLine(g.getX() - shiftX, g.getY() + shiftY, g.getX() + shiftX, g.getY() + shiftY);
                graphicsContext.strokeLine(g.getX() + shiftX, g.getY() + shiftY, g.getX() + shiftX, g.getY() - shiftY);
                graphicsContext.strokeLine(g.getX() + shiftX, g.getY() - shiftY, g.getX() - shiftX, g.getY() - shiftY);
            }
            graphicsContext.strokeLine(x - shiftX, y - shiftY, x - shiftX, y + shiftY);
            graphicsContext.strokeLine(x - shiftX, y + shiftY, x + shiftX, y + shiftY);
            graphicsContext.strokeLine(x + shiftX, y + shiftY, x + shiftX, y - shiftY);
            graphicsContext.strokeLine(x + shiftX, y - shiftY, x - shiftX, y - shiftY);
        }
    }

    public void actionCanvasMouseClicked(double x, double y){
        if(!checkIfCover(x, y)) {
            try {
                Gate gate = tableViewComponents.getSelectionModel().getSelectedItem();
                String name = gate.getName();
                tableViewComponents.getSelectionModel().clearSelection();

                if (name.equals("And 2")) {
                    Gate newGate = new And2(x, y);
                    arrayListCreatedGates.add(newGate);
                } else if (name.equals("Or 2")) {
                    Gate newGate = new Or2(x, y);
                    arrayListCreatedGates.add(newGate);
                }

                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
            coveredError = false;
        }
        else{
            coveredError = true;
            actionCanvasMouseMoved(x, y);
        }
    }

    private boolean checkIfCover(double x, double y){
        double xShift = Sizes.baseGateXShift;
        double yShift = Sizes.baseGateYShift;

        for(Gate g : arrayListCreatedGates) {
            System.out.println(Math.abs(x - g.getX()) + " " + Math.abs(y - g.getY()) + " " + Sizes.baseGateXSize + " " + Sizes.baseGateYSize);
            if (Math.abs(x - g.getX()) <= Sizes.baseGateXSize &&
                    Math.abs(y - g.getY()) <= Sizes.baseGateYSize) {
                return true;
            }
        }
        return false;
    }

    private void repaint(){
        graphicsContext.clearRect(0, 0, canvas.getWidth() + 1, canvas.getHeight() + 1);
        for(Gate g : arrayListCreatedGates){
            graphicsContext.drawImage(g.getImage().getImage(), g.getX() - Sizes.baseGateXShift, g.getY() - Sizes.baseGateYShift);
        }

        for(Line l : arrayListCreatedLines){
            graphicsContext.strokeLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
        }
    }

    private void actionCanvasKeyPressed(KeyCode code){
        if(code == KeyCode.ESCAPE){
            coveredError = false;
            repaint();
            tableViewComponents.getSelectionModel().clearSelection();
        }
    }
}

