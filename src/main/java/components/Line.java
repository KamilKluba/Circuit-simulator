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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Line {
    private double x1;
    private double y1;
    private boolean input1IsOutput;
    private double x2;
    private double y2;
    private boolean input2IsOutput;
    private AtomicBoolean state = new AtomicBoolean(false);
    private boolean lastState = false;
    private boolean selected = false;
    private Gate gate1;
    private Gate gate2;
    private Switch switch1;
    private Switch switch2;
    private FlipFlop flipFlop1;
    private FlipFlop flipFlop2;
    private Color color;
    private Color selectionColor = new Color(0.459, 0, 0, 1);

    public Line(double x1, double y1, double x2, double y2, Gate gate1, Gate gate2, Switch switch1, Switch switch2,
                FlipFlop flipFlop1, FlipFlop flipFlop2, Color color){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.gate1 = gate1;
        this.gate2 = gate2;
        this.switch1 = switch1;
        this.switch2 = switch2;
        this.flipFlop1 = flipFlop1;
        this.flipFlop2 = flipFlop2;
        this.color = color;
    }

    public void lifeCycle(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            while(true){
                    if(input1IsOutput){
                        if(gate1 != null){
                            state.set(gate1.getOutput());
                        }
                        else if(flipFlop1 != null){
                            state.set(flipFlop1.isSignalOutput());
                        }
                        else if(switch1 != null){
                            state.set(switch1.isState());
                        }
                    }
                    if(input2IsOutput){
                        if(gate2 != null){
                            state.set(gate2.getOutput());
                        }
                        else if(flipFlop2 != null){
                            state.set(flipFlop2.isSignalOutput());
                        }
                        else if(switch2 != null){
                            state.set(switch2.isState());
                        }
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
                    Thread.sleep(1);
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


            if (gate1 != null && !input1IsOutput) {
                setSignalOnGateInput(gate1);
            }
            if (gate2 != null && !input2IsOutput) {
                setSignalOnGateInput(gate2);
            }
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
        if(gate1 != null){
            gate1.getArrayListLinesOutput().remove(this);
            for(ArrayList al : gate1.getArrayArrayListLines()){
                al.remove(this);
            }
        }
        if(switch1 != null){
            switch1.getArrayListlines().remove(this);
        }
        if(flipFlop1 != null){
            flipFlop1.getArrayListLinesInput().remove(this);
            if(flipFlop1.getName().equals(Names.flipFlopJK)){
                ((FlipFlopJK)flipFlop1).getArrayListLinesInputK().remove(this);
            }
            flipFlop1.getArrayListLinesOutput().remove(this);
            flipFlop1.getArrayListLinesOutputReverted().remove(this);
            flipFlop1.getArrayListLinesClock().remove(this);
            flipFlop1.getArrayListLinesReset().remove(this);
        }

        if(gate2 != null){
            gate2.getArrayListLinesOutput().remove(this);
            for(ArrayList al : gate2.getArrayArrayListLines()){
                al.remove(this);
            }
        }
        if(switch2 != null){
            switch2.getArrayListlines().remove(this);
        }
        if(flipFlop2 != null){
            flipFlop2.getArrayListLinesInput().remove(this);
            if(flipFlop2.getName().equals(Names.flipFlopJK)){
                ((FlipFlopJK)flipFlop2).getArrayListLinesInputK().remove(this);
            }
            flipFlop2.getArrayListLinesOutput().remove(this);
            flipFlop2.getArrayListLinesOutputReverted().remove(this);
            flipFlop2.getArrayListLinesClock().remove(this);
            flipFlop2.getArrayListLinesReset().remove(this);
        }

        arrayListCreatedLines.remove(this);
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

    public Gate getGate1() {
        return gate1;
    }

    public void setGate1(Gate gate1) {
        this.gate1 = gate1;
    }

    public Gate getGate2() {
        return gate2;
    }

    public void setGate2(Gate gate2) {
        this.gate2 = gate2;
    }

    public Switch getSwitch1() {
        return switch1;
    }

    public void setSwitch1(Switch switch1) {
        this.switch1 = switch1;
    }

    public Switch getSwitch2() {
        return switch2;
    }

    public void setSwitch2(Switch switch2) {
        this.switch2 = switch2;
    }

    public FlipFlop getFlipFlop1() {
        return flipFlop1;
    }

    public void setFlipFlop1(FlipFlop flipFlop1) {
        this.flipFlop1 = flipFlop1;
    }

    public FlipFlop getFlipFlop2() {
        return flipFlop2;
    }

    public void setFlipFlop2(FlipFlop flipFlop2) {
        this.flipFlop2 = flipFlop2;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    //this method is implemented only to show line in tableview
    public int getArrayListInputSize(){
        return 2;
    }
}
