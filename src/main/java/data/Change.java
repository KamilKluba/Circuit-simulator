package data;

public class Change {
    private int id;
    private String componentName;
    private int componentId;
    // 1 - creation, 2 - deletion, 3 - moving, 4 - changing state
    private int description;

    private boolean state;
    private double oldX;
    private double oldY;
    private double newX;
    private double newY;

    public Change(int id, String componentName, int componentId, int description, boolean state, double oldX,
                  double oldY, double newX, double newY) {
        this.id = id;
        this.componentName = componentName;
        this.componentId = componentId;
        this.description = description;
        this.state = state;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
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

    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

    public double getNewX() {
        return newX;
    }

    public void setNewX(double newX) {
        this.newX = newX;
    }

    public double getNewY() {
        return newY;
    }

    public void setNewY(double newY) {
        this.newY = newY;
    }
}
