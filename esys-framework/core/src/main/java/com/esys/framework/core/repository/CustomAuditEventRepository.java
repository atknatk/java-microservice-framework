package com.esys.framework.core.repository;

import com.esys.framework.core.audit.AuditEventConverter;
import com.esys.framework.core.entity.core.PersistentAuditEvent;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class CustomAuditEventRepository implements AuditEventRepository {

    private static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";

    private static final String ANONYMOUS_USER = "anonymoususer";

    @Inject
    private IPersistenceAuditEventRepository persistenceAuditEventRepository;

    @Inject
    private AuditEventConverter auditEventConverter;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void add(AuditEvent event) {
        if (!AUTHORIZATION_FAILURE.equals(event.getType()) &&
                !ANONYMOUS_USER.equals(event.getPrincipal().toString())) {

//            PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
//            persistentAuditEvent.setPrincipal(event.getPrincipal());
//            persistentAuditEvent.setAuditEventType(event.getType());
//            Instant instant = event.getTimestamp();
//            persistentAuditEvent.setAuditEventDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
//            persistentAuditEvent.setData(auditEventConverter.convertDataToStrings(event.getData()));
//            persistenceAuditEventRepository.save(persistentAuditEvent);
        }
    }

    @Override
    public List<AuditEvent> find(String principal, Instant after, String type) {
        Iterable<PersistentAuditEvent> persistentAuditEvents;
        if (principal == null && after == null) {
            persistentAuditEvents = persistenceAuditEventRepository.findAll();
        } else if (after == null) {
            persistentAuditEvents = persistenceAuditEventRepository.findByPrincipal(principal);
        } else {
            persistentAuditEvents =
                    persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfter(principal, LocalDateTime.from(after));
        }
        return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
    }
}
