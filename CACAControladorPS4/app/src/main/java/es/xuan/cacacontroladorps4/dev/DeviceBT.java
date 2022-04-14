package es.xuan.cacacontroladorps4.dev;

import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

public class DeviceBT implements Serializable {

    private String name = "";
    private String MAC = "";
    private String UUID_DEFAULT = "";
    //private String UUID_DEFAULT = "00001101-0000-1000-8000-00805f9b34fb";
    private String UUID = "";

    public DeviceBT(BluetoothDevice pDevice) {
        setName(pDevice.getName());
        setMAC(pDevice.getAddress());
        if (pDevice.getUuids() != null && pDevice.getUuids().length > 0)
            setUUID(pDevice.getUuids()[0].toString());
        else
            setUUID(UUID_DEFAULT);
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
