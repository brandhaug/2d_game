package ClassicLevels;

import javafx.beans.property.SimpleStringProperty;

public class LevelColumn {
    private SimpleStringProperty name;
    private SimpleStringProperty  status;

    public LevelColumn(String name, String status) {
        this.name = new SimpleStringProperty(name);
        this.status = new SimpleStringProperty(status);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
