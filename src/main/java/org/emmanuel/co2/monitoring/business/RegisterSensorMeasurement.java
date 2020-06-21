package org.emmanuel.co2.monitoring.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.repository.SensorMeasurementRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterSensorMeasurement {

    private final SensorMeasurementRepository repository;

    public void register(SensorMeasurement measurement) {
        this.validateRequiredFields(measurement);
        this.repository.save(measurement);
    }

    private void validateRequiredFields(SensorMeasurement measurement) {
        if (measurement.getSensor() == null) {
            throw new IllegalArgumentException("Sensor measurement should contains a sensor");
        }

        if (measurement.getTimestamp() == null) {
            throw new IllegalArgumentException("Measurement must contains valid time");
        }
    }
}
