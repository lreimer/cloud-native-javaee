package de.qaware.oss.cloud.service.process.domain;

import de.qaware.oss.cloud.service.process.integration.JsonObjectConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.json.JsonObject;
import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "process_event_log")
@NoArgsConstructor
@Getter
@Setter
public class ProcessEventLog {
    @Id
    @SequenceGenerator(name = "process_event_log_seq_gen", sequenceName = "process_event_log_seq", allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "process_event_log_seq_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "process_id", nullable = false)
    private String processId;

    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private ProcessEvent.EventType eventType;

    @Column(name = "payload", columnDefinition = "json")
    @Convert(converter = JsonObjectConverter.class)
    private JsonObject payload;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    /**
     * Set the timestamp to the date when the event log is persisted.
     */
    @PrePersist
    protected void onCreate() {
        processedAt = OffsetDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcessEventLog event = (ProcessEventLog) o;

        return new EqualsBuilder()
                .append(processId, event.processId)
                .append(eventType, event.eventType)
                .append(processedAt, event.processedAt)
                .append(payload, event.payload)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(processId)
                .append(eventType)
                .append(processedAt)
                .append(payload)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("processId", processId)
                .append("eventType", eventType)
                .append("processedAt", processedAt)
                .append("payload", payload)
                .toString();
    }

    public static ProcessEventLog processEventLog(ProcessEvent event) {
        ProcessEventLog eventLog = new ProcessEventLog();
        eventLog.setProcessId(event.getProcessId());
        eventLog.setEventType(event.getEventType());
        eventLog.setPayload(event.getPayload());
        return eventLog;
    }

}
