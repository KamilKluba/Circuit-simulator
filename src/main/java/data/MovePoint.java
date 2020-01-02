package data;

import components.Point;

public class MovePoint {
    private String componentName;
    private int componentId;
    private Point point;

    public MovePoint(String componentName, int componentId, Point point) {
        this.componentName = componentName;
        this.componentId = componentId;
        this.point = point;
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

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
