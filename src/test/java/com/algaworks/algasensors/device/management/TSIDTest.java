package com.algaworks.algasensors.device.management;

import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Test;

public class TSIDTest {

    @Test
    public void shouldGenerateTSID(){
        TSID tsid = TSID.fast();
        System.out.println(tsid);
        System.out.println(tsid.toLong());
    }
}
