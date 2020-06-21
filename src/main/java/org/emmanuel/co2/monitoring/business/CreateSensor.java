package org.emmanuel.co2.monitoring.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateSensor {

    private final SensorRepository sensorRepository;

    public Sensor getOrCreate(String id) {
        this.validateRequiredFields(id);
        Supplier<Sensor> onNonExistenceSensor = () -> this.sensorRepository.save(new Sensor(id));

        return this.sensorRepository
            .findById(id)
            .orElseGet(onNonExistenceSensor);
    }

    private void validateRequiredFields(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("The sensor must contains valid id");
        }
    }

}
