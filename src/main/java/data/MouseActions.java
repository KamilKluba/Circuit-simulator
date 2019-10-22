package data;

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
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MouseActions {
    private MainWindowController mwc;
    private GraphicsContext graphicsContext;
    private TableView<TableComponent> tableViewComponents;
    private ArrayList<Gate> arrayListCreatedGates;
    private ArrayList<Switch> arrayListCreatedSwitches;
    private ArrayList<Line> arrayListCreatedLines;

    public MouseActions(MainWindowController mwc){
        this.mwc = mwc;
        this.graphicsContext = mwc.getGraphicsContext();
        this.tableViewComponents = mwc.getTableViewComponents();
        this.arrayListCreatedGates = mwc.getArrayListCreatedGates();
        this.arrayListCreatedSwitches = mwc.getArrayListCreatedSwitches();
        this.arrayListCreatedLines = mwc.getArrayListCreatedLines();
    }

    public void actionCanvasMouseMoved(double x, double y){
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
        else if(mwc.isWaitForGate2()){
            graphicsContext.strokeLine(mwc.getLineBuffer().getX1(), mwc.getLineBuffer().getY1(), x, y);
        }
        if(mwc.isWaitForPlaceComponent()){
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

    public void actionCanvasMouseClicked(double x, double y){
        String selectedItemName = "";

        try{
            selectedItemName = tableViewComponents.getSelectionModel().getSelectedItem().getName();
        } catch (Exception e){}

        System.out.println("Click parameters: coverTotal:" + mwc.checkIfCoverTotal(selectedItemName, x, y) + ", coverHalf:" + mwc.checkIfCoverHalf(selectedItemName, x, y) +
                " selItemName:" + selectedItemName + ", waitForGate2:" + mwc.isWaitForGate2());

        //Clicked on a existing gate (1 or 2), and creating a line
        if(mwc.checkIfCoverHalf(selectedItemName, x, y) && (mwc.isWaitForGate2() || selectedItemName.equals(Names.lineName))){
            System.out.println("Creating line");
            mwc.createNewLine(x, y);
            mwc.setCoveredError(false);
        }
        //Clicked on a free space while creating line
        else if(!mwc.checkIfCoverHalf(selectedItemName, x, y) && (mwc.isWaitForGate2() || selectedItemName.equals(Names.lineName))){
            System.out.println("Stopped creating line");
            mwc.setLineBuffer(null);
            mwc.setWaitForGate2(false);
            mwc.setCoveredError(false);
        }
        //Clicked on a free space while creating component (not line)
        else if(!mwc.checkIfCoverTotal(selectedItemName, x, y) && !selectedItemName.contains(Names.lineName)) {
            System.out.println("Create new component " + selectedItemName);
            mwc.createNewComponent(x, y, selectedItemName);
            mwc.setWaitForPlaceComponent(false);
        }
        //Clicked on a occupied space while creating gate
        else if(selectedItemName != ""){
            System.out.println("Covered gate while trying to create new one");
            mwc.setCoveredError(true);
            mwc.setWaitForPlaceComponent(true);
            actionCanvasMouseMoved(x, y);
        }
        //Just clicked on a free space
        else{
            System.out.println("No special action, trying to select a gate");
            for(Gate g : arrayListCreatedGates){
                g.select(x, y);
            }
            mwc.setCoveredError(false);
        }

        mwc.repaint();
    }

    public void actionCanvasMouseDragged(double x, double y){
        for(Gate g : arrayListCreatedGates){
            if(g.isSelected()){
                g.move(x, y, mwc.getPointMousePressed().getX(), mwc.getPointMousePressed().getY());
            }
        }

        mwc.repaint();

        mwc.getPointMousePressed().setX(x);
        mwc.getPointMousePressed().setY(y);
    }

    public void actionCanvasMousePressed(double x, double y){
        mwc.getPointMousePressed().setX(x);
        mwc.getPointMousePressed().setY(y);
    }

}
