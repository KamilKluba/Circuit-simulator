package components;

import components.flipflops.FlipFlop;
import components.flipflops.FlipFlopJK;
import components.gates.Gate;
import components.switches.Switch;
import data.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Line extends Component implements Serializable {
    public static final long serialVersionUID = 1L;
    private int id;
    private Point point1;
    private Point point2;
    private boolean input1IsOutput;
    private boolean input2IsOutput;
    private AtomicBoolean state = new AtomicBoolean(false);
    private boolean lastState = false;
    private Component component1;
    private Component component2;
    private SerializableColor color = new SerializableColor(Color.BLACK);
    private SerializableColor selectionColor = new SerializableColor(new Color(0.459, 0, 0, 1));
    private SerializableColor onColor = new SerializableColor(new Color(0, 0.8, 0.8, 1));
    private ArrayList<Component> arrayListDependentComponents = new ArrayList<>();
    private ArrayList<String> arrayListDependentComponentPin = new ArrayList<>();
    private ArrayList<Line> arrayListVisitedLines = new ArrayList<>();
    private ArrayList<DependentFlipFlopOutput> arrayListDependentFlipFlopOutput = new ArrayList<>();
    private ArrayList<Point> arrayListBreakPoints = new ArrayList<>();
    private Point point1ToBreak;
    private Point point2ToBreak;
    private Point newBreakPoint;
    private Point closePoint;

    private boolean horizontal;
    private boolean vertical;
    private double a;
    private double b;
    private double c;
    //if < 0, count distance to p1, > 1, to p2, <0;1>, to line
    private double whereToCount = 0;
    private boolean isUnderX1 = false;
    private boolean isUnderX2 = false;
    private boolean isUnderTheLine = false;

    public Line(){
    }

    public Line(double x1, double y1, double x2, double y2, Component component1, Component component2) {
        this.point1 = new Point("Edge 1", x1, y1);
        this.point2 = new Point("Edge 2", x2, y2);
        this.component1 = component1;
        this.component2 = component2;
        this.name = Names.lineName;

        arrayListBreakPoints.add(point1);
        arrayListBreakPoints.add(point2);

        executorService.execute(() -> lifeCycle());
    }

    public void checkForSignals(Line line, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        if (!arrayListVisitedLines.contains(this)) { //2
            arrayListVisitedLines.add(this);
            if (component1.getName().contains(Names.gateSearchName)) {
                checkForSignalsGate(line, (Gate) component1, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component1.getName().contains(Names.switchSearchName)) {
                checkForSignalsSwitch(line, (Switch) component1, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component1.getName().contains(Names.flipFlopSearchName)) {
                checkForSignalsFlipFlop(line, (FlipFlop) component1, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component1.getName().contains(Names.bulbName)) {
                checkForSignalsBulb(line, (Bulb) component1, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component1.getName().contains(Names.connectorName)) {
                checkForSignalsConnector(line, (Connector) component1, arrayListDependentComponents, arrayListVisitedLines);
            }
            if (component2.getName().contains(Names.gateSearchName)) {
                checkForSignalsGate(line, (Gate) component2, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component2.getName().contains(Names.switchSearchName)) {
                checkForSignalsSwitch(line, (Switch) component2, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component2.getName().contains(Names.flipFlopSearchName)) {
                checkForSignalsFlipFlop(line, (FlipFlop) component2, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component2.getName().contains(Names.bulbName)) {
                checkForSignalsBulb(line, (Bulb) component2, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component2.getName().contains(Names.connectorName)) {
                checkForSignalsConnector(line, (Connector) component2, arrayListDependentComponents, arrayListVisitedLines);
            }
        }
    }

    public void checkForSignalsGate(Line line, Gate gate, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        if (gate.getArrayListLinesOutput().contains(this)) {
            if(!arrayListDependentComponents.contains(gate)) {
                arrayListDependentComponents.add(gate);
            }
            for (Line l : gate.getArrayListLinesOutput()) { //3
                l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
            }
        } else {
            for (int i = 0; i < gate.getArrayArrayListLines().length; i++) {
                if (gate.getArrayArrayListLines()[i].contains(this)) {
                    for (Line l : gate.getArrayArrayListLines()[i]) { //3
                        l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
                    }
                    break;
                }
            }
        }
    }

    public void checkForSignalsSwitch(Line line, Switch sw, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        if(!arrayListDependentComponents.contains(sw)) {
            arrayListDependentComponents.add(sw);
        }
        for(Line l : sw.getArrayListLines()){
            l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
        }
    }

    public void checkForSignalsFlipFlop(Line line, FlipFlop flipFlop, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        if(flipFlop.getArrayListLinesOutput().contains(this)){
            if(!arrayListDependentComponents.contains(flipFlop)) {
                arrayListDependentComponents.add(flipFlop);
            }
            for(Line l : flipFlop.getArrayListLinesOutput()){
                if(flipFlop.getArrayListLinesOutput().contains(this)){
                    line.getArrayListDependentFlipFlopOutput().add(new DependentFlipFlopOutput(flipFlop.getId(), true));
                }
                l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        else if(flipFlop.getArrayListLinesOutputReverted().contains(this)){
            if(!arrayListDependentComponents.contains(flipFlop)) {
                arrayListDependentComponents.add(flipFlop);
            }
            for(Line l : flipFlop.getArrayListLinesOutputReverted()){
                if(flipFlop.getArrayListLinesOutputReverted().contains(this)){
                    line.getArrayListDependentFlipFlopOutput().add(new DependentFlipFlopOutput(flipFlop.getId(), false));
                }
                l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        else if(flipFlop.getArrayListLinesReset().contains(this)){
            for(Line l : flipFlop.getArrayListLinesReset()){
                l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        else if(flipFlop.getArrayListLinesClock().contains(this)){
            for(Line l : flipFlop.getArrayListLinesClock()){
                l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        else if(flipFlop.getArrayListLinesInput().contains(this)){
            for(Line l : flipFlop.getArrayListLinesInput()){
                l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        else if(flipFlop.getName().equals(Names.flipFlopJK) && ((FlipFlopJK) flipFlop).getArrayListLinesInputK().contains(this)){
            for(Line l : ((FlipFlopJK) flipFlop).getArrayListLinesInputK()){
                l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
            }
        }
    }

    public void checkForSignalsConnector(Line line, Connector con, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        for(Line l : con.getArrayListLines()){
            l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
        }
    }

    public void checkForSignalsBulb(Line line, Bulb b, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        for(Line l : b.getArrayListLines()){
            l.checkForSignals(line, arrayListDependentComponents, arrayListVisitedLines);
        }
    }

    public void lifeCycle(){
        while(alive){
            try{
                boolean dependentComponentsState = false;
                for(Component c : arrayListDependentComponents){
                    if(!c.getName().contains(Names.flipFlopSearchName) && c.isSignalOutput()){
                        dependentComponentsState = true;
                        break;
                    }
                    else if(c.getName().contains(Names.flipFlopSearchName)){
                        for(DependentFlipFlopOutput dffo : arrayListDependentFlipFlopOutput){
                            if(c.getId() == dffo.getId() && dffo.isMainOutput() && ((FlipFlop)c).isSignalOutput()){
                                dependentComponentsState = true;
                            }
                            else if(c.getId() == dffo.getId() && !dffo.isMainOutput() && ((FlipFlop)c).isSignalReversedOutput()){
                                dependentComponentsState = true;
                            }
                        }
                        break;
                    }
                }

                if(state.get() != dependentComponentsState){
                    state.set(dependentComponentsState);
                    stateChanged.set(true);
                }

                Thread.sleep(Sizes.lineSleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setState(boolean state) {
        this.state.set(state);
        if(state != lastState) {
            lastState = state;
        }
    }

    public void selectForDrag(double x, double y){
        selectedForDrag = checkIfCouldBeSelected(x, y);
    }

    public void select(double x, double y){
        selected = checkIfCouldBeSelected(x, y);
    }

    public void select(double x1, double y1, double x2, double y2){
        double biggerX;
        double lesserX;
        double biggerY;
        double lesserY;

        for(int i = 0; i < arrayListBreakPoints.size() - 1; i++) {
            point1ToBreak = arrayListBreakPoints.get(i);
            point2ToBreak = arrayListBreakPoints.get(i + 1);

            if (point1ToBreak.getX() < point2ToBreak.getX()) {
                lesserX = point1ToBreak.getX();
                biggerX = point2ToBreak.getX();
            } else {
                lesserX = point2ToBreak.getX();
                biggerX = point1ToBreak.getX();
            }
            if (point1ToBreak.getY() < point2ToBreak.getY()) {
                lesserY = point1ToBreak.getY();
                biggerY = point2ToBreak.getY();
            } else {
                lesserY = point2ToBreak.getY();
                biggerY = point1ToBreak.getY();
            }

            double xDifference = biggerX - lesserX;
            double yDifference = biggerY - lesserY;
            int iterationCounter = xDifference > yDifference ? (int) xDifference / 10 : (int) yDifference / 10;

            for (int j = 0; j < iterationCounter; j++) {
                double partX;
                double partY;
                if (point1ToBreak.getX() > point2ToBreak.getX()) {
                    partX = biggerX - (biggerX - lesserX) * j / (iterationCounter - 1);
                } else {
                    partX = lesserX + (biggerX - lesserX) * j / (iterationCounter - 1);
                }
                if (point1ToBreak.getY() > point2ToBreak.getY()) {
                    partY = biggerY - (biggerY - lesserY) * j / (iterationCounter - 1);
                } else {
                    partY = lesserY + (biggerY - lesserY) * j / (iterationCounter - 1);
                }

                if (partX > x1 && partX < x2 && partY > y1 && partY < y2) {
                    selected = true;
                    return;
                }
            }
        }
        selected = false;
    }

    public boolean checkIfCouldBeSelected(double x, double y) {
        boolean farFromHook1 = Math.sqrt(Math.pow(x - arrayListBreakPoints.get(0).getX(), 2) +
                Math.pow(y - arrayListBreakPoints.get(0).getY(), 2)) > Sizes.lineSelectDistance * 2;
        boolean farFromHook2 = Math.sqrt(Math.pow(x - arrayListBreakPoints.get(arrayListBreakPoints.size() - 1).getX(), 2) +
                Math.pow(y - arrayListBreakPoints.get(arrayListBreakPoints.size() - 1).getY(), 2)) > Sizes.lineSelectDistance * 2;
        for(int i = 0; i < arrayListBreakPoints.size() - 1; i++){
            point1ToBreak = arrayListBreakPoints.get(i);
            point2ToBreak = arrayListBreakPoints.get(i + 1);

            setSelectionParameters(x, y);

            if (whereToCount < 0)
                isUnderX1 = Math.sqrt(Math.pow((x - point1ToBreak.getX()), 2) + Math.pow((y - point1ToBreak.getY()), 2)) < Sizes.lineSelectDistance;
            else if (whereToCount > 1)
                isUnderX2 = Math.sqrt(Math.pow((x - point2ToBreak.getX()), 2) + Math.pow((y - point2ToBreak.getY()), 2)) < Sizes.lineSelectDistance;
            else {
                if (!vertical && !horizontal)
                    isUnderTheLine = Math.abs(a * x + b * y + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)) < Sizes.lineSelectDistance;
                if (vertical)
                    isUnderTheLine = Math.abs(x - point1ToBreak.getX()) < Sizes.lineSelectDistance;
                if (horizontal)
                    isUnderTheLine = Math.abs(y - point1ToBreak.getY()) < Sizes.lineSelectDistance;
            }
            if((isUnderX1 || isUnderX2 || isUnderTheLine) && farFromHook1 && farFromHook2) {
                return true;
            }
        }
        return false;
    }

    public int createNewBreakPoint(Point newBreakPoint){
        this.newBreakPoint = newBreakPoint;
        for(int i = 0; i < arrayListBreakPoints.size(); i++){
            if(point2ToBreak.equals(arrayListBreakPoints.get(i))){
                return i;
            }
        }
        return -1;
    }

    public void breakLine(double x, double y, boolean fitToCheck){
        if(newBreakPoint != null) {
            if(fitToCheck) {
                double x1 = x % Sizes.fitLinePlace > Sizes.fitLinePlace / 2 ? Sizes.fitLinePlace : 0;
                double y1 = y % Sizes.fitLinePlace > Sizes.fitLinePlace / 2 ? Sizes.fitLinePlace : 0;
                newBreakPoint.setX(x - x % Sizes.fitLinePlace + x1);
                newBreakPoint.setY(y - y % Sizes.fitLinePlace + y1);
            }
            else{
                newBreakPoint.setX(x);
                newBreakPoint.setY(y);
            }
            if(!arrayListBreakPoints.contains(newBreakPoint)) {
                for (int i = 0; i < arrayListBreakPoints.size() - 1; i++) {
                    if (arrayListBreakPoints.get(i).equals(point1ToBreak)) {
                        arrayListBreakPoints.add(i + 1, newBreakPoint);
                    }
                }
            }
        }
    }

    private void setSelectionParameters(double x, double y){
        whereToCount = ((point2ToBreak.getX() - point1ToBreak.getX()) * (x - point1ToBreak.getX()) + (point2ToBreak.getY() - point1ToBreak.getY()) * (y - point1ToBreak.getY())) / (Math.pow((point2ToBreak.getX() - point1ToBreak.getX()), 2) + Math.pow((point2ToBreak.getY() - point1ToBreak.getY()), 2));
        isUnderX1 = false;
        isUnderX2 = false;
        isUnderTheLine = false;

        if (point1ToBreak.getX() != point2ToBreak.getX() && point1ToBreak.getY() != point2ToBreak.getY()) {
            a = (point2ToBreak.getY() - point1ToBreak.getY()) / (point2ToBreak.getX() - point1ToBreak.getX());
            b = -1;
            if (a != 0) {
                c = point1ToBreak.getY() - (a * point1ToBreak.getX());
            } else {
                c = 0;
            }
            horizontal = false;
            vertical = false;
        } else if (point1ToBreak.getX() == point2ToBreak.getX()) {
            horizontal = false;
            vertical = true;
            a = point1ToBreak.getX();
            b = 0;
            c = 0;
        }
        //if(point1.getY() == point2.getY())
        else {
            vertical = false;
            horizontal = true;
            a = 0;
            b = point1ToBreak.getY();
            c = 0;
        }
    }

    public void move(double x, double y, double mousePressX, double mousePressY) {
        for(int i = 1; i < arrayListBreakPoints.size() - 1; i++){
            arrayListBreakPoints.get(i).setX(arrayListBreakPoints.get(i).getX() + x - mousePressX);
            arrayListBreakPoints.get(i).setY(arrayListBreakPoints.get(i).getY() + y - mousePressY);
        }
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected) {
            graphicsContext.setStroke(selectionColor.getFXColor());
            graphicsContext.setFill(selectionColor.getFXColor());
        }
        else if(state.get()){
            graphicsContext.setStroke(onColor.getFXColor());
            graphicsContext.setFill(onColor.getFXColor());
        }
        else{
            graphicsContext.setStroke(color.getFXColor());
            graphicsContext.setFill(color.getFXColor());
        }
        for(int i = 0; i < arrayListBreakPoints.size() - 1; i++) {
            Point p1 = arrayListBreakPoints.get(i);
            Point p2 = arrayListBreakPoints.get(i + 1);

            graphicsContext.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            graphicsContext.fillOval(p1.getX() - Sizes.lineSelectDistance / 4, p1.getY() - Sizes.lineSelectDistance / 4,
                    Sizes.lineSelectDistance / 2, Sizes.lineSelectDistance / 2);
        }
        graphicsContext.fillOval(point2.getX() - Sizes.lineSelectDistance / 4, point2.getY() - Sizes.lineSelectDistance / 4,
                Sizes.lineSelectDistance / 2, Sizes.lineSelectDistance / 2);
    }
    
    public void delete(){
        deleteComponent(component1);
        deleteComponent(component2);
    }

    private void deleteComponent(Component component){
        if(component.getName().contains(Names.gateSearchName)){
            Gate gate = (Gate)component;
            gate.getArrayListLinesOutput().remove(this);
            for(ArrayList al : gate.getArrayArrayListLines()){
                al.remove(this);
            }
        }
        else if(component.getName().contains(Names.switchSearchName)){
            ((Switch)component).getArrayListLines().remove(this);
        }
        else if(component.getName().contains(Names.flipFlopSearchName)){
            FlipFlop flipFlop = (FlipFlop)component;
            flipFlop.getArrayListLinesInput().remove(this);
            if(flipFlop.getName().equals(Names.flipFlopJK)){
                ((FlipFlopJK)flipFlop).getArrayListLinesInputK().remove(this);
            }
            flipFlop.getArrayListLinesOutput().remove(this);
            flipFlop.getArrayListLinesOutputReverted().remove(this);
            flipFlop.getArrayListLinesAsynchronousInput().remove(this);
            flipFlop.getArrayListLinesClock().remove(this);
            flipFlop.getArrayListLinesReset().remove(this);
        }
        else if(component.getName().contains(Names.bulbName)){
            ((Bulb)component).getArrayListLines().remove(this);
        }
        else if(component.getName().contains(Names.connectorName)){
            ((Connector)component).getArrayListLines().remove(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSignalOutput() {
        return state.get();
    }

    public boolean isState() {
        return state.get();
    }

    public double getX1() {
        return point1.getX();
    }

    public void setX1(double x1) {
        point1.setX(x1);
    }

    public double getY1() {
        return point1.getY();
    }

    public void setY1(double y1) {
        point1.setY(y1);
    }

    public boolean isInput1IsOutput() {
        return input1IsOutput;
    }

    public void setInput1IsOutput(boolean input1IsOutput) {
        this.input1IsOutput = input1IsOutput;
    }

    public double getX2() {
        return point2.getX();
    }

    public void setX2(double x2) {
        point2.setX(x2);
    }

    public double getY2() {
        return point2.getY();
    }

    public void setY2(double y2) {
        point2.setY(y2);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isInput2IsOutput() {
        return input2IsOutput;
    }

    public void setInput2IsOutput(boolean input2IsOutput) {
        this.input2IsOutput = input2IsOutput;
    }

    public Component getComponent1() {
        return component1;
    }

    public void setComponent1(Component component1) {
        this.component1 = component1;
    }

    public Component getComponent2() {
        return component2;
    }

    public void setComponent2(Component component2) {
        this.component2 = component2;
    }

    public ArrayList<Component> getArrayListDependentComponents() {
        return arrayListDependentComponents;
    }

    public ArrayList<Line> getArrayListVisitedLines() {
        return arrayListVisitedLines;
    }

    public ArrayList<DependentFlipFlopOutput> getArrayListDependentFlipFlopOutput() {
        return arrayListDependentFlipFlopOutput;
    }

    public ArrayList<Point> getArrayListBreakPoints() {
        return arrayListBreakPoints;
    }

    public ArrayList<String> getArrayListDependentComponentPin() {
        return arrayListDependentComponentPin;
    }

    public Point getNewBreakPoint() {
        return newBreakPoint;
    }

    public void setNewBreakPoint(Point newBreakPoint) {
        this.newBreakPoint = newBreakPoint;
    }

    public Point getClosePoint() {
        return closePoint;
    }

    public void setClosePoint(Point closePoint) {
        this.closePoint = closePoint;
    }
}
