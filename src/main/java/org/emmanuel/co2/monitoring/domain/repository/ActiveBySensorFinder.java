package org.emmanuel.co2.monitoring.domain.repository;

import java.util.Optional;

public interface ActiveBySensorFinder<T> {

    Optional<T> findActiveBySensorId(String sensorId);
}
