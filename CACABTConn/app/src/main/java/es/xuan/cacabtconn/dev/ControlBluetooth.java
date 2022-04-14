package es.xuan.cacabtconn.dev;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import es.xuan.cacabtconn.MainActivity;
import es.xuan.cacabtconn.thread.ConnectedThread;

public class ControlBluetooth {
    private MainActivity m_activity = null;
    private ArrayList<DeviceBT> m_listDevices = null;
    //
    private final static String CTE_CAMBIO_LINEA_FIC = "\r\n";
    private static String MODULE_MAC = "";                 // 98:D3:34:90:6F:A1
    private static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");    // 00001101-0000-1000-8000-00805f9b34fb
    private static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bta;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private ConnectedThread btt = null;
    public Handler mHandler;

    public ControlBluetooth(MainActivity p_activity) {
        m_activity = p_activity;
    }

    public ArrayList<DeviceBT> listDevicesBT() {
        //
        m_listDevices = new ArrayList<DeviceBT>();
        //
        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            // Device won't support Bluetooth
            Log.i("[BLUETOOTH]", "Device won't support Bluetooth");
        }
        if (!bAdapter.isEnabled()) {
            int intVal = 1;
            Intent eintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            m_activity.startActivityForResult(eintent, intVal);
        }
        //
        Intent dIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        dIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        m_activity.startActivity(dIntent);
        // Get paired devices.
        Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            DeviceBT deviceBT = null;
            for (BluetoothDevice device : pairedDevices) {
                deviceBT = new DeviceBT(device);
                m_listDevices.add(deviceBT);
            }
        }
        return m_listDevices;
    }

    public void printDevices() {
        for (DeviceBT dev : m_listDevices) {
            Log.i("[DEVICES]", "NAME: " + dev.getName() + " - MAC: " + dev.getMAC() + " - UUID: " + dev.getUUID());
        }
    }

    public ArrayList<String> convertDevicesBT2NamesString() {
        ArrayList<String> llistaNames = new ArrayList<String>();
        for (DeviceBT dev : m_listDevices) {
            llistaNames.add(dev.getName());
        }
        return llistaNames;
    }

    public void inicialitzarBT(String pMAC, String pUUID) {
        MODULE_MAC = pMAC;
        //MODULE_MAC = "69:DC:D8:A3:FF:DB";
        MY_UUID = UUID.fromString(pUUID);
        bta = BluetoothAdapter.getDefaultAdapter();
        //if bluetooth is not enabled then create Intent for user to turn it on
        if(!bta.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            m_activity.startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }else{
            initiateBluetoothProcess();
        }
    }

    public void initiateBluetoothProcess() {
        if (bta.isEnabled()) {
            //attempt to connect to bluetooth module
            BluetoothSocket tmp = null;
            //
            boolean bConnected = false;
            String strMAC = MODULE_MAC;
                //create socket
            try {
                mmDevice = bta.getRemoteDevice(MODULE_MAC);
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
                Log.i("[BLUETOOTH]", "Connected to: " + mmDevice.getName() + " - MAC: " + strMAC);
                bConnected = true;
            } catch (IOException e) {
                Log.e("[BLUETOOTH]", "MAC: " + strMAC + " - Error to Connect: " + e);
                try {
                    mmSocket.close();
                } catch (IOException c) {
                    return;
                    //break;
                }
            }
            if (!bConnected)
                return;
            Log.i("[BLUETOOTH]", "Creating handler");
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    if (msg.what == ConnectedThread.RESPONSE_MESSAGE) {
                        String txt = (String) msg.obj;
                        try {
                            if (!txt.equals(""))
                                m_activity.getTvResponse().setText(formatDate2String() + " BT " + txt + CTE_CAMBIO_LINEA_FIC + m_activity.getTvResponse().getText().toString());
                            if (txt.contains("ACCION_FINALIZADA"))
                                m_activity.setbProcesFin(true);
                        } catch (Exception ex) {
                            System.err.println("Error en CBT: " + ex);
                        }
                    }
                }
            };
            Log.i("[BLUETOOTH]", "Creating and running Thread");
            btt = new ConnectedThread(mmSocket, mHandler);
            btt.start();
        }
    }

    private String formatDate2String() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }

    public String enviarMissatge(String pMissatge) {
        String strMensaje = "";
        Log.i("[BLUETOOTH]", "Attempting to send data");
        if (mmSocket.isConnected() && btt != null) { //if we have connection to the bluetoothmodule
            byte[] bMissatge = (pMissatge + "\n").getBytes();
            btt.write(bMissatge);
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
            }
        } else {
            strMensaje = " FICHERO " + "Se ha producido un ERROR" + CTE_CAMBIO_LINEA_FIC;
        }
        System.out.println(pMissatge);
        return strMensaje;
    }
}
