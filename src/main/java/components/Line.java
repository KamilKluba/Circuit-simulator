package components;

import components.flipflops.FlipFlop;
import components.flipflops.FlipFlopJK;
import components.gates.Gate;
import components.switches.Switch;
import data.Names;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Line {
    private int id;
    private double x1;
    private double y1;
    private boolean input1IsOutput;
    private double x2;
    private double y2;
    private boolean input2IsOutput;
    private AtomicBoolean state = new AtomicBoolean(false);
    private boolean lastState = false;
    private boolean selected = false;
    private Component component1;
    private Component component2;
    private Color color;
    private Color selectionColor = new Color(0.459, 0, 0, 1);
    private ArrayList<Component> arrayListDependentComponents = new ArrayList<>();
    private ArrayList<String> arrayListDependentComponentPin = new ArrayList<>();
    private ArrayList<Line> arrayListVisitedLines = new ArrayList<>();

    public Line(double x1, double y1, double x2, double y2, Component component1, Component component2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.component1 = component1;
        this.component2 = component2;
        this.color = color;
    }

    public void checkForSignals(ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        System.out.println("Line: " + id);
        if (!arrayListVisitedLines.contains(this)) { //2
            arrayListVisitedLines.add(this);
            if (component1.getName().contains(Names.gateSearchName)) {
                checkForSignalsGate((Gate) component1, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component1.getName().contains(Names.switchSearchName)) {
                checkForSignalsSwitch((Switch) component1, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component1.getName().contains(Names.flipFlopSearchName)) {
                checkForSignalsFlipFlop((FlipFlop) component1, arrayListDependentComponents, arrayListVisitedLines);
            }
            if (component2.getName().contains(Names.gateSearchName)) {
                checkForSignalsGate((Gate) component2, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component2.getName().contains(Names.switchSearchName)) {
                checkForSignalsSwitch((Switch) component2, arrayListDependentComponents, arrayListVisitedLines);
            } else if (component2.getName().contains(Names.flipFlopSearchName)) {
                checkForSignalsFlipFlop((FlipFlop) component2, arrayListDependentComponents, arrayListVisitedLines);
            }
        }
    }

    public void checkForSignalsGate(Gate gate, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        if (gate.getArrayListLinesOutput().contains(this)) {
            if(!arrayListDependentComponents.contains(gate)) {
                arrayListDependentComponents.add(gate);
            }
            for (Line l : gate.getArrayListLinesOutput()) { //3
                l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
            }
        } else {
            for (int i = 0; i < gate.getArrayArrayListLines().length; i++) {
                if (gate.getArrayArrayListLines()[i].contains(this)) {
                    for (Line l : gate.getArrayArrayListLines()[i]) { //3
                        l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
                    }
                    break;
                }
            }
        }
    }

    public void checkForSignalsSwitch(Switch sw, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        if(!arrayListDependentComponents.contains(sw)) {
            arrayListDependentComponents.add(sw);
        }
        for(Line l : sw.getArrayListlines()){
            l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
        }
    }

    public void checkForSignalsFlipFlop(FlipFlop flipFlop, ArrayList<Component> arrayListDependentComponents, ArrayList<Line> arrayListVisitedLines) {
        if(flipFlop.getArrayListLinesOutput().contains(this)){
            if(!arrayListDependentComponents.contains(flipFlop)) {
                arrayListDependentComponents.add(flipFlop);
            }
            for(Line l : flipFlop.getArrayListLinesOutput()){
                l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        if(flipFlop.getArrayListLinesOutputReverted().contains(this)){
            if(!arrayListDependentComponents.contains(flipFlop)) {
                arrayListDependentComponents.add(flipFlop);
            }
            for(Line l : flipFlop.getArrayListLinesOutputReverted()){
                l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        if(flipFlop.getArrayListLinesReset().contains(this)){
            for(Line l : flipFlop.getArrayListLinesReset()){
                l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        if(flipFlop.getArrayListLinesClock().contains(this)){
            for(Line l : flipFlop.getArrayListLinesClock()){
                l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        if(flipFlop.getArrayListLinesInput().contains(this)){
            for(Line l : flipFlop.getArrayListLinesInput()){
                l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
            }
        }
        if(flipFlop.getName().equals(Names.flipFlopJK) && ((FlipFlopJK) flipFlop).getArrayListLinesInputK().contains(this)){
            for(Line l : ((FlipFlopJK) flipFlop).getArrayListLinesInputK()){
                l.checkForSignals(arrayListDependentComponents, arrayListVisitedLines);
            }
        }
    }

    public void lifeCycle(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            while(true){
                boolean dependentComponentsState = false;
                for(Component c : arrayListDependentComponents){
                    if(c.isSignalOutput()){
                        dependentComponentsState = true;
                        break;
                    }
                }

                if(state.get() != dependentComponentsState){
                    state.set(dependentComponentsState);
                }

                if(state.get() != lastState) {
                    lastState = state.get();
                    if (state.get()) {
                        color = new Color(0, 0.8, 0.8, 1);
                    } else {
                        color = Color.BLACK;
                    }
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setState(boolean state) {
        this.state.set(state);
        if(state != lastState) {
            lastState = state;
            if (state) {
                color = new Color(0, 0.8, 0.8, 1);
            } else {
                color = Color.BLACK;
            }


//            if (gate1 != null && !input1IsOutput) {
//                setSignalOnGateInput(gate1);
//            }
//            if (gate2 != null && !input2IsOutput) {
//                setSignalOnGateInput(gate2);
//            }
        }
    }

    private void setSignalOnGateInput(Gate gate){
//        ArrayList<Line>[] arrayArrayListLines = gate.getArrayArrayListLines();
//        boolean endLoop = false;
//        for (int i = 0; i < arrayArrayListLines.length; i++) {
//            for(Line l : arrayArrayListLines[i]) {
//                if (l.equals(this)) {
//                    endLoop = true;
//                    gate.getArraySignalsInputs()[i] = state;
//                    for(Line line : arrayArrayListLines[i]){
//                        line.setState(state);
//                    }
//                    break;
//                }
//            }
//            if(endLoop){
//                break;
//            }
//        }
//        gate.computeSignal();
    }

    public void select(double x, double y){
        boolean horizontal;
        boolean vertical;
        double a;
        double b;
        double c;
        //if < 0, count distance to p1, > 1, to p2, <0;1>, to line
        double whereToCount = ((x2 - x1) * (x - x1) + (y2 - y1) * (y - y1)) / (Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
        boolean isUnderX1 = false;
        boolean isUnderX2 = false;
        boolean isUnderTheLine = false;

        if(x1 != x2 && y1 != y2) {
            a = (y2 - y1) / (x2 - x1);
            b = -1;
            if (a != 0) {
                c = y1 - (a * x1);
            } else {
                c = 0;
            }
            horizontal = false;
            vertical = false;
        }
        else if(x1 == x2){
            horizontal = false;
            vertical = true;
            a = x1;
            b = 0;
            c = 0;
        }
        //if(y1 == y2)
        else{
            vertical = false;
            horizontal = true;
            a = 0;
            b = y1;
            c = 0;
        }

        if(whereToCount < 0)
            isUnderX1 = Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2)) < Sizes.lineSelectDistance;
        else if(whereToCount > 1)
            isUnderX2 =  Math.sqrt(Math.pow((x - x2), 2) + Math.pow((y - y2), 2)) < Sizes.lineSelectDistance;
        else {
            if(!vertical && !horizontal)
                isUnderTheLine = Math.abs(a * x + b * y + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)) < Sizes.lineSelectDistance;
            if(vertical)
                isUnderTheLine = Math.abs(x - x1) < Sizes.lineSelectDistance;
            if(horizontal)
                isUnderTheLine = Math.abs(y - y1) < Sizes.lineSelectDistance;
        }

        selected =  isUnderX1 || isUnderX2 || isUnderTheLine;
    }

    public void select(double x1, double y1, double x2, double y2){
        selected = (this.x1 + this.x2) / 2 > x1 && (this.x1 + this.x2) / 2 < x2 && (this.y1 + this.y2) / 2 > y1 && (this.y1 + this.y2) / 2 < y2;
    }

    public boolean checkIfCouldBeSelected(double x, double y) {
        boolean horizontal;
        boolean vertical;
        double a;
        double b;
        double c;
        //if < 0, count distance to p1, > 1, to p2, <0;1>, to line
        double whereToCount = ((x2 - x1) * (x - x1) + (y2 - y1) * (y - y1)) / (Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
        boolean isUnderX1 = false;
        boolean isUnderX2 = false;
        boolean isUnderTheLine = false;

        if (x1 != x2 && y1 != y2) {
            a = (y2 - y1) / (x2 - x1);
            b = -1;
            if (a != 0) {
                c = y1 - (a * x1);
            } else {
                c = 0;
            }
            horizontal = false;
            vertical = false;
        } else if (x1 == x2) {
            horizontal = false;
            vertical = true;
            a = x1;
            b = 0;
            c = 0;
        }
        //if(y1 == y2)
        else {
            vertical = false;
            horizontal = true;
            a = 0;
            b = y1;
            c = 0;
        }

        if (whereToCount < 0)
            isUnderX1 = Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2)) < Sizes.lineSelectDistance;
        else if (whereToCount > 1)
            isUnderX2 = Math.sqrt(Math.pow((x - x2), 2) + Math.pow((y - y2), 2)) < Sizes.lineSelectDistance;
        else {
            if (!vertical && !horizontal)
                isUnderTheLine = Math.abs(a * x + b * y + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)) < Sizes.lineSelectDistance;
            if (vertical)
                isUnderTheLine = Math.abs(x - x1) < Sizes.lineSelectDistance;
            if (horizontal)
                isUnderTheLine = Math.abs(y - y1) < Sizes.lineSelectDistance;
        }

        return isUnderX1 || isUnderX2 || isUnderTheLine;
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected) {
            graphicsContext.setStroke(selectionColor);
        }
        else{
            graphicsContext.setStroke(color);
        }
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }
    
    public void delete(ArrayList<Line> arrayListCreatedLines){
        deleteComponent(component1);
        deleteComponent(component2);

        arrayListCreatedLines.remove(this);
    }

    private void deleteComponent(Component component){
        System.out.println(component.getName());
        if(component.getName().contains(Names.gateSearchName)){
            Gate gate = (Gate)component;
            gate.getArrayListLinesOutput().remove(this);
            for(ArrayList al : gate.getArrayArrayListLines()){
                al.remove(this);
            }
        }
        else if(component.getName().contains(Names.switchSearchName)){
            ((Switch)component).getArrayListlines().remove(this);
        }
        else if(component.getName().contains(Names.flipFlopSearchName)){
            FlipFlop flipFlop = (FlipFlop)component;
            flipFlop.getArrayListLinesInput().remove(this);
            if(flipFlop.getName().equals(Names.flipFlopJK)){
                ((FlipFlopJK)flipFlop).getArrayListLinesInputK().remove(this);
            }
            flipFlop.getArrayListLinesOutput().remove(this);
            flipFlop.getArrayListLinesOutputReverted().remove(this);
            flipFlop.getArrayListLinesClock().remove(this);
            flipFlop.getArrayListLinesReset().remove(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isState() {
        return state.get();
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public boolean isInput1IsOutput() {
        return input1IsOutput;
    }

    public void setInput1IsOutput(boolean input1IsOutput) {
        this.input1IsOutput = input1IsOutput;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ArrayList<Component> getArrayListDependentComponents() {
        return arrayListDependentComponents;
    }

    public ArrayList<Line> getArrayListVisitedLines() {
        return arrayListVisitedLines;
    }

    public ArrayList<String> getArrayListDependentComponentPin() {
        return arrayListDependentComponentPin;
    }
}
