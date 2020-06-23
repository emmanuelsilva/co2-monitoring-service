package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;

public interface SensorWarningRepository extends ActiveBySensorFinder<SensorWarning> {

    SensorWarning save(SensorWarning warning);

}
