package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemorySensorRepositoryTest {

    private InMemorySensorRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new InMemorySensorRepository();
    }

    @Test
    void shouldSave() {
        var sensor = new Sensor("123");
        var saved = this.repository.save(sensor);

        assertSensorWasSaved(sensor, saved);
    }

    @Test
    void shouldFindById() {
        var sensor = new Sensor("123");
        var saved = this.repository.save(sensor);

        var searchSensorOpt = this.repository.findById(saved.getId());
        assertSensorWasFound(saved, searchSensorOpt);
    }

    @Test
    void shouldFindEmptyWhenNoExists() {
        var searchSensorOpt = this.repository.findById("122434");
        assertFalse(searchSensorOpt.isPresent());
    }

    private void assertSensorWasFound(Sensor saved, Optional<Sensor> searchSensorOpt) {
        assertTrue(searchSensorOpt.isPresent());
        assertEquals(saved, searchSensorOpt.get());
    }

    private void assertSensorWasSaved(Sensor sensor, Sensor saved) {
        assertNotNull(saved);
        assertEquals(sensor, saved);
        assertTrue(this.repository.findAll().contains(saved));
    }
}