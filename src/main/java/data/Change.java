package data;

import components.*;
import components.flipflops.FlipFlop;
import components.flipflops.FlipFlopJK;
import components.gates.Gate;
import components.switches.Switch;

import java.util.ArrayList;

public class Change {
    // 1 - creation, 2 - deletion, 3 - moving, 4 - changing state, 5 - break on line, 6 - moved break point
    private int description;
    private Component component;
    private String componentName;
    private int componentId;

    //1
    private Component component1;
    private ArrayList<Line> arrayListHook1;
    private Point pointHook1;
    private Component component2;
    private ArrayList<Line> arrayListHook2;
    private Point pointHook2;
    //3, 6
    private double oldX;
    private double oldY;
    private double newX;
    private double newY;
    //4
    private boolean oldState;
    private boolean newState;
    //5, 6
    private Point pointBreak;
    private int newPointIndex;

    public Change(int description, Component component) {
        this.description = description;
        this.component = component;
        this.componentId = component.getId();
        this.componentName = component.getName();

        if(componentName.contains(Names.lineName)){
            Line line = (Line)component;
            component1 = line.getComponent1();
            component2 = line.getComponent2();

            if (component1.getName().contains(Names.gateSearchName)) {
                checkForArrayGate((Gate) component1,1, line);
            } else if (component1.getName().contains(Names.switchSearchName)) {
                checkForArraySwitch((Switch) component1, 1, line);
            } else if (component1.getName().contains(Names.flipFlopSearchName)) {
                checkForArrayFlipFlop((FlipFlop) component1, 1, line);
            } else if (component1.getName().contains(Names.bulbName)) {
                checkForArrayBulb((Bulb) component1, 1, line);
            } else if (component1.getName().contains(Names.connectorName)) {
                checkForArrayConnector((Connector) component1, 1, line);
            }
            if (component2.getName().contains(Names.gateSearchName)) {
                checkForArrayGate((Gate) component2, 2, line);
            } else if (component2.getName().contains(Names.switchSearchName)) {
                checkForArraySwitch((Switch) component2, 2, line);
            } else if (component2.getName().contains(Names.flipFlopSearchName)) {
                checkForArrayFlipFlop((FlipFlop) component2, 2, line);
            } else if (component2.getName().contains(Names.bulbName)) {
                checkForArrayBulb((Bulb) component2, 2, line);
            } else if (component2.getName().contains(Names.connectorName)) {
                checkForArrayConnector((Connector) component2,2, line);
            }
        }
    }

    public Change(int description, Component component, double oldX, double oldY, double newX, double newY) {
        this.description = description;
        this.component = component;
        this.componentId = component.getId();
        this.componentName = component.getName();
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    public Change(int description, Component component, boolean oldState, boolean newState) {
        this.description = description;
        this.component = component;
        this.componentId = component.getId();
        this.componentName = component.getName();
        this.oldState = oldState;
        this.newState = newState;
    }

    public Change(int description, Component component, Point newBreakPoint, int newPointIndex) {
        this.description = description;
        this.component = component;
        this.componentId = component.getId();
        this.componentName = component.getName();
        this.pointBreak = newBreakPoint;
        this.newPointIndex = newPointIndex;
    }

    public Change(int description, Component component, Point oldPoint, Point newPoint){
        this.description = description;
        this.component = component;
        this.componentId = component.getId();
        this.componentName = component.getName();
        this.pointBreak = newPoint;
        this.oldX = oldPoint.getX();
        this.oldY = oldPoint.getY();
        this.newX = newPoint.getX();
        this.newY = newPoint.getY();
    }

    public void restoreLine(){
        Line line = (Line)component;
        line.setComponent1(component1);
        line.setComponent2(component2);
        arrayListHook1.add(line);
        arrayListHook2.add(line);
    }

    public void checkForArrayGate(Gate gate, int componentNumber, Line line) {

        if (gate.getArrayListLinesOutput().contains(line)) {
            if(componentNumber == 1) {
                arrayListHook1 = gate.getArrayListLinesOutput();
                pointHook1 = gate.getPointOutput();
            }
            else{
                arrayListHook2 = gate.getArrayListLinesOutput();
                pointHook2 = gate.getPointOutput();
            }
        } else {
            for (int i = 0; i < gate.getArrayArrayListLines().length; i++) {
                if (gate.getArrayArrayListLines()[i].contains(line)) {
                    if(componentNumber == 1) {
                        arrayListHook1 = gate.getArrayArrayListLines()[i];
                        pointHook1 = gate.getArrayPointsInputs()[i];
                    }
                    else{
                        arrayListHook2 = gate.getArrayArrayListLines()[i];
                        pointHook2 = gate.getArrayPointsInputs()[i];
                    }
                    break;
                }
            }
        }
    }

    public void checkForArraySwitch(Switch sw, int componentNumber, Line line) {
        if(componentNumber == 1) {
            arrayListHook1 = sw.getArrayListLines();
            pointHook1 = sw.getPointLineHook();
        }
        else{
            arrayListHook2 = sw.getArrayListLines();
            pointHook2 = sw.getPointLineHook();
        }
    }

    public void checkForArrayFlipFlop(FlipFlop flipFlop, int componentNumber, Line line) {
        if(flipFlop.getArrayListLinesOutput().contains(line)){
            if(componentNumber == 1) {
                arrayListHook1 = flipFlop.getArrayListLinesOutput();
                pointHook1 = flipFlop.getPointOutput();
            }
            else{
                arrayListHook2 = flipFlop.getArrayListLinesOutput();
                pointHook2 = flipFlop.getPointOutput();
            }
        }
        if(flipFlop.getArrayListLinesOutputReverted().contains(line)){
            if(componentNumber == 1) {
                arrayListHook1 = flipFlop.getArrayListLinesOutputReverted();
                pointHook1 = flipFlop.getPointOutputReversed();
            }
            else{
                arrayListHook2 = flipFlop.getArrayListLinesOutputReverted();
                pointHook2 = flipFlop.getPointOutputReversed();
            }
        }
        if(flipFlop.getArrayListLinesReset().contains(line)){
            if(componentNumber == 1) {
                arrayListHook1 = flipFlop.getArrayListLinesReset();
                pointHook1 = flipFlop.getPointReset();
            }
            else{
                arrayListHook2 = flipFlop.getArrayListLinesReset();
                pointHook2 = flipFlop.getPointReset();
            }
        }
        if(flipFlop.getArrayListLinesClock().contains(line)){
            if(componentNumber == 1) {
                arrayListHook1 = flipFlop.getArrayListLinesClock();
                pointHook1 = flipFlop.getPointClock();
            }
            else{
                arrayListHook2 = flipFlop.getArrayListLinesClock();
                pointHook2 = flipFlop.getPointClock();
            }
        }
        if(flipFlop.getArrayListLinesInput().contains(line)){
            if(componentNumber == 1) {
                arrayListHook1 = flipFlop.getArrayListLinesInput();
                pointHook1 = flipFlop.getPointInput();
            }
            else{
                arrayListHook2 = flipFlop.getArrayListLinesInput();
                pointHook2 = flipFlop.getPointInput();
            }
        }
        if(flipFlop.getName().equals(Names.flipFlopJK) && ((FlipFlopJK) flipFlop).getArrayListLinesInputK().contains(line)){
            if(componentNumber == 1) {
                arrayListHook1 = ((FlipFlopJK) flipFlop).getArrayListLinesInputK();
                pointHook1 = ((FlipFlopJK) flipFlop).getPointInputK();
            }
            else{
                arrayListHook2 = ((FlipFlopJK) flipFlop).getArrayListLinesInputK();
                pointHook2 = ((FlipFlopJK) flipFlop).getPointInputK();
            }
        }
    }

    public void checkForArrayConnector(Connector con, int componentNumber, Line line) {
        if(componentNumber == 1) {
            arrayListHook1 = con.getArrayListLines();
            pointHook1 = con.getPointCenter();
        }
        else{
            arrayListHook2 = con.getArrayListLines();
            pointHook2 = con.getPointCenter();
        }
    }

    public void checkForArrayBulb(Bulb b, int componentNumber, Line line) {
        if(componentNumber == 1) {
            arrayListHook1 = b.getArrayListLines();
            pointHook1 = b.getPointLineHook();
        }
        else{
            arrayListHook2 = b.getArrayListLines();
            pointHook2 = b.getPointLineHook();
        }
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public boolean isOldState() {
        return oldState;
    }

    public void setOldState(boolean oldState) {
        this.oldState = oldState;
    }

    public boolean isNewState() {
        return newState;
    }

    public void setNewState(boolean newState) {
        this.newState = newState;
    }

    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

    public double getNewX() {
        return newX;
    }

    public void setNewX(double newX) {
        this.newX = newX;
    }

    public double getNewY() {
        return newY;
    }

    public void setNewY(double newY) {
        this.newY = newY;
    }

    public Point getPointBreak() {
        return pointBreak;
    }

    public void setPointBreak(Point pointBreak) {
        this.pointBreak = pointBreak;
    }

    public int getNewPointIndex() {
        return newPointIndex;
    }

    public void setNewPointIndex(int newPointIndex) {
        this.newPointIndex = newPointIndex;
    }
}
