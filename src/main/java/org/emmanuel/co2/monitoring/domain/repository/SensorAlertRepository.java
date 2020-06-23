package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;

public interface SensorAlertRepository extends ActiveBySensorFinder<SensorAlert> {

    SensorAlert save(SensorAlert alert);

}
