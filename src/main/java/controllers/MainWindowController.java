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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

import java.util.*;
import java.util.concurrent.Executors;
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
    @FXML private Button buttonStartStopChart;

    private Main main;
    private ArrayList<Line> arrayListCreatedLines = new ArrayList<>();
    private ArrayList<Connector> arrayListCreatedConnectors = new ArrayList<>();
    private ArrayList<Gate> arrayListCreatedGates = new ArrayList<>();
    private ArrayList<Switch> arrayListCreatedSwitches = new ArrayList<>();
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops = new ArrayList<>();
    private ArrayList<Bulb> arrayListCreatedBulbs = new ArrayList<>();
    private ArrayList<Component> arrayListCreatedEndComponents = new ArrayList<>();
    private ArrayList<Component> arrayListAllCreatedComponents = new ArrayList<>();
    private ArrayList<Component> arrayListDeletedComponents = new ArrayList<>();
    private GraphicsContext graphicsContext;
    private ArrayList<TableComponent> arrayListPossibleComponents = new ArrayList<>();
    private ArrayList<XYChart.Series<Long, String>> arrayListSeries = new ArrayList<>();
    private Stack<Change> stackUndoChanges = new Stack<>();
    private Stack<Change> stackRedoChanges = new Stack<>();

    private Line lineBuffer;
    private boolean waitForComponent2 = false;
    private boolean waitForPlaceComponent = false;
    private boolean coveredError = false;
    private boolean draggedSelectionRectangle = false;
    private ComboBox<Point> comboBoxNewLineHook;
    private MouseActions mouseActions;
    private ComponentCreator componentCreator;
    private FileOperator fileOperator;
    private Component componentBuffer = null;
    private MouseButton mouseButton = null;
    private int chartExtension = 0;

    private ZoomableScrollPaneWorkspace zoomableScrollPaneWorkspace;
    private ZoomableScrollPaneChart zoomableScrollPaneChart;

    private Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    private Text textAlertContext = new Text();

    @FXML
    public void initialize(){
        arrayListPossibleComponents.add(new TableComponent("Line", 2,
                                        new ImageView(new Image(getClass().getResource("/graphics/line_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent("Bulb", 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/bulb/bulb_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent("Connector", 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/connector.png").toExternalForm(),
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
        canvas.setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));
        zoomableScrollPaneWorkspace.setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));

        Executors.newFixedThreadPool(1).execute(() -> repaintThread());

        graphicsContext.setFont(new Font("Arial", 24));

        informationAlert.setTitle(Names.manualTitle);
        informationAlert.setGraphic(new ImageView(new Image(getClass().getResource("/graphics/happy_gate.png").toExternalForm(),
                Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false)));
        informationAlert.initModality(Modality.NONE);
        textAlertContext.setWrappingWidth(500);
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
//        new Thread(() -> {
//            while(true){
//                System.out.println(stackUndoChanges.size() + " " + stackRedoChanges.size() + " " + arrayListCreatedEndComponents.size());
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        System.out.println("SWITCHES--------------------");
        for(Switch s : arrayListCreatedSwitches){
            System.out.println(s.getId() + " " + s.isSignalOutput() + " " + s.isAlive() + " " + s.getImageViewOff() + " " + s.getPointCenter().getX() + " " + s.getPointCenter().getY());
        }
        System.out.println("LINES--------------------");
        for(Line l : arrayListCreatedLines){
            System.out.println(l.getId() + " " + l.isSignalOutput() + " " + l.isAlive() + " " + l.getImageViewOff());
        }
        System.out.println("BULBS--------------------");
        for(Bulb b : arrayListCreatedBulbs){
            System.out.println(b.getId() + " " + b.isSignalOutput() + " " + b.isAlive() + " " + b.getImageViewOff() + " " + b.getPointCenter().getX() + " " + b.getPointCenter().getY());
        }
    }

    private void setChart(){
        lineChartStates.setCreateSymbols(false);
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
                if(maxLength > 0 && lineChartStates.getPrefWidth() < 924 + maxLength * 21.5 + chartExtension){
                    lineChartStates.setPrefWidth(924 + maxLength * 21.5 + chartExtension);
                }
                lineChartStates.setPrefHeight(lineChartStates.getData().size() * 100);
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

    public void repaintThread(){
        boolean stateChanged;

        while(true) {
            try {
                stateChanged = false;
                for (Component c : arrayListAllCreatedComponents) {
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
            for (Component c : arrayListAllCreatedComponents) {
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
            if (draggedSelectionRectangle && mouseButton == MouseButton.PRIMARY){
                mouseActions.drawSelectionRectangle();
            }
        });
    }

    public void actionStartStopChart(){
        boolean checked = false;
        boolean chartAlive = false;
        for(Component c : arrayListCreatedEndComponents){
            c.setAddingDataToSeriesEnabled(!c.isAddingDataToSeriesEnabled());
            if(!checked){
                checked = true;
                chartAlive = c.isAddingDataToSeriesEnabled();
            }
            XYChart.Data<Long, String> data = c.getSeries().getData().get(c.getSeries().getData().size() - 1);
            c.getSeries().getData().add(new XYChart.Data<Long, String>(System.currentTimeMillis() - c.getChartMillisCounter(), data.getYValue()));
            if(c.isSignalOutput()) {
                c.getSeries().getData().add(new XYChart.Data<Long, String>(System.currentTimeMillis() - c.getChartMillisCounter(), c.getName() + " " + c.getId() + ": 1"));
            }
            else{
                c.getSeries().getData().add(new XYChart.Data<Long, String>(System.currentTimeMillis() - c.getChartMillisCounter(), c.getName() + " " + c.getId() + ": 0"));
            }
        }
        if(chartAlive){
            buttonStartStopChart.setText("Zatrzymaj");
        }
        else{
            buttonStartStopChart.setText("Wznów");
        }
    }

    public void actionResetChart(){
        lineChartStates.getData().clear();
        arrayListSeries.clear();
        for(Component c : arrayListCreatedEndComponents){
            XYChart.Series<Long, String> s = new XYChart.Series<>();
            s.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 0"));
            s.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 1"));
            s.getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 0"));
            if(c.isSignalOutput()) {
                c.getSeries().getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 1"));
            }
            else{
                c.getSeries().getData().add(new XYChart.Data<Long, String>(0L, c.getName() + " " + c.getId() + ": 0"));
            }
            c.resetSeries(s);
            arrayListSeries.add(s);
            lineChartStates.getData().add(s);
        }

    }

    public void actionExtendChart(){
        chartExtension += 100;
        lineChartStates.setPrefWidth(lineChartStates.getPrefWidth() + chartExtension);
        if(checkBoxScrollToRight.isSelected()){
            zoomableScrollPaneChart.setHvalue(1);
        }
    }

    public void actionConstrictChart(){
        chartExtension -= 100;
        lineChartStates.setPrefWidth(lineChartStates.getPrefWidth() - chartExtension);
        if(checkBoxScrollToRight.isSelected()){
            zoomableScrollPaneChart.setHvalue(1);
        }
    }

    public void actionDelete(){
        componentCreator.deleteComponent();
    }

    public void actionSelectionChanged() {
        if (Accesses.logTableView){
            System.out.println("TableView selection changed");
        }

        try {
            String selectedName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
            waitForPlaceComponent = selectedName.contains(Names.gateSearchName) || selectedName.contains(Names.switchSearchName) ||
                    selectedName.contains(Names.flipFlopSearchName) || selectedName.contains(Names.bulbName) || selectedName.contains(Names.connectorName);
            createComponentBuffer(selectedName);
            mouseActions.setNewComponentName(selectedName);
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
        for(Component c : arrayListCreatedEndComponents){
            if(c.isSelected()){
                c.rotate();
            }
        }
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
            mouseActions.setFitToCheck(true);
            zoomableScrollPaneChart.setCtrldown(true);
        }
        else if(code == KeyCode.DELETE) {
            actionDelete();
        }
        else if(code == KeyCode.SHIFT){
            componentCreator.setShiftDown(true);
            mouseActions.setShiftDown(true);
        }
        else if(code == KeyCode.ALT){
            zoomableScrollPaneChart.setAltDown(true);
        }

        mouseActions.actionCanvasMouseMoved(mouseActions.getPointMouseMoved().getX(), mouseActions.getPointMouseMoved().getY());
    }

    private void actionCanvasKeyTyped(String character){
        int charValue = character.getBytes()[0];
        System.out.println(charValue);

        if(character.matches("[0-9]")){
            int index = Integer.parseInt(character);
            if(tableViewComponents.getItems().size() >= index){
                tableViewComponents.getSelectionModel().select(index - 1);
            }
            else{
                tableViewComponents.getSelectionModel().clearSelection();
            }
        }
        else if(character.matches("r") || character.matches("R")){
            for(Component c : arrayListCreatedEndComponents){
                if(c.isSelected()){
                    c.rotate();
                }
            }
        }
        else if(charValue == 26){
            componentCreator.revertChange(true);
        }
        else if(charValue == 25){
            componentCreator.revertChange(false);
        }
        else if(charValue == 19){
            fileOperator.actionMenuItemSave();
        }
        else if(charValue == 15){
            fileOperator.actionMenuItemLoad();
        }
        else if(charValue == 1){
            for(Component c : arrayListAllCreatedComponents){
                c.setSelected(true);
            }
            for(Line l : arrayListCreatedLines){
                l.setSelected(true);
            }
        }
        else if(charValue == 27){
            for(Component c : arrayListAllCreatedComponents){
                c.setSelected(false);
            }
            for(Line l : arrayListCreatedLines){
                l.setSelected(false);
            }
        }

        mouseActions.actionCanvasMouseMoved(mouseActions.getPointMouseMoved().getX(), mouseActions.getPointMouseMoved().getY());
    }

    private void actionCanvasKeyReleased(KeyCode code){
        if(code == KeyCode.CONTROL){
            mouseActions.setFitToCheck(false);
            zoomableScrollPaneChart.setCtrldown(false);
        }
        else if(code == KeyCode.SHIFT){
            componentCreator.setShiftDown(false);
            mouseActions.setShiftDown(false);
        }
        else if(code == KeyCode.ALT){
            zoomableScrollPaneChart.setAltDown(false);
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
        for(Component c : arrayListCreatedEndComponents){
            c.kill();
        }
        System.exit(0);
    }

    public void actionMenuItemUndoChange(){
        componentCreator.revertChange(true);
    }

    public void actionMenuItemRedoChange(){
        componentCreator.revertChange(false);
    }

    public void actionMenuItemChangeCanvasSize(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChangeCanvasSizeWindow.fxml"));
            Pane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);
            ChangeCanvasSizeController changeCanvasSizeController = loader.getController();
            changeCanvasSizeController.setArrayListComponents(arrayListCreatedEndComponents);
            changeCanvasSizeController.setCanvas(canvas);
            changeCanvasSizeController.setPaneWorkspace(paneWorkspace);
            changeCanvasSizeController.setStage(stage);
            changeCanvasSizeController.myInitialize();

            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void actionMenuItemAuthor(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Names.aboutAuthorTitle);
        alert.setHeaderText(Names.aboutAuthorHeader);
        alert.setContentText(Names.aboutAuthorContent);
        alert.showAndWait();
    }

    public void actionMenuItemComponents(){
        informationAlert.setHeaderText(Names.manualComponentsHeader);
        textAlertContext.setText(Names.manualComponentsContent);
        textAlertContext.setWrappingWidth(500);
        informationAlert.setContentText(textAlertContext.getText());
        informationAlert.setWidth(600);
        informationAlert.show();
    }

    public void actionMenuItemGates(){
        informationAlert.setHeaderText(Names.manualGatesHeader);
        textAlertContext.setText(Names.manualGatesContent);
        textAlertContext.setWrappingWidth(500);
        informationAlert.setContentText(textAlertContext.getText());
        informationAlert.setWidth(600);
        informationAlert.show();
    }

    public void actionMenuItemSwitches(){
        informationAlert.setHeaderText(Names.manualSwitchesHeader);
        textAlertContext.setText(Names.manualSwitchesContent);
        textAlertContext.setWrappingWidth(500);
        informationAlert.setContentText(textAlertContext.getText());
        informationAlert.setWidth(600);
        informationAlert.show();
    }

    public void actionMenuItemFlipFlops(){

    }

    public void actionMenuItemBulbs(){

    }

    public void actionMenuItemConnectors(){

    }

    public void actionMenuItemLines(){

    }

    public void actionMenuItemWorkspace(){

    }

    public void actionMenuItemTimeCourses(){

    }

    public void actionMenuItemComponentsFilter(){

    }

    public void actionMenuItemRevertingChanges(){

    }

    public void actionMenuItemSavingCircuit(){

    }

    public void actionMenuItemKeyboardActions(){

    }

    public void actionMenuItemMouseActions(){

    }

    public void actionMenuItemErrors(){

    }

    public void actionMenuItemCreatingComponents(){

    }

    public void createComponentBuffer(String newComponentName){
        if (newComponentName.equals(Names.bulbName)) {
            componentBuffer = new Bulb(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.connectorName)) {
            componentBuffer = new Connector(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateNotName)) {
            componentBuffer = new Not(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateAnd2Name)) {
            componentBuffer = new And2(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateAnd3Name)) {
            componentBuffer = new And3(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateAnd4Name)) {
            componentBuffer = new And4(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateOr2Name)) {
            componentBuffer = new Or2(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateOr3Name)) {
            componentBuffer = new Or3(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateOr4Name)) {
            componentBuffer = new Or4(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateXor2Name)) {
            componentBuffer = new Xor2(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateXor3Name)) {
            componentBuffer = new Xor3(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateXor4Name)) {
            componentBuffer = new Xor4(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateNand2Name)) {
            componentBuffer = new Nand2(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateNand3Name)) {
            componentBuffer = new Nand3(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateNand4Name)) {
            componentBuffer = new Nand4(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateNor2Name)) {
            componentBuffer = new Nor2(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateNor3Name)) {
            componentBuffer = new Nor3(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateNor4Name)) {
            componentBuffer = new Nor4(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateXnor2Name)) {
            componentBuffer = new Xnor2(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateXnor3Name)) {
            componentBuffer = new Xnor3(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.gateXnor4Name)) {
            componentBuffer = new Xnor4(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.switchMonostableName)) {
            componentBuffer = new SwitchMonostable(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.switchBistableName)) {
            componentBuffer = new SwitchBistatble(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.switchPulseName)) {
            componentBuffer = new SwitchPulse(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.flipFlopD)) {
            componentBuffer = new FlipFlopD(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.flipFlopT)) {
            componentBuffer = new FlipFlopT(-200, -200, false, null, null);
        } else if (newComponentName.equals(Names.flipFlopJK)) {
            componentBuffer = new FlipFlopJK(-200, -200, false, null, null);
        } else {
            componentBuffer = null;
        }
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

    public ArrayList<Component> getArrayListCreatedEndComponents() {
        return arrayListCreatedEndComponents;
    }

    public ArrayList<Connector> getArrayListCreatedConnectors() {
        return arrayListCreatedConnectors;
    }

    public ArrayList<Bulb> getArrayListCreatedBulbs() {
        return arrayListCreatedBulbs;
    }

    public ArrayList<Component> getArrayListDeletedComponents() {
        return arrayListDeletedComponents;
    }

    public ArrayList<Component> getArrayListAllCreatedComponents() {
        return arrayListAllCreatedComponents;
    }

    public Stack<Change> getStackUndoChanges() {
        return stackUndoChanges;
    }

    public Stack<Change> getStackRedoChanges() {
        return stackRedoChanges;
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

    public boolean isDraggedSelectionRectangle() {
        return draggedSelectionRectangle;
    }

    public void setDraggedSelectionRectangle(boolean draggedSelectionRectangle) {
        this.draggedSelectionRectangle = draggedSelectionRectangle;
    }

    public MouseButton getMouseButton() {
        return mouseButton;
    }

    public void setMouseButton(MouseButton mouseButton) {
        this.mouseButton = mouseButton;
    }

    public ComponentCreator getComponentCreator() {
        return componentCreator;
    }
}

