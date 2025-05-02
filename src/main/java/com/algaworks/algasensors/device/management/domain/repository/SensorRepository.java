package com.algaworks.algasensors.device.management.domain.repository;

import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, SensorID > {

}
