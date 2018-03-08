package de.qaware.oss.cloud.service.payment.domain;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;

import static de.qaware.oss.cloud.service.payment.domain.PaymentEventLog.paymentEventLog;

@Stateless
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class PaymentEventLogStorage {

    @PersistenceContext
    private EntityManager entityManager;

    public void store(@Observes PaymentEvent paymentEvent) {
        entityManager.persist(paymentEventLog(paymentEvent));
        entityManager.flush();
    }

    public Collection<PaymentEventLog> all() {
        return entityManager.createQuery("SELECT e FROM PaymentEventLog e", PaymentEventLog.class).getResultList();
    }
}
