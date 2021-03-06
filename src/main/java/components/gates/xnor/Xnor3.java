package components.gates.xnor;

import data.Point;
import data.Names;
import data.Sizes;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class Xnor3 extends Xnor implements Serializable {
    private static final long serialVersionUID = 10502000000L;

    public Xnor3(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);

        inputsNumber = 3;
        arrayArrayListLines = new ArrayList[3];
        arrayArrayListLines[0] = new ArrayList<>();
        arrayArrayListLines[1] = new ArrayList<>();
        arrayArrayListLines[2] = new ArrayList<>();
        arrayPointsInputs = new Point[3];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y - 30);
        arrayPointsInputs[1] = new Point(Names.pointInputName + "2", x - 93, y);
        arrayPointsInputs[2] = new Point(Names.pointInputName + "3", x - 93, y + 30);
        arraySignalsInputs = new boolean[3];
        name = Names.gateXnor3Name;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor3_gate_off.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor3_gate_on.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/xnor/xnor3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
    }


    public ArrayList[] getArrayArrayListLines() {
        return arrayArrayListLines;
    }
}
