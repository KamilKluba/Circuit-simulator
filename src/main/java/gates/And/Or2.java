package gates.And;

import data.Sizes;
import gates.Gate;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Or2 extends Gate {

    public Or2(){
        name = "Or 2";
        image = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_right.png").toExternalForm(), Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false));
        for (int i = 0; i < 2; i++) {
            arrayListInputs.add(new Boolean(false));
        }
    }

    public Or2(double x, double y) {
        super(x, y);

        name = "Or 2";
        for (int i = 0; i < 2; i++) {
            arrayListInputs.add(new Boolean(false));
        }

        image = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_right.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
    }
}
