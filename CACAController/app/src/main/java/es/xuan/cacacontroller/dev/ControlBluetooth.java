package es.xuan.cacacontroller.dev;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import es.xuan.cacacontroller.ControlMandoJPActivity;
import es.xuan.cacacontroller.MainActivity;
import es.xuan.cacacontroller.thread.ConnectedThread;

public class ControlBluetooth extends Context {
    //private MainActivity m_activity = null;
    //private ControlMandoJPActivity m_activity = null;
    private Activity m_activity = null;
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
    private boolean mensajeACK = true;
    //
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    private boolean isConnected = false;

    public boolean isMensajeACK() {
        return mensajeACK;
    }

    public void setMensajeACK(boolean mensajeACK) {
        this.mensajeACK = mensajeACK;
    }

    public ControlBluetooth(ControlMandoJPActivity p_activity) {
        m_activity = p_activity;
    }

    public ControlBluetooth(MainActivity p_activity) {
        m_activity = p_activity;
        //
        checkPermissions();
    }

    private void checkPermissions(){
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    m_activity,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    m_activity,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }
    @SuppressLint("MissingPermission")
    public ArrayList<DeviceBT> listDevicesBT() {
        //
        m_listDevices = new ArrayList<DeviceBT>();
        //
        BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            // Device won't support Bluetooth
            Log.e("[BLUETOOTH]", "Device won't support Bluetooth");
            return null;
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
            Log.d("[DEVICES]", "NAME: " + dev.getName() + " - MAC: " + dev.getMAC() + " - UUID: " + dev.getUUID());
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
        if (!bta.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            m_activity.startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        } else {
            initiateBluetoothProcess();
        }
    }

    public void initiateBluetoothProcess() {
        if (bta.isEnabled()) {
            //attempt to connect to bluetooth module
            BluetoothSocket tmp = null;
            //
            setConnected(false);
            String strMAC = MODULE_MAC;
            //create socket
            try {
                mmDevice = bta.getRemoteDevice(MODULE_MAC);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
                Log.d("[BLUETOOTH]", "Connected to: " + mmDevice.getName() + " - MAC: " + strMAC);
                setConnected(true);
            } catch (IOException e) {
                Log.e("[BLUETOOTH]", "MAC: " + strMAC + " - Error to Connect: " + e);
                try {
                    mmSocket.close();
                } catch (IOException c) {
                    return;
                    //break;
                }
            }
            if (!isConnected())
                return;
            Log.d("[BLUETOOTH]", "Creating handler");
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    if (msg.what == ConnectedThread.RESPONSE_MESSAGE) {
                        String txt = (String) msg.obj;
                        try {
                            Log.d("[BLUETOOTH-rec]", txt);
                            if (txt.contains("ACK")) {
                                setMensajeACK(true);
                                Log.d("[BLUETOOTH-rec]", "Fin ejecución en H3!");
                            }
                        } catch (Exception ex) {
                            Log.e("[BLUETOOTH-error]", ex.toString());
                        }
                    }
                }
            };
            Log.d("[BLUETOOTH]", "Creating and running Thread");
            btt = new ConnectedThread(mmSocket, mHandler);
            btt.start();
        }
    }

    private String formatDate2String() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }

    public String enviarMissatge(String pMissatge) {
        String strMensaje = "";
        if (mmSocket.isConnected() && btt != null) { //if we have connection to the bluetoothmodule
            byte[] bMissatge = (pMissatge + "\r").getBytes();
            Log.d("[BLUETOOTH]", new String(bMissatge, StandardCharsets.UTF_8));
            btt.write(bMissatge);
        } else {
            strMensaje = "[BLUETOOTH] Error - Connected: " + mmSocket.isConnected();
            Log.e("[BLUETOOTH]", strMensaje);
        }
        return strMensaje;
    }

    @Override
    public AssetManager getAssets() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public PackageManager getPackageManager() {
        return null;
    }

    @Override
    public ContentResolver getContentResolver() {
        return null;
    }

    @Override
    public Looper getMainLooper() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void setTheme(int i) {

    }

    @Override
    public Resources.Theme getTheme() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public String getPackageResourcePath() {
        return null;
    }

    @Override
    public String getPackageCodePath() {
        return null;
    }

    @Override
    public SharedPreferences getSharedPreferences(String s, int i) {
        return null;
    }

    @Override
    public boolean moveSharedPreferencesFrom(Context context, String s) {
        return false;
    }

    @Override
    public boolean deleteSharedPreferences(String s) {
        return false;
    }

    @Override
    public FileInputStream openFileInput(String s) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileOutputStream openFileOutput(String s, int i) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean deleteFile(String s) {
        return false;
    }

    @Override
    public File getFileStreamPath(String s) {
        return null;
    }

    @Override
    public File getDataDir() {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(@Nullable String s) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(String s) {
        return new File[0];
    }

    @Override
    public File getObbDir() {
        return null;
    }

    @Override
    public File[] getObbDirs() {
        return new File[0];
    }

    @Override
    public File getCacheDir() {
        return null;
    }

    @Override
    public File getCodeCacheDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return new String[0];
    }

    @Override
    public File getDir(String s, int i) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory, @Nullable DatabaseErrorHandler databaseErrorHandler) {
        return null;
    }

    @Override
    public boolean moveDatabaseFrom(Context context, String s) {
        return false;
    }

    @Override
    public boolean deleteDatabase(String s) {
        return false;
    }

    @Override
    public File getDatabasePath(String s) {
        return null;
    }

    @Override
    public String[] databaseList() {
        return new String[0];
    }

    @Override
    public Drawable getWallpaper() {
        return null;
    }

    @Override
    public Drawable peekWallpaper() {
        return null;
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return 0;
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return 0;
    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {

    }

    @Override
    public void setWallpaper(InputStream inputStream) throws IOException {

    }

    @Override
    public void clearWallpaper() throws IOException {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle bundle) {

    }

    @Override
    public void startActivities(Intent[] intents) {

    }

    @Override
    public void startActivities(Intent[] intents, Bundle bundle) {

    }

    @Override
    public void startIntentSender(IntentSender intentSender, @Nullable Intent intent, int i, int i1, int i2) throws IntentSender.SendIntentException {

    }

    @Override
    public void startIntentSender(IntentSender intentSender, @Nullable Intent intent, int i, int i1, int i2, @Nullable Bundle bundle) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(Intent intent) {

    }

    @Override
    public void sendBroadcast(Intent intent, @Nullable String s) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, @Nullable String s) {

    }

    @Override
    public void sendOrderedBroadcast(@NonNull Intent intent, @Nullable String s, @Nullable BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s1, @Nullable Bundle bundle) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, @Nullable String s) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, @Nullable String s, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s1, @Nullable Bundle bundle) {

    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s, @Nullable Bundle bundle) {

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s, @Nullable Bundle bundle) {

    }

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, int i) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @Nullable String s, @Nullable Handler handler) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @Nullable String s, @Nullable Handler handler, int i) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent intent) {
        return false;
    }

    @Override
    public boolean bindService(Intent intent, @NonNull ServiceConnection serviceConnection, int i) {
        return false;
    }

    @Override
    public void unbindService(@NonNull ServiceConnection serviceConnection) {

    }

    @Override
    public boolean startInstrumentation(@NonNull ComponentName componentName, @Nullable String s, @Nullable Bundle bundle) {
        return false;
    }

    @Override
    public Object getSystemService(@NonNull String s) {
        return null;
    }

    @Nullable
    @Override
    public String getSystemServiceName(@NonNull Class<?> aClass) {
        return null;
    }

    @Override
    public int checkPermission(@NonNull String s, int i, int i1) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int checkCallingPermission(@NonNull String s) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int checkCallingOrSelfPermission(@NonNull String s) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int checkSelfPermission(@NonNull String s) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void enforcePermission(@NonNull String s, int i, int i1, @Nullable String s1) {

    }

    @Override
    public void enforceCallingPermission(@NonNull String s, @Nullable String s1) {

    }

    @Override
    public void enforceCallingOrSelfPermission(@NonNull String s, @Nullable String s1) {

    }

    @Override
    public void grantUriPermission(String s, Uri uri, int i) {

    }

    @Override
    public void revokeUriPermission(Uri uri, int i) {

    }

    @Override
    public void revokeUriPermission(String s, Uri uri, int i) {

    }

    @Override
    public int checkUriPermission(Uri uri, int i, int i1, int i2) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int checkCallingUriPermission(Uri uri, int i) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int i) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public int checkUriPermission(@Nullable Uri uri, @Nullable String s, @Nullable String s1, int i, int i1, int i2) {
        return PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void enforceUriPermission(Uri uri, int i, int i1, int i2, String s) {

    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int i, String s) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int i, String s) {

    }

    @Override
    public void enforceUriPermission(@Nullable Uri uri, @Nullable String s, @Nullable String s1, int i, int i1, int i2, @Nullable String s2) {

    }

    @Override
    public Context createPackageContext(String s, int i) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createContextForSplit(String s) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createConfigurationContext(@NonNull Configuration configuration) {
        return null;
    }

    @Override
    public Context createDisplayContext(@NonNull Display display) {
        return null;
    }

    @Override
    public Context createDeviceProtectedStorageContext() {
        return null;
    }

    @Override
    public boolean isDeviceProtectedStorage() {
        return false;
    }
}
