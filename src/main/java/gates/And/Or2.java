package gates.And;

import data.Sizes;
import gates.Gate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Or2 extends Gate {

    public Or2(){
        name = "Or 2";
        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_off.png").toExternalForm(), Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false));
    }

    public Or2(double x, double y) {
        super(x, y);

        name = "Or 2";

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_off.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_on.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
    }
}
