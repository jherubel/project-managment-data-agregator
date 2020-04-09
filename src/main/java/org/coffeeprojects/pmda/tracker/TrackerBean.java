package org.coffeeprojects.pmda.tracker;

public class TrackerBean {
    // TODO: je l'aurai nommé TrackerParametersBean un truc comme ça pour etre plus precis
    private String type;
    private String localId;
    private String clientId;
    private String url;
    private String user;
    private String password;

    public TrackerBean(String type, String localId, String clientId, String url, String user, String password) {
        this.type = type;
        this.localId = localId;
        this.clientId = clientId;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public TrackerBean() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocalId() {
        return localId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
