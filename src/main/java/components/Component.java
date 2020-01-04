package components;

import data.Names;
import data.Sizes;
import javafx.application.Platform;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Component implements Serializable {
    private static final long serialVersionUID = 600000000000L;
    protected transient int id;
    protected boolean selected = false;
    protected boolean selectedForDrag = false;
    protected boolean alive = true;
    protected AtomicBoolean output = new AtomicBoolean(false);
    protected String name;
    protected int rotation = 0;
    protected Point pointCenter;
    protected transient ImageView imageViewOff;
    protected transient ImageView imageViewOn;
    protected transient ImageView imageViewSelectedOff;
    protected transient ImageView imageViewSelectedOn;
    protected transient ExecutorService executorService = Executors.newFixedThreadPool(1);
    protected AtomicBoolean stateChanged = new AtomicBoolean(false);
    protected transient XYChart.Series<Long, String> series;
    protected Long chartMillisCounter;
    private boolean addingDataToSeriesEnabled = true;
    protected int repaintPoints = 0;

    public Component(){}

    public Component(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        this.pointCenter = new Point("Center", x, y);
        this.series = series;
        this.chartMillisCounter = chartMillisCounter;

        if (startLife) {
            executorService.execute(this::lifeCycle);
        }
    }

    public void addDataToSeries() {
        if(addingDataToSeriesEnabled) {
            Platform.runLater(() -> {
                if (output.get()) {
                    series.getData().add(new XYChart.Data<Long, String>(System.currentTimeMillis() - chartMillisCounter, name + " " + id + ": 0"));
                    if (series.getData().size() > 3) {
                        series.getData().add(new XYChart.Data<Long, String>(System.currentTimeMillis() - chartMillisCounter, name + " " + id + ": 1"));
                    }
                } else {
                    series.getData().add(new XYChart.Data<Long, String>(System.currentTimeMillis() - chartMillisCounter, name + " " + id + ": 1"));
                    if (series.getData().size() > 3) {
                        series.getData().add(new XYChart.Data<Long, String>(System.currentTimeMillis() - chartMillisCounter, name + " " + id + ": 0"));
                    }
                }
            });
        }
    }

    public void kill(){
        alive = false;
    }

    public void revive() {
        alive = true;
        executorService.execute(this::lifeCycle);
    }

    public void setLife(){
        executorService = Executors.newFixedThreadPool(1);
        executorService.execute(this::lifeCycle);
    }

    public void setSeriesWithTime(XYChart.Series<Long, String> series, long chartMillisCounter){
        this.chartMillisCounter = chartMillisCounter;
        this.series = series;
    }

    public void resetSeries(XYChart.Series<Long, String> series){
        chartMillisCounter = System.currentTimeMillis();
        this.series = series;
    }

    public void setPictures(){
        if(name.equals(Names.gateNotName)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/not/not_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/not/not_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/not/not_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateAnd2Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/and/and2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/and/and2_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/and/and2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateAnd3Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/and/and3_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/and/and3_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/and/and3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateAnd4Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/and/and4_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/and/and4_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/and/and4_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateNand2Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/nand/nand2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/nand/nand2_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/nand/nand2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateNand3Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/nand/nand3_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/nand/nand3_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/nand/nand3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateNand4Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/nand/nand4_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/nand/nand4_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/nand/nand4_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        if(name.equals(Names.gateOr2Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or/or2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/or/or2_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/or/or2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateOr3Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or/or3_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/or/or3_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/or/or3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateOr4Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or/or4_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/or/or4_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/or/or4_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateNor2Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/nor/nor2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/nor/nor2_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/nor/nor2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateNor3Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/nor/nor3_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/nor/nor3_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/nor/nor3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateNor4Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/nor/nor4_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/nor/nor4_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/nor/nor4_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        if(name.equals(Names.gateXor2Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/xor/xor2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/xor/xor2_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/xor/xor2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateXor3Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/xor/xor3_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/xor/xor3_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/xor/xor3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateXor4Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/xor/xor4_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/xor/xor4_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/xor/xor4_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateXnor2Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor2_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateXnor3Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor3_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor3_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.gateXnor4Name)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor4_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor4_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor4_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        }
        else if(name.equals(Names.switchBistableName)){
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_bistable_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_bistable_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewOn.setRotate(180);
            imageViewOn.setImage(imageViewOn.snapshot(snapshotParameters, null));
            imageViewSelectedOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_selected.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_selected.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewSelectedOn.setRotate(180);
            imageViewSelectedOn.setImage(imageViewSelectedOn.snapshot(snapshotParameters, null));
        }
        else if(name.equals(Names.switchMonostableName)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_selected_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewSelectedOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_selected_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        }
        else if(name.equals(Names.switchPulseName)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_selected_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
            imageViewSelectedOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_selected_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        }
        else if(name.equals(Names.flipFlopJK)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        }
        else if(name.equals(Names.flipFlopT)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        }
        else if(name.equals(Names.flipFlopD)){
            imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
            imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
            imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        }

        for(int i = 0; i < rotation; i++){
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            imageViewOff.setRotate(90);
            imageViewOff.setImage(imageViewOff.snapshot(snapshotParameters, null));
            imageViewOn.setRotate(90);
            imageViewOn.setImage(imageViewOn.snapshot(snapshotParameters, null));
            imageViewSelectedOn.setRotate(90);
            imageViewSelectedOn.setImage(imageViewSelectedOn.snapshot(snapshotParameters, null));
        }
    }

    protected void lifeCycle(){};
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
    public void move(double x, double y, double mousePressX, double mousePressY, boolean fitToCheck){};
    public void movePoints(){};

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

    public boolean isSignalOutput() {return output.get();}

    public boolean isStateChanged() {
        return stateChanged.get();
    }

    public void setStateChanged(boolean stateChanged) {
        this.stateChanged.set(stateChanged);
    }

    public XYChart.Series<Long, String> getSeries() {
        return series;
    }

    public Point getPointCenter() {
        return pointCenter;
    }

    public boolean isAddingDataToSeriesEnabled() {
        return addingDataToSeriesEnabled;
    }

    public void setAddingDataToSeriesEnabled(boolean addingDataToSeriesEnabled) {
        this.addingDataToSeriesEnabled = addingDataToSeriesEnabled;
    }

    public Long getChartMillisCounter() {
        return chartMillisCounter;
    }

    public boolean isAlive() {
        return alive;
    }
}

