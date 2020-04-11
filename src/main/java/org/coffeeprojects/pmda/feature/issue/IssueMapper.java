package org.coffeeprojects.pmda.feature.issue;

import org.apache.commons.lang3.StringUtils;
import org.coffeeprojects.pmda.entity.CompositeIdBaseEntity;
import org.coffeeprojects.pmda.feature.component.ComponentMapper;
import org.coffeeprojects.pmda.feature.issue.jirabean.IssueJiraBean;
import org.coffeeprojects.pmda.feature.issueType.IssueTypeMapper;
import org.coffeeprojects.pmda.feature.priority.PriorityMapper;
import org.coffeeprojects.pmda.feature.project.ProjectMapper;
import org.coffeeprojects.pmda.feature.resolution.ResolutionMapper;
import org.coffeeprojects.pmda.feature.sprint.SprintEntity;
import org.coffeeprojects.pmda.feature.sprint.SprintMapper;
import org.coffeeprojects.pmda.feature.status.StatusMapper;
import org.coffeeprojects.pmda.feature.user.UserEntity;
import org.coffeeprojects.pmda.feature.user.UserMapper;
import org.coffeeprojects.pmda.feature.version.VersionMapper;
import org.coffeeprojects.pmda.tracker.TrackerUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class, StatusMapper.class, ResolutionMapper.class,
        PriorityMapper.class, IssueTypeMapper.class, ProjectMapper.class, VersionMapper.class,
        ComponentMapper.class, SprintMapper.class})
public interface IssueMapper {

    @Mapping(target = "id.clientId", source = "id")
    @Mapping(target = "assignee", source = "fields.assignee")
    @Mapping(target = "reporter", source = "fields.reporter")
    @Mapping(target = "creator", source = "fields.creator")
    @Mapping(target = "summary", source = "fields.summary")
    @Mapping(target = "status", source = "fields.status")
    @Mapping(target = "resolution", source = "fields.resolution")
    @Mapping(target = "resolutionDate", source = "fields.resolutionDate")
    @Mapping(target = "priority", source = "fields.priority")
    @Mapping(target = "issueType", source = "fields.issueType")
    @Mapping(target = "project", source = "fields.project")
    @Mapping(target = "fixVersions", source = "fields.fixVersions")
    @Mapping(target = "labels", source = "fields.labels")
    @Mapping(target = "components", source = "fields.components")
    @Mapping(target = "created", source = "fields.created")
    @Mapping(target = "updated", source = "fields.updated")
    IssueEntity toEntity(IssueJiraBean issueJiraBean);

    @AfterMapping
    default void fromIssueJiraBeanToIssueEntity(@MappingTarget IssueEntity output) {

        // Suppression des types en doublon
        UserEntity assignee = output.getAssignee();
        UserEntity creator = output.getCreator();
        UserEntity reporter = output.getReporter();

        if (output != null) {
            assignee = assignee != null && creator != null && assignee.getId().getClientId().equals(creator.getId().getClientId()) ? creator : assignee;
            assignee = assignee != null && reporter != null && assignee.getId().getClientId().equals(reporter.getId().getClientId()) ? reporter : assignee;
            creator = creator != null && reporter != null && creator.getId().getClientId().equals(reporter.getId().getClientId()) ? reporter : creator;
        }

        output.setAssignee(assignee);
        output.setCreator(creator);
        output.setReporter(reporter);
    }
}
