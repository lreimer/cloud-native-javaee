package de.qaware.oss.cloud.service.process.integration;

import org.apache.deltaspike.core.impl.config.MapConfigSource;

import java.util.Locale;

/**
 * A custom {@link org.apache.deltaspike.core.spi.config.ConfigSource} implementation that
 * uses the System ENV to look for properties. The property name must be Uppcase and use
 * underscore _ instead of dot. Also the ordinal of this config source is higher than all
 * the Apache DeltaSpike default source.
 */
public class EnvironmentConfigSource extends MapConfigSource {

    /**
     * Initialize the {@link org.apache.deltaspike.core.spi.config.ConfigSource}
     * from the System ENV map and set ordinal to 500.
     */
    public EnvironmentConfigSource() {
        super(System.getenv());
        initOrdinal(500);
    }

    @Override
    public String getPropertyValue(String key) {
        String cloudKey = key.replace('.', '_').toUpperCase(Locale.getDefault());
        return super.getPropertyValue(cloudKey);
    }

    @Override
    public boolean isScannable() {
        return false;
    }

    @Override
    public String getConfigName() {
        return "environment-properties";
    }
}
