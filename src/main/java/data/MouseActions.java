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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MouseActions {
    private boolean mouseDragged = false;
    private boolean couldBeSelected = false;
    private MainWindowController mwc;
    private GraphicsContext graphicsContext;
    private TableView<TableComponent> tableViewComponents;
    private ArrayList<Gate> arrayListCreatedGates;
    private ArrayList<Switch> arrayListCreatedSwitches;
    private ArrayList<Line> arrayListCreatedLines;
    private ArrayList<FlipFlop> arrayListCreatedFlipFlops;
    private ArrayList<Component> arrayListCreatedComponents;
    private Point pointMousePressed = new Point();
    private Point pointMouseMoved = new Point();
    private Point pointMouseReleased = new Point();
    private Point pointMousePressedToDrag = new Point();
    private ZoomableScrollPaneChart zoomableScrollPaneChart;
    private HBox hBoxChartArea;
    private Component componentMoveBuffer = null;
    private String newComponentName = null;


    public MouseActions(MainWindowController mwc){
        this.mwc = mwc;
        this.graphicsContext = mwc.getGraphicsContext();
        this.tableViewComponents = mwc.getTableViewComponents();
        this.arrayListCreatedGates = mwc.getArrayListCreatedGates();
        this.arrayListCreatedSwitches = mwc.getArrayListCreatedSwitches();
        this.arrayListCreatedLines = mwc.getArrayListCreatedLines();
        this.arrayListCreatedFlipFlops = mwc.getArrayListCreatedFlipFlops();
        this.arrayListCreatedComponents = mwc.getArrayListCreatedComponents();
        this.zoomableScrollPaneChart = mwc.getZoomableScrollPaneChart();
        this.hBoxChartArea = mwc.gethBoxChartArea();
    }

    public void actionCanvasMouseMoved(double x, double y){
        pointMouseMoved.setX(x);
        pointMouseMoved.setY(y);

        newComponentName = tableViewComponents.getSelectionModel().getSelectedItem() != null ?
                tableViewComponents.getSelectionModel().getSelectedItem().getName() : null;

        if(mwc.isWaitForComponent2()){
            mwc.getLineBuffer().setX2(x);
            mwc.getLineBuffer().setY2(y);
        }

        if(mwc.isWaitForPlaceComponent()) {
            graphicsContext.setStroke(Color.BLACK);
            if (newComponentName.equals(Names.gateNotName)) {
                componentMoveBuffer = new Not(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateAnd2Name)) {
                componentMoveBuffer = new And2(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateAnd3Name)) {
                componentMoveBuffer = new And3(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateAnd4Name)) {
                componentMoveBuffer = new And4(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateOr2Name)) {
                componentMoveBuffer = new Or2(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateOr3Name)) {
                componentMoveBuffer = new Or3(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateOr4Name)) {
                componentMoveBuffer = new Or4(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateXor2Name)) {
                componentMoveBuffer = new Xor2(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateXor3Name)) {
                componentMoveBuffer = new Xor3(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateXor4Name)) {
                componentMoveBuffer = new Xor4(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateNand2Name)) {
                componentMoveBuffer = new Nand2(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateNand3Name)) {
                componentMoveBuffer = new Nand3(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateNand4Name)) {
                componentMoveBuffer = new Nand4(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateNor2Name)) {
                componentMoveBuffer = new Nor2(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateNor3Name)) {
                componentMoveBuffer = new Nor3(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateNor4Name)) {
                componentMoveBuffer = new Nor4(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateXnor2Name)) {
                componentMoveBuffer = new Xnor2(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateXnor3Name)) {
                componentMoveBuffer = new Xnor3(x, y, false, null, null);
            } else if (newComponentName.equals(Names.gateXnor4Name)) {
                componentMoveBuffer = new Xnor4(x, y, false, null, null);
            } else if (newComponentName.equals(Names.switchMonostableName)) {
                componentMoveBuffer = new SwitchMonostable(x, y, false, null, null);
            } else if (newComponentName.equals(Names.switchBistableName)) {
                componentMoveBuffer = new SwitchBistatble(x, y, false, null, null);
            } else if (newComponentName.equals(Names.switchPulseName)) {
                componentMoveBuffer = new SwitchPulse(x, y, false, null, null);
            } else if (newComponentName.equals(Names.flipFlopD)) {
                componentMoveBuffer = new FlipFlopD(x, y, false, null, null);
            } else if (newComponentName.equals(Names.flipFlopT)) {
                componentMoveBuffer = new FlipFlopT(x, y, false, null, null);
            } else if (newComponentName.equals(Names.flipFlopJK)) {
                componentMoveBuffer = new FlipFlopJK(x, y, false, null, null);
            }

            mwc.setComponentBuffer(componentMoveBuffer);
        }
        mwc.repaintScreen();
    }

    public void actionCanvasMouseClicked(MouseEvent event){
        double x = event.getX();
        double y = event.getY();
        MouseButton button = event.getButton();

        TableComponent selectedItem = null;
        String selectedItemName = "";

        try{
            selectedItem = tableViewComponents.getSelectionModel().getSelectedItem();
            selectedItemName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
        } catch (Exception e){}

        if(Accesses.logMouseActions) {
            System.out.println("Click parameters: coverTotal:" + mwc.checkIfCoverTotal(selectedItemName, x, y) + ", coverHalf:" + mwc.checkIfCoverHalf(selectedItemName, x, y) +
                    " selItemName:" + selectedItemName + ", waitForGate2:" + mwc.isWaitForComponent2() + ",  mouseButton:" + button);
        }

        //Clicked on a existing component (1 or 2), and creating a line
        if(mwc.checkIfCoverHalf(selectedItemName, x, y) && (mwc.isWaitForComponent2() || selectedItemName.equals(Names.lineName))){
            if(Accesses.logMouseActions) {
                System.out.println("Creating line");
            }
            mwc.createNewLine(x, y);
            mwc.setCoveredError(false);
        }
        //Clicked on a free space while creating line
        else if(!mwc.checkIfCoverHalf(selectedItemName, x, y) && (mwc.isWaitForComponent2() || selectedItemName.equals(Names.lineName))){
            if(Accesses.logMouseActions) {
                System.out.println("Stopped creating line");
            }
            mwc.deleteLineBuffer();
            mwc.setWaitForComponent2(false);
            mwc.setCoveredError(false);
        }
        //Clicked on a free space while creating component (not line)
        else if(!mwc.checkIfCoverTotal(selectedItemName, x, y) && tableViewComponents.getSelectionModel().getSelectedItem() != null
                && !selectedItemName.contains(Names.lineName) && button != MouseButton.SECONDARY) {
            if(Accesses.logMouseActions) {
                System.out.println("Create new component " + selectedItemName);
            }
            componentMoveBuffer = null;
            mwc.setComponentBuffer(null);
            mwc.createNewComponent(x, y, selectedItemName);
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

            for(Component c : arrayListCreatedComponents){
                if(button == MouseButton.PRIMARY){
                    c.select(x, y);
                }
                else if(c.getName().equals(Names.switchBistableName) && c.inside(x, y)){
                    ((Switch)c).invertState();
                }
            }
            mwc.setCoveredError(false);
        }
        //Turning on switch monostable
        else if(mwc.checkIfCoverHalf(selectedItemName, x, y) && button == MouseButton.SECONDARY){
            if(Accesses.logMouseActions) {
                System.out.println("Turning on switch monostable");
            }
            for(Component c : arrayListCreatedComponents) {
                if (c.getName().equals(Names.switchPulseName) && event.getButton() == MouseButton.SECONDARY && c.inside(x, y)) {
                    ((SwitchPulse) c).setTurnedOn(!((SwitchPulse) c).isTurnedOn());
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
            if (c.isSelectedForDrag()) {
                c.move(x, y, pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY());
            }
        }

        pointMousePressedToDrag.setX(x);
        pointMousePressedToDrag.setY(y);
        if(e.getButton() == MouseButton.PRIMARY && !couldBeSelected) {
            graphicsContext.setStroke(Color.BLUE);
            graphicsContext.setLineWidth(Sizes.baseLineContourWidth / (mwc.getPaneWorkspace().getScaleX() * 10 / 9) + 0.5);
            graphicsContext.strokeLine(pointMousePressed.getX(), pointMousePressed.getY(), x, pointMousePressed.getY());
            graphicsContext.strokeLine(pointMousePressed.getX(), pointMousePressed.getY(), pointMousePressed.getX(), y);
            graphicsContext.strokeLine(x, pointMousePressed.getY(), x, y);
            graphicsContext.strokeLine(pointMousePressed.getX(), y, x, y);

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
            if(l.checkIfCouldBeSelected(e.getX(), e.getY())){
                couldBeSelected = true;
            }
        }
        for (Component c : arrayListCreatedComponents) {
            if(c.checkIfCouldBeSelected(e.getX(), e.getY())){
                couldBeSelected = true;
            }
            if(c.inside(x, y)){
                c.selectForDrag(x, y);
                if(c.getName().equals(Names.switchMonostableName) && e.getButton() == MouseButton.SECONDARY){
                    ((Switch)c).setState(true);
                }
            }
        }
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

    public void drawCoverErrorSquares(){
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(Sizes.baseLineContourWidth);
        double shiftGateX = Sizes.baseGateXShift;
        double shiftGateY = Sizes.baseGateYShift;
        double shiftSwitchX = Sizes.baseSwitchXShift;
        double shiftSwitchY = Sizes.baseSwitchYShift;
        double shiftFlipFlopX = Sizes.baseFlipFlopXShift;
        double shiftFlipFlopY = Sizes.baseFlipFlopYShift;
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

        if(mwc.checkIfCoverTotal(newComponentName, x, y)){
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
    }

    public Point getPointMousePressed() {
        return pointMousePressed;
    }

    public Point getPointMouseMoved() {
        return pointMouseMoved;
    }
}
