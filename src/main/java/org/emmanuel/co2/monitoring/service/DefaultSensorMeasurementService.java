package org.emmanuel.co2.monitoring.service;

import lombok.RequiredArgsConstructor;
import org.emmanuel.co2.monitoring.business.CreateSensor;
import org.emmanuel.co2.monitoring.business.RegisterSensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DefaultSensorMeasurementService implements SensorMeasurementService {

    private final CreateSensor createSensor;
    private final RegisterSensorMeasurement registerSensorMeasurement;

    @Override
    public void measure(SensorMeasurementRequest request) {
        var sensor = createSensor.getOrCreate(request.getSensorId());
        SensorMeasurement measurement = new SensorMeasurement(sensor, request.getValue(), request.getTime());
        registerSensorMeasurement.register(measurement);
    }
}
