package controllers;

import components.Line;
import data.Main;
import components.Point;
import data.Sizes;
import components.gates.and.And2;
import components.gates.or.Or2;
import components.gates.Gate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
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
    private Point pointMousePressed = new Point();
    private Line lineBuffer;
    private boolean waitForGate2 = false;
    private boolean waitForPlaceGate = false;

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
    @FXML private ScrollPane scrollPaneWorkspace;
    @FXML private Pane paneWorkspace;

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
        canvas.setOnMouseDragged(e -> actionCanvasMouseDragged(e.getX(), e.getY()));
        canvas.setOnMousePressed(e -> actionCanvasMousePressed(e.getX(), e.getY()));
    }

    public void myInitialize(Main main){
        this.main = main;
        main.getScene().setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));
        main.getScene().setOnKeyTyped(e -> actionCanvasKeyTyped(e.getCode()));
        main.getScene().setOnKeyReleased(e -> actionCanvasKeyReleased(e.getCode()));
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
        System.out.println("Click parameters: coverTotal:" + checkIfCoverTotal(x, y) + ", coverHalf:" + checkIfCoverHalf(x, y) + " selGate:" + selectedGate + ", newLineOnClick:" + createNewLineOnCLick + ", waitForGate2:" + waitForGate2);

        if(!checkIfCoverTotal(x, y) && selectedGate != null && !createNewLineOnCLick) {
            createNewGate(x, y, selectedGate);
            waitForPlaceGate = false;
        }
        else if(checkIfCoverHalf(x, y) && selectedGate == null && (createNewLineOnCLick || waitForGate2)){
            createNewLine(x, y);
        }
        else if(!checkIfCoverHalf(x, y) && selectedGate == null && (createNewLineOnCLick || waitForGate2)){
            lineBuffer = null;
            waitForGate2 = false;
        }
        else if(selectedGate != null){
            System.out.println("Covered gate while trying to create new one");
            coveredError = true;
            actionCanvasMouseMoved(x, y);
        }
        else{
            System.out.println("No special action, trying to select a gate");
            for(Gate g : arrayListCreatedGates){
                g.select(x, y);
            }
        }

        createNewLineOnCLick = false;
        repaint();
    }

    private void actionCanvasMouseMoved(double x, double y){
        repaint();
        if(coveredError) {
            graphicsContext.setStroke(Color.RED);
            graphicsContext.setLineWidth(Sizes.baseLineContourWidth);
            double shiftX = Sizes.baseGateXShift;
            double shiftY = Sizes.baseGateYShift;
            for (Gate g : arrayListCreatedGates) {
                Point pointCenter = g.getPointCenter();
                graphicsContext.strokeLine(pointCenter.getX() - shiftX, pointCenter.getY() - shiftY, pointCenter.getX() - shiftX, pointCenter.getY() + shiftY);
                graphicsContext.strokeLine(pointCenter.getX() - shiftX, pointCenter.getY() + shiftY, pointCenter.getX() + shiftX, pointCenter.getY() + shiftY);
                graphicsContext.strokeLine(pointCenter.getX() + shiftX, pointCenter.getY() + shiftY, pointCenter.getX() + shiftX, pointCenter.getY() - shiftY);
                graphicsContext.strokeLine(pointCenter.getX() + shiftX, pointCenter.getY() - shiftY, pointCenter.getX() - shiftX, pointCenter.getY() - shiftY);
            }
            if(checkIfCoverTotal(x, y)){
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
        else if(waitForGate2){
            graphicsContext.strokeLine(lineBuffer.getX1(), lineBuffer.getY1(), x, y);
        }
        else if(waitForPlaceGate){
            String newGateName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
            Gate g;
            if(newGateName.equals("And 2")){
                g = new And2(x, y);
                g.draw(graphicsContext);
            }
        }
    }

    private void actionCanvasMouseDragged(double x, double y){
        for(Gate g : arrayListCreatedGates){
            if(g.isSelected()){
                g.move(x, y, pointMousePressed.getX(), pointMousePressed.getY());
            }
        }

        repaint();

        pointMousePressed.setX(x);
        pointMousePressed.setY(y);
    }

    private void actionCanvasMousePressed(double x, double y){
        pointMousePressed.setX(x);
        pointMousePressed.setY(y);
    }

    private boolean checkIfCoverTotal(double x, double y){
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

    private boolean checkIfCoverHalf(double x, double y){
        double xShift = Sizes.baseGateXShift;
        double yShift = Sizes.baseGateYShift;

        for(Gate g : arrayListCreatedGates) {
            if (Math.abs(x - g.getPointCenter().getX()) <= Sizes.baseGateXShift &&
                    Math.abs(y - g.getPointCenter().getY()) <= Sizes.baseGateYShift) {
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

        double shiftX = Sizes.baseGateXShift;
        double shiftY = Sizes.baseGateYShift;
        for(Gate g : arrayListCreatedGates){
            g.draw(graphicsContext);

            graphicsContext.setStroke(Color.BLUE);
            graphicsContext.setLineWidth(Sizes.baseLineContourWidth);
            Point pointCenter = g.getPointCenter();
            graphicsContext.strokeLine(pointCenter.getX() - shiftX, pointCenter.getY() - shiftY, pointCenter.getX() - shiftX, pointCenter.getY() + shiftY);
            graphicsContext.strokeLine(pointCenter.getX() - shiftX, pointCenter.getY() + shiftY, pointCenter.getX() + shiftX, pointCenter.getY() + shiftY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftX, pointCenter.getY() + shiftY, pointCenter.getX() + shiftX, pointCenter.getY() - shiftY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftX, pointCenter.getY() - shiftY, pointCenter.getX() - shiftX, pointCenter.getY() - shiftY);
        }

        graphicsContext.setLineWidth(Sizes.baseLineWidth);
        graphicsContext.setStroke(Color.BLACK);
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
        else if(code == KeyCode.CONTROL){
            scrollPaneWorkspace.setPannable(true);
        }
    }

    private void actionCanvasKeyTyped(KeyCode code){

    }

    private void actionCanvasKeyReleased(KeyCode code){
        if(code == KeyCode.CONTROL){
            scrollPaneWorkspace.setPannable(false);
        }
    }

    private void createNewGate(double x, double y, Gate selectedGate){
        System.out.println("Create new gate");
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

    private void createNewLine(double x, double y){
        Gate g = getCoveredGate(x, y);
        ComboBox<Point> comboBoxNewLineHook = new ComboBox<>();
        comboBoxNewLineHook.setPrefSize(150, 30);
        comboBoxNewLineHook.setLayoutX(x - 75);
        comboBoxNewLineHook.setLayoutY(y);
        comboBoxNewLineHook.promptTextProperty().setValue("Wybierz pin");
        comboBoxNewLineHook.getItems().add(g.getPointOutput());
        for(Point p : g.getArrayListPointsInputs()){
            comboBoxNewLineHook.getItems().add(p);
        }
        paneWorkspace.getChildren().add(comboBoxNewLineHook);

        canvas.setOnMouseClicked(e -> {
            paneWorkspace.getChildren().remove(comboBoxNewLineHook);
            canvas.setOnMouseClicked(f -> actionCanvasMouseClicked(f.getX(), f.getY()));
            System.out.println("Click on canvas while combo is on");
        });

        if(!waitForGate2) {
            comboBoxNewLineHook.setOnAction(e -> chooseNewLineHook1(x, y, g, comboBoxNewLineHook));
        }
        else{
            comboBoxNewLineHook.setOnAction(e -> chooseNewLineHook2(x, y, g, comboBoxNewLineHook));
        }
        System.out.println("Create new line");
    }

    private void chooseNewLineHook1(double x, double y, Gate g, ComboBox<Point> comboBoxNewLineHook){
        Point p = comboBoxNewLineHook.getSelectionModel().getSelectedItem();
        lineBuffer = new Line(p.getX(), p.getY(), x, y, g, null, Color.BLACK);
        if(p.getName().contains("Output")){
            g.setLineOutput(lineBuffer);
        }
        else if(p.getName().contains("Input")){
            g.getArrayListLines().add(lineBuffer);
        }

        canvas.setOnMouseClicked(e -> actionCanvasMouseClicked(e.getX(), e.getY()));
        waitForGate2 = true;
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);
        repaint();
    }

    private void chooseNewLineHook2(double x, double y, Gate g, ComboBox<Point> comboBoxNewLineHook){
        Point p = comboBoxNewLineHook.getSelectionModel().getSelectedItem();
        lineBuffer.setX2(comboBoxNewLineHook.getSelectionModel().getSelectedItem().getX());
        lineBuffer.setY2(comboBoxNewLineHook.getSelectionModel().getSelectedItem().getY());
        lineBuffer.setGate2(g);
        if(p.getName().contains("Output")){
            g.setLineOutput(lineBuffer);
        }
        else if(p.getName().contains("Input")){
            g.getArrayListLines().add(lineBuffer);
        }
        arrayListCreatedLines.add(lineBuffer);

        canvas.setOnMouseClicked(e -> actionCanvasMouseClicked(e.getX(), e.getY()));
        waitForGate2 = false;
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);
        repaint();
    }
}

