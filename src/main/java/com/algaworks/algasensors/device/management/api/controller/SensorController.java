package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.client.SensorMonitoringClient;
import com.algaworks.algasensors.device.management.api.model.SensorDetailOutput;
import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.common.IdGenerator;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorID;
import com.algaworks.algasensors.device.management.domain.service.SensorService;
import io.hypersistence.tsid.TSID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorMonitoringClient client;

    private final SensorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@Valid @RequestBody SensorInput sensorInput){
        Sensor sensor = convertToModel(sensorInput);
        return convertToOutput(service.save(sensor));
    }

    @GetMapping
    public Page<SensorOutput> search(@PageableDefault  Pageable pageable){
        Page<Sensor> sensors = service.findAllPaged(pageable);
        return sensors.map(this::convertToOutput);
    }


    @GetMapping("{sensorId}")
    public SensorOutput getOne(@PathVariable TSID sensorId){
        SensorID sensorID = buildSensorID(sensorId);
        Sensor sensor = service.findById(sensorID);
        return convertToOutput(sensor);
    }

    @GetMapping("{sensorId}/detail")
    public SensorDetailOutput getOneWithDetail(@PathVariable TSID sensorId){
        SensorID sensorID = buildSensorID(sensorId);
        SensorMonitoringOutput monitoringOutput = client.getDetail(sensorId);
        Sensor sensor = service.findById(sensorID);
        SensorOutput sensorOutput = convertToOutput(sensor);
        return SensorDetailOutput.builder()
                .monitoring(monitoringOutput)
                .sensor(sensorOutput)
                .build();
    }

    @PutMapping("{sensorId}")
    public ResponseEntity<SensorOutput> update(@Valid @RequestBody SensorInput sensorInput, @PathVariable TSID sensorId ) {
        SensorID sensorID = buildSensorID(sensorId);
        if (service.existsBySensorId(sensorID)) {
            Sensor sensor = convertToModel(sensorInput);
            sensor.setId(sensorID);
            return ResponseEntity.ok(convertToOutput(service.save(sensor)));
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("{sensorId}/enable")
    public ResponseEntity<SensorOutput> enable(@PathVariable TSID sensorId ) {
        SensorID sensorID = buildSensorID(sensorId);
        Sensor sensor = service.findById(sensorID);
        if (sensor != null) {
            sensor.setEnable(Boolean.TRUE);
            client.enableMonitoring(sensorId);
            return ResponseEntity.ok(convertToOutput(service.save(sensor)));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("{sensorId}/disable")
    public ResponseEntity<SensorOutput> disable(@PathVariable TSID sensorId ) {
        SensorID sensorID = buildSensorID(sensorId);
        Sensor sensor = service.findById(sensorID);
        if (sensor != null) {
            sensor.setEnable(Boolean.FALSE);
            client.disableMonitoring(sensorId);
            return ResponseEntity.ok(convertToOutput(service.save(sensor)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{sensorId}")
    public ResponseEntity<Void> deleteBySensorId(@PathVariable  TSID sensorId ){
        SensorID sensorID = buildSensorID(sensorId);
        if (service.existsBySensorId(sensorID)) {
            service.deleteBySensorId(sensorID);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private SensorID buildSensorID(TSID sensorId) {
        return new SensorID(sensorId);
    }

    private Sensor convertToModel(SensorInput sensorInput) {
        return Sensor.builder()
                .id(new SensorID(IdGenerator.generateTSID()))
                .name(sensorInput.getName())
                .ip(sensorInput.getIp())
                .location(sensorInput.getLocation())
                .protocol(sensorInput.getProtocol())
                .model(sensorInput.getModel())
                .enable(false)
                .build();
    }

    private SensorOutput convertToOutput(Sensor sensor) {
        return SensorOutput.builder()
                .id(sensor.getId().getValue())
                .name(sensor.getName())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .ip(sensor.getIp())
                .model(sensor.getModel())
                .enable(sensor.getEnable())
                .build();
    }
}
