package components.switches;

import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SwitchBistatble extends Switch{
    public SwitchBistatble(double x, double y){
        super(x, y);

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switch_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switch_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
    }
}
