package org.coffeeprojects.pmda.status;

public class StatusJiraBean {

    private String id;

    private String name;

    private String description;

    public String getId() {
        return id;
    }

    public StatusJiraBean setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StatusJiraBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public StatusJiraBean setDescription(String description) {
        this.description = description;
        return this;
    }
}
