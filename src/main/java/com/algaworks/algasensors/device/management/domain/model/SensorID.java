package com.algaworks.algasensors.device.management.domain.model;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SensorID implements Serializable {

    private TSID value;

    public SensorID(TSID value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public SensorID(Long value) {
        Objects.requireNonNull(value);
        this.value = TSID.from(value);
    }

    public SensorID(String value) {
        Objects.requireNonNull(value);
        this.value = TSID.from(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
