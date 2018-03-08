package de.qaware.oss.cloud.service.process.domain;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;

import static de.qaware.oss.cloud.service.process.domain.ProcessEventLog.processEventLog;

@Stateless
@Transactional(Transactional.TxType.REQUIRED)
public class ProcessEventLogStorage {

    @PersistenceContext
    private EntityManager entityManager;

    public void store(@Observes ProcessEvent processEvent) {
        entityManager.persist(processEventLog(processEvent));
    }

    public Collection<ProcessEventLog> all() {
        return entityManager.createQuery("SELECT e FROM ProcessEventLog e", ProcessEventLog.class).getResultList();
    }
}
