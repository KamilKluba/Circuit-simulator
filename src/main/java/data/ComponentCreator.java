package data;

import components.*;
import components.flipflops.FlipFlop;
import components.flipflops.FlipFlopD;
import components.flipflops.FlipFlopJK;
import components.flipflops.FlipFlopT;
import components.gates.Gate;
import components.gates.Not;
import components.gates.and.And2;
import components.gates.and.And3;
import components.gates.and.And4;
import components.gates.nand.Nand2;
import components.gates.nand.Nand3;
import components.gates.nand.Nand4;
import components.gates.nor.Nor2;
import components.gates.nor.Nor3;
import components.gates.nor.Nor4;
import components.gates.or.Or2;
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
import controllers.MainWindowController;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Stack;

public class ComponentCreator {
    private MainWindowController mwc;
    private TableView<TableComponent> tableViewComponents;
    private ArrayList<Gate> arrayListCreatedGates;
    private ArrayList<Switch> arrayListCreatedSwitches;
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops;
    private ArrayList<Bulb> arrayListCreatedBulbs;
    private ArrayList<Line> arrayListCreatedLines;
    private ArrayList<Connector> arrayListCreatedConnectors;
    private ArrayList<Component> arrayListCreatedEndComponents;
    private ArrayList<Component> arrayListAllCreatedComponents;
    private ArrayList<Component> arrayListDeletedComponents;
    private Stack<Change> stackUndoChanges;
    private Stack<Change> stackRedoChanges;
    private ArrayList<XYChart.Series<Long, String>> arrayListSeries;
    private LineChart lineChartStates;
    private ComboBox<Point> comboBoxNewLineHook;
    private Canvas canvas;
    private Pane paneWorkspace;
    private MouseActions mouseActions;

    private long timeStart;
    private int lineCounter = 0;
    private int gateCounter = 0;
    private int switchCounter = 0;
    private int flipFlopCounter = 0;
    private int bulbCounter = 0;
    private int connectorCounter = 0;
    private boolean shiftDown = false;

    public ComponentCreator(MainWindowController mwc) {
        this.mwc = mwc;
        this.tableViewComponents = mwc.getTableViewComponents();
        this.arrayListCreatedGates = mwc.getArrayListCreatedGates();
        this.arrayListCreatedSwitches = mwc.getArrayListCreatedSwitches();
        this.arrayListCreatedFlipFlops = mwc.getArrayListCreatedFlipFlops();
        this.arrayListCreatedBulbs = mwc.getArrayListCreatedBulbs();
        this.arrayListCreatedLines = mwc.getArrayListCreatedLines();
        this.arrayListCreatedConnectors = mwc.getArrayListCreatedConnectors();
        this.arrayListCreatedEndComponents = mwc.getArrayListCreatedEndComponents();
        this.arrayListAllCreatedComponents = mwc.getArrayListAllCreatedComponents();
        this.arrayListDeletedComponents = mwc.getArrayListDeletedComponents();
        this.stackUndoChanges = mwc.getStackUndoChanges();
        this.stackRedoChanges = mwc.getStackRedoChanges();
        this.arrayListSeries = mwc.getArrayListSeries();
        this.lineChartStates = mwc.getLineChartStates();
        this.canvas = mwc.getCanvas();
        this.paneWorkspace = mwc.getPaneWorkspace();
        this.mouseActions = mwc.getMouseActions();

        timeStart = System.currentTimeMillis();
    }

    public void revertChange(boolean undoChange){
        Change change = null;

        if(undoChange && stackUndoChanges.size() > 0) {
            change = stackUndoChanges.pop();
            stackRedoChanges.push(change);
        }
        else if(stackRedoChanges.size() > 0) {
            change = stackRedoChanges.pop();
            stackUndoChanges.push(change);
        }

        if(change != null){
            String componentName = change.getComponentName();
            int componentId = change.getComponentId();
            Component component = null;

            switch (change.getDescription()) {
                case 1:
                    if(undoChange) {
                        for (Component c : arrayListAllCreatedComponents) {
                            if (c.getName().equals(componentName) && c.getId() == componentId) {
                                component = c;
                            }
                        }
                        if(componentName.contains(Names.lineName)) {
                            for (Line l : arrayListCreatedLines) {
                                if (l.getId() == componentId) {
                                    component = l;
                                    l.delete();
                                }
                            }
                        }
                        component.kill();
                        arrayListDeletedComponents.add(component);
                        arrayListCreatedLines.remove(component);
                        arrayListCreatedGates.remove(component);
                        arrayListCreatedSwitches.remove(component);
                        arrayListCreatedFlipFlops.remove(component);
                        arrayListCreatedConnectors.remove(component);
                        arrayListCreatedEndComponents.remove(component);
                        arrayListAllCreatedComponents.remove(component);
                    }
                    else{
                        for (Component c : arrayListDeletedComponents) {
                            if (c.getName().equals(componentName) && c.getId() == componentId) {
                                component = c;
                            }
                        }
                        component.revive();
                        if(component.getName().contains(Names.lineName)) {
                            arrayListCreatedLines.add((Line) component);
                        }
                        else if(component.getName().contains(Names.gateSearchName)){
                            arrayListCreatedGates.add((Gate)component);
                            arrayListCreatedEndComponents.add(component);
                            arrayListAllCreatedComponents.add(component);
                        }
                        else if(component.getName().contains(Names.switchSearchName)){
                            arrayListCreatedSwitches.add((Switch)component);
                            arrayListCreatedEndComponents.add(component);
                            arrayListAllCreatedComponents.add(component);
                        }
                        else if(component.getName().contains(Names.flipFlopSearchName)){
                            arrayListCreatedFlipFlops.add((FlipFlop)component);
                            arrayListCreatedEndComponents.add(component);
                            arrayListAllCreatedComponents.add(component);
                        }
                        else if(component.getName().contains(Names.bulbName)){
                            arrayListCreatedBulbs.add((Bulb)component);
                            arrayListCreatedEndComponents.add(component);
                            arrayListAllCreatedComponents.add(component);
                        }
                        else if(component.getName().contains(Names.connectorName)){
                            arrayListCreatedConnectors.add((Connector)component);
                            arrayListAllCreatedComponents.add(component);
                        }
                        arrayListDeletedComponents.remove(component);
                    }
                    break;
                case 2:
                    if(undoChange) {
                        for (Component c : arrayListDeletedComponents) {
                            if (c.getName().equals(componentName) && c.getId() == componentId) {
                                component = c;
                            }
                        }
                        component.revive();
                        if (component.getName().contains(Names.lineName)) {
                            arrayListCreatedLines.add((Line) component);
                        } else if (component.getName().contains(Names.gateSearchName)) {
                            arrayListCreatedGates.add((Gate) component);
                            arrayListCreatedEndComponents.add(component);
                            arrayListAllCreatedComponents.add(component);
                        } else if (component.getName().contains(Names.switchSearchName)) {
                            arrayListCreatedSwitches.add((Switch) component);
                            arrayListAllCreatedComponents.add(component);
                            arrayListCreatedEndComponents.add(component);
                        } else if (component.getName().contains(Names.flipFlopSearchName)) {
                            arrayListCreatedFlipFlops.add((FlipFlop) component);
                            arrayListCreatedEndComponents.add(component);
                            arrayListAllCreatedComponents.add(component);
                        } else if (component.getName().contains(Names.bulbName)) {
                            arrayListCreatedBulbs.add((Bulb) component);
                            arrayListCreatedEndComponents.add(component);
                            arrayListAllCreatedComponents.add(component);
                        } else if (component.getName().contains(Names.connectorName)) {
                            arrayListCreatedConnectors.add((Connector) component);
                            arrayListAllCreatedComponents.add(component);
                        }
                        arrayListDeletedComponents.remove(component);
                    }
                    else{
                        for (Component c : arrayListAllCreatedComponents) {
                            if (c.getName().equals(componentName) && c.getId() == componentId) {
                                component = c;
                            }
                        }
                        if(componentName.contains(Names.lineName)) {
                            for (Line l : arrayListCreatedLines) {
                                if (l.getId() == componentId) {
                                    component = l;
                                }
                            }
                        }
                        component.kill();
                        arrayListDeletedComponents.add(component);
                        arrayListCreatedLines.remove(component);
                        arrayListCreatedGates.remove(component);
                        arrayListCreatedSwitches.remove(component);
                        arrayListCreatedFlipFlops.remove(component);
                        arrayListCreatedConnectors.remove(component);
                        arrayListCreatedEndComponents.remove(component);
                        arrayListAllCreatedComponents.remove(component);
                    }
                    break;
                case 3:
                    for (Component c : arrayListAllCreatedComponents) {
                        if (c.getName().equals(componentName) && c.getId() == componentId) {
                            component = c;
                        }
                    }
                    if(undoChange){
                        component.getPointCenter().setX(change.getOldX());
                        component.getPointCenter().setY(change.getOldY());
                    }
                    else{
                        component.getPointCenter().setX(change.getNewX());
                        component.getPointCenter().setY(change.getNewY());
                    }
                    component.movePoints();
                    break;
                case 4:
                    for(Switch s : arrayListCreatedSwitches){
                        if(s.getId() == componentId){
                            if(undoChange) {
                                s.setState(change.isOldState());
                            }
                            else{
                                s.setState(change.isNewState());
                            }
                            break;
                        }
                    }
            }
        }
    }

    public Gate getCoveredGate(double x, double y){
        for(Gate g : arrayListCreatedGates) {
            if (Math.abs(x - g.getPointCenter().getX()) <= Sizes.baseGateXShift &&
                    Math.abs(y - g.getPointCenter().getY()) <= Sizes.baseGateYShift) {
                return g;
            }
        }
        return null;
    }

    public Switch getCoveredSwitch(double x, double y){
        for(Switch s : arrayListCreatedSwitches) {
            if (Math.abs(x - s.getPointCenter().getX()) <= Sizes.baseSwitchXShift &&
                    Math.abs(y - s.getPointCenter().getY()) <= Sizes.baseSwitchYShift) {
                return s;
            }
        }
        return null;
    }

    public FlipFlop getCoveredFlipFlop(double x, double y){
        for(FlipFlop ff : arrayListCreatedFlipFlops) {
            if (Math.abs(x - ff.getPointCenter().getX()) <= Sizes.baseFlipFlopXShift &&
                    Math.abs(y - ff.getPointCenter().getY()) <= Sizes.baseFlipFlopYShift) {
                return ff;
            }
        }
        return null;
    }

    public Bulb getCoveredBulb(double x, double y){
        for(Bulb b : arrayListCreatedBulbs) {
            if (Math.abs(x - b.getPointCenter().getX()) <= Sizes.baseGateXShift &&
                    Math.abs(y - b.getPointCenter().getY()) <= Sizes.baseGateYShift) {
                return b;
            }
        }
        return null;
    }

    public Connector getCoveredConnector(double x, double y){
        for(Connector con : arrayListCreatedConnectors) {
            if (Math.abs(x - con.getPointCenter().getX()) <= Sizes.baseConnectorXShift &&
                    Math.abs(y - con.getPointCenter().getY()) <= Sizes.baseConnectorYShift) {
                return con;
            }
        }
        return null;
    }

    public void deleteComponent(){
        ArrayList<Component> arrayListComponentsToDelete = new ArrayList<>();
        for (Line l : arrayListCreatedLines) {
            if (l.isSelected()) {
                if(shiftDown){
                    l.getArrayListBreakPoints().remove(l.getClosePoint());
                    l.setClosePoint(null);
                }
                else {
                    arrayListComponentsToDelete.add(l);
                    l.delete();
                }
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
                while (s.getArrayListLines().size() > 0) {
                    arrayListComponentsToDelete.add(s.getArrayListLines().get(0));
                    s.getArrayListLines().get(0).delete();
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

        for (Bulb b : arrayListCreatedBulbs) {
            if (b.isSelected()) {
                while (b.getArrayListLines().size() > 0) {
                    arrayListComponentsToDelete.add(b.getArrayListLines().get(0));
                    b.getArrayListLines().get(0).delete();
                }
                arrayListComponentsToDelete.add(b);
            }
        }

        for (Connector con : arrayListCreatedConnectors) {
            if (con.isSelected()) {
                while (con.getArrayListLines().size() > 0) {
                    arrayListComponentsToDelete.add(con.getArrayListLines().get(0));
                    con.getArrayListLines().get(0).delete();
                }
                arrayListComponentsToDelete.add(con);
            }
        }

        for(Component c : arrayListComponentsToDelete){
            c.kill();
            arrayListCreatedLines.remove(c);
            arrayListCreatedGates.remove(c);
            arrayListCreatedSwitches.remove(c);
            arrayListCreatedFlipFlops.remove(c);
            arrayListCreatedBulbs.remove(c);
            arrayListCreatedConnectors.remove(c);
            arrayListAllCreatedComponents.remove(c);
            arrayListCreatedEndComponents.remove(c);
            arrayListSeries.remove(c.getSeries());
            lineChartStates.getData().remove(c.getSeries());
            arrayListDeletedComponents.add(c);
            stackUndoChanges.push(new Change(2, c));
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

    public void createNewComponent(double x, double y, String newComponentName){
        tableViewComponents.getSelectionModel().clearSelection();
        try {
            Component newComponent = null;
            XYChart.Series<Long, String> newSeries= new XYChart.Series<>();

            if (newComponentName.equals(Names.connectorName)) {
                newComponent = new Connector(x, y, true, null, timeStart);
            }
            else if (newComponentName.equals(Names.bulbName)) {
                newComponent = new Bulb(x, y, true, newSeries, timeStart);
            }
            else if (newComponentName.equals(Names.gateNotName)) {
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
                else if(newComponent.getName().contains(Names.bulbName)){
                    arrayListCreatedBulbs.add((Bulb)newComponent);
                    bulbCounter++;
                    newComponent.setId(bulbCounter);
                }
                else if(newComponent.getName().contains(Names.connectorName)){
                    arrayListCreatedConnectors.add((Connector)newComponent);
                    connectorCounter++;
                    newComponent.setId(connectorCounter);
                }

                if(!newComponent.getName().equals(Names.connectorName)) {
                    arrayListCreatedEndComponents.add(newComponent);
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, newComponent.getName() + " " + newComponent.getId() + ": 0"));
                    newSeries.getData().add(new XYChart.Data<Long, String>(0L, newComponent.getName() + " " + newComponent.getId() + ": 1"));
                    if (newComponent.isSignalOutput()) {
                        newSeries.getData().add(new XYChart.Data<Long, String>(0L, newComponent.getName() + " " + newComponent.getId() + ": 1"));
                    } else {
                        newSeries.getData().add(new XYChart.Data<Long, String>(0L, newComponent.getName() + " " + newComponent.getId() + ": 0"));
                    }
                    arrayListSeries.add(newSeries);
                    lineChartStates.getData().add(newSeries);
                }

                arrayListAllCreatedComponents.add(newComponent);

                stackUndoChanges.add(new Change(1, newComponent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mwc.setCoveredError(false);
        canvas.requestFocus();
    }

    public void createNewLine(double x, double y) {
        Gate g = getCoveredGate(x, y);
        Switch s = getCoveredSwitch(x, y);
        FlipFlop ff = getCoveredFlipFlop(x, y);
        Bulb b = getCoveredBulb(x, y);
        Connector con = getCoveredConnector(x, y);

        tableViewComponents.getSelectionModel().clearSelection();
        if(s != null || b != null || con != null){
            if (!mwc.isWaitForComponent2()) {
                chooseNewLineHook1(x, y, g, s, ff, b, con, null);
            } else {
                chooseNewLineHook2(x, y, g, s, ff, b, con, null);
            }
        }
        else {
            comboBoxNewLineHook = new ComboBox<>();
            comboBoxNewLineHook.setLayoutX(x - 75);
            comboBoxNewLineHook.setLayoutY(y);
            comboBoxNewLineHook.promptTextProperty().setValue("Wybierz pin");
            if (g != null) {
                comboBoxNewLineHook.getItems().add(g.getPointOutput());
                for (Point p : g.getArrayPointsInputs()) {
                    comboBoxNewLineHook.getItems().add(p);
                }
            } else if (ff != null) {
                comboBoxNewLineHook.getItems().add(ff.getPointInput());
                if (ff.getName().equals(Names.flipFlopJK)) {
                    comboBoxNewLineHook.getItems().add(((FlipFlopJK) ff).getPointInputK());
                }
                comboBoxNewLineHook.getItems().add(ff.getPointOutput());
                comboBoxNewLineHook.getItems().add(ff.getPointOutputReversed());
                comboBoxNewLineHook.getItems().add(ff.getPointAsynchronousInput());
                comboBoxNewLineHook.getItems().add(ff.getPointClock());
                comboBoxNewLineHook.getItems().add(ff.getPointReset());
            }
            paneWorkspace.getChildren().add(comboBoxNewLineHook);

            canvas.setOnMouseClicked(e -> {
                paneWorkspace.getChildren().remove(comboBoxNewLineHook);
                canvas.setOnMouseClicked(f -> mouseActions.actionCanvasMouseClicked(f));
            });

            final Gate finalG = g;
            final Switch finalS = s;
            final FlipFlop finalFF = ff;
            final Bulb finalB = b;
            final Connector finalCon = con;
            if (!mwc.isWaitForComponent2()) {
                comboBoxNewLineHook.setOnAction(e -> chooseNewLineHook1(x, y, finalG, finalS, finalFF, finalB, finalCon, comboBoxNewLineHook));
            } else {
                comboBoxNewLineHook.setOnAction(e -> chooseNewLineHook2(x, y, finalG, finalS, finalFF, finalB, finalCon, comboBoxNewLineHook));
            }
        }
    }

    private void chooseNewLineHook1(double x, double y, Gate g, Switch s, FlipFlop ff, Bulb b, Connector con, ComboBox<Point> comboBoxNewLineHook){
        if(s != null){
            mwc.setLineBuffer(new Line(s.getPointLineHook().getX(), s.getPointLineHook().getY(), x, y, s, null, Color.BLACK));
            s.getArrayListLines().add(mwc.getLineBuffer());
            mwc.getLineBuffer().setInput1IsOutput(true);
        }
        else if(b != null){
            mwc.setLineBuffer(new Line(b.getPointLineHook().getX(), b.getPointLineHook().getY(), x, y, b, null, Color.BLACK));
            b.getArrayListLines().add(mwc.getLineBuffer());
            mwc.getLineBuffer().setInput1IsOutput(false);
        }
        else if(con != null){
            mwc.setLineBuffer(new Line(con.getPointCenter().getX(), con.getPointCenter().getY(), x, y, con, null, Color.BLACK));
            con.getArrayListLines().add(mwc.getLineBuffer());
            mwc.getLineBuffer().setInput1IsOutput(false);
        }
        else {
            Point p = comboBoxNewLineHook.getSelectionModel().getSelectedItem();
            String pointName = p.getName();

            if (g != null) {
                mwc.setLineBuffer(new Line(p.getX(), p.getY(), x, y, g, null, Color.BLACK));
                if (pointName.contains("Output")) {
                    g.getArrayListLinesOutput().add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(true);
                } else if (pointName.contains("Input")) {
                    int inputNumber = Integer.parseInt(p.getName().split("Input")[1]);
                    g.getArrayArrayListLines()[inputNumber - 1].add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(false);
                }
            } else if (ff != null) {
                mwc.setLineBuffer(new Line(p.getX(), p.getY(), x, y, ff, null, Color.BLACK));
                if (pointName.equals("Input") || pointName.equals("Input J")) {
                    ff.getArrayListLinesInput().add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(false);
                } else if (pointName.equals("Input K")) {
                    ((FlipFlopJK) ff).getArrayListLinesInputK().add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(false);
                } else if (pointName.equals("Output")) {
                    ff.getArrayListLinesOutput().add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(true);
                } else if (pointName.equals("Output reversed")) {
                    ff.getArrayListLinesOutputReverted().add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(true);
                } else if (pointName.equals("Asynchronous input")) {
                    ff.getArrayListLinesAsynchronousInput().add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(false);
                } else if (pointName.equals("Clock")) {
                    ff.getArrayListLinesClock().add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(false);
                } else if (pointName.equals("Reset")) {
                    ff.getArrayListLinesReset().add(mwc.getLineBuffer());
                    mwc.getLineBuffer().setInput1IsOutput(false);
                }
            }
            canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
            paneWorkspace.getChildren().remove(comboBoxNewLineHook);
        }
        mwc.setWaitForComponent2(true);
        canvas.requestFocus();
    }

    private void chooseNewLineHook2(double x, double y, Gate g, Switch s, FlipFlop ff, Bulb b, Connector con, ComboBox<Point> comboBoxNewLineHook) {
        if((g != null && mwc.getLineBuffer().getComponent1() != g) ||
        (s != null && mwc.getLineBuffer().getComponent1() != s) ||
        (ff != null && mwc.getLineBuffer().getComponent1() != ff) ||
        (b != null && mwc.getLineBuffer().getComponent1() != b) ||
        (con != null && mwc.getLineBuffer().getComponent1() != con)){
            if (s != null) {
                mwc.getLineBuffer().setComponent2(s);
                s.getArrayListLines().add(mwc.getLineBuffer());
                mwc.getLineBuffer().setInput2IsOutput(true);
                mwc.getLineBuffer().setX2(s.getPointLineHook().getX());
                mwc.getLineBuffer().setY2(s.getPointLineHook().getY());
            }
            else if(b != null) {
                mwc.getLineBuffer().setComponent2(b);
                b.getArrayListLines().add(mwc.getLineBuffer());
                mwc.getLineBuffer().setInput2IsOutput(false);
                mwc.getLineBuffer().setX2(b.getPointLineHook().getX());
                mwc.getLineBuffer().setY2(b.getPointLineHook().getY());
            }
            else if(con != null) {
                mwc.getLineBuffer().setComponent2(con);
                con.getArrayListLines().add(mwc.getLineBuffer());
                mwc.getLineBuffer().setInput2IsOutput(false);
                mwc.getLineBuffer().setX2(con.getPointCenter().getX());
                mwc.getLineBuffer().setY2(con.getPointCenter().getY());
            }
            else {
                Point p = comboBoxNewLineHook.getSelectionModel().getSelectedItem();
                String pointName = p.getName();

                mwc.getLineBuffer().setX2(comboBoxNewLineHook.getSelectionModel().getSelectedItem().getX());
                mwc.getLineBuffer().setY2(comboBoxNewLineHook.getSelectionModel().getSelectedItem().getY());

                if (g != null) {
                    mwc.getLineBuffer().setComponent2(g);
                    if (pointName.contains("Output")) {
                        g.getArrayListLinesOutput().add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(true);
                    } else if (pointName.contains("Input")) {
                        int inputNumber = Integer.parseInt(pointName.split("Input")[1]);
                        g.getArrayArrayListLines()[inputNumber - 1].add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(false);
                    }
                } else if (ff != null) {
                    mwc.getLineBuffer().setComponent2(ff);
                    if (pointName.equals("Input") || pointName.equals("Input J")) {
                        ff.getArrayListLinesInput().add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(false);
                    } else if (pointName.equals("Input K")) {
                        ((FlipFlopJK) ff).getArrayListLinesInputK().add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(false);
                    } else if (pointName.equals("Output")) {
                        ff.getArrayListLinesOutput().add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(true);
                    } else if (pointName.equals("Output reversed")) {
                        ff.getArrayListLinesOutputReverted().add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(true);
                    } else if (pointName.equals("Asynchronous input")) {
                        ff.getArrayListLinesAsynchronousInput().add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(true);
                    } else if (pointName.equals("Clock")) {
                        ff.getArrayListLinesClock().add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(false);
                    } else if (pointName.equals("Reset")) {
                        ff.getArrayListLinesReset().add(mwc.getLineBuffer());
                        mwc.getLineBuffer().setInput2IsOutput(false);
                    }
                }
            }
            lineCounter++;
            mwc.getLineBuffer().setId(lineCounter);
            arrayListCreatedLines.add(mwc.getLineBuffer());
            stackRedoChanges.clear();
            stackUndoChanges.push(new Change(1, mwc.getLineBuffer()));

            for (Line l : arrayListCreatedLines) {
                l.getArrayListVisitedLines().clear();
                l.getArrayListDependentComponents().clear();
            }
            for (Line l : arrayListCreatedLines) {
                l.checkForSignals(l.getArrayListDependentComponents(), l.getArrayListVisitedLines());
            }
            for (Line l : arrayListCreatedLines) {
                l.lifeCycle();
            }
        }
        else {
            deleteLineBuffer();
        }
        mwc.setLineBuffer(null);
        canvas.setOnMouseClicked(e -> mouseActions.actionCanvasMouseClicked(e));
        mwc.setWaitForComponent2(false);
        paneWorkspace.getChildren().remove(comboBoxNewLineHook);
        canvas.requestFocus();
    }

    public void deleteLineBuffer(){
        if(mwc.getLineBuffer() != null) {
            Component component = mwc.getLineBuffer().getComponent1();
            String name = mwc.getLineBuffer().getComponent1().getName();

            if (name.contains(Names.gateSearchName)) {
                Gate gate = (Gate) component;
                if (gate.getArrayListLinesOutput().contains(mwc.getLineBuffer())) {
                    gate.getArrayListLinesOutput().remove(mwc.getLineBuffer());
                } else {
                    for (ArrayList<Line> al : gate.getArrayArrayListLines()) {
                        if (al.contains(mwc.getLineBuffer())) {
                            al.remove(mwc.getLineBuffer());
                            return;
                        }
                    }
                }
            } else if (name.contains(Names.switchSearchName)) {
                Switch sw = (Switch) component;
                sw.getArrayListLines().remove(mwc.getLineBuffer());
            } else if (name.contains(Names.flipFlopSearchName)) {
                FlipFlop flipFlop = (FlipFlop) component;
                flipFlop.getArrayListLinesOutput().remove(mwc.getLineBuffer());
                flipFlop.getArrayListLinesOutputReverted().remove(mwc.getLineBuffer());
                flipFlop.getArrayListLinesReset().remove(mwc.getLineBuffer());
                flipFlop.getArrayListLinesClock().remove(mwc.getLineBuffer());
                flipFlop.getArrayListLinesInput().remove(mwc.getLineBuffer());
                if (flipFlop.getName().equals(Names.flipFlopJK)) {
                    ((FlipFlopJK) flipFlop).getArrayListLinesInputK().remove(mwc.getLineBuffer());
                }
            } else if (name.contains(Names.bulbName)) {
                Bulb b = (Bulb) component;
                b.getArrayListLines().remove(mwc.getLineBuffer());
            } else if (name.contains(Names.connectorName)) {
                Connector con = (Connector) component;
                con.getArrayListLines().remove(mwc.getLineBuffer());
            }
            mwc.setLineBuffer(null);
        }
    }

    public void setMouseActions(MouseActions mouseActions) {
        this.mouseActions = mouseActions;
    }

    public int getLineCounter() {
        return lineCounter;
    }

    public void setLineCounter(int lineCounter) {
        this.lineCounter = lineCounter;
    }

    public int getGateCounter() {
        return gateCounter;
    }

    public void setGateCounter(int gateCounter) {
        this.gateCounter = gateCounter;
    }

    public int getSwitchCounter() {
        return switchCounter;
    }

    public void setSwitchCounter(int switchCounter) {
        this.switchCounter = switchCounter;
    }

    public int getFlipFlopCounter() {
        return flipFlopCounter;
    }

    public void setFlipFlopCounter(int flipFlopCounter) {
        this.flipFlopCounter = flipFlopCounter;
    }

    public int getBulbCounter() {
        return bulbCounter;
    }

    public void setBulbCounter(int bulbCounter) {
        this.bulbCounter = bulbCounter;
    }

    public int getConnectorCounter() {
        return connectorCounter;
    }

    public void setConnectorCounter(int connectorCounter) {
        this.connectorCounter = connectorCounter;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public void setShiftDown(boolean shiftDown) {
        this.shiftDown = shiftDown;
    }
}
