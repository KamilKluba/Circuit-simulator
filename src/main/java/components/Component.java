package components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Component {
    protected int id;
    protected boolean selected = false;
    protected boolean selectedForDrag = false;
    protected boolean alive = true;
    protected String name;
    protected int rotation = 0;
    protected Point pointCenter;
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;
    protected ImageView imageViewSelectedOff;
    protected ImageView imageViewSelectgedOn;
    protected ExecutorService executorService = Executors.newFixedThreadPool(1);
    protected AtomicBoolean stateChanged = new AtomicBoolean(false);
    protected XYChart.Series<Integer, String> series;

    public Component(){}

    public Component(double x, double y, boolean startLife, XYChart.Series<Integer, String> series){
        this.pointCenter = new Point("Center", x, y);
        this.series = series;

        if (startLife) {
            executorService.execute(() -> lifeCycle());
        }
    }

    public void kill(){
        alive = false;
    }

    public void lifeCycle(){};
    public void searchForSignals(Line line, ArrayList<Line> arrayListDependentComponents,
                                 ArrayList<String> arrayListDependentComponentsPin, ArrayList<Line> arayListVisitedComponents){}
    public void select(double x, double y){}
    public void select(double x1, double y1, double x2, double y2){}
    public boolean checkIfCouldBeSelected(double x, double y){
        return false;
    }
    public void draw(GraphicsContext graphicsContext){}
    public void selectForDrag(double x, double y){}
    public boolean inside(double x, double y){
        return false;
    }
    public void rotate(){}
    public void move(double x, double y, double mousePressX, double mousePressY){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectedForDrag() {
        return selectedForDrag;
    }

    public void setSelectedForDrag(boolean selectedForDrag) {
        this.selectedForDrag = selectedForDrag;
    }

    public boolean isSignalOutput(){return false;}

    public boolean isStateChanged() {
        return stateChanged.get();
    }

    public void setStateChanged(boolean stateChanged) {
        this.stateChanged.set(stateChanged);
    }

    public XYChart.Series<Integer, String> getSeries() {
        return series;
    }
}

