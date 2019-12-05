package components.switches;

import data.Names;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SwitchPulse extends Switch {
    public SwitchPulse(double x, double y, boolean startLife) {
        super(x, y, startLife);
        name = Names.switchPulseName;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_bistable_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_bistable_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
//        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_selected.png.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
    }

    public void draw(GraphicsContext graphicsContext){
        graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);

//        if(selected){
//            if(state.get()){
//                graphicsContext.drawImage(imageViewSelectedOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
//            }
//            else{
//                graphicsContext.drawImage(imageViewSelectedOff.getImage(),pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
//            }
//        }
//        else {
//            if (state.get()) {
//                graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
//            } else {
//                graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
//            }
//        }
    }

    public void lifeCycle(){
        boolean currentValue;
        while(alive){
            currentValue = state.get();
            state.set(!currentValue);
            try {
                Thread.sleep(Sizes.switchPulseInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
