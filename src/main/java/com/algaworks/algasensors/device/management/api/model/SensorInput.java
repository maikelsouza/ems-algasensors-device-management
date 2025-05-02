package com.algaworks.algasensors.device.management.api.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorInput {

    @NotNull(message = "is requeried")
    private String name;

    @NotNull(message = "is requeried")
    private String ip;

    @NotNull(message = "is requeried")
    private String location;

    @NotNull(message = "is requeried")
    private String protocol;

    @NotNull(message = "is requeried")
    private String model;

}
