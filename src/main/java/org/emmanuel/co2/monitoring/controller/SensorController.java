package org.emmanuel.co2.monitoring.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;
import org.emmanuel.co2.monitoring.service.SensorMeasurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorMeasurementService sensorMeasurementService;

    @PostMapping("/{sensorId}/mesurements")
    ResponseEntity<?> sensorMeasurementApi(@PathVariable("sensorId") String sensorId,
                                           @RequestBody Co2MeasurementRequest request) {

        log.info("receiving new measurement request for sensor {} and body {}", sensorId, request);

        SensorMeasurementRequest sensorMeasurementRequest = SensorMeasurementRequest.builder()
                .sensorId(sensorId)
                .time(request.getTime())
                .value(request.getCo2())
                .build();

        sensorMeasurementService.measure(sensorMeasurementRequest);

        return ResponseEntity.ok().build();
    }

    @Data
    private static class Co2MeasurementRequest {
        private int co2;
        private OffsetDateTime time;
    }

}
