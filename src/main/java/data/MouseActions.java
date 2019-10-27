package data;

import com.sun.glass.ui.Size;
import components.Line;
import components.Point;
import components.TableComponent;
import components.gates.Gate;
import components.gates.and.And2;
import components.gates.or.Or2;
import components.switches.Switch;
import components.switches.SwitchBistatble;
import controllers.MainWindowController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MouseActions {
    private boolean mouseDragged = false;

    private MainWindowController mwc;
    private GraphicsContext graphicsContext;
    private TableView<TableComponent> tableViewComponents;
    private ArrayList<Gate> arrayListCreatedGates;
    private ArrayList<Switch> arrayListCreatedSwitches;
    private ArrayList<Line> arrayListCreatedLines;    
    private Point pointMousePressed = new Point();
    private Point pointMouseMoved = new Point();
    private Point pointMouseReleased = new Point();
    private Point pointMousePressedToDrag = new Point();
    

    public MouseActions(MainWindowController mwc){
        this.mwc = mwc;
        this.graphicsContext = mwc.getGraphicsContext();
        this.tableViewComponents = mwc.getTableViewComponents();
        this.arrayListCreatedGates = mwc.getArrayListCreatedGates();
        this.arrayListCreatedSwitches = mwc.getArrayListCreatedSwitches();
        this.arrayListCreatedLines = mwc.getArrayListCreatedLines();
    }

    public void actionCanvasMouseMoved(double x, double y){
        pointMouseMoved.setX(x);
        pointMouseMoved.setY(y);
        mwc.repaint();

        String newComponentName = tableViewComponents.getSelectionModel().getSelectedItem() != null ?
                tableViewComponents.getSelectionModel().getSelectedItem().getName() : null;


        if(mwc.isCoveredError()) {
            graphicsContext.setStroke(Color.RED);
            graphicsContext.setLineWidth(Sizes.baseLineContourWidth);
            double shiftGateX = Sizes.baseGateXShift;
            double shiftGateY = Sizes.baseGateYShift;
            double shiftSwitchX = Sizes.baseSwitchXShift;
            double shiftSwitchY = Sizes.baseSwitchYShift;

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
            else if(newComponentName.contains(Names.switchName)){
                graphicsContext.strokeLine(x - shiftSwitchX, y - shiftSwitchY, x - shiftSwitchX, y + shiftSwitchY);
                graphicsContext.strokeLine(x - shiftSwitchX, y + shiftSwitchY, x + shiftSwitchX, y + shiftSwitchY);
                graphicsContext.strokeLine(x + shiftSwitchX, y + shiftSwitchY, x + shiftSwitchX, y - shiftSwitchY);
                graphicsContext.strokeLine(x + shiftSwitchX, y - shiftSwitchY, x - shiftSwitchX, y - shiftSwitchY);
            }
        }
        else if(mwc.isWaitForComponent2()){
            graphicsContext.strokeLine(mwc.getLineBuffer().getX1(), mwc.getLineBuffer().getY1(), x, y);
        }

        if(mwc.isWaitForPlaceComponent()){
            graphicsContext.setStroke(Color.BLACK);
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

    public void actionCanvasMouseClicked(double x, double y, MouseButton button){
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

        //Revert creating component
        if(button == MouseButton.SECONDARY){
            if(Accesses.logMouseActions) {
                System.out.println("Reverted creating component");
            }
            mwc.setCoveredError(false);
            mwc.setLineBuffer(null);
            mwc.setWaitForComponent2(false);
            mwc.setWaitForPlaceComponent(false);
            tableViewComponents.getSelectionModel().clearSelection();
        }
        //Clicked on a existing gate (1 or 2), and creating a line
        else if(mwc.checkIfCoverHalf(selectedItemName, x, y) && (mwc.isWaitForComponent2() || selectedItemName.equals(Names.lineName))){
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
            mwc.setLineBuffer(null);
            mwc.setWaitForComponent2(false);
            mwc.setCoveredError(false);
        }
        //Clicked on a free space while creating component (not line)
        else if(!mwc.checkIfCoverTotal(selectedItemName, x, y) && tableViewComponents.getSelectionModel().getSelectedItem() != null && !selectedItemName.contains(Names.lineName)) {
            if(Accesses.logMouseActions) {
                System.out.println("Create new component " + selectedItemName);
            }
            mwc.createNewComponent(x, y, selectedItemName);
            mwc.setWaitForPlaceComponent(false);
        }
        //Clicked on a occupied space while creating gate
        else if(selectedItemName != ""){
            if(Accesses.logMouseActions) {
                System.out.println("Covered gate while trying to create new one");
            }
            mwc.setCoveredError(true);
            mwc.setWaitForPlaceComponent(true);
            actionCanvasMouseMoved(x, y);
        }
        //Just clicked on a free space
        else if(!mouseDragged){
            if(Accesses.logMouseActions) {
                System.out.println("No special action, trying to select a gate");
            }
            for(Gate g : arrayListCreatedGates){
                g.select(x, y);
            }
            for(Switch s : arrayListCreatedSwitches){
                s.select(x, y);
            }
            mwc.setCoveredError(false);
        }

        mwc.repaint();
    }

    public void actionCanvasMouseDragged(double x, double y){
        for(Gate g : arrayListCreatedGates){
            if(g.isSelectedForDrag()){
                g.move(x, y, pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY());
            }
        }
        for(Switch s : arrayListCreatedSwitches){
            if(s.isSelectedForDrag()){
                s.move(x, y, pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY());
            }
        }

        mwc.repaint();

        pointMousePressedToDrag.setX(x);
        pointMousePressedToDrag.setY(y);
    }

    public void actionCanvasMousePressed(double x, double y){
        if(Accesses.logMouseActions) {
            System.out.println("Mouse pressed");
        }
        pointMousePressed.setX(x);
        pointMousePressed.setY(y);

        pointMousePressedToDrag.setX(x);
        pointMousePressedToDrag.setY(y);

        for(Gate g : arrayListCreatedGates){
            if(g.inside(x, y)){
                g.selectForDrag(x, y);
            }
        }
        for(Switch s : arrayListCreatedSwitches){
            if(s.inside(x, y)){
                s.selectForDrag(x, y);
            }
        }
    }

    public void actionCanvasMouseReleased(double x, double y){
        if(Accesses.logMouseActions) {
            System.out.println("Mouse released");
        }
        pointMouseReleased.setX(x);
        pointMouseReleased.setY(y);

        mouseDragged = Math.abs(pointMousePressed.getX() - x) > Sizes.minimalXDragToSelect || Math.abs(pointMousePressed.getY() - y) > Sizes.minimalYDragToSelect;

        for(Gate g : arrayListCreatedGates){
            g.setSelectedForDrag(false);
        }
        for(Switch s : arrayListCreatedSwitches){
            s.setSelectedForDrag(false);
        }
    }

    public Point getPointMousePressed() {
        return pointMousePressed;
    }

    public Point getPointMouseMoved() {
        return pointMouseMoved;
    }
}
