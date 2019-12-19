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
import data.Accesses;
import data.MouseActions;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import main.Main;
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

    private Main main;
    private ArrayList<Line> arrayListCreatedLines = new ArrayList<>();
    private ArrayList<Gate> arrayListCreatedGates = new ArrayList<>();
    private ArrayList<Switch> arrayListCreatedSwitches = new ArrayList<>();
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops = new ArrayList<>();
    private ArrayList<Component> arrayListCreatedComponents = new ArrayList<>();
    private GraphicsContext graphicsContext;
    private ArrayList<TableComponent> arrayListPossibleComponents = new ArrayList<>();
    private boolean coveredError = false;
    private ArrayList<XYChart.Series<Long, String>> arrayListSeries = new ArrayList<>();

    private Line lineBuffer;
    private boolean waitForComponent2 = false;
    private boolean waitForPlaceComponent = false;
    private ComboBox<Point> comboBoxNewLineHook;
    private MouseActions mouseActions;
    private int lineCounter = 0;
    private int gateCounter = 0;
    private int switchCounter = 0;
    private int flipFlopCounter = 0;

    private long timeStart;

    private ZoomableScrollPaneWorkspace zoomableScrollPaneWorkspace;
    private ZoomableScrollPaneChart zoomableScrollPaneChart;

    private File saveFile = null;

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
        mouseActions = new MouseActions(this);

        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
        canvas.setOnMouseMoved(e -> mouseActions.actionCanvasMouseMoved(e.getX(), e.getY()));
        canvas.setOnMouseDragged(e -> mouseActions.actionCanvasMouseDragged(e));
        canvas.setOnMousePressed(e -> mouseActions.actionCanvasMousePressed(e));
        canvas.setOnMouseReleased(e -> mouseActions.actionCanvasMouseReleased(e.getX(), e.getY()));
        hBoxChartArea.setOnMouseClicked(e -> mouseActions.actionZoomableScrollPaneClicked());

        tableViewComponents.setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));

        timeStart = System.currentTimeMillis();
    }

    private void setChart(){
        zoomableScrollPaneChart.setPrefHeight(200);
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
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void myInitialize(main.Main main){
        this.main = main;
        main.getScene().setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));
        main.getScene().setOnKeyTyped(e -> actionCanvasKeyTyped(e.getCharacter()));
        main.getScene().setOnKeyReleased(e -> actionCanvasKeyReleased(e.getCode()));

        setChart();
    }

    public void actionDebug(){
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

        repaint();
    }

    public void actionSelectionChanged() {
        if (Accesses.logTableView){
            System.out.println("TableView selection changed");
        }

        try {
            String selectedName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
            waitForPlaceComponent = selectedName.contains(Names.gateSearchName) || selectedName.contains("Switch") ||
                    selectedName.contains(Names.flipFlopSearchName);
        } catch(Exception e){
            waitForPlaceComponent = false;
        }
        coveredError = false;
        waitForComponent2 = false;
        deleteLineBuffer();
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);
        comboBoxNewLineHook = null;
        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));

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
        for(Component c : arrayListCreatedComponents){
            if(c.isSelected()){
                c.rotate();
            }
        }
        repaint();
    }

    public boolean checkIfCoverTotal(String componentName, double x, double y){
        double xSizeCompare = 0;
        double ySizeCompare = 0;

        if(componentName.contains(Names.gateSearchName)){
            xSizeCompare = Sizes.baseGateXSize;
            ySizeCompare = Sizes.baseGateYSize;
        }
        else if(componentName.contains(Names.switchSearchName)){
            xSizeCompare = Sizes.baseSwitchXSize;
            ySizeCompare = Sizes.baseSwitchYSize;
        }
        else if(componentName.contains(Names.flipFlopSearchName)){
            xSizeCompare = Sizes.baseFlipFlopXSize;
            ySizeCompare = Sizes.baseFlipFlopYSize;
        }

        for(Gate g : arrayListCreatedGates) {
            if (Math.abs(x - g.getPointCenter().getX()) <= (Sizes.baseGateXSize + xSizeCompare) / 2 &&
                Math.abs(y - g.getPointCenter().getY()) <= (Sizes.baseGateYSize + ySizeCompare) / 2) {
                return true;
            }
        }

        for(Switch s : arrayListCreatedSwitches) {
            if (Math.abs(x - s.getPointCenter().getX()) <= (Sizes.baseSwitchXSize + xSizeCompare) / 2 &&
                Math.abs(y - s.getPointCenter().getY()) <= (Sizes.baseSwitchYSize + ySizeCompare) / 2){
                return true;
            }
        }

        for(FlipFlop ff : arrayListCreatedFlipFlops) {
            if (Math.abs(x - ff.getPointCenter().getX()) <= (Sizes.baseFlipFlopXSize + xSizeCompare) / 2 &&
                Math.abs(y - ff.getPointCenter().getY()) <= (Sizes.baseFlipFlopYSize + ySizeCompare) / 2){
                return true;
            }
        }

        return false;
    }

    public boolean checkIfCoverHalf(String componentName, double x, double y){
        double xShiftCompare = 0;
        double yShiftCompare = 0;

        if(componentName.contains(Names.gateSearchName)){
            xShiftCompare = Sizes.baseGateXShift;
            yShiftCompare = Sizes.baseGateYShift;
        }
        else if(componentName.contains(Names.switchSearchName)){
            xShiftCompare = Sizes.baseSwitchXShift;
            yShiftCompare = Sizes.baseSwitchYShift;
        }
        else if(componentName.contains(Names.flipFlopSearchName)){
            xShiftCompare = Sizes.baseFlipFlopXShift;
            yShiftCompare = Sizes.baseFlipFlopYShift;
        }

        for(Gate g : arrayListCreatedGates) {
            if (Math.abs(x - g.getPointCenter().getX()) <= (Sizes.baseGateXShift + xShiftCompare) / 2 &&
                    Math.abs(y - g.getPointCenter().getY()) <= (Sizes.baseGateYShift + yShiftCompare) / 2) {
                return true;
            }
        }

        for(Switch s : arrayListCreatedSwitches) {
            if (Math.abs(x - s.getPointCenter().getX()) <= (Sizes.baseSwitchXShift + xShiftCompare) / 2 &&
                    Math.abs(y - s.getPointCenter().getY()) <= (Sizes.baseSwitchYShift + yShiftCompare) / 2){
                return true;
            }
        }

        for(FlipFlop ff : arrayListCreatedFlipFlops){
            if (Math.abs(x - ff.getPointCenter().getX()) <= (Sizes.baseFlipFlopXShift + xShiftCompare) / 2 &&
                    Math.abs(y - ff.getPointCenter().getY()) <= (Sizes.baseFlipFlopYShift + yShiftCompare) / 2) {
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

    private Switch getCoveredSwitch(double x, double y){
        double xShift = Sizes.baseSwitchXShift;
        double yShift = Sizes.baseSwitchYShift;

        for(Switch s : arrayListCreatedSwitches) {
            if (Math.abs(x - s.getPointCenter().getX()) <= Sizes.baseSwitchXShift &&
                    Math.abs(y - s.getPointCenter().getY()) <= Sizes.baseSwitchYShift) {
                return s;
            }
        }
        return null;
    }

    private FlipFlop getCoveredFlipFlop(double x, double y){
        double xShift = Sizes.baseFlipFlopXShift;
        double yShift = Sizes.baseFlipFlopYShift;

        for(FlipFlop ff : arrayListCreatedFlipFlops) {
            if (Math.abs(x - ff.getPointCenter().getX()) <= Sizes.baseFlipFlopXShift &&
                    Math.abs(y - ff.getPointCenter().getY()) <= Sizes.baseFlipFlopYShift) {
                return ff;
            }
        }
        return null;
    }

    public void repaint(){
        graphicsContext.clearRect(0, 0, canvas.getWidth() + 1, canvas.getHeight() + 1);

        graphicsContext.setLineWidth(Sizes.baseLineWidth);
        for(Line l : arrayListCreatedLines){
            l.draw(graphicsContext);
        }

        for(Component c : arrayListCreatedComponents){
            c.draw(graphicsContext);
        }

        graphicsContext.setStroke(Color.BLACK);
    }

    private void actionCanvasKeyPressed(KeyCode code){
        if(code == KeyCode.ESCAPE){
            coveredError = false;
            repaint();
            tableViewComponents.getSelectionModel().clearSelection();
            waitForComponent2 = false;
            deleteLineBuffer();
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

    public void createNewComponent(double x, double y, String newComponentName){
        tableViewComponents.getSelectionModel().clearSelection();
        try {
            Component newComponent = null;
            XYChart.Series<Long, String> newSeries= new XYChart.Series<>();
            if (newComponentName.equals(Names.gateNotName)) {
                newComponent = new Not(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateAnd2Name)) {
                newComponent = new And2(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateAnd3Name)) {
                newComponent = new And3(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateAnd4Name)) {
                newComponent = new And4(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateOr2Name)) {
                newComponent = new Or2(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateOr3Name)) {
                newComponent = new Or3(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateOr4Name)) {
                newComponent = new Or4(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateXor2Name)) {
                newComponent = new Xor2(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateXor3Name)) {
                newComponent = new Xor3(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateXor4Name)) {
                newComponent = new Xor4(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateNand2Name)) {
                newComponent = new Nand2(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateNand3Name)) {
                newComponent = new Nand3(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateNand4Name)) {
                newComponent = new Nand4(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateNor2Name)) {
                newComponent = new Nor2(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateNor3Name)) {
                newComponent = new Nor3(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateNor4Name)) {
                newComponent = new Nor4(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateXnor2Name)) {
                newComponent = new Xnor2(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateXnor3Name)) {
                newComponent = new Xnor3(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateXnor4Name)) {
                newComponent = new Xnor4(x, y, true, newSeries, timeStart);
            }
            else if(newComponentName.equals(Names.switchMonostableName)){
                newComponent = new SwitchMonostable(x, y, true, newSeries, timeStart);
            }
            else if(newComponentName.equals(Names.switchBistableName)){
                newComponent = new SwitchBistatble(x, y, true, newSeries, timeStart);
            }
            else if(newComponentName.equals(Names.switchPulseName)){
                newComponent = new SwitchPulse(x, y, true, newSeries, timeStart);
            }
            else if(newComponentName.equals(Names.flipFlopD)){
                newComponent = new FlipFlopD(x, y, true, newSeries, timeStart);
            }
            else if(newComponentName.equals(Names.flipFlopT)){
                newComponent = new FlipFlopT(x, y, true, newSeries, timeStart);
            }
            else if(newComponentName.equals(Names.flipFlopJK)){
                newComponent = new FlipFlopJK(x, y, true, newSeries, timeStart);
            }

            if(newComponent != null){
                if(newComponent.getName().contains(Names.gateSearchName)){
                    arrayListCreatedGates.add((Gate)newComponent);
                    gateCounter++;
                    newComponent.setId(gateCounter);
                }
                else if(newComponent.getName().contains(Names.switchSearchName)){
                    arrayListCreatedSwitches.add((Switch)newComponent);
                    switchCounter++;    
                    newComponent.setId(switchCounter);
                }
                else if(newComponent.getName().contains(Names.flipFlopSearchName)){
                    arrayListCreatedFlipFlops.add((FlipFlop)newComponent);
                    flipFlopCounter++;
                    newComponent.setId(flipFlopCounter);
                }
                arrayListCreatedComponents.add(newComponent);

                newSeries.getData().add(new XYChart.Data<Long, String>(0L, newComponent.getName() + " " + newComponent.getId() + ": 0"));
                newSeries.getData().add(new XYChart.Data<Long, String>(0L, newComponent.getName() + " " + newComponent.getId() + ": 1"));
                newSeries.getData().add(new XYChart.Data<Long, String>(0L, newComponent.getName() + " " + newComponent.getId() + ": 0"));

//                arrayListSeries.add(newSeries);
//                lineChartStates.getData().add(newSeries);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        coveredError = false;
        canvas.requestFocus();
    }

    public void createNewLine(double x, double y) {
        Gate g = getCoveredGate(x, y);
        Switch s = getCoveredSwitch(x, y);
        FlipFlop ff = getCoveredFlipFlop(x, y);

        tableViewComponents.getSelectionModel().clearSelection();
        comboBoxNewLineHook = new ComboBox<>();
        comboBoxNewLineHook.setLayoutX(x - 75);
        comboBoxNewLineHook.setLayoutY(y);
        comboBoxNewLineHook.promptTextProperty().setValue("Wybierz pin");
        if (g != null) {
            comboBoxNewLineHook.getItems().add(g.getPointOutput());
            for (Point p : g.getArrayPointsInputs()) {
                comboBoxNewLineHook.getItems().add(p);
            }
            s = null;
            ff = null;
        }
        else if(s != null){
            comboBoxNewLineHook.getItems().add(s.getPointLineHook());
            g = null;
            ff = null;
        }
        else if(ff != null){
            comboBoxNewLineHook.getItems().add(ff.getPointInput());
            if(ff.getName().equals(Names.flipFlopJK)){
                comboBoxNewLineHook.getItems().add(((FlipFlopJK)ff).getPointInputK());
            }
            comboBoxNewLineHook.getItems().add(ff.getPointOutput());
            comboBoxNewLineHook.getItems().add(ff.getPointOutputReversed());
            comboBoxNewLineHook.getItems().add(ff.getPointAsynchronousInput());
            comboBoxNewLineHook.getItems().add(ff.getPointClock());
            comboBoxNewLineHook.getItems().add(ff.getPointReset());
            g = null;
            s = null;
        }
        paneWorkspace.getChildren().add(comboBoxNewLineHook);

        canvas.setOnMouseClicked(e -> {
            paneWorkspace.getChildren().remove(comboBoxNewLineHook);
            canvas.setOnMouseClicked(f -> mouseActions.actionCanvasMouseClicked(f));
        });

        final Gate finalG = g;
        final Switch finalS = s;
        final FlipFlop finalFF = ff;
        if(!waitForComponent2) {
            comboBoxNewLineHook.setOnAction(e -> chooseNewLineHook1(x, y, finalG, finalS, finalFF, comboBoxNewLineHook));
        }
        else{
            comboBoxNewLineHook.setOnAction(e -> chooseNewLineHook2(x, y, finalG, finalS, finalFF, comboBoxNewLineHook));
        }
    }

    private void chooseNewLineHook1(double x, double y, Gate g, Switch s, FlipFlop ff, ComboBox<Point> comboBoxNewLineHook){
        Point p = comboBoxNewLineHook.getSelectionModel().getSelectedItem();
        String pointName = p.getName();

        if(g != null) {
            lineBuffer = new Line(p.getX(), p.getY(), x, y, g, null, Color.BLACK);
            if (pointName.contains("Output")) {
                g.getArrayListLinesOutput().add(lineBuffer);
                lineBuffer.setInput1IsOutput(true);
            }
            else if (pointName.contains("Input")) {
                int inputNumber = Integer.parseInt(p.getName().split("Input")[1]);
                g.getArrayArrayListLines()[inputNumber - 1].add(lineBuffer);
                lineBuffer.setInput1IsOutput(false);
            }
        }
        else if(s != null){
            lineBuffer = new Line(p.getX(), p.getY(), x, y, s, null, Color.BLACK);
            s.getArrayListlines().add(lineBuffer);
            lineBuffer.setInput1IsOutput(true);
        }
        else if(ff != null){
            lineBuffer = new Line(p.getX(), p.getY(), x, y, ff, null, Color.BLACK);
            if(pointName.equals("Input") || pointName.equals("Input J")){
                ff.getArrayListLinesInput().add(lineBuffer);
                lineBuffer.setInput1IsOutput(false);
            }
            else if(pointName.equals("Input K")){
                ((FlipFlopJK)ff).getArrayListLinesInputK().add(lineBuffer);
                lineBuffer.setInput1IsOutput(false);
            }
            else if(pointName.equals("Output")){
                ff.getArrayListLinesOutput().add(lineBuffer);
                lineBuffer.setInput1IsOutput(true);
            }
            else if(pointName.equals("Output reversed")){
                ff.getArrayListLinesOutputReverted().add(lineBuffer);
                lineBuffer.setInput1IsOutput(true);
            }
            else if(pointName.equals("Asynchronous input")){
                ff.getArrayListLinesAsynchronousInput().add(lineBuffer);
                lineBuffer.setInput1IsOutput(false);
            }
            else if(pointName.equals("Clock")){
                ff.getArrayListLinesClock().add(lineBuffer);
                lineBuffer.setInput1IsOutput(false);
            }
            else if(pointName.equals("Reset")){
                ff.getArrayListLinesReset().add(lineBuffer);
                lineBuffer.setInput1IsOutput(false);
            }
        }
        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
        waitForComponent2 = true;
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);

        canvas.requestFocus();
        repaint();
    }

    private void chooseNewLineHook2(double x, double y, Gate g, Switch s, FlipFlop ff, ComboBox<Point> comboBoxNewLineHook) {
        Point p = comboBoxNewLineHook.getSelectionModel().getSelectedItem();
        String pointName = p.getName();

        lineBuffer.setX2(comboBoxNewLineHook.getSelectionModel().getSelectedItem().getX());
        lineBuffer.setY2(comboBoxNewLineHook.getSelectionModel().getSelectedItem().getY());
        lineCounter++;
        lineBuffer.setId(lineCounter);
        lineBuffer.setState(lineBuffer.isState());
        arrayListCreatedLines.add(lineBuffer);

        if(g != null) {
            lineBuffer.setComponent2(g);
            if (pointName.contains("Output")) {
                g.getArrayListLinesOutput().add(lineBuffer);
                lineBuffer.setInput2IsOutput(true);
            }
            else if (pointName.contains("Input")) {
                int inputNumber = Integer.parseInt(pointName.split("Input")[1]);
                g.getArrayArrayListLines()[inputNumber - 1].add(lineBuffer);
                lineBuffer.setInput2IsOutput(false);
            }
        }
        else if(s != null){
            lineBuffer.setComponent2(s);
            s.getArrayListlines().add(lineBuffer);
            lineBuffer.setInput2IsOutput(true);
        }
        else if(ff != null){
            lineBuffer.setComponent2(ff);
            if(pointName.equals("Input") || pointName.equals("Input J")){
                ff.getArrayListLinesInput().add(lineBuffer);
                lineBuffer.setInput2IsOutput(false);
            }
            else if(pointName.equals("Input K")){
                ((FlipFlopJK)ff).getArrayListLinesInputK().add(lineBuffer);
                lineBuffer.setInput2IsOutput(false);
            }
            else if(pointName.equals("Output")){
                ff.getArrayListLinesOutput().add(lineBuffer);
                lineBuffer.setInput2IsOutput(true);
            }
            else if(pointName.equals("Output reversed")){
                ff.getArrayListLinesOutputReverted().add(lineBuffer);
                lineBuffer.setInput2IsOutput(true);
            }
            else if(pointName.equals("Asynchronous input")){
                ff.getArrayListLinesAsynchronousInput().add(lineBuffer);
                lineBuffer.setInput2IsOutput(true);
            }
            else if(pointName.equals("Clock")){
                ff.getArrayListLinesClock().add(lineBuffer);
                lineBuffer.setInput2IsOutput(false);
            }
            else if(pointName.equals("Reset")){
                ff.getArrayListLinesReset().add(lineBuffer);
                lineBuffer.setInput2IsOutput(false);
            }
        }

        lineBuffer = null;
        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
        waitForComponent2 = false;
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);

        for(Line l : arrayListCreatedLines) {
            l.getArrayListVisitedLines().clear();
            l.getArrayListDependentComponents().clear();
        }
        for(Line l : arrayListCreatedLines){
            l.checkForSignals(l.getArrayListDependentComponents(), l.getArrayListVisitedLines());
        }
        for(Line l : arrayListCreatedLines){
            l.lifeCycle();
        }

        canvas.requestFocus();
        repaint();
    }

    public void deleteLineBuffer(){
        if(lineBuffer != null) {
            Component component = lineBuffer.getComponent1();
            String name = lineBuffer.getComponent1().getName();

            if (name.contains(Names.gateSearchName)) {
                Gate gate = (Gate) component;
                if (gate.getArrayListLinesOutput().contains(lineBuffer)) {
                    gate.getArrayListLinesOutput().remove(lineBuffer);
                } else {
                    for (ArrayList<Line> al : gate.getArrayArrayListLines()) {
                        if (al.contains(lineBuffer)) {
                            al.remove(lineBuffer);
                            return;
                        }
                    }
                }
            } else if (name.contains(Names.switchSearchName)) {
                Switch sw = (Switch) component;
                sw.getArrayListlines().remove(lineBuffer);
            } else if (name.contains(Names.flipFlopSearchName)) {
                FlipFlop flipFlop = (FlipFlop) component;
                flipFlop.getArrayListLinesOutput().remove(lineBuffer);
                flipFlop.getArrayListLinesOutputReverted().remove(lineBuffer);
                flipFlop.getArrayListLinesReset().remove(lineBuffer);
                flipFlop.getArrayListLinesClock().remove(lineBuffer);
                flipFlop.getArrayListLinesInput().remove(lineBuffer);
                if (flipFlop.getName().equals(Names.flipFlopJK)) {
                    ((FlipFlopJK) flipFlop).getArrayListLinesInputK().remove(lineBuffer);
                }
            }
        }
    }

    public void loadCircuit(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while(true){
                Component c = (Component)ois.readObject();
                if(c == null){
                    break;
                }
                else if(c.getName().equals(Names.lineName)){
                    arrayListCreatedLines.add((Line)c);
                }
                else if(c.getName().contains(Names.gateSearchName)){
                    arrayListCreatedGates.add((Gate)c);
                }
                else if(c.getName().contains(Names.switchSearchName)){
                    arrayListCreatedSwitches.add((Switch)c);
                }
                else if(c.getName().contains(Names.flipFlopSearchName)){
                    arrayListCreatedFlipFlops.add((FlipFlop)c);
                }
                c.setPictures();
                arrayListCreatedComponents.add(c);
            }

            ois.close();
            fis.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveCircuit(File file){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for(Component c : arrayListCreatedComponents){
                oos.writeObject(c);
            }
            for(Line l : arrayListCreatedLines){
                oos.writeObject(l);
            }

            oos.close();
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actionMenuItemLoad(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik do załadowania");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki symulatora układów cyfrowych", "*.kksuc"));
            File file = fileChooser.showOpenDialog(main.getPrimaryStage());
            loadCircuit(file);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actionMenuItemSave(){
        if(saveFile != null){
            saveCircuit(saveFile);
        }
        else{
            actionMenuItemSaveAs();
        }
    }

    public void actionMenuItemSaveAs(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz miejsce do zapisu");
            fileChooser.setInitialFileName("Schemat1 " + new SimpleDateFormat("hh.mm dd-MM-yyyy").format(new Date()));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki symulatora układów cyfrowych", "*.kksuc"));

            saveFile = fileChooser.showSaveDialog(main.getPrimaryStage());
            if(saveFile != null) {
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }
                saveCircuit(saveFile);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
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
}

