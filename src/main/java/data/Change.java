package data;

public class Change {
    private int id;
    private String componentName;
    private int componentId;
    // 1 - creation, 2 - deletion, 3 - moving, 4 - changing state
    private int description;

    private boolean state;
    private double x;
    private double y;

    public Change(int id, String componentName, int componentId, int description, boolean state, double x, double y) {
        this.id = id;
        this.componentName = componentName;
        this.componentId = componentId;
        this.description = description;
        this.state = state;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
