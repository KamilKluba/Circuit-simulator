package components.switches;

import data.Names;
import data.Sizes;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class SwitchBistatble extends Switch{
    public SwitchBistatble(double x, double y){
        super(x, y);
        name = Names.switchBistableName;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_bistable_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_bistable_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        imageViewOn.setRotate(180);
        imageViewOn.setImage(imageViewOn.snapshot(snapshotParameters, null));
    }

    public void setState(boolean state){
        this.state = state;

        sendSignal();
    }
}
