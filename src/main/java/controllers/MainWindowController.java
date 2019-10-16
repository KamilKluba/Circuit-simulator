package controllers;

import data.Line;
import data.Main;
import data.Point;
import data.Sizes;
import gates.And.And2;
import gates.And.Or2;
import gates.Gate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
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
    private boolean createNewLineOnCLick = false;

    @FXML private Canvas canvas;
    @FXML private TextField textFieldFilterGate;
    @FXML private TableView<Gate> tableViewComponents;
    @FXML private TableColumn<Gate, ImageView> tableColumnComponentsPictures;
    @FXML private TableColumn<Gate, Integer> tableColumnComponentnputsNumber;
    @FXML private Button buttonAddGate;
    @FXML private Button buttonDeleteGate;
    @FXML private Button buttonRotate;
    @FXML private ImageView imageViewLine;
    @FXML private Button buttonNewLine;

    @FXML
    public void initialize(){
        arrayListPossibleGates.add(new And2());
        arrayListPossibleGates.add(new Or2());

        ObservableList<Gate> ol = FXCollections.observableList(arrayListPossibleGates);
        tableViewComponents.setItems(ol);

        tableColumnComponentsPictures.setCellValueFactory(new PropertyValueFactory<>("imageViewOff"));
        tableColumnComponentnputsNumber.setCellValueFactory(new PropertyValueFactory<>("arrayListInputsSize"));

        graphicsContext = canvas.getGraphicsContext2D();

        Image image = new Image(getClass().getResource("/graphics/or2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(Sizes.baseLineWidth);
        graphicsContext.strokeLine(200, 200, 700, 400);
        graphicsContext.drawImage(image, 500, 500);

        imageViewLine.setImage(new Image(getClass().getResource("/graphics/line_off.png").toExternalForm()));

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

    public void actionNewLine(){
        createNewLineOnCLick = true;
        tableViewComponents.getSelectionModel().clearSelection();
    }

    public void actionRotate(){
        for(Gate g : arrayListCreatedGates){
            if(g.isSelected()){
                g.rotate();
            }
        }
        repaint();
    }

    public void actionCanvasMouseClicked(double x, double y){
        Gate selectedGate = tableViewComponents.getSelectionModel().getSelectedItem();
        System.out.println(checkIfCover(x, y) + " " + selectedGate + " " + createNewLineOnCLick);

        if(!checkIfCover(x, y) && selectedGate != null && !createNewLineOnCLick) {
            System.out.println("Halo1");
            try {
                String name = selectedGate.getName();
                tableViewComponents.getSelectionModel().clearSelection();

                if (name.equals("And 2")) {
                    Gate newGate = new And2(x, y);
                    arrayListCreatedGates.add(newGate);
                } else if (name.equals("Or 2")) {
                    Gate newGate = new Or2(x, y);
                    arrayListCreatedGates.add(newGate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            coveredError = false;
        }
        else if(checkIfCover(x, y) && selectedGate == null && createNewLineOnCLick){
            ///////////////////////////////////////////////////////////////////////////ZMIEN TO CHECK IF COVER!!!///////////////////////////////////////////////////////////////////////
            System.out.println("Halo2");
            Gate g = getCoveredGate(x, y);
            arrayListCreatedLines.add(new Line(0, 0, g.getArrayListPointsInputs().get(0).getX(), g.getArrayListPointsInputs().get(0).getY(), g, null, Color.BLACK));
            arrayListCreatedLines.add(new Line(100, 200, g.getArrayListPointsInputs().get(1).getX(), g.getArrayListPointsInputs().get(1).getY(), g, null, Color.BLACK));
        }
        else if(selectedGate != null){
            System.out.println("Halo3");
            coveredError = true;
            actionCanvasMouseMoved(x, y);
        }
        else{
            System.out.println("Halo4");
            for(Gate g : arrayListCreatedGates){
                g.select(x, y);
            }
        }

        createNewLineOnCLick = false;
        repaint();
    }

    private void actionCanvasMouseMoved(double x, double y){
        if(coveredError) {
            repaint();

            graphicsContext.setStroke(Color.RED);
            graphicsContext.setLineWidth(1);
            double shiftX = Sizes.baseGateXShift;
            double shiftY = Sizes.baseGateYShift;
            for (Gate g : arrayListCreatedGates) {
                Point pointCenter = g.getPointCenter();
                graphicsContext.strokeLine(pointCenter.getX() - shiftX, pointCenter.getY() - shiftY, pointCenter.getX() - shiftX, pointCenter.getY() + shiftY);
                graphicsContext.strokeLine(pointCenter.getX() - shiftX, pointCenter.getY() + shiftY, pointCenter.getX() + shiftX, pointCenter.getY() + shiftY);
                graphicsContext.strokeLine(pointCenter.getX() + shiftX, pointCenter.getY() + shiftY, pointCenter.getX() + shiftX, pointCenter.getY() - shiftY);
                graphicsContext.strokeLine(pointCenter.getX() + shiftX, pointCenter.getY() - shiftY, pointCenter.getX() - shiftX, pointCenter.getY() - shiftY);
            }
            if(checkIfCover(x, y)){
                graphicsContext.setStroke(Color.RED);
            }
            else{
                graphicsContext.setStroke(Color.GREEN);
            }
            graphicsContext.strokeLine(x - shiftX, y - shiftY, x - shiftX, y + shiftY);
            graphicsContext.strokeLine(x - shiftX, y + shiftY, x + shiftX, y + shiftY);
            graphicsContext.strokeLine(x + shiftX, y + shiftY, x + shiftX, y - shiftY);
            graphicsContext.strokeLine(x + shiftX, y - shiftY, x - shiftX, y - shiftY);
        }
    }

    private boolean checkIfCover(double x, double y){
        double xShift = Sizes.baseGateXShift;
        double yShift = Sizes.baseGateYShift;

        for(Gate g : arrayListCreatedGates) {
            if (Math.abs(x - g.getPointCenter().getX()) <= Sizes.baseGateXSize &&
                    Math.abs(y - g.getPointCenter().getY()) <= Sizes.baseGateYSize) {
                return true;
            }
        }
        return false;
    }

    private Gate getCoveredGate(double x, double y){
        double xShift = Sizes.baseGateXShift;
        double yShift = Sizes.baseGateYShift;

        for(Gate g : arrayListCreatedGates) {
            if (Math.abs(x - g.getPointCenter().getX()) <= Sizes.baseGateXShift &&
                    Math.abs(y - g.getPointCenter().getY()) <= Sizes.baseGateYShift) {
                return g;
            }
        }
        return null;
    }

    private void repaint(){
        graphicsContext.clearRect(0, 0, canvas.getWidth() + 1, canvas.getHeight() + 1);
        for(Gate g : arrayListCreatedGates){
            g.draw(graphicsContext);
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

