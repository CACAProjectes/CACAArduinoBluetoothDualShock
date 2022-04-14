package es.xuan.cacacontrollerps4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import es.xuan.cacacontrollerps4.dev.ControlBluetooth;

public class MainActivity extends AppCompatActivity {
    private ControlBluetooth m_cb = null;
    private ArrayList<DeviceBT> m_listaDev = null;
    private Spinner m_spinnerDevices = null;
    private static final String DISPOSITIU_DEFECTE = "H3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        //
        inicialitzarFilesPermissions();
        //
        inicialitzarControlBT();
        //
        omplirDispositiusBT();
    }


    private void inicialitzarControlBT() {
        // Mantener la pantalla activa, siempre
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Orientación de la pantalla en vertical, siempre
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }
        if (m_cb == null)
            m_cb = new ControlBluetooth(this);
    }
    private void inicialitzarBT(String p_strDispName) {
        if (m_cb != null) {
            String strDevice = p_strDispName;
            if (p_strDispName.equals(""))
                strDevice = m_spinnerDevices.getSelectedItem().toString();
            DeviceBT devBT = cercarPropsDevice(m_listaDev, strDevice);
            if (devBT != null)
                m_cb.inicialitzarBT(devBT.getMAC(), devBT.getUUID());
        }
    }

    private void inicialitzarFilesPermissions() {
        /*
        Sol·licita els permissos de READ i WRITE del dispositiu
         */
        es.xuan.cacabtconn.files.FilesDao.sollicitarPermissos(this);
    }
    private boolean conteDispositiuDefecte(List<String> p_lDevices, String pDeviceName) {
        for (String strDevice : p_lDevices) {
            if (strDevice.equals(pDeviceName))
                return true;
        }
        return false;
    }
    private void omplirDispositiusBT() {
        // Seleccionar Dispositius
        m_spinnerDevices = (Spinner) findViewById(R.id.spDevices);
        List<String> list = obtenirDispositiusBT();
        list.add(0, getString(R.string.select_a_device));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        m_spinnerDevices.setAdapter(dataAdapter);
        if (conteDispositiuDefecte(list, DISPOSITIU_DEFECTE)) {
            m_spinnerDevices.setSelection(dataAdapter.getPosition(DISPOSITIU_DEFECTE));
            inicialitzarBT(DISPOSITIU_DEFECTE);
        }
        //
        m_spinnerDevices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                inicialitzarBT("");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //
            }
        });
    }

    private DeviceBT cercarPropsDevice(ArrayList<DeviceBT> plistaDev, String pDeviceName) {
        for (DeviceBT devBT : plistaDev) {
            if (devBT.getName().equals(pDeviceName))
                return devBT;
        }
        return null;
    }

    private ArrayList<String> obtenirDispositiusBT() {
        //
        m_listaDev = m_cb.listDevicesBT();
        Log.i("[BLUETOOTH]","Nº Devices: " + m_listaDev.size());
        //
        m_cb.printDevices();
        //
        return m_cb.convertDevicesBT2NamesString();
    }
}