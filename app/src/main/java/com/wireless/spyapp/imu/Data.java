package com.wireless.spyapp.imu;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public class Data {
    public static long totalId = 0;
    public long time;
    public long id;
    public float x;
    public float y;
    public float z;

    public Data(long time, float x, float y, float z) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = totalId;
        totalId++;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return time + "," + x + "," + y + "," + z;
    }
}
