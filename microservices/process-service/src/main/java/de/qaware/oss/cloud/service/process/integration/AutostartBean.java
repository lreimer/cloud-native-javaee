package de.qaware.oss.cloud.service.process.integration;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Singleton
@Startup
public class AutostartBean {

    @Inject
    private Logger logger;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    void autostart() {
        logger.info("Autostarting Process service entity manager.");
        entityManager.clear();
    }
}
