package com.algaworks.algasensors.device.management.domain.service;

import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorID;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class SensorService {

    private SensorRepository repository;

    public boolean existsBySensorId(SensorID sensorID){
        return repository.existsById(sensorID);
    }

    @Transactional
    public Sensor save(Sensor sensor){
        return repository.save(sensor);
    }

    @Transactional
    public void deleteBySensorId(SensorID sensorID){
        repository.deleteById(sensorID);
    }

    public Sensor findById(SensorID sensorID){
        return repository.findById(sensorID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Page<Sensor> findAllPaged(Pageable pageable){
        return repository.findAll(pageable);
    }


}
