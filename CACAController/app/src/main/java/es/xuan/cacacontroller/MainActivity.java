package es.xuan.cacacontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import es.xuan.cacacontroller.dev.ControlBluetooth;
import es.xuan.cacacontroller.dev.DeviceBT;
import es.xuan.cacacontroller.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final long CTE_VIBRATION_MS = 50;
    private static final String CTE_NAME_H6 = "H6";
    private final static String CTE_CAMBIO_LINEA = "\r\n";
    //
    private Vibrator mVibr = null;
    //
    private ControlBluetooth m_cb = null;
    private ArrayList<DeviceBT> m_listaDev = null;
    //
    private ImageView ivBTJPok = null;
    private ImageView ivBTJPko = null;
    private ImageView ivBTH6ok = null;
    private ImageView ivBTH6ko = null;
    //
    private TextView tvJPADConnect = null;
    private TextView tvH6Connect = null;
    private TextView tvTrazas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        inicialitzar_pantalla();
        //
        inicialitzarControlBT();
        //
        obtenirDispositiusBT();
        //
        inicialitzarBT(CTE_NAME_H6);
        //
        actualitzarBT();
    }

    private void escribirTrazasPantalla(String pText) {
        String str = tvTrazas.getText().toString();
        tvTrazas.setText(pText + CTE_CAMBIO_LINEA + str);
    }
    private void actualitzarBT() {
        // JPAD
        ivBTJPko.setVisibility(ImageView.GONE);
        ivBTJPok.setVisibility(ImageView.VISIBLE);
        tvJPADConnect.setText(R.string.conectado);
        escribirTrazasPantalla(getString(R.string.logJPADConectado));
        // H6
        if (m_cb.isConnected()) {
            // H6 conectado
            ivBTH6ko.setVisibility(ImageView.GONE);
            ivBTH6ok.setVisibility(ImageView.VISIBLE);
            tvH6Connect.setText(R.string.conectado);
            escribirTrazasPantalla(getString(R.string.logH6Conectado));
        }
        else {
            // H6 NO conectado
            ivBTH6ko.setVisibility(ImageView.VISIBLE);
            ivBTH6ok.setVisibility(ImageView.GONE);
            tvH6Connect.setText(R.string.desconectado);
            escribirTrazasPantalla(getString(R.string.logH6Desconectado));
        }
    }

    private void inicialitzar_pantalla() {
        //
        mVibr = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //
        LinearLayout m_llCuadroMando1 = (LinearLayout)findViewById(R.id.llCuadroMando1);
        m_llCuadroMando1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Utils.vibrar(mVibr, CTE_VIBRATION_MS);
                //
                Intent intent = new Intent(arg0.getContext(), ControlMandoJPActivity.class);
                startActivity(intent);
            }
        });
        ivBTJPok = (ImageView)findViewById(R.id.ivSenyalJpadOK);
        ivBTJPok.setVisibility(ImageView.GONE);
        ivBTJPko = (ImageView)findViewById(R.id.ivSenyalJpadKO);
        ivBTJPko.setVisibility(ImageView.VISIBLE);
        ivBTH6ok = (ImageView)findViewById(R.id.ivSenyalH6OK);
        ivBTH6ok.setVisibility(ImageView.GONE);
        ivBTH6ko = (ImageView)findViewById(R.id.ivSenyalH6KO);
        ivBTH6ko.setVisibility(ImageView.VISIBLE);
        //
        tvJPADConnect = (TextView)findViewById(R.id.tvJPADConnect);
        tvJPADConnect.setText(R.string.desconectado);
        tvH6Connect = (TextView)findViewById(R.id.tvH6Connect);
        tvH6Connect.setText(R.string.desconectado);
        //
        tvTrazas = (TextView)findViewById(R.id.tvTrazas);
        tvTrazas.setText("");
    }
    private void inicialitzarControlBT() {
        //
        if (m_cb == null)
            m_cb = new ControlBluetooth(this);
    }
    private void inicialitzarBT(String p_strDispName) {
        if (m_cb != null) {
            DeviceBT devBT = cercarPropsDevice(m_listaDev, p_strDispName);
            if (devBT != null)
                m_cb.inicialitzarBT(devBT.getMAC(), devBT.getUUID());
        }
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