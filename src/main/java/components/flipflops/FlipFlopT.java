package components.flipflops;

import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FlipFlopT extends FlipFlop {
    public FlipFlopT(double x, double y) {

        super(x, y);

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
    }
}
