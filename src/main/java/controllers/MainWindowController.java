package controllers;

import components.*;
import components.flipflops.FlipFlop;
import components.flipflops.FlipFlopD;
import components.flipflops.FlipFlopJK;
import components.flipflops.FlipFlopT;
import components.gates.Not;
import components.gates.and.And3;
import components.gates.and.And4;
import components.gates.or.Or3;
import components.gates.or.Or4;
import components.gates.xor.Xor2;
import components.gates.xor.Xor3;
import components.gates.xor.Xor4;
import components.switches.Switch;
import components.switches.SwitchBistatble;
import components.switches.SwitchMonostable;
import data.Accesses;
import data.MouseActions;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MainWindowController {
    private Main main;
    private ArrayList<Line> arrayListCreatedLines = new ArrayList<>();
    private ArrayList<Gate> arrayListCreatedGates = new ArrayList<>();
    private ArrayList<Switch> arrayListCreatedSwitches = new ArrayList<>();
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops = new ArrayList<>();
    private GraphicsContext graphicsContext;
    private ArrayList<TableComponent> arrayListPossibleComponents = new ArrayList<>();
    private boolean coveredError = false;

    private Line lineBuffer;
    private boolean waitForComponent2 = false;
    private boolean waitForPlaceComponent = false;
    private ComboBox<Point> comboBoxNewLineHook;
    private MouseActions mouseActions;

    private ZoomableScrollPane zsp;
    @FXML private Canvas canvas;
    @FXML private TextField textFieldFilterComponents;
    @FXML private TableView<TableComponent> tableViewComponents;
    @FXML private TableColumn<TableComponent, ImageView> tableColumnComponentsPictures;
    @FXML private TableColumn<TableComponent, Integer> tableColumnComponentInputsNumber;
    @FXML private Button buttonDelete;
    @FXML private Button buttonRotate;
    @FXML private ScrollPane scrollPaneWorkspace;
    @FXML private Pane paneWorkspace;

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
        arrayListPossibleComponents.add(new TableComponent(Names.flipFlopJK, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.flipFlopD, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/flipflops/D_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));
        arrayListPossibleComponents.add(new TableComponent(Names.flipFlopT, 1,
                                        new ImageView(new Image(getClass().getResource("/graphics/flipflops/T_off.png").toExternalForm(),
                                                Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false))));

        ObservableList<TableComponent> ol = FXCollections.observableList(arrayListPossibleComponents);
        tableViewComponents.setItems(ol);
        tableViewComponents.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> actionSelectionChanged());
        tableColumnComponentsPictures.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        tableColumnComponentInputsNumber.setCellValueFactory(new PropertyValueFactory<>("InputsNumber"));

        graphicsContext = canvas.getGraphicsContext2D();

        mouseActions = new MouseActions(this);

        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
        canvas.setOnMouseMoved(e -> mouseActions.actionCanvasMouseMoved(e.getX(), e.getY()));
        canvas.setOnMouseDragged(e -> mouseActions.actionCanvasMouseDragged(e));
        canvas.setOnMousePressed(e -> mouseActions.actionCanvasMousePressed(e));
        canvas.setOnMouseReleased(e -> mouseActions.actionCanvasMouseReleased(e.getX(), e.getY()));

        tableViewComponents.setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));
    }

    public void myInitialize(Main main){
        this.main = main;
        this.zsp = main.getZsp();
        main.getScene().setOnKeyPressed(e -> actionCanvasKeyPressed(e.getCode()));
        main.getScene().setOnKeyTyped(e -> actionCanvasKeyTyped(e.getCharacter()));
        main.getScene().setOnKeyReleased(e -> actionCanvasKeyReleased(e.getCode()));
    }

    public void loadCircuit(File file){

    }

    public void actionDelete(){
        Iterator<Line> iteratorLines = arrayListCreatedLines.iterator();
        if(iteratorLines.hasNext()){
            Line l = iteratorLines.next();
            if(l.isSelected()){
                l.delete(arrayListCreatedLines);
            }
        }

        Iterator<Gate> iteratorGates = arrayListCreatedGates.iterator();
        if(iteratorGates.hasNext()){
            Gate g = iteratorGates.next();
            if(g.isSelected()){
                while(g.getArrayListLinesOutput().size() > 0){
                    g.getArrayListLinesOutput().get(0).delete(arrayListCreatedLines);
                }
                for(ArrayList<Line> al : g.getArrayArrayListLines()){
                    while(al.size() > 0){
                        al.get(0).delete(arrayListCreatedLines);
                    }
                }
                arrayListCreatedGates.remove(g);
            }
        }

        Iterator<Switch> iteratorSwitches = arrayListCreatedSwitches.iterator();
        if(iteratorSwitches.hasNext()){
            Switch s = iteratorSwitches.next();
            if(s.isSelected()){
                while(s.getArrayListlines().size() > 0){
                    s.getArrayListlines().get(0).delete(arrayListCreatedLines);
                }
                arrayListCreatedSwitches.remove(s);
            }
        }
        repaint();
    }

    public void actionSelectionChanged() {
        if (Accesses.logTableView){
            System.out.println("TableView selection changed");
        }

        try {
            String selectedName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
            waitForPlaceComponent = selectedName.contains("And") || selectedName.contains("Or") || selectedName.contains("Xor") || selectedName.contains("Not") ||
                    selectedName.contains("Nand") || selectedName.contains("Nor") || selectedName.contains("Xnor") || selectedName.contains("Switch");
        } catch(Exception e){
            waitForPlaceComponent = false;
        }
        coveredError = false;
        waitForComponent2 = false;
        lineBuffer = null;
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
        for(Gate g : arrayListCreatedGates){
            if(g.isSelected()){
                g.rotate();
            }
        }

        for(Switch s : arrayListCreatedSwitches){
            if(s.isSelected()){
                s.rotate();
            }
        }
        repaint();

        for(Line l : arrayListCreatedLines){
            System.out.println(l.getX1() + " " + l.getY1() + " " + l.getX2() + " " + l.getY2());
        }
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

        for(Gate g : arrayListCreatedGates){
            g.draw(graphicsContext);
        }

        for(Switch s : arrayListCreatedSwitches){
            s.draw(graphicsContext);
        }

        graphicsContext.setLineWidth(Sizes.baseLineWidth);
        for(Line l : arrayListCreatedLines){
            l.draw(graphicsContext);
        }

        for(FlipFlop ff : arrayListCreatedFlipFlops){
            ff.draw(graphicsContext);
        }

        graphicsContext.setStroke(Color.BLACK);
    }

    private void actionCanvasKeyPressed(KeyCode code){
        if(code == KeyCode.ESCAPE){
            coveredError = false;
            repaint();
            tableViewComponents.getSelectionModel().clearSelection();
            lineBuffer = null;
            waitForComponent2 = false;
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
            if (newComponentName.equals(Names.gateNotName)) {
                Gate newGate = new Not(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateAnd2Name)) {
                Gate newGate = new And2(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateAnd3Name)) {
                Gate newGate = new And3(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateAnd4Name)) {
                Gate newGate = new And4(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateOr2Name)) {
                Gate newGate = new Or2(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateOr3Name)) {
                Gate newGate = new Or3(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateOr4Name)) {
                Gate newGate = new Or4(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateXor2Name)) {
                Gate newGate = new Xor2(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateXor3Name)) {
                Gate newGate = new Xor3(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if (newComponentName.equals(Names.gateXor4Name)) {
                Gate newGate = new Xor4(x, y);
                arrayListCreatedGates.add(newGate);
            }
            else if(newComponentName.equals(Names.switchMonostableName)){
                SwitchMonostable newSwitch = new SwitchMonostable(x, y);
                arrayListCreatedSwitches.add(newSwitch);
            }
            else if(newComponentName.equals(Names.switchBistableName)){
                SwitchBistatble newSwitch = new SwitchBistatble(x, y);
                arrayListCreatedSwitches.add(newSwitch);
            }
            else if(newComponentName.equals(Names.flipFlopD)){
                FlipFlop newFlipflop = new FlipFlopD(x, y);
                arrayListCreatedFlipFlops.add(newFlipflop);
            }
            else if(newComponentName.equals(Names.flipFlopT)){
                FlipFlop newFlipflop = new FlipFlopT(x, y);
                arrayListCreatedFlipFlops.add(newFlipflop);
            }
            else if(newComponentName.equals(Names.flipFlopJK)){
                FlipFlop newFlipflop = new FlipFlopJK(x, y);
                arrayListCreatedFlipFlops.add(newFlipflop);
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
        }
        if(s != null){
            comboBoxNewLineHook.getItems().add(s.getPointLineHook());
        }
        paneWorkspace.getChildren().add(comboBoxNewLineHook);

        canvas.setOnMouseClicked(e -> {
            paneWorkspace.getChildren().remove(comboBoxNewLineHook);
            canvas.setOnMouseClicked(f -> mouseActions.actionCanvasMouseClicked(f));
        });

        if(!waitForComponent2) {
            comboBoxNewLineHook.setOnAction(e -> chooseNewLineHook1(x, y, g, s, comboBoxNewLineHook));
        }
        else{
            comboBoxNewLineHook.setOnAction(e -> chooseNewLineHook2(x, y, g, s, comboBoxNewLineHook));
        }
    }

    private void chooseNewLineHook1(double x, double y, Gate g, Switch s, ComboBox<Point> comboBoxNewLineHook){
        Point p = comboBoxNewLineHook.getSelectionModel().getSelectedItem();
        if(g != null) {
            lineBuffer = new Line(p.getX(), p.getY(), x, y, g, null, null, null, null, null,  Color.BLACK);
            if (p.getName().contains("Output")) {
                g.getArrayListLinesOutput().add(lineBuffer);
                lineBuffer.setInput1IsOutput(true);
            }
            else if (p.getName().contains("Input")) {
                int inputNumber = Integer.parseInt(p.getName().split("Input")[1]);
                g.getArrayArrayListLines()[inputNumber - 1].add(lineBuffer);
                lineBuffer.setInput1IsOutput(false);
            }
        }
        else if(s != null){
            lineBuffer = new Line(p.getX(), p.getY(), x, y, null, null, s, null, null, null,  Color.BLACK);
            s.getArrayListlines().add(lineBuffer);
            lineBuffer.setInput1IsOutput(false);
            s.sendSignal();
        }

        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
        waitForComponent2 = true;
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);

        canvas.requestFocus();
        repaint();
    }

    private void chooseNewLineHook2(double x, double y, Gate g, Switch s, ComboBox<Point> comboBoxNewLineHook) {
        Point p = comboBoxNewLineHook.getSelectionModel().getSelectedItem();
        lineBuffer.setX2(comboBoxNewLineHook.getSelectionModel().getSelectedItem().getX());
        lineBuffer.setY2(comboBoxNewLineHook.getSelectionModel().getSelectedItem().getY());

        arrayListCreatedLines.add(lineBuffer);
        lineBuffer.setState(lineBuffer.isState());

        if(g != null) {
            lineBuffer.setGate2(g);
            if (p.getName().contains("Output")) {
                g.getArrayListLinesOutput().add(lineBuffer);
                lineBuffer.setInput2IsOutput(true);
            }
            else if (p.getName().contains("Input")) {
                int inputNumber = Integer.parseInt(p.getName().split("Input")[1]);
                g.getArrayArrayListLines()[inputNumber - 1].add(lineBuffer);
                lineBuffer.setInput2IsOutput(false);
            }
        }
        else if(s != null){
            lineBuffer.setSwitch2(s);
            s.getArrayListlines().add(lineBuffer);
            lineBuffer.setInput2IsOutput(false);
            s.sendSignal();
        }

        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
        waitForComponent2 = false;
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);

        canvas.requestFocus();
        repaint();
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

    public ZoomableScrollPane getZsp() {
        return zsp;
    }
}

