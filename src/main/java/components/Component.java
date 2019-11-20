package components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public abstract class Component {
    protected boolean selected = false;
    protected boolean selectedForDrag = false;
    protected String name;
    protected int rotation = 0;
    protected Point pointCenter;
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;
    protected ImageView imageViewSelectedOff;
    protected ImageView imageViewSelectgedOn;

    public Component(){}

    public Component(double x, double y){
        this.pointCenter = new Point("Center", x, y);
    }

    public void select(double x, double y){}
    public void select(double x1, double y1, double x2, double y2){}
    public boolean checkIfCouldBeSelected(double x, double y){
        return false;
    }
    public void draw(GraphicsContext graphicsContext){}
    public void selectForDrag(double x, double y){}
    public boolean inside(double x, double y){
        return false;
    }
    public void rotate(){}
    public void move(double x, double y, double mousePressX, double mousePressY){};

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectedForDrag() {
        return selectedForDrag;
    }

    public void setSelectedForDrag(boolean selectedForDrag) {
        this.selectedForDrag = selectedForDrag;
    }
}

