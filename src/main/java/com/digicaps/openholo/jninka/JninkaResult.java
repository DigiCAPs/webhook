package com.digicaps.openholo.jninka;

import java.util.ArrayList;
import java.util.List;

public class JninkaResult {

    public String scanTime;

    public List<JninkaFiles> files;

    public JninkaResult() {
        scanTime = new String();

        this.files = new ArrayList<>();
    }
}
