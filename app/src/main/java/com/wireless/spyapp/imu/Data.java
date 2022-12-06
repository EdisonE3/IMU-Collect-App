package com.wireless.spyapp.imu;

public class Data {
    public static long totalId = 0;
    public long id;
    public float x;
    public float y;
    public float z;

    public Data(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = totalId;
        totalId++;
    }
}
