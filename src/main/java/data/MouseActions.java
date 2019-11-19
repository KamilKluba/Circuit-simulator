package data;

import com.sun.glass.ui.Size;
import components.Line;
import components.Point;
import components.TableComponent;
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
import controllers.MainWindowController;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
        this.arrayListCreatedFlipFlops = mwc.getArrayListCreatedFlipFlops();
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
            double shiftFlipFlopX = Sizes.baseFlipFlopXShift;
            double shiftFlipFlopY = Sizes.baseFlipFlopYShift;

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
        else if(mwc.isWaitForComponent2()){
            graphicsContext.strokeLine(mwc.getLineBuffer().getX1(), mwc.getLineBuffer().getY1(), x, y);
        }

        if(mwc.isWaitForPlaceComponent()){
            graphicsContext.setStroke(Color.BLACK);
            if(newComponentName.equals(Names.gateNotName)){
                Gate g = new Not(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateAnd2Name)){
                Gate g = new And2(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateAnd3Name)){
                Gate g = new And3(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateAnd4Name)){
                Gate g = new And4(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateOr2Name)){
                Gate g = new Or2(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateOr3Name)){
                Gate g = new Or3(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateOr4Name)){
                Gate g = new Or4(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateXor2Name)){
                Gate g = new Xor2(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateXor3Name)){
                Gate g = new Xor3(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateXor4Name)){
                Gate g = new Xor4(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateNand2Name)){
                Gate g = new Nand2(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateNand3Name)){
                Gate g = new Nand3(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateNand4Name)){
                Gate g = new Nand4(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateNor2Name)){
                Gate g = new Nor2(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateNor3Name)){
                Gate g = new Nor3(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateNor4Name)){
                Gate g = new Nor4(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateXnor2Name)){
                Gate g = new Xnor2(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateXnor3Name)){
                Gate g = new Xnor3(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.gateXnor4Name)){
                Gate g = new Xnor4(x, y);
                g.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.switchMonostableName)){
                SwitchMonostable sm = new SwitchMonostable(x, y);
                sm.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.switchBistableName)){
                SwitchBistatble sb = new SwitchBistatble(x, y);
                sb.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.flipFlopD)){
                FlipFlopD ffd = new FlipFlopD(x, y);
                ffd.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.flipFlopT)){
                FlipFlopT fft = new FlipFlopT(x, y);
                fft.draw(graphicsContext);
            }
            else if(newComponentName.equals(Names.flipFlopJK)){
                FlipFlopJK ffjk = new FlipFlopJK(x, y);
                ffjk.draw(graphicsContext);
            }
        }
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

        //Clicked on a existing gate (1 or 2), and creating a line
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
            for(Line l : arrayListCreatedLines) {
                l.select(x, y);
            }
            for(Gate g : arrayListCreatedGates){
                g.select(x, y);
            }
            for(Switch s : arrayListCreatedSwitches){
                if(s.getName().equals(Names.switchBistableName)) {
                    if(button == MouseButton.PRIMARY){
                        s.select(x, y);
                    }
                    else if(button == MouseButton.SECONDARY && s.inside(x, y)) {
                        s.invertState();
                    }
                }
            }
            for(FlipFlop ff : arrayListCreatedFlipFlops){
                ff.select(x, y);
            }
            mwc.setCoveredError(false);
        }
        //Revert creating component
        else if(button == MouseButton.SECONDARY){
            if(Accesses.logMouseActions) {
                System.out.println("Reverted creating component");
            }
            mwc.setCoveredError(false);
            mwc.setLineBuffer(null);
            mwc.setWaitForComponent2(false);
            mwc.setWaitForPlaceComponent(false);
            tableViewComponents.getSelectionModel().clearSelection();
        }

        mwc.repaint();
    }

    public void actionCanvasMouseDragged(MouseEvent e){
        double x = e.getX();
        double y = e.getY();

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
        for(FlipFlop ff : arrayListCreatedFlipFlops){
            if(ff.isSelectedForDrag()){
                ff.move(x, y, pointMousePressedToDrag.getX(), pointMousePressedToDrag.getY());
            }
        }

        mwc.repaint();

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
            for (Gate g : arrayListCreatedGates) {
                g.select(x1, y1, x2, y2);
            }
            for (Switch s : arrayListCreatedSwitches) {
                s.select(x1, y1, x2, y2);
            }
            for (FlipFlop ff : arrayListCreatedFlipFlops){
                ff.select(x1, y1, x2, y2);
            }
        }
    }

    public void actionCanvasMousePressed(MouseEvent e){
        double x = e.getX();
        double y = e.getY();

        if(Accesses.logMouseActions) {
            System.out.println("Mouse pressed");
        }

        if(e.getButton() == MouseButton.SECONDARY){
            mwc.getZsp().setPannable(true);
        }
        else{
            mwc.getZsp().setPannable(false);
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
        for (Gate g : arrayListCreatedGates) {
            if(g.checkIfCouldBeSelected(e.getX(), e.getY())){
                couldBeSelected = true;
            }
            if(g.inside(x, y)){
                g.selectForDrag(x, y);
            }
        }
        for (Switch s : arrayListCreatedSwitches) {
            if(s.checkIfCouldBeSelected(e.getX(), e.getY())){
                couldBeSelected = true;
            }
            if(s.inside(x, y)){
                s.selectForDrag(x, y);
                if(s.getName().equals(Names.switchMonostableName)){
                    s.setState(true);
                }
            }
        }
        for (FlipFlop ff : arrayListCreatedFlipFlops) {
            if(ff.checkIfCouldBeSelected(e.getX(), e.getY())){
                couldBeSelected = true;
            }
            if(ff.inside(x, y)){
                ff.selectForDrag(x, y);
            }
        }

        mwc.repaint();
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
            if(s.getName().equals(Names.switchMonostableName)){
                s.setState(false);
            }
        }
        for(FlipFlop ff : arrayListCreatedFlipFlops){
            ff.setSelectedForDrag(false);
        }
        mwc.repaint();
    }

    public Point getPointMousePressed() {
        return pointMousePressed;
    }

    public Point getPointMouseMoved() {
        return pointMouseMoved;
    }
}
