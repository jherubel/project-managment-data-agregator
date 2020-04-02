package org.coffeeprojects.pmda.project.quartz;

import org.coffeeprojects.pmda.project.ProjectEntity;
import org.coffeeprojects.pmda.project.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ProjectUpdateStep implements Tasklet, StepExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(ProjectUpdateStep.class);

    @Autowired
    ProjectService projectService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.debug("Project update step initialized.");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        try {
            logger.info("Project update step is running ...");

            List<ProjectEntity> projectEntities = projectService.getAllProjectsFromDatabase();
            for (ProjectEntity projectEntity: projectEntities) {
                if (projectEntity.isActive() && projectEntity.getLastCheck().compareTo(new Date()) < 0) {
                    projectService.updateProjectByKey(projectEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.debug("Project update step ended.");
        return ExitStatus.COMPLETED;
    }
}