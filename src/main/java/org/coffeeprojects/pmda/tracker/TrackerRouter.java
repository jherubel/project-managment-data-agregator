package org.coffeeprojects.pmda.tracker;

import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.coffeeprojects.pmda.feature.project.ProjectEntity;
import org.coffeeprojects.pmda.tracker.jira.JiraClient;
import org.coffeeprojects.pmda.tracker.mantis.MantisClient;
import org.coffeeprojects.pmda.tracker.redmine.RedmineClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Import(FeignClientsConfiguration.class)
public class TrackerRouter {

    private static final Logger log = LoggerFactory.getLogger(TrackerRouter.class);

    private List<TrackerParametersBean> trackerParametersBeans = new ArrayList();

    @Autowired
    public TrackerRouter(Decoder decoder, Encoder encoder, Client client, TrackerService trackerService) {
        trackerService.getTrackers().forEach(p -> {
            TrackerParametersBean trackerParametersBean = new TrackerParametersBean();
            trackerParametersBean.setType(TrackerTypeEnum.valueOf(p.getType().toUpperCase()));
            trackerParametersBean.setLocalId(p.getLocalId());
            trackerParametersBean.setClientId(p.getClientId());
            trackerParametersBean.setClient(buildClient(decoder, encoder, client, getClientInterface(p), p.getUrl(), p.getUser(), p.getPassword()));
            trackerParametersBeans.add(trackerParametersBean);
        });
    }

    private Class getClientInterface(TrackerDataBean trackerDataBean) {
        switch (TrackerTypeEnum.valueOf(trackerDataBean.getType().toUpperCase())) {
            case JIRA:
                return JiraClient.class;
            case MANTIS:
                return MantisClient.class;
            case REDMINE:
                return RedmineClient.class;
            default:
                log.error("No interface available for this tracker : " + trackerDataBean.toString());
                return null;
        }
    }

    private Object buildClient(Decoder decoder, Encoder encoder, Client client, Class clientClass,
                                      String url, String user, String password) {
        return Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .requestInterceptor(new BasicAuthRequestInterceptor(user, password))
                .target(clientClass, url);
    }

    public static final Object getTracker(TrackerRouter trackerRouter, ProjectEntity projectEntity) {
        if (trackerRouter != null && trackerRouter.trackerParametersBeans != null &&
                projectEntity != null && projectEntity.getId() != null && projectEntity.getId().getTrackerType() != null) {

            TrackerParametersBean trackerParametersBean = trackerRouter.trackerParametersBeans.stream()
                    .filter(t -> t.getType() == projectEntity.getId().getTrackerType())
                    .filter(t -> t.getLocalId() == projectEntity.getId().getTrackerLocalId())
                    .filter(t -> t.getClientId() == projectEntity.getId().getClientId())
                    .findFirst()
                    .orElse(new TrackerParametersBean());

            return trackerParametersBean.getClient();
        }
        if (projectEntity != null) {
            log.error("Tracker not found for project : " + projectEntity.toString());
        } else {
            log.error("Tracker not found");
        }

        return null;
    }

    public List<TrackerParametersBean> getTrackerParametersBeans() {
        return trackerParametersBeans;
    }

    public TrackerRouter setTrackerParametersBeans(List<TrackerParametersBean> trackerParametersBeans) {
        this.trackerParametersBeans = trackerParametersBeans;
        return this;
    }
}
