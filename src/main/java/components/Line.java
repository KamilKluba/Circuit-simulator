package components;

import components.gates.Gate;
import javafx.scene.paint.Color;

public class Line {
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private boolean signal = false;
    private Gate gate1;
    private Gate gate2;
    private Color color;

    public Line(double x1, double y1, double x2, double y2, Gate gate1, Gate gate2, Color color){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.gate1 = gate1;
        this.gate2 = gate2;
        this.color = color;
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

    public boolean isSignal() {
        return signal;
    }

    public void setSignal(boolean signal) {
        this.signal = signal;
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
