package gates.And;

import data.Sizes;
import gates.Gate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class And2 extends Gate {

    public And2(){
        name = "And 2";
        image = new ImageView(new Image(getClass().getResource("/graphics/and2_gate_right.png").toExternalForm(), Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false));
        for (int i = 0; i < 2; i++) {
            arrayListInputs.add(new Boolean(false));
        }
    }

    public And2(double x, double y) {
        super(x, y);

        name = "And 2";
        for (int i = 0; i < 2; i++) {
            arrayListInputs.add(new Boolean(false));
        }

        image = new ImageView(new Image(getClass().getResource("/graphics/and2_gate_right.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
    }
}
