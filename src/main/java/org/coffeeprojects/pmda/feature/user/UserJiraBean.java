package org.coffeeprojects.pmda.feature.user;

import java.util.Objects;

public class UserJiraBean {

    private String accountId;

    private String emailAddress;

    private String displayName;

    private String timeZone;

    private boolean active;

    public String getAccountId() {
        return accountId;
    }

    public UserJiraBean setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public UserJiraBean setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserJiraBean setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isActive() {
        return active;
    }

    public UserJiraBean setActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJiraBean that = (UserJiraBean) o;
        return active == that.active &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(emailAddress, that.emailAddress) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(timeZone, that.timeZone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, emailAddress, displayName, timeZone, active);
    }

    @Override
    public String toString() {
        return "UserJiraBean{" +
                "accountId='" + accountId + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", displayName='" + displayName + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", active=" + active +
                '}';
    }
}
