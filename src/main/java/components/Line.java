package components;

import components.gates.Gate;
import components.switches.Switch;
import javafx.scene.paint.Color;

public class Line {
    private double x1;
    private double y1;
    private boolean input1IsOutput;
    private double x2;
    private double y2;
    private boolean input2IsOutput;
    private boolean state = false;
    private boolean lastState = false;
    private Gate gate1;
    private Gate gate2;
    private Switch switch1;
    private Switch switch2;
    private Color color;

    public Line(double x1, double y1, double x2, double y2, Gate gate1, Gate gate2, Switch switch1, Switch switch2, Color color){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.gate1 = gate1;
        this.gate2 = gate2;
        this.switch1 = switch1;
        this.switch2 = switch2;
        this.color = color;
    }


    public void setState(boolean state) {
        this.state = state;
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
        Line[] arrayLines = gate.getArrayLines();
        for (int i = 0; i < arrayLines.length; i++) {
            if (arrayLines[i] != null && arrayLines[i].equals(this)) {
                gate.getArraySignalsInputs()[i] = state;
            }
        }
        gate.computeSignal();
    }

    public boolean isState() {
        return state;
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
