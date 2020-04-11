package org.coffeeprojects.pmda.feature.project;

import org.coffeeprojects.pmda.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "project")
public class ProjectEntity extends BaseEntity implements Serializable {

    private String key;

    private String name;

    private Instant lastCheck;

    @Column(insertable = false, updatable = false)
    private Boolean active;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_client_id", referencedColumnName="clientId")
    @JoinColumn(name = "project_tracker_local_id", referencedColumnName="trackerLocalId")
    @JoinColumn(name = "project_tracker_type", referencedColumnName="trackerType")
    private Set<ProjectCustomField> projectCustomFields;

    public String getKey() {
        return key;
    }

    public ProjectEntity setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProjectEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Instant getLastCheck() {
        return lastCheck;
    }

    public ProjectEntity setLastCheck(Instant lastCheck) {
        this.lastCheck = lastCheck;
        return this;
    }

    public Boolean isActive() {
        return active;
    }

    public ProjectEntity setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Set<ProjectCustomField> getProjectCustomFields() {
        return projectCustomFields;
    }

    public ProjectEntity setProjectCustomFields(Set<ProjectCustomField> projectCustomFields) {
        this.projectCustomFields = projectCustomFields;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectEntity that = (ProjectEntity) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(name, that.name) &&
                Objects.equals(lastCheck, that.lastCheck) &&
                Objects.equals(active, that.active) &&
                Objects.equals(projectCustomFields, that.projectCustomFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, name, lastCheck, active, projectCustomFields);
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", lastCheck=" + lastCheck +
                ", active=" + active +
                ", projectCustomFields=" + projectCustomFields +
                '}';
    }
}
