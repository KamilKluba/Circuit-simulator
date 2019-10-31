package components.switches;

import data.Names;
import data.Sizes;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class SwitchMonostable extends Switch{
    public SwitchMonostable(double x, double y){
        super(x, y);
        name = Names.switchMonostableName;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switch_monostable_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        imageViewOff.setRotate(90);
        imageViewOff.setImage(imageViewOff.snapshot(snapshotParameters, null));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switch_monostable_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        imageViewOn.setRotate(270);
        imageViewOn.setImage(imageViewOn.snapshot(snapshotParameters, null));
    }

    public void setState(boolean state){
        this.state = state;
    }
}
