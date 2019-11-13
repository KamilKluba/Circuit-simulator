package components.flipflops;

import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FlipFlopD extends FlipFlop{
    public FlipFlopD(double x, double y) {
        super(x, y);

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
    }
}
