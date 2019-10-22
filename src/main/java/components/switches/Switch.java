package components.switches;

import components.Line;
import components.Point;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public abstract class Switch {
    private Point pointCenter;
    private boolean state = false;
    private Line line;
    private Point pointLineHook;
    protected ImageView imageViewOn;
    protected ImageView imageViewOff;

    public void draw(GraphicsContext graphicsContext){
        if(state){
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
        }
        else{
            graphicsContext.drawImage(imageViewOff.getImage(),pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
        }
    }

    public Switch(double x, double y){
        this.pointCenter = new Point("Output", x, y);
    }

    public Point getPointCenter() {
        return pointCenter;
    }

    public void setPointCenter(Point pointCenter) {
        this.pointCenter = pointCenter;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Point getPointLineHook() {
        return pointLineHook;
    }

    public void setPointLineHook(Point pointLineHook) {
        this.pointLineHook = pointLineHook;
    }
}
