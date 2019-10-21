package controllers;

import components.*;
import components.switches.Switch;
import components.switches.SwitchBistatble;
import data.Main;
import data.Names;
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

import javax.naming.Name;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainWindowController {
    private Main main;
    private ArrayList<Gate> arrayListCreatedGates = new ArrayList<>();
    private ArrayList<Switch> arrayListCreatedSwitches = new ArrayList<>();
    private ArrayList<Line> arrayListCreatedLines = new ArrayList<>();
    private GraphicsContext graphicsContext;
    private ArrayList<TableComponent> arrayListPossibleComponents = new ArrayList<>();
    private boolean coveredError = false;
    private Point pointMousePressed = new Point();
    private Line lineBuffer;
    private boolean waitForGate2 = false;
    private boolean waitForPlaceComponent = false;
    private ComboBox<Point> comboBoxNewLineHook;

    @FXML private Canvas canvas;
    @FXML private TextField textFieldFilterComponents;
    @FXML private TableView<TableComponent> tableViewComponents;
    @FXML private TableColumn<TableComponent, ImageView> tableColumnComponentsPictures;
    @FXML private TableColumn<TableComponent, Integer> tableColumnComponentInputsNumber;
    @FXML private Button buttonAddGate;
    @FXML private Button buttonDeleteGate;
    @FXML private Button buttonRotate;
    @FXML private ScrollPane scrollPaneWorkspace;
    @FXML private Pane paneWorkspace;

    @FXML
    public void initialize(){
        arrayListPossibleComponents.add(new TableComponent("Line", 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/line_off.png").toExternalForm(),
                                                    Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.switchName, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/switch_off.png").toExternalForm(),
                                                    Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateAnd2Name, 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/and2_gate_off.png").toExternalForm(),
                                                    Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateOr2Name, 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/or2_gate_off.png").toExternalForm(),
                                                    Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));

        ObservableList<TableComponent> ol = FXCollections.observableList(arrayListPossibleComponents);
        tableViewComponents.setItems(ol);
        tableViewComponents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> actionSelectionChanged());
        tableColumnComponentsPictures.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        tableColumnComponentInputsNumber.setCellValueFactory(new PropertyValueFactory<>("InputsNumber"));

        graphicsContext = canvas.getGraphicsContext2D();

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

    public void actionSelectionChanged(){
        System.out.println("TableView selection changed");

        try {
            String selectedName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
            waitForPlaceComponent = selectedName.contains("And") || selectedName.contains("Or") || selectedName.contains("Xor") || selectedName.contains("Not") ||
                    selectedName.contains("Nand") || selectedName.contains("Nor") || selectedName.contains("Xnor");
        } catch(Exception e){
            waitForPlaceComponent = false;
        }
        coveredError = false;
        waitForGate2 = false;
        lineBuffer = null;
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);
        comboBoxNewLineHook = null;
        canvas.setOnMouseClicked(e -> actionCanvasMouseClicked(e.getX(), e.getY()));

        repaint();
    }

    public void actionFilterComponents(){
        String insertedText = textFieldFilterComponents.getText();
        ArrayList<TableComponent> arrayListFilteredGates = new ArrayList<>();

        for(TableComponent tc : arrayListPossibleComponents){
            if(Pattern.compile(Pattern.quote(insertedText), Pattern.CASE_INSENSITIVE).matcher(tc.getName()).find()){
                arrayListFilteredGates.add(tc);
            }
        }

        ObservableList<TableComponent> ol = FXCollections.observableList(arrayListFilteredGates);
        tableViewComponents.setItems(ol);
    }

    public void actionClearTextFilterComponents(){
        textFieldFilterComponents.clear();
        actionFilterComponents();
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
        String selectedItemName = null;

        try{
            selectedItemName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
        } catch (Exception e){}

        System.out.println("Click parameters: coverTotal:" + checkIfCoverTotal(x, y) + ", coverHalf:" + checkIfCoverHalf(x, y) +
                            " selItemName:" + selectedItemName + ", waitForGate2:" + waitForGate2);

        //Clicked on a existing gate (1 or 2), and creating a line
        if(checkIfCoverHalf(x, y) && (waitForGate2 || (selectedItemName != null && selectedItemName.equals(Names.lineName)))){
            System.out.println("Creating line");
            createNewLine(x, y);
            coveredError = false;
        }
        //Clicked on a free space while creating line
        else if(!checkIfCoverHalf(x, y) && (waitForGate2 || (selectedItemName != null && selectedItemName.equals(Names.lineName)))){
            System.out.println("Stopped creating line");
            lineBuffer = null;
            waitForGate2 = false;
            coveredError = false;
        }
        //Clicked on a free space while creating component (not line)
        else if(!checkIfCoverTotal(x, y) && selectedItemName != null && !selectedItemName.contains(Names.lineName)) {
            System.out.println("Create new component " + selectedItemName);
            createNewComponent(x, y, selectedItemName);
            waitForPlaceComponent = false;
        }
        //Clicked on a occupied space while creating gate
        else if(selectedItemName != null){
            System.out.println("Covered gate while trying to create new one");
            coveredError = true;
            waitForPlaceComponent = true;
            actionCanvasMouseMoved(x, y);
        }
        //Just clicked on a free space
        else{
            System.out.println("No special action, trying to select a gate");
            for(Gate g : arrayListCreatedGates){
                g.select(x, y);
            }
            coveredError = false;
        }

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
        if(waitForPlaceComponent){
            String newComponentName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
            if(newComponentName.equals(Names.gateAnd2Name)){
                Gate g = new And2(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateOr2Name)){
                Gate g = new Or2(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.switchName)){
                SwitchBistatble sm = new SwitchBistatble(x, y);
                sm.draw(graphicsContext);
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

        for(Gate g : arrayListCreatedGates){
            g.draw(graphicsContext);
        }

        for(Switch s : arrayListCreatedSwitches){
            s.draw(graphicsContext);
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
            lineBuffer = null;
            waitForGate2 = false;
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

    private void createNewComponent(double x, double y, String newComponentName){
        tableViewComponents.getSelectionModel().clearSelection();
        try {
            if (newComponentName.equals(Names.gateAnd2Name)) {
                System.out.println("And 2");
                Gate newGate = new And2(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateOr2Name)) {
                System.out.println("or 2");
                Gate newGate = new Or2(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if(newComponentName.equals(Names.switchName)){
                System.out.println("Switch");
                SwitchBistatble newSwitch = new SwitchBistatble(x, y);
                arrayListCreatedSwitches.add(newSwitch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        coveredError = false;
    }

    private void createNewLine(double x, double y){
        tableViewComponents.getSelectionModel().clearSelection();
        Gate g = getCoveredGate(x, y);
        comboBoxNewLineHook = new ComboBox<>();
        comboBoxNewLineHook.setPrefSize(150, 30);
        comboBoxNewLineHook.setLayoutX(x - 75);
        comboBoxNewLineHook.setLayoutY(y);
        comboBoxNewLineHook.promptTextProperty().setValue("Wybierz pin");
        comboBoxNewLineHook.getItems().add(g.getPointOutput());
        for(Point p : g.getArrayPointsInputs()){
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
            int inputNumber = Integer.parseInt(p.getName().split("Input")[1]);
            g.getArrayLines()[inputNumber - 1] = lineBuffer;
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
            int inputNumber = Integer.parseInt(p.getName().split("Input")[1]);
            g.getArrayLines()[inputNumber - 1] = lineBuffer;
        }
        arrayListCreatedLines.add(lineBuffer);

        canvas.setOnMouseClicked(e -> actionCanvasMouseClicked(e.getX(), e.getY()));
        waitForGate2 = false;
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);
        repaint();
    }
}

