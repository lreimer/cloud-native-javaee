package de.qaware.oss.cloud.service.billing.domain;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;

import static de.qaware.oss.cloud.service.billing.domain.BillingEventLog.billingEventLog;

@Stateless
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class BillingEventLogStorage {

    @PersistenceContext
    private EntityManager entityManager;

    public void store(@Observes BillingEvent billingEvent) {
        entityManager.persist(billingEventLog(billingEvent));
        entityManager.flush();
    }

    public Collection<BillingEventLog> all() {
        return entityManager.createQuery("SELECT e FROM BillingEventLog e", BillingEventLog.class).getResultList();
    }
}
