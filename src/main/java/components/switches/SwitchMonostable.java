package components.switches;

import components.Line;
import data.Names;
import data.Sizes;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class SwitchMonostable extends Switch implements Serializable {
    private static final long serialVersionUID = 20200000000L;

    public SwitchMonostable(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);
        name = Names.switchMonostableName;
        pointLineHook.setY(pointCenter.getY() + 35);

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_selected_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewSelectedOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_selected_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
    }

    public void rotate(){
        rotation++;
        if(rotation == 3){
            rotation = 0;
        }

        movePoints();
        stateChanged.set(true);
    }

    public void movePoints(){
        if(rotation == 0){
            pointLineHook.setX(pointCenter.getX());
            pointLineHook.setY(pointCenter.getY() + 35);
        }
        else if(rotation == 1){
            pointLineHook.setX(pointCenter.getX() - 30);
            pointLineHook.setY(pointCenter.getY());
        }
        else{
            pointLineHook.setX(pointCenter.getX() + 30);
            pointLineHook.setY(pointCenter.getY());
        }

        for(Line l : arrayListLines){
            if(l.getComponent1() != null && l.getComponent1().equals(this)){
                l.setX1(pointLineHook.getX());
                l.setY1(pointLineHook.getY());
            }
            else if(l.getComponent2() != null && l.getComponent2().equals(this)){
                l.setX2(pointLineHook.getX());
                l.setY2(pointLineHook.getY());
            }
        }
    }

    @Override
    public void setState(boolean state){
        this.output.set(state);
        addDataToSeries();
        stateChanged.set(true);
    }
}
