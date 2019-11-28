package components.gates;

import components.Line;
import components.Point;
import data.Names;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Not extends Gate {

    public Not(double x, double y, boolean startLife){
        super(x, y, startLife);

        inputsNumber = 1;
        arrayArrayListLines = new ArrayList[1];
        arrayArrayListLines[0] = new ArrayList<>();
        arrayPointsInputs = new Point[1];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y);
        arraySignalsInputs = new boolean[1];
        name = Names.gateNotName;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/not/not_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/not/not_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/not/not_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
    }
}
