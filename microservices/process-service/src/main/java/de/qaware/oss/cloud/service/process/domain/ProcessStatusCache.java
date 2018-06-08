package de.qaware.oss.cloud.service.process.domain;

import io.opentracing.contrib.cdi.Traced;

import javax.cache.Cache;
import javax.cache.annotation.CacheDefaults;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
@CacheDefaults(cacheName = "processCache")
public class ProcessStatusCache {
    @Inject
    private Cache<String, String> processCache;

    @Traced
    public void put(@Observes ProcessEvent processEvent) {
        processCache.put(processEvent.getProcessId(), processEvent.getEventType().name());
    }

    @Traced
    public Optional<ProcessEvent.EventType> get(String processId) {
        Optional<String> status = Optional.ofNullable(processCache.get(processId));
        return status.map(ProcessEvent.EventType::valueOf);
    }
}
