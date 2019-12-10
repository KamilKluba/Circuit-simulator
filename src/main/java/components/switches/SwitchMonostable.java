package components.switches;

import components.Line;
import data.Names;
import data.Sizes;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class SwitchMonostable extends Switch{
    public SwitchMonostable(double x, double y, boolean startLife, XYChart.Series<Integer, String> series){
        super(x, y, startLife, series);
        name = Names.switchMonostableName;
        pointLineHook.setY(pointCenter.getY() + 35);

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_monostable_selected.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected){
            graphicsContext.drawImage(imageViewSelected.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
        }
        else if(state.get()){
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
        }
        else{
            graphicsContext.drawImage(imageViewOff.getImage(),pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
        }
    }

    public void rotate(){
        rotation++;
        if(rotation == 3){
            rotation = 0;
        }

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

        for(Line l : arrayListlines){
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
        this.state.set(state);
    }
}
