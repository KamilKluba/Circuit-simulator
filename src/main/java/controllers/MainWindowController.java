package controllers;

import components.*;
import components.flipflops.FlipFlop;
import components.flipflops.FlipFlopD;
import components.flipflops.FlipFlopJK;
import components.flipflops.FlipFlopT;
import components.gates.Not;
import components.gates.and.And3;
import components.gates.and.And4;
import components.gates.nand.Nand2;
import components.gates.nand.Nand3;
import components.gates.nand.Nand4;
import components.gates.nor.Nor2;
import components.gates.nor.Nor3;
import components.gates.nor.Nor4;
import components.gates.or.Or3;
import components.gates.or.Or4;
import components.gates.xnor.Xnor2;
import components.gates.xnor.Xnor3;
import components.gates.xnor.Xnor4;
import components.gates.xor.Xor2;
import components.gates.xor.Xor3;
import components.gates.xor.Xor4;
import components.switches.Switch;
import components.switches.SwitchBistatble;
import components.switches.SwitchMonostable;
import components.switches.SwitchPulse;
import data.*;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import main.Main;
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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class MainWindowController {
    @FXML private BorderPane borderPaneMainView;
    @FXML private Canvas canvas;
    @FXML private TextField textFieldFilterComponents;
    @FXML private TableView<TableComponent> tableViewComponents;
    @FXML private TableColumn<TableComponent, ImageView> tableColumnComponentsPictures;
    @FXML private TableColumn<TableComponent, Integer> tableColumnComponentInputsNumber;
    @FXML private Button buttonDelete;
    @FXML private Button buttonRotate;
    @FXML private ScrollPane scrollPaneWorkspace;
    @FXML private Pane paneWorkspace;
    @FXML private ScrollPane scrollPaneOptions;
    @FXML private AnchorPane anchorPaneOptions;
    @FXML private ScrollPane scrollPaneChart;
    @FXML private LineChart lineChartStates;
    @FXML private HBox hBoxChartArea;
    @FXML private AnchorPane anchorPaneChartOptions;
    @FXML private CheckBox checkBoxScrollToRight;

    private Main main;
    private ArrayList<Line> arrayListCreatedLines = new ArrayList<>();
    private ArrayList<Gate> arrayListCreatedGates = new ArrayList<>();
    private ArrayList<Switch> arrayListCreatedSwitches = new ArrayList<>();
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops = new ArrayList<>();
    private ArrayList<Component> arrayListCreatedComponents = new ArrayList<>();
    private GraphicsContext graphicsContext;
    private ArrayList<TableComponent> arrayListPossibleComponents = new ArrayList<>();
    private ArrayList<XYChart.Series<Long, String>> arrayListSeries = new ArrayList<>();

    private Line lineBuffer;
    private boolean waitForComponent2 = false;
    private boolean waitForPlaceComponent = false;
    private boolean coveredError = false;
    private boolean draggedSelectionRectngle = false;
    private ComboBox<Point> comboBoxNewLineHook;
    private MouseActions mouseActions;
    private ComponentCreator componentCreator;
    private FileOperator fileOperator;
    private Component componentBuffer = null;
    private MouseButton mouseButton = null;

    private ZoomableScrollPaneWorkspace zoomableScrollPaneWorkspace;
    private ZoomableScrollPaneChart zoomableScrollPaneChart;

    @FXML
    public void initialize(){
        arrayListPossibleComponents.add(new TableComponent("Line", 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/line_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.switchMonostableName, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.switchBistableName, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/switches/switch_bistable_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.switchPulseName, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateNotName, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/not/not_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateAnd2Name, 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/and/and2_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateAnd3Name, 3,
                                        new ImageView(new Image(getClass().getResource("/graphics/and/and3_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateAnd4Name, 4,
                                        new ImageView(new Image(getClass().getResource("/graphics/and/and4_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateOr2Name, 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/or/or2_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateOr3Name, 3,
                                        new ImageView(new Image(getClass().getResource("/graphics/or/or3_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateOr4Name, 4,
                                        new ImageView(new Image(getClass().getResource("/graphics/or/or4_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateXor2Name, 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/xor/xor2_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateXor3Name, 3,
                                        new ImageView(new Image(getClass().getResource("/graphics/xor/xor3_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateXor4Name, 4,
                                        new ImageView(new Image(getClass().getResource("/graphics/xor/xor4_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateNand2Name, 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/nand/nand2_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateNand3Name, 3,
                                        new ImageView(new Image(getClass().getResource("/graphics/nand/nand3_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateNand4Name, 4,
                                        new ImageView(new Image(getClass().getResource("/graphics/nand/nand4_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateNor2Name, 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/nor/nor2_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateNor3Name, 3,
                                        new ImageView(new Image(getClass().getResource("/graphics/nor/nor3_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateNor4Name, 4,
                                        new ImageView(new Image(getClass().getResource("/graphics/nor/nor4_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateXnor2Name, 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor2_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateXnor3Name, 3,
                                        new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor3_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.gateXnor4Name, 4,
                                        new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor4_gate_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.flipFlopJK, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_off.png").toExternalForm(),
                                                Sizes.baseFlipFLopImageInTableXSize, Sizes.baseFlipFLopImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.flipFlopD, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_off.png").toExternalForm(),
                                                Sizes.baseFlipFLopImageInTableXSize, Sizes.baseFlipFLopImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.flipFlopT, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_off.png").toExternalForm(),
                                                Sizes.baseFlipFLopImageInTableXSize, Sizes.baseFlipFLopImageInTableYSize, false, false))));

        ObservableList<TableComponent> ol = FXCollections.observableList(arrayListPossibleComponents);
        tableViewComponents.setItems(ol);
        tableViewComponents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> actionSelectionChanged());
        tableColumnComponentsPictures.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        tableColumnComponentInputsNumber.setCellValueFactory(new PropertyValueFactory<>("InputsNumber"));

        graphicsContext = canvas.getGraphicsContext2D();

        zoomableScrollPaneWorkspace = new ZoomableScrollPaneWorkspace(paneWorkspace);
        borderPaneMainView.setCenter(zoomableScrollPaneWorkspace);
        zoomableScrollPaneChart = new ZoomableScrollPaneChart(lineChartStates);
//        zoomableScrollPaneChart.setPrefWidth(1820);
        hBoxChartArea.getChildren().remove(scrollPaneChart);
        hBoxChartArea.getChildren().add(zoomableScrollPaneChart);
        checkBoxScrollToRight.setText("Przesuń do \n prawej");
        mouseActions = new MouseActions(this);
        componentCreator = new ComponentCreator(this);
        mouseActions.setComponentCreator(componentCreator);
        componentCreator.setMouseActions(mouseActions);
        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
        canvas.setOnMouseMoved(e -> mouseActions.actionCanvasMouseMoved(e.getX(), e.getY()));
        canvas.setOnMouseDragged(e -> mouseActions.actionCanvasMouseDragged(e));
        canvas.setOnMousePressed(e -> mouseActions.actionCanvasMousePressed(e));
        canvas.setOnMouseReleased(e -> mouseActions.actionCanvasMouseReleased(e.getX(), e.getY()));
        hBoxChartArea.setOnMouseClicked(e -> mouseActions.actionZoomableScrollPaneClicked());

        tableViewComponents.setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));

        Executors.newFixedThreadPool(1).execute(() -> repaintThread());
    }

    private void setChart(){
        zoomableScrollPaneChart.setPrefHeight(150);
        zoomableScrollPaneChart.setPrefWidth(3000);
        new Thread(() -> {
            while(true){
                int maxLength = 0;
                for(XYChart.Series s : arrayListSeries){
                    if(s.getData().size() > maxLength){
                        maxLength = s.getData().size();
                    }
                }
                if(maxLength > 0){
                    lineChartStates.setPrefWidth(924 + maxLength * 20);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(checkBoxScrollToRight.isSelected()){
                    zoomableScrollPaneChart.setHvalue(1);
                }
                try {
                    Thread.sleep(4900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void myInitialize(main.Main main){
        this.main = main;
        fileOperator = new FileOperator(this);
        main.getScene().setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));
        main.getScene().setOnKeyTyped(e -> actionCanvasKeyTyped(e.getCharacter()));
        main.getScene().setOnKeyReleased(e -> actionCanvasKeyReleased(e.getCode()));

        setChart();
    }

    public void actionDebug(){
        zoomableScrollPaneChart.setHvalue(1.0);
    }

    public void actionDelete(){
        ArrayList<Component> arrayListComponentsToDelete = new ArrayList<>();
        for (Line l : arrayListCreatedLines) {
            if (l.isSelected()) {
                arrayListComponentsToDelete.add(l);
                l.delete();
            }
        }

        for (Gate g : arrayListCreatedGates) {
            if (g.isSelected()) {
                while (g.getArrayListLinesOutput().size() > 0) {
                    arrayListComponentsToDelete.add(g.getArrayListLinesOutput().get(0));
                    g.getArrayListLinesOutput().get(0).delete();
                }
                for (ArrayList<Line> al : g.getArrayArrayListLines()) {
                    while (al.size() > 0) {
                        arrayListComponentsToDelete.add(al.get(0));
                        al.get(0).delete();
                    }
                }
                arrayListComponentsToDelete.add(g);
            }
        }

        for (Switch s : arrayListCreatedSwitches) {
            if (s.isSelected()) {
                while (s.getArrayListlines().size() > 0) {
                    arrayListComponentsToDelete.add(s.getArrayListlines().get(0));
                    s.getArrayListlines().get(0).delete();
                }
                arrayListComponentsToDelete.add(s);
            }
        }

        for (FlipFlop ff : arrayListCreatedFlipFlops) {
            if (ff.isSelected()) {
                while (ff.getArrayListLinesInput().size() > 0) {
                    arrayListComponentsToDelete.add(ff.getArrayListLinesInput().get(0));
                    ff.getArrayListLinesInput().get(0).delete();
                }
                if (ff.getName().equals(Names.flipFlopJK)) {
                    while (((FlipFlopJK) ff).getArrayListLinesInputK().size() > 0) {
                        arrayListComponentsToDelete.add(((FlipFlopJK) ff).getArrayListLinesInputK().get(0));
                        ((FlipFlopJK) ff).getArrayListLinesInputK().get(0).delete();
                    }
                }
                while (ff.getArrayListLinesOutput().size() > 0) {
                    arrayListComponentsToDelete.add(ff.getArrayListLinesOutput().get(0));
                    ff.getArrayListLinesOutput().get(0).delete();
                }
                while (ff.getArrayListLinesOutputReverted().size() > 0) {
                    arrayListComponentsToDelete.add(ff.getArrayListLinesOutputReverted().get(0));
                    ff.getArrayListLinesOutputReverted().get(0).delete();
                }
                while (ff.getArrayListLinesAsynchronousInput().size() > 0) {
                    arrayListComponentsToDelete.add(ff.getArrayListLinesAsynchronousInput().get(0));
                    ff.getArrayListLinesAsynchronousInput().get(0).delete();
                }
                while (ff.getArrayListLinesClock().size() > 0) {
                    arrayListComponentsToDelete.add(ff.getArrayListLinesClock().get(0));
                    ff.getArrayListLinesClock().get(0).delete();
                }
                while (ff.getArrayListLinesReset().size() > 0) {
                    arrayListComponentsToDelete.add(ff.getArrayListLinesReset().get(0));
                    ff.getArrayListLinesReset().get(0).delete();
                }
                arrayListComponentsToDelete.add(ff);
            }
        }

        for(Component c : arrayListComponentsToDelete){
            c.kill();
            arrayListCreatedLines.remove(c);
            arrayListCreatedGates.remove(c);
            arrayListCreatedSwitches.remove(c);
            arrayListCreatedFlipFlops.remove(c);
            arrayListCreatedComponents.remove(c);
        }

        for(Line l : arrayListCreatedLines){
            l.getArrayListVisitedLines().clear();
            l.getArrayListDependentComponents().clear();
        }
        for(Line l : arrayListCreatedLines){
            l.checkForSignals(l.getArrayListDependentComponents(), l.getArrayListVisitedLines());
        }
        for(Line l : arrayListCreatedLines){
            l.lifeCycle();
        }
    }

    public void actionSelectionChanged() {
        if (Accesses.logTableView){
            System.out.println("TableView selection changed");
        }

        try {
            String selectedName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
            waitForPlaceComponent = selectedName.contains(Names.gateSearchName) || selectedName.contains(Names.switchSearchName) ||
                    selectedName.contains(Names.flipFlopSearchName);
        } catch(Exception e){
            waitForPlaceComponent = false;
        }
        coveredError = false;
        waitForComponent2 = false;
        componentCreator.deleteLineBuffer();
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);
        comboBoxNewLineHook = null;
        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
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
        for(Component c : arrayListCreatedComponents){
            if(c.isSelected()){
                c.rotate();
            }
        }
    }

    public void repaintThread(){
        boolean stateChanged;

        while(true) {
            try {
                stateChanged = false;
                for (Component c : arrayListCreatedComponents) {
                    if (c.isStateChanged()) {
                        stateChanged = true;
                    }
                    c.setStateChanged(false);
                }
                for (Line l : arrayListCreatedLines) {
                    if (l.isStateChanged()) {
                        stateChanged = true;
                    }
                    l.setStateChanged(false);
                }
                if (stateChanged) {
                    repaintScreen();
                }
                if (stateChanged) {
                    Thread.sleep(1);
                } else {
                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void repaintScreen(){
        Platform.runLater(() -> {
            graphicsContext.clearRect(0, 0, canvas.getWidth() + 1, canvas.getHeight() + 1);
            graphicsContext.setLineWidth(Sizes.baseLineWidth);
            for (Line l : arrayListCreatedLines) {
                l.draw(graphicsContext);
            }
            for (Component c : arrayListCreatedComponents) {
                c.draw(graphicsContext);
            }
            if (componentBuffer != null) {
                componentBuffer.draw(graphicsContext);
            }
            graphicsContext.setStroke(Color.BLACK);
            if (lineBuffer != null) {
                lineBuffer.draw(graphicsContext);
            }
            if (coveredError) {
                mouseActions.drawCoverErrorSquares();
            }
            if (draggedSelectionRectngle && mouseButton == MouseButton.PRIMARY){
                mouseActions.drawSelectionRectangle();
            }
        });
    }

    private void actionCanvasKeyPressed(KeyCode code){
        if(code == KeyCode.ESCAPE){
            componentBuffer = null;
            coveredError = false;
            tableViewComponents.getSelectionModel().clearSelection();
            waitForComponent2 = false;
            componentCreator.deleteLineBuffer();
        }
        else if(code == KeyCode.CONTROL){
            scrollPaneWorkspace.setPannable(true);
        }
        else if(code == KeyCode.DELETE) {
            actionDelete();
        }
        else if(code == KeyCode.UP){
            System.out.println(code);
            paneWorkspace.setTranslateY(paneWorkspace.getTranslateY() + 10);
            canvas.setTranslateY(canvas.getTranslateY() + 10);
        }
        else if(code == KeyCode.DOWN){
            System.out.println(code);
            paneWorkspace.setTranslateY(paneWorkspace.getTranslateY() - 10);
            canvas.setTranslateY(canvas.getTranslateY() - 10);
        }
        else if(code == KeyCode.LEFT){
            System.out.println(code);
            paneWorkspace.setTranslateX(paneWorkspace.getTranslateX() - 10);
            canvas.setTranslateX(canvas.getTranslateX() - 10);
        }
        else if(code == KeyCode.RIGHT){
            System.out.println(code);
            paneWorkspace.setTranslateX(paneWorkspace.getTranslateX() + 10);
            canvas.setTranslateX(canvas.getTranslateX() + 10);
        }

        mouseActions.actionCanvasMouseMoved(mouseActions.getPointMouseMoved().getX(), mouseActions.getPointMouseMoved().getY());
    }

    private void actionCanvasKeyTyped(String character){
        if(character.matches("[0-9]")){
            int index = Integer.parseInt(character);
            if(tableViewComponents.getItems().size() >= index){
                tableViewComponents.getSelectionModel().select(index - 1);
            }
            else{
                tableViewComponents.getSelectionModel().clearSelection();
            }
        }

        mouseActions.actionCanvasMouseMoved(mouseActions.getPointMouseMoved().getX(), mouseActions.getPointMouseMoved().getY());
    }

    private void actionCanvasKeyReleased(KeyCode code){
        if(code == KeyCode.CONTROL){
            scrollPaneWorkspace.setPannable(false);
        }
    }

    public void actionMenuItemLoad(){
        fileOperator.actionMenuItemLoad();
    }

    public void actionMenuItemSave(){
        fileOperator.actionMenuItemSave();
    }

    public void actionMenuItemSaveAs(){
        fileOperator.actionMenuItemSaveAs();
    }

    public void actionMenuItemExit(){
        for(Component c : arrayListCreatedComponents){
            c.kill();
        }
        System.exit(0);
    }

    public void actionMenuItemManual(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Names.manualTitle);
        alert.setHeaderText(Names.manualHeader);
        alert.setContentText(Names.manualContent);
        alert.showAndWait();
    }

    public void actionMenuItemAuthor(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Names.aboutAuthorTitle);
        alert.setHeaderText(Names.aboutAuthorHeader);
        alert.setContentText(Names.aboutAuthorContent);
        alert.showAndWait();
    }

    public boolean isCoveredError() {
        return coveredError;
    }

    public void setCoveredError(boolean coveredError) {
        this.coveredError = coveredError;
    }

    public boolean isWaitForComponent2() {
        return waitForComponent2;
    }

    public void setWaitForComponent2(boolean waitForComponent2) {
        this.waitForComponent2 = waitForComponent2;
    }

    public boolean isWaitForPlaceComponent() {
        return waitForPlaceComponent;
    }

    public void setWaitForPlaceComponent(boolean waitForPlaceComponent) {
        this.waitForPlaceComponent = waitForPlaceComponent;
    }

    public Line getLineBuffer() {
        return lineBuffer;
    }

    public void setLineBuffer(Line lineBuffer) {
        this.lineBuffer = lineBuffer;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public TableView<TableComponent> getTableViewComponents() {
        return tableViewComponents;
    }

    public ArrayList<Gate> getArrayListCreatedGates() {
        return arrayListCreatedGates;
    }

    public ArrayList<Switch> getArrayListCreatedSwitches() {
        return arrayListCreatedSwitches;
    }

    public ArrayList<Line> getArrayListCreatedLines() {
        return arrayListCreatedLines;
    }

    public ArrayList<FlipFlop> getArrayListCreatedFlipFlops() {
        return arrayListCreatedFlipFlops;
    }

    public ArrayList<Component> getArrayListCreatedComponents() {
        return arrayListCreatedComponents;
    }

    public ArrayList<TableComponent> getArrayListPossibleComponents() {
        return arrayListPossibleComponents;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public ScrollPane getScrollPaneWorkspace() {
        return scrollPaneWorkspace;
    }

    public Pane getPaneWorkspace() {
        return paneWorkspace;
    }

    public ZoomableScrollPaneWorkspace getZoomableScrollPaneWorkspace() {
        return zoomableScrollPaneWorkspace;
    }

    public ZoomableScrollPaneChart getZoomableScrollPaneChart() {
        return zoomableScrollPaneChart;
    }

    public Main getMain() {
        return main;
    }

    public HBox gethBoxChartArea() {
        return hBoxChartArea;
    }

    public Component getComponentBuffer() {
        return componentBuffer;
    }

    public void setComponentBuffer(Component componentBuffer) {
        this.componentBuffer = componentBuffer;
    }

    public MouseActions getMouseActions() {
        return mouseActions;
    }

    public LineChart getLineChartStates() {
        return lineChartStates;
    }

    public ArrayList<XYChart.Series<Long, String>> getArrayListSeries() {
        return arrayListSeries;
    }

    public FileOperator getFileOperator() {
        return fileOperator;
    }

    public boolean isDraggedSelectionRectngle() {
        return draggedSelectionRectngle;
    }

    public void setDraggedSelectionRectngle(boolean draggedSelectionRectngle) {
        this.draggedSelectionRectngle = draggedSelectionRectngle;
    }

    public MouseButton getMouseButton() {
        return mouseButton;
    }

    public void setMouseButton(MouseButton mouseButton) {
        this.mouseButton = mouseButton;
    }
}

