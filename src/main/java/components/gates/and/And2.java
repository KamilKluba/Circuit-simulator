package components.gates.and;

import components.Line;
import components.Point;
import components.gates.Gate;
import data.Names;
import data.Sizes;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class And2 extends And {

    public And2(double x, double y, boolean startLife, XYChart.Series<Integer, String> series) {
        super(x, y, startLife, series);

        inputsNumber = 2;
        arrayArrayListLines = new ArrayList[2];
        arrayArrayListLines[0] = new ArrayList<>();
        arrayArrayListLines[1] = new ArrayList<>();
        arrayPointsInputs = new Point[2];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y - 30);
        arrayPointsInputs[1] = new Point(Names.pointInputName + "2", x - 93, y + 30);
        arraySignalsInputs = new boolean[2];
        name = Names.gateAnd2Name;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/and/and2_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/and/and2_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/and/and2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));

        executorService.execute(() -> lifeCycle());
    }

    public ArrayList[] getArrayArrayListLines() {
        return arrayArrayListLines;
    }
}
