package data;

import java.io.Serializable;

public class DependentFlipFlopOutput implements Serializable {
    public static final long serialVersionUID = 2L;
    private int id;
    private boolean mainOutput;

    public DependentFlipFlopOutput(int id, boolean mainOutput) {
        this.id = id;
        this.mainOutput = mainOutput;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isMainOutput() {
        return mainOutput;
    }

    public void setMainOutput(boolean mainOutput) {
        this.mainOutput = mainOutput;
    }
}
