package de.qaware.oss.cloud.service.dashboard;

import lombok.Data;

import javax.json.JsonObject;

@Data
public class DashboardEvent {

    private final String eventType;
    private final JsonObject payload;

}
