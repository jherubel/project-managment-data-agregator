package org.coffeeprojects.pmda.feature.project.service.impl;

import org.coffeeprojects.pmda.entity.CompositeIdBaseEntity;
import org.coffeeprojects.pmda.feature.project.ProjectEntity;
import org.coffeeprojects.pmda.feature.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RedmineProjectService implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(RedmineProjectService.class);

    public ProjectEntity getProjectById(CompositeIdBaseEntity id) {
        return new ProjectEntity();
    }

    @Transactional
    public void updateProjectByKey(ProjectEntity projectEntity) {
        log.debug("Redmine - update project by key");
    }
}
