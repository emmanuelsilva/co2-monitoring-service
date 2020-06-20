package org.emmanuel.co2.monitoring.business;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emmanuel.co2.monitoring.domain.Sensor;
import org.emmanuel.co2.monitoring.repository.SensorRepository;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class CreateSensor {

    private final SensorRepository sensorRepository;

    public void create(Sensor sensor) {
        this.validateRequiredFields(sensor);

        Consumer<Sensor> onExistentSensor = (savedSensor) -> log.info("ID {} already", savedSensor.getId());
        Runnable onNonExistenceSensor = () -> this.sensorRepository.save(sensor);

        this.sensorRepository
            .findById(sensor.getId())
            .ifPresentOrElse(onExistentSensor, onNonExistenceSensor);
    }

    private void validateRequiredFields(Sensor sensor) {
        if (StringUtils.isEmpty(sensor.getId())) {
            throw new IllegalArgumentException("The sensor must contains valid id");
        }
    }

}
