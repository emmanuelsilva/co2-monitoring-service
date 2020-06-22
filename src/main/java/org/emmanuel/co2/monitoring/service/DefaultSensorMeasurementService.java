package org.emmanuel.co2.monitoring.service;

import lombok.RequiredArgsConstructor;
import org.emmanuel.co2.monitoring.business.CreateSensor;
import org.emmanuel.co2.monitoring.business.CreateSensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.repository.SensorMeasurementRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorRepository;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;
import org.emmanuel.co2.monitoring.event.SensorMeasuredEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
class DefaultSensorMeasurementService implements SensorMeasurementService {

    private final SensorRepository sensorRepository;
    private final SensorMeasurementRepository sensorMeasurementRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public SensorMeasurement measure(SensorMeasurementRequest request) {
        var sensor = this.getOrCreate(request.getSensorId());
        var measurement =  this.saveMeasurement(sensor, request);
        this.eventPublisher.publishEvent(new SensorMeasuredEvent(measurement));
        return measurement;
    }

    private Sensor getOrCreate(String id) {
        Supplier<Sensor> saveSensor = () -> {
            var createSensor = new CreateSensor();
            var sensorToSave = createSensor.create(id);
            return sensorRepository.save(sensorToSave);
        };

        return sensorRepository
                .findById(id)
                .orElseGet(saveSensor);
    }

    private SensorMeasurement saveMeasurement(Sensor sensor, SensorMeasurementRequest request) {
        CreateSensorMeasurement createSensorMeasurement = new CreateSensorMeasurement();
        var sensorMeasurement = createSensorMeasurement.create(sensor, request);
        return this.sensorMeasurementRepository.save(sensorMeasurement);
    }
}
