package data;

import components.*;
import components.flipflops.FlipFlop;
import components.gates.Gate;
import components.switches.Switch;
import components.switches.SwitchPulse;
import controllers.MainWindowController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MouseActions {
    private ComponentCreator componentCreator;
    private boolean mouseDragged = false;
    private boolean couldBeSelected = false;
    private MainWindowController mwc;
    private GraphicsContext graphicsContext;
    private TableView<TableComponent> tableViewComponents;
    private ArrayList<Gate> arrayListCreatedGates;
    private ArrayList<Switch> arrayListCreatedSwitches;
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops;
    private ArrayList<Bulb> arrayListCreatedBulbs;
    private ArrayList<Component> arrayListCreatedComponents;
    private ArrayList<Line> arrayListCreatedLines;
    private ArrayList<Connector> arrayListCreatedConnectors;
    private Point pointMousePressed = new Point();
    private Point pointMouseMoved = new Point();
    private Point pointMouseReleased = new Point();
    private Point pointMousePressedToDrag = new Point();
    private ZoomableScrollPaneChart zoomableScrollPaneChart;
    private HBox hBoxChartArea;
    private Component componentMoveBuffer = null;
    private String newComponentName = null;
    private boolean fitToCheck = false;
    private boolean shiftDown = false;
    

    public MouseActions(MainWindowController mwc){
        this.mwc = mwc;
        this.graphicsContext = mwc.getGraphicsContext();
        this.tableViewComponents = mwc.getTableViewComponents();
        this.arrayListCreatedGates = mwc.getArrayListCreatedGates();
        this.arrayListCreatedSwitches = mwc.getArrayListCreatedSwitches();
        this.arrayListCreatedFlipFlops = mwc.getArrayListCreatedFlipFlops();
        this.arrayListCreatedBulbs = mwc.getArrayListCreatedBulbs();
        this.arrayListCreatedComponents = mwc.getArrayListCreatedComponents();
        this.arrayListCreatedLines = mwc.getArrayListCreatedLines();
        this.arrayListCreatedConnectors = mwc.getArrayListCreatedConnectors();
        this.zoomableScrollPaneChart = mwc.getZoomableScrollPaneChart();
        this.hBoxChartArea = mwc.gethBoxChartArea();
    }

    public void actionCanvasMouseMoved(double x, double y){
        pointMouseMoved.setX(x);
        pointMouseMoved.setY(y);

        if(mwc.isWaitForComponent2()){
            mwc.getLineBuffer().setX2(x);
            mwc.getLineBuffer().setY2(y);
        }

        if(mwc.getComponentBuffer() != null){
            if(fitToCheck){
                double x1 = x % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
                double y1 = y % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
                mwc.getComponentBuffer().getPointCenter().setX(x - x % Sizes.fitComponentPlace + x1);
                mwc.getComponentBuffer().getPointCenter().setY(y - y % Sizes.fitComponentPlace + y1);
            }
            else{
                mwc.getComponentBuffer().getPointCenter().setX(x);
                mwc.getComponentBuffer().getPointCenter().setY(y);
            }
        }

        mwc.repaintScreen();
    }

    public void actionCanvasMouseClicked(MouseEvent event){
        double x = event.getX();
        double y = event.getY();
        MouseButton button = event.getButton();

        String selectedItemName = "";

        if(tableViewComponents.getSelectionModel().getSelectedItem() != null){
            selectedItemName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
        }

        if(Accesses.logMouseActions) {
            System.out.println("Click parameters: coverTotal:" + checkIfCoverTotal(selectedItemName, x, y) + ", coverHalf:" + checkIfCoverHalf(selectedItemName, x, y) +
                    " selItemName:" + selectedItemName + ", waitForGate2:" + mwc.isWaitForComponent2() + ",  mouseButton:" + button);
        }

        //Clicked on a existing component (1 or 2), and creating a line
        if(checkIfCoverHalf(selectedItemName, x, y) && (mwc.isWaitForComponent2() || selectedItemName.equals(Names.lineName))){
            if(Accesses.logMouseActions) {
                System.out.println("Creating line");
            }
            componentCreator.createNewLine(x, y);
            mwc.setCoveredError(false);
        }
        //Clicked on a free space while creating line
        else if(!checkIfCoverHalf(selectedItemName, x, y) && (mwc.isWaitForComponent2() || selectedItemName.equals(Names.lineName))){
            if(Accesses.logMouseActions) {
                System.out.println("Stopped creating line");
            }
            componentCreator.deleteLineBuffer();
            mwc.setWaitForComponent2(false);
            mwc.setCoveredError(false);
        }
        //Clicked on a free space while creating component (not line)
        else if(!checkIfCoverTotal(selectedItemName, x, y) && tableViewComponents.getSelectionModel().getSelectedItem() != null
                && !selectedItemName.contains(Names.lineName) && button != MouseButton.SECONDARY) {
            if(Accesses.logMouseActions) {
                System.out.println("Create new component " + selectedItemName);
            }
            componentMoveBuffer = null;
            mwc.setComponentBuffer(null);
            if(fitToCheck){
                double x1 = x % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
                double y1 = y % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
                componentCreator.createNewComponent(x - x % Sizes.fitComponentPlace + x1, y - y % Sizes.fitComponentPlace + y1, selectedItemName);
            }
            else {
                componentCreator.createNewComponent(x, y, selectedItemName);
            }
            mwc.setWaitForPlaceComponent(false);
        }
        //Clicked on a occupied space while creating gate
        else if(selectedItemName != "" && button != MouseButton.SECONDARY){
            if(Accesses.logMouseActions) {
                System.out.println("Covered gate while trying to create new one");
            }
            mwc.setCoveredError(true);
            mwc.setWaitForPlaceComponent(true);
            actionCanvasMouseMoved(x, y);
        }
        //Just clicked on a free space
        else if(!mouseDragged && button != MouseButton.SECONDARY){
            if(Accesses.logMouseActions) {
                System.out.println("No special action, trying to select a gate");
            }

            if(button == MouseButton.PRIMARY) {
                for (Component c : arrayListCreatedComponents) {
                    c.select(x, y);
                }
                for (Line l : arrayListCreatedLines) {
                    l.select(x, y);
                }
            }
            mwc.setCoveredError(false);
        }
        //Turning on switch
        else if(checkIfCoverHalf(selectedItemName, x, y) && button == MouseButton.SECONDARY){
            if(Accesses.logMouseActions) {
                System.out.println("Turning on switch monostable");
            }
            for(Component c : arrayListCreatedComponents) {
                if (c.getName().equals(Names.switchPulseName) && event.getButton() == MouseButton.SECONDARY && c.inside(x, y)) {
                    ((SwitchPulse) c).setTurnedOn(!((SwitchPulse) c).isTurnedOn());
                }
                else if(c.getName().equals(Names.switchBistableName) && c.inside(x, y)){
                    ((Switch)c).invertState();
                }
            }
        }
        //Revert creating component
        else if(button == MouseButton.SECONDARY){
            if(Accesses.logMouseActions) {
                System.out.println("Reverted creating component");
            }
            componentMoveBuffer = null;
            mwc.setComponentBuffer(null);
            mwc.setCoveredError(false);
            mwc.setLineBuffer(null);
            mwc.setWaitForComponent2(false);
            mwc.setWaitForPlaceComponent(false);
            tableViewComponents.getSelectionModel().clearSelection();
        }
        mwc.repaintScreen();
    }

    public void actionCanvasMouseDragged(MouseEvent e){
        double x = e.getX();
        double y = e.getY();

        for(Component c : arrayListCreatedComponents) {
            if (c.isSelectedForDrag() || (c.isSelected() && !mwc.isDraggedSelectionRectngle())) {
                c.move(x, y, pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY(), fitToCheck);
            }
        }
        for(Line l : arrayListCreatedLines){
            if(l.isSelectedForDrag() || (l.isSelected() && !mwc.isDraggedSelectionRectngle())){
                if(shiftDown) {
                    l.breakLine(x, y, fitToCheck);
                }
                else{
                    l.move(x, y, pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY(), fitToCheck);
                }
            }
        }
        for(Connector con : arrayListCreatedConnectors){
            if (con.isSelectedForDrag() || (con.isSelected() && !mwc.isDraggedSelectionRectngle())) {
                con.move(x, y, pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY(), fitToCheck);
            }
        }

        pointMousePressedToDrag.setX(x);
        pointMousePressedToDrag.setY(y);
        if(e.getButton() == MouseButton.PRIMARY && !couldBeSelected) {
            double x1, x2, y1, y2;
            if (pointMousePressed.getX() < x) {
                x1 = pointMousePressed.getX();
                x2 = x;
            } else {
                x1 = x;
                x2 = pointMousePressed.getX();
            }
            if (pointMousePressed.getY() < y) {
                y1 = pointMousePressed.getY();
                y2 = y;
            } else {
                y1 = y;
                y2 = pointMousePressed.getY();
            }

            for (Line l : arrayListCreatedLines) {
                l.select(x1, y1, x2, y2);
            }
            for (Component c : arrayListCreatedComponents) {
                c.select(x1, y1, x2, y2);
            }
            for (Connector con : arrayListCreatedConnectors){
                con.select(x1, y1, x2, y2);
          }
        }
        mwc.repaintScreen();
    }

    public void actionCanvasMousePressed(MouseEvent e){
        double x = e.getX();
        double y = e.getY();

        if(Accesses.logMouseActions) {
            System.out.println("Mouse pressed");
        }

        if(e.getButton() == MouseButton.SECONDARY){
            mwc.getZoomableScrollPaneWorkspace().setPannable(true);
        }
        else{
            mwc.getZoomableScrollPaneWorkspace().setPannable(false);
        }

        pointMousePressed.setX(x);
        pointMousePressed.setY(y);

        pointMousePressedToDrag.setX(x);
        pointMousePressedToDrag.setY(y);

        couldBeSelected = false;
        for (Line l : arrayListCreatedLines) {
            l.selectForDrag(x, y);
            if(l.checkIfCouldBeSelected(x, y)) {
                couldBeSelected = true;
                Point closePoint = null;
                for (Point p : l.getArrayListBreakPoints()) {
                    if (Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2)) < Sizes.lineSelectDistance * 2) {
                        closePoint = p;
                        l.setClosePoint(p);
                    }
                }
                if (closePoint != null) {
                    l.setNewBreakPoint(closePoint);
                } else {
                    l.createNewBreakPoint(x, y);
                }
            }
        }
        for (Component c : arrayListCreatedComponents) {
            if(c.checkIfCouldBeSelected(x, y)){
                couldBeSelected = true;
            }
            if(c.inside(x, y)){
                c.selectForDrag(x, y);
                if(c.getName().equals(Names.switchMonostableName) && e.getButton() == MouseButton.SECONDARY){
                    ((Switch)c).setState(true);
                }
            }
        }
        for (Connector con :arrayListCreatedConnectors){
            if(con.checkIfCouldBeSelected(x, y)){
                couldBeSelected = true;
            }
            if(con.inside(x, y)) {
                con.selectForDrag(x, y);
            }
        }

        mwc.setMouseButton(e.getButton());
        mwc.setDraggedSelectionRectngle(!couldBeSelected);
        mwc.repaintScreen();
    }

    public void actionCanvasMouseReleased(double x, double y){
        if(Accesses.logMouseActions) {
            System.out.println("Mouse released");
        }
        pointMouseReleased.setX(x);
        pointMouseReleased.setY(y);

        mouseDragged = Math.abs(pointMousePressed.getX() - x) > Sizes.minimalXDragToSelect || Math.abs(pointMousePressed.getY() - y) > Sizes.minimalYDragToSelect;

        for(Component c : arrayListCreatedComponents){
            c.setSelectedForDrag(false);
            if(c.getName().equals(Names.switchMonostableName)){
                ((Switch)c).setState(false);
            }
        }
        for(Line l : arrayListCreatedLines){
            l.setSelectedForDrag(false);
            l.setNewBreakPoint(null);
        }
        for (Connector con :arrayListCreatedConnectors) {
            con.setSelectedForDrag(false);
        }

        mwc.setMouseButton(null);
        mwc.setDraggedSelectionRectngle(false);
        mwc.repaintScreen();
    }

    public void actionZoomableScrollPaneClicked() {
        new Thread(() -> {
            while (zoomableScrollPaneChart.getPrefHeight() < mwc.getMain().getPrimaryStage().getHeight() - 200) {
                zoomableScrollPaneChart.setPrefHeight(zoomableScrollPaneChart.getPrefHeight() + 1.5);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        mwc.getCanvas().setOnMouseClicked(e -> actionCanvasClickedAfterZoomable());
    }

    public void actionCanvasClickedAfterZoomable(){
        new Thread(() -> {
            while(zoomableScrollPaneChart.getPrefHeight() > 150){
                zoomableScrollPaneChart.setPrefHeight(zoomableScrollPaneChart.getPrefHeight() - 1.5);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        mwc.getCanvas().setOnMouseClicked(e -> actionCanvasMouseClicked(e));
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
        else if(componentName.contains(Names.bulbName)){
            xSizeCompare = Sizes.baseGateXSize;
            ySizeCompare = Sizes.baseGateYSize;
        }
        else if(componentName.contains(Names.connectorName)){
            xSizeCompare = Sizes.baseConnectorXSize;
            ySizeCompare = Sizes.baseConnectorYSize;
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

        for(Bulb b : arrayListCreatedBulbs) {
            if (Math.abs(x - b.getPointCenter().getX()) <= (Sizes.baseGateXSize + xSizeCompare) / 2 &&
                    Math.abs(y - b.getPointCenter().getY()) <= (Sizes.baseGateYSize + ySizeCompare) / 2){
                return true;
            }
        }

        for(Connector con : arrayListCreatedConnectors) {
            if (Math.abs(x - con.getPointCenter().getX()) <= (Sizes.baseConnectorXSize + xSizeCompare) / 2 &&
                    Math.abs(y - con.getPointCenter().getY()) <= (Sizes.baseConnectorYSize + ySizeCompare) / 2){
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
        else if(componentName.contains(Names.bulbName)){
            xShiftCompare = Sizes.baseGateXShift;
            yShiftCompare = Sizes.baseGateYShift;
        }
        else if(componentName.contains(Names.connectorName)){
            xShiftCompare = Sizes.baseConnectorXShift;
            yShiftCompare = Sizes.baseConnectorYShift;
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

        for(Bulb b : arrayListCreatedBulbs) {
            if (Math.abs(x - b.getPointCenter().getX()) <= (Sizes.baseGateXShift + xShiftCompare) / 2 &&
                    Math.abs(y - b.getPointCenter().getY()) <= (Sizes.baseGateYShift + yShiftCompare) / 2){
                return true;
            }
        }

        for(Connector con : arrayListCreatedConnectors) {
            if (Math.abs(x - con.getPointCenter().getX()) <= (Sizes.baseConnectorXShift + xShiftCompare) / 2 &&
                    Math.abs(y - con.getPointCenter().getY()) <= (Sizes.baseConnectorYShift + yShiftCompare) / 2){
                return true;
            }
        }
        return false;
    }

    public void drawCoverErrorSquares(){
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(Sizes.baseLineContourWidth);
        double shiftGateX = Sizes.baseGateXShift;
        double shiftGateY = Sizes.baseGateYShift;
        double shiftSwitchX = Sizes.baseSwitchXShift;
        double shiftSwitchY = Sizes.baseSwitchYShift;
        double shiftFlipFlopX = Sizes.baseFlipFlopXShift;
        double shiftFlipFlopY = Sizes.baseFlipFlopYShift;
        double shiftConnectorX = Sizes.baseConnectorXShift;
        double shiftConnectorY = Sizes.baseConnectorYShift;
        double x = pointMouseMoved.getX();
        double y = pointMouseMoved.getY();

        for (Gate g : arrayListCreatedGates) {
            Point pointCenter = g.getPointCenter();
            graphicsContext.strokeLine(pointCenter.getX() - shiftGateX, pointCenter.getY() - shiftGateY, pointCenter.getX() - shiftGateX, pointCenter.getY() + shiftGateY);
            graphicsContext.strokeLine(pointCenter.getX() - shiftGateX, pointCenter.getY() + shiftGateY, pointCenter.getX() + shiftGateX, pointCenter.getY() + shiftGateY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftGateX, pointCenter.getY() + shiftGateY, pointCenter.getX() + shiftGateX, pointCenter.getY() - shiftGateY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftGateX, pointCenter.getY() - shiftGateY, pointCenter.getX() - shiftGateX, pointCenter.getY() - shiftGateY);
        }
        for (Switch s : arrayListCreatedSwitches) {
            Point pointCenter = s.getPointCenter();
            graphicsContext.strokeLine(pointCenter.getX() - shiftSwitchX, pointCenter.getY() - shiftSwitchY, pointCenter.getX() - shiftSwitchX, pointCenter.getY() + shiftSwitchY);
            graphicsContext.strokeLine(pointCenter.getX() - shiftSwitchX, pointCenter.getY() + shiftSwitchY, pointCenter.getX() + shiftSwitchX, pointCenter.getY() + shiftSwitchY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftSwitchX, pointCenter.getY() + shiftSwitchY, pointCenter.getX() + shiftSwitchX, pointCenter.getY() - shiftSwitchY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftSwitchX, pointCenter.getY() - shiftSwitchY, pointCenter.getX() - shiftSwitchX, pointCenter.getY() - shiftSwitchY);
        }
        for (FlipFlop ff : arrayListCreatedFlipFlops){
            Point pointCenter = ff.getPointCenter();
            graphicsContext.strokeLine(pointCenter.getX() - shiftFlipFlopX, pointCenter.getY() - shiftFlipFlopY, pointCenter.getX() - shiftFlipFlopX, pointCenter.getY() + shiftFlipFlopY);
            graphicsContext.strokeLine(pointCenter.getX() - shiftFlipFlopX, pointCenter.getY() + shiftFlipFlopY, pointCenter.getX() + shiftFlipFlopX, pointCenter.getY() + shiftFlipFlopY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftFlipFlopX, pointCenter.getY() + shiftFlipFlopY, pointCenter.getX() + shiftFlipFlopX, pointCenter.getY() - shiftFlipFlopY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftFlipFlopX, pointCenter.getY() - shiftFlipFlopY, pointCenter.getX() - shiftFlipFlopX, pointCenter.getY() - shiftFlipFlopY);
        }
        for (Bulb b : arrayListCreatedBulbs) {
            Point pointCenter = b.getPointCenter();
            graphicsContext.strokeLine(pointCenter.getX() - shiftGateX, pointCenter.getY() - shiftGateY, pointCenter.getX() - shiftGateX, pointCenter.getY() + shiftGateY);
            graphicsContext.strokeLine(pointCenter.getX() - shiftGateX, pointCenter.getY() + shiftGateY, pointCenter.getX() + shiftGateX, pointCenter.getY() + shiftGateY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftGateX, pointCenter.getY() + shiftGateY, pointCenter.getX() + shiftGateX, pointCenter.getY() - shiftGateY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftGateX, pointCenter.getY() - shiftGateY, pointCenter.getX() - shiftGateX, pointCenter.getY() - shiftGateY);
        }
        for (Connector con : arrayListCreatedConnectors) {
            Point pointCenter = con.getPointCenter();
            graphicsContext.strokeLine(pointCenter.getX() - shiftConnectorX, pointCenter.getY() - shiftConnectorY, pointCenter.getX() - shiftConnectorX, pointCenter.getY() + shiftConnectorY);
            graphicsContext.strokeLine(pointCenter.getX() - shiftConnectorX, pointCenter.getY() + shiftConnectorY, pointCenter.getX() + shiftConnectorX, pointCenter.getY() + shiftConnectorY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftConnectorX, pointCenter.getY() + shiftConnectorY, pointCenter.getX() + shiftConnectorX, pointCenter.getY() - shiftConnectorY);
            graphicsContext.strokeLine(pointCenter.getX() + shiftConnectorX, pointCenter.getY() - shiftConnectorY, pointCenter.getX() - shiftConnectorX, pointCenter.getY() - shiftConnectorY);
        }

        if(checkIfCoverTotal(newComponentName, x, y)){
            graphicsContext.setStroke(Color.RED);
        }
        else{
            graphicsContext.setStroke(Color.GREEN);
        }

        if(newComponentName.contains(Names.gateSearchName)) {
            graphicsContext.strokeLine(x - shiftGateX, y - shiftGateY, x - shiftGateX, y + shiftGateY);
            graphicsContext.strokeLine(x - shiftGateX, y + shiftGateY, x + shiftGateX, y + shiftGateY);
            graphicsContext.strokeLine(x + shiftGateX, y + shiftGateY, x + shiftGateX, y - shiftGateY);
            graphicsContext.strokeLine(x + shiftGateX, y - shiftGateY, x - shiftGateX, y - shiftGateY);
        }
        else if(newComponentName.contains(Names.switchSearchName)){
            graphicsContext.strokeLine(x - shiftSwitchX, y - shiftSwitchY, x - shiftSwitchX, y + shiftSwitchY);
            graphicsContext.strokeLine(x - shiftSwitchX, y + shiftSwitchY, x + shiftSwitchX, y + shiftSwitchY);
            graphicsContext.strokeLine(x + shiftSwitchX, y + shiftSwitchY, x + shiftSwitchX, y - shiftSwitchY);
            graphicsContext.strokeLine(x + shiftSwitchX, y - shiftSwitchY, x - shiftSwitchX, y - shiftSwitchY);
        }
        else if(newComponentName.contains(Names.flipFlopSearchName)){
            graphicsContext.strokeLine(x - shiftFlipFlopX, y - shiftFlipFlopY, x - shiftFlipFlopX, y + shiftFlipFlopY);
            graphicsContext.strokeLine(x - shiftFlipFlopX, y + shiftFlipFlopY, x + shiftFlipFlopX, y + shiftFlipFlopY);
            graphicsContext.strokeLine(x + shiftFlipFlopX, y + shiftFlipFlopY, x + shiftFlipFlopX, y - shiftFlipFlopY);
            graphicsContext.strokeLine(x + shiftFlipFlopX, y - shiftFlipFlopY, x - shiftFlipFlopX, y - shiftFlipFlopY);
        }
        else if(newComponentName.contains(Names.bulbName)){
            graphicsContext.strokeLine(x - shiftSwitchX, y - shiftSwitchY, x - shiftSwitchX, y + shiftSwitchY);
            graphicsContext.strokeLine(x - shiftSwitchX, y + shiftSwitchY, x + shiftSwitchX, y + shiftSwitchY);
            graphicsContext.strokeLine(x + shiftSwitchX, y + shiftSwitchY, x + shiftSwitchX, y - shiftSwitchY);
            graphicsContext.strokeLine(x + shiftSwitchX, y - shiftSwitchY, x - shiftSwitchX, y - shiftSwitchY);
        }
        else if(newComponentName.contains(Names.connectorName)){
            graphicsContext.strokeLine(x - shiftConnectorX, y - shiftConnectorY, x - shiftConnectorX, y + shiftConnectorY);
            graphicsContext.strokeLine(x - shiftConnectorX, y + shiftConnectorY, x + shiftConnectorX, y + shiftConnectorY);
            graphicsContext.strokeLine(x + shiftConnectorX, y + shiftConnectorY, x + shiftConnectorX, y - shiftConnectorY);
            graphicsContext.strokeLine(x + shiftConnectorX, y - shiftConnectorY, x - shiftConnectorX, y - shiftConnectorY);
        }
    }

    public void drawSelectionRectangle(){
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(Sizes.baseLineContourWidth / (mwc.getPaneWorkspace().getScaleX() * 10 / 9) + 0.5);
        graphicsContext.strokeLine(pointMousePressed.getX(), pointMousePressed.getY(), pointMousePressedToDrag.getX(), pointMousePressed.getY());
        graphicsContext.strokeLine(pointMousePressed.getX(), pointMousePressed.getY(), pointMousePressed.getX(), pointMousePressedToDrag.getY());
        graphicsContext.strokeLine(pointMousePressedToDrag.getX(), pointMousePressed.getY(), pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY());
        graphicsContext.strokeLine(pointMousePressed.getX(), pointMousePressedToDrag.getY(), pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY());

    }

    public Point getPointMousePressed() {
        return pointMousePressed;
    }

    public Point getPointMouseMoved() {
        return pointMouseMoved;
    }

    public void setComponentCreator(ComponentCreator componentCreator) {
        this.componentCreator = componentCreator;
    }

    public String getNewComponentName() {
        return newComponentName;
    }

    public void setNewComponentName(String newComponentName) {
        this.newComponentName = newComponentName;
    }

    public void setFitToCheck(boolean setFitToCheck) {
        this.fitToCheck = setFitToCheck;
    }

    public void setShiftDown(boolean shiftDown) {
        this.shiftDown = shiftDown;
    }
}
