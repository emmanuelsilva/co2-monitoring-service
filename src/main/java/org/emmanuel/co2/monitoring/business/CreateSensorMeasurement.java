package org.emmanuel.co2.monitoring.business;

import lombok.RequiredArgsConstructor;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;

@RequiredArgsConstructor
public class CreateSensorMeasurement {

    public SensorMeasurement create(Sensor sensor, SensorMeasurementRequest request) {
        this.validateRequiredFields(sensor, request);
        return new SensorMeasurement(sensor, request.getValue(), request.getTime());
    }

    private void validateRequiredFields(Sensor sensor, SensorMeasurementRequest request) {
        if (sensor == null) {
            throw  new IllegalArgumentException("Sensor should be defined");
        }

        if (request.getTime() == null) {
            throw new IllegalArgumentException("Measurement must contains valid time");
        }
    }
}
