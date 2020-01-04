package components.gates;

import components.Line;
import components.Point;
import data.Names;
import data.Sizes;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Not extends Gate implements Serializable {
    private static final long serialVersionUID = 190000000000L;

    public Not(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);

        inputsNumber = 1;
        arrayArrayListLines = new ArrayList[1];
        arrayArrayListLines[0] = new ArrayList<>();
        arrayPointsInputs = new Point[1];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y);
        arraySignalsInputs = new boolean[1];
        name = Names.gateNotName;
        output.set(true);

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/not/not_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/not/not_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/not/not_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
    }

    public void lifeCycle() {
        while (alive) {
            repaintPoints++;
            if(repaintPoints == 100){
                repaintPoints = 0;
                movePoints();
            }
            boolean nextState = arrayArrayListLines[0].size() > 0 && arrayArrayListLines[0].get(0).isSignalOutput();

            if (output.get() == nextState) {
                try {
                    Thread.sleep(Sizes.gatePropagationTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean nextState2 = arrayArrayListLines[0].size() > 0 && arrayArrayListLines[0].get(0).isSignalOutput();

                if(nextState == nextState2){
                    output.set(!nextState);
                    addDataToSeries();
                    stateChanged.set(true);
                }
            }
            try {
                Thread.sleep(Sizes.gateSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
