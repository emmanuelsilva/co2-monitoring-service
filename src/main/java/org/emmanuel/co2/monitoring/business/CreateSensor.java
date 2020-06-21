package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.springframework.util.StringUtils;

public class CreateSensor {

    public Sensor create(String id) {
        this.validateRequiredFields(id);
        return new Sensor(id);
    }

    private void validateRequiredFields(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("The sensor must contains valid id");
        }
    }

}
