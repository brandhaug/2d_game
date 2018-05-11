package ClassicLevels;

import javafx.beans.property.SimpleStringProperty;

public class LevelColumn {
    private SimpleStringProperty name;
    private SimpleStringProperty  status;

    /**
     * Constructor
     * @param name
     * @param status
     */
    public LevelColumn(String name, String status) {
        this.name = new SimpleStringProperty(name);
        this.status = new SimpleStringProperty(status);
    }

    /**
     * Get name
     * @return name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Get name property
     * @return nameProperty
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Set name
     * @param name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Get status
     * @return status
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Get status property
     * @return statusProperty
     */
    public SimpleStringProperty statusProperty() {
        return status;
    }

    /**
     * Set status
     * @param status
     */
    public void setStatus(String status) {
        this.status.set(status);
    }
}
