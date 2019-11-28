package components.flipflops;

import components.Line;
import components.Point;
import data.Names;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class FlipFlopJK extends FlipFlop{
    private boolean signalInputK = false;
    private ArrayList<Line> arrayListLinesInputK = new ArrayList<>();
    private Point pointInputK;

    public FlipFlopJK(double x, double y, boolean startLife){
        super(x, y, startLife);

        name = Names.flipFlopJK;

        pointInput.setName("Input J");
        pointInputK = new Point("Input K", x - 145, y + 75);

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
    }

    @Override
    public void move(double x, double y, double mousePressX, double mousePressY){
        super.move(x, y, mousePressX, mousePressY);

        pointInputK.setX(pointInputK.getX() + x - mousePressX);
        pointInputK.setY(pointInputK.getY() + y - mousePressY);

        for(Line l : arrayListLinesInputK){
            if(l.getComponent1() != null && l.getComponent1().equals(this)){
                l.setX1(pointInputK.getX() + x - mousePressX);
                l.setY1(pointInputK.getY() + y - mousePressY);
            }
            else if(l.getComponent2() != null && l.getComponent2().equals(this)){
                l.setX2(pointInputK.getX() + x - mousePressX);
                l.setY2(pointInputK.getY() + y - mousePressY);
            }
        }
    }

    public boolean isSignalInputK() {
        return signalInputK;
    }

    public void setSignalInputK(boolean signalInputK) {
        this.signalInputK = signalInputK;
    }

    public ArrayList<Line> getArrayListLinesInputK() {
        return arrayListLinesInputK;
    }

    public Point getPointInputK() {
        return pointInputK;
    }
}
