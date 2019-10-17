package gates.And;

import data.Sizes;
import gates.Gate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class And2 extends Gate {

    public And2(){
        name = "And 2";
        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/and2_gate_off.png").toExternalForm(), Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false));
    }

    public And2(double x, double y) {
        super(x, y);

        name = "And 2";

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/and2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/and2_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/and2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
    }
}
