package org.coffeeprojects.pmda.tracker;

import feign.Client;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.coffeeprojects.pmda.feature.project.ProjectEntity;
import org.coffeeprojects.pmda.feature.project.ProjectEnum;
import org.coffeeprojects.pmda.tracker.jira.JiraClient;
import org.coffeeprojects.pmda.tracker.mantis.MantisClient;
import org.coffeeprojects.pmda.tracker.redmine.RedmineClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Import(FeignClientsConfiguration.class)
public class TrackerRouter {

    private static final Logger log = LoggerFactory.getLogger(TrackerRouter.class);

    private Map<Map<String, String>, Object> trackers = new HashMap(); // TODO: Map trop complexe c'est une map avec des TrackerBean ?

    @Autowired
    public TrackerRouter(Decoder decoder, Encoder encoder, Client client, TrackerService trackerService) {
        trackerService.getTrackers().forEach(p -> {
            Map<String, String> trackerId = new HashMap();
            trackerId.put(p.getType(), p.getLocalId());

            this.trackers.put(trackerId, buildClient(decoder, encoder, client, getClientInterface(p), p.getUrl(), p.getUser(), p.getPassword()));
        });
    }

    private Class getClientInterface(TrackerBean trackerBean) {
        // TODO: à remplacer par un switch / case : il faudrait que dans trackerBean le type soit une enum
        if (ProjectEnum.JIRA.toString().equalsIgnoreCase(trackerBean.getType())) {
            return JiraClient.class;
        } else if (ProjectEnum.MANTIS.toString().equalsIgnoreCase(trackerBean.getType())) {
            return MantisClient.class;
        } else if (ProjectEnum.REDMINE.toString().equalsIgnoreCase(trackerBean.getType())) {
            return RedmineClient.class;
        }
        log.error("No interface available for the tracker TYPE : " + trackerBean.getType() + "ID : " + trackerBean.getLocalId());
        return null;
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
        if (trackerRouter != null && trackerRouter.getTrackers() !=null &&
                projectEntity != null && projectEntity.getId() != null && projectEntity.getId().getTrackerType() != null) {
            // TODO: je ne pense pas que ce if soit necessaire, vu que c'est cette classe qui remplie la map, sinon fait le controle avant
            for (Map.Entry<Map<String, String>, Object> trackerEntry : trackerRouter.getTrackers().entrySet()) {
                for (Map.Entry<String, String> trackerIdEntry : trackerEntry.getKey().entrySet()) {
                    ProjectEnum trackerType = ProjectEnum.valueOf(trackerIdEntry.getKey());
                    // TODO: Ce n est pas mieux TrackerType à la place de ProjectEnum ?
                    String trackerId = trackerIdEntry.getValue();
                    Object tracker = trackerEntry.getValue();
                    if (trackerType == projectEntity.getId().getTrackerType() &&
                            trackerId.equalsIgnoreCase(projectEntity.getId().getTrackerLocalId())) {
                        return tracker;
                    }
                }
            }
        }
        return null;
    }

    public Map<Map<String, String>, Object> getTrackers() {
        return trackers;
    }

    public TrackerRouter setTrackers(Map<Map<String, String>, Object> trackers) {
        this.trackers = trackers;
        return this;
    }
}
