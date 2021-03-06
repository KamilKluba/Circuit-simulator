package components.switches;

import com.sun.glass.ui.Size;
import data.Names;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class SwitchPulse extends Switch implements Serializable {
    private static final long serialVersionUID = 20300000000L;
    private boolean turnedOn = false;

    public SwitchPulse(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);
        name = Names.switchPulseName;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_selected_on.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
        imageViewSelectedOff = new ImageView(new Image(getClass().getResource("/graphics/switches/switch_pulse_selected_off.png").toExternalForm(), Sizes.baseSwitchXSize, Sizes.baseSwitchYSize, false, false));
    }

    public void lifeCycle(){
        boolean currentValue;
        while(alive){
            if(turnedOn) {
                currentValue = output.get();
                output.set(!currentValue);
                addDataToSeries();
                stateChanged.set(true);
                try {
                    Thread.sleep(Sizes.switchPulseInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(Sizes.gateSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }
}
