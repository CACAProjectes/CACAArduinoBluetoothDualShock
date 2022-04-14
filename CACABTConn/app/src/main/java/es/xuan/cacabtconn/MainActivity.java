package es.xuan.cacabtconn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import es.xuan.cacabtconn.ctrl.ControlCalculo;
import es.xuan.cacabtconn.ctrl.MensajePlot;
import es.xuan.cacabtconn.dev.ControlBluetooth;
import es.xuan.cacabtconn.dev.DeviceBT;
import es.xuan.cacabtconn.files.FilesDao;
import es.xuan.cacabtconn.view.PlotterView;
import es.xuan.cacabtconn.view.Punto;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    //
    private final static String CTE_SEPARADOR_FIC = ";";
    private final static String CTE_CAMBIO_LINEA_FIC = "\r\n";
    private static final String DISPOSITIU_DEFECTE = "H3";
    private static final String CTE_PATH_FITXERS = "/apps/cacabtconn/";
    private static final long CTE_TIMEOUT_EXTRA = 1000; // Tiempo extra de TIMEOUT
    private Spinner m_spinnerDevices = null;
    private Spinner m_spinnerFitxers = null;
    private ImageView m_ivControl = null;
    private Switch m_swPujarBaixar = null;
    private TextView tvResponse = null;
    private TextView tvSend = null;
    //
    private ControlBluetooth m_cb = null;
    private ArrayList<DeviceBT> m_listaDev = null;
    private ArrayList<File> m_listaFiles = null;
    //
    private PlotterView m_mpPlotterView = null;
    //
    private float dX, dY;
    static final int CLICK_DURATION = 175;
    static final float MARGEN_MAX = 100;
    /*
        Datos para la ventana de proceso
    */
    private static boolean bFin = false;
    private static boolean bEsprimeraVez = true;
    private static int iContador = 0;
    private static long timeActual = 0;
    private static long timeOut = 0;
    private static long timeInicial = 0;    // Tiempo inicial
    private static ArrayList<String> arrLineas = new ArrayList<String>();
    private ProgressBar progressBar = null;
    private TextView txt = null;
    private static int numLineasTotal = 0;
    //
    private boolean bProcesFin = false;  // Sólo procesa una linea y cuando acaba, la siguiente
    private String strRespuesta = "";

    public String getRespuesta() {
        return strRespuesta;
    }
    public void setRespuesta(String pStrRespuesta) {
        strRespuesta = pStrRespuesta;
    }
    public boolean isbProcesFin() {
        return bProcesFin;
    }

    public void setbProcesFin(boolean bProcesFin) {
        this.bProcesFin = bProcesFin;
    }

    public TextView getTvResponse() {
        return tvResponse;
    }

    public void setTvResponse(TextView tvResponse) {
        this.tvResponse = tvResponse;
    }

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        inicialitzarFilesPermissions();
        //
        inicialitzarControlBT();
        //
        omplirDispositiusBT();
        //
        inicialitzarBT(DISPOSITIU_DEFECTE);
        //
        inicializarAlertDialog();
    }

    private void inicialitzarFilesPermissions() {
        /*
        Sol·licita els permissos de READ i WRITE del dispositiu
         */
        FilesDao.sollicitarPermissos(this);
    }

    public void escribirMensajeThread(final String pStrMensaje){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!pStrMensaje.equals(""))
                            tvResponse.setText(formatDate2String() + pStrMensaje + tvResponse.getText().toString());
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event != null) {
            try {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (event.getEventTime() - event.getDownTime() < CLICK_DURATION) {
                            view.setY(0);
                            view.setX(0);
                            m_mpPlotterView.setxPosicion(0);
                            m_mpPlotterView.setyPosicion(0);
                            /*
                                JOYP;125;125    // NOM;VELOCIDAD M1;VELOCIDAD M2
                            */
                            String strMensaje = "JOYPAD" + CTE_SEPARADOR_FIC +
                                    (m_swPujarBaixar.isChecked() ? "DW" : "UP") + CTE_SEPARADOR_FIC +
                                    0 + CTE_SEPARADOR_FIC +
                                    0;
                            String strRes = m_cb.enviarMissatge(strMensaje);
                            escribirMensajeThread(strRes);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Controlar margenes
                        /*
                        y>-100 a 100
                        x>-100 a 100
                         */
                        float xx = event.getRawX() + dX;
                        float yy = event.getRawY() + dY;
                        if (xx < -MARGEN_MAX)
                            xx = -MARGEN_MAX;
                        if (xx > MARGEN_MAX)
                            xx = MARGEN_MAX;
                        if (yy < -MARGEN_MAX)
                            yy = -MARGEN_MAX;
                        if (yy > MARGEN_MAX)
                            yy = MARGEN_MAX;
                        view.animate()
                                .x(xx)
                                .y(yy)
                                .setDuration(0)
                                .start();
                        m_mpPlotterView.setxPosicion((int) xx);
                        m_mpPlotterView.setyPosicion((int) yy);
                        /*
                            JOYP;125;125    // NOM;VELOCIDAD M1;VELOCIDAD M2
                        */
                        String strMensaje = "JOYPAD" + CTE_SEPARADOR_FIC +
                                (m_swPujarBaixar.isChecked() ? "DW" : "UP") + CTE_SEPARADOR_FIC +
                                transformar2Motor(xx) + CTE_SEPARADOR_FIC +
                                transformar2Motor(yy);
                        String strRes = m_cb.enviarMissatge(strMensaje);
                        escribirMensajeThread(strRes);
                        //
                        break;
                    default:
                        return false;
                }
            } catch (Exception ex) {
            }
        }
        return true;
    }

    private int transformar2Motor(float pCoordenada) {
        float fMAXIMO = 255f;
        float fMAXIMO_LIM = 100f;
        return new Float(pCoordenada * fMAXIMO / fMAXIMO_LIM).intValue();
    }

    private void inicialitzarControlBT() {
        // Mantener la pantalla activa, siempre
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Orientación de la pantalla en vertical, siempre
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }
        //
        tvResponse = findViewById(R.id.tvMissatgeRec);
        tvResponse.setMovementMethod(new ScrollingMovementMethod());
        tvSend = findViewById(R.id.etMissatgeSend);
        m_swPujarBaixar = findViewById(R.id.swPujarBaixar);
        m_swPujarBaixar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String strMensaje = "PEN" + CTE_SEPARADOR_FIC +
                        (m_swPujarBaixar.isChecked() ? "DW" : "UP");    // Valores posibles: DW, UP, Nº de grados [0..180]
                String strRes = m_cb.enviarMissatge(strMensaje);
                escribirMensajeThread(strRes);
                m_mpPlotterView.setbEscriure(m_swPujarBaixar.isChecked());
            }
        });
        m_ivControl = findViewById(R.id.ivControl);
        m_ivControl.setOnTouchListener(this);
        //
        m_mpPlotterView = (PlotterView) findViewById(R.id.mpPerfil);
        m_mpPlotterView.setxPosicion(0);	// Posición X
        m_mpPlotterView.setyPosicion(0);	// Posición Y
        m_mpPlotterView.setbEscriure(false);// Escribir o no
        //
        Button btEnviar = findViewById(R.id.btSend);
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_cb != null) {
                    String str = tvSend.getText().toString();
                    String strRes = m_cb.enviarMissatge(str);
                    escribirMensajeThread(strRes);
                }
            }
        });
        //
        Button btEnviarFile = findViewById(R.id.btEnviarFile);
        btEnviarFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strFileName = m_spinnerFitxers.getSelectedItem().toString();
                //enviarFitxer2BT(m_spinnerFitxers.getItemAtPosition(position).toString());
                //enviarFitxer2BT(strFileName);
                enviarSimularFichero(strFileName);
            }
        });
        //
        if (m_cb == null && tvResponse != null)
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

    private DeviceBT cercarPropsDevice(ArrayList<DeviceBT> plistaDev, String pDeviceName) {
        for (DeviceBT devBT : plistaDev) {
            if (devBT.getName().equals(pDeviceName))
                return devBT;
        }
        return null;
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
        // Seleccionar Fitxers
        m_spinnerFitxers = (Spinner) findViewById(R.id.spFitxers);
        List<String> listFitxers = obtenirFileNames();
        listFitxers.add(0,getString(R.string.select_a_fitxer));
        ArrayAdapter<String> dataAdapterFitxers = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listFitxers);
        dataAdapterFitxers.setDropDownViewResource(android.R.layout.simple_spinner_item);
        m_spinnerFitxers.setAdapter(dataAdapterFitxers);
        //
        m_spinnerFitxers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //
            }
        });
    }

    private void enviarSimularFichero(String pFileName) {
        alertMensajeEnviarSimular(pFileName);
    }
    private void enviarFitxer2BT(String pFileName) {
        setbProcesFin(false);    // Sólo procesa una linea y cuando acaba, la siguiente
        arrLineas = FilesDao.llegirFitxerLinies(CTE_PATH_FITXERS + pFileName);
        if (arrLineas == null || arrLineas.size() == 0)
            return;
        // Inicializar datos
        bFin = false;
        bEsprimeraVez = true;
        iContador = 0;
        numLineasTotal = arrLineas.size();

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(numLineasTotal);
        new MyAsyncTask().execute(numLineasTotal);
    }
    private void alertMensajeEnviarSimular(final String pFileName) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.gestion_fichero))
            .setMessage(R.string.enviar_simular)
            .setNegativeButton(getString(R.string.simular_texto), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                  // SIMULAR
                    pintarFichero(pFileName);
                  }
            })
            .setPositiveButton(getString(R.string.enviar_texto), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    // ENVIAR
                    enviarFitxer2BT(pFileName);
                }
            }).show();
    }
    private void pintarFichero(String pFileName) {
        arrLineas = FilesDao.llegirFitxerLinies(CTE_PATH_FITXERS + pFileName);
        // Controlar margenes
        /*
            y>-100 a 100
            x>-100 a 100
         */
        m_mpPlotterView.iniArrPuntos();
        Punto[] puntos = new Punto[100];
        int iContador = 0;
        float xx = 0, xxAux = 0;
        float yy = 0, yyAux = 0;
        boolean UPDWN = true;   // Escribir en Blanco
        //
        for (String linea : arrLineas) {
            try {
                MensajePlot mpSalida = null;
                mpSalida = ControlCalculo.transformarMensaje2Pantalla(linea);
                xx = new Float(mpSalida.getMENSAJE_M1_ANALOG());
                yy = new Float(mpSalida.getMENSAJE_M2_ANALOG());
                if (xx < -MARGEN_MAX)
                    xx = -MARGEN_MAX;
                if (xx > MARGEN_MAX)
                    xx = MARGEN_MAX;
                if (yy < -MARGEN_MAX)
                    yy = -MARGEN_MAX;
                if (yy > MARGEN_MAX)
                    yy = MARGEN_MAX;
                //
                UPDWN = mpSalida.getMENSAJE_UP_DOWN().equals("DW");
                puntos[iContador++] = new Punto(10 + 10 + xxAux, 10 + 10 + yyAux, UPDWN);
                xxAux += xx;
                yyAux += yy;
            } catch (Exception ex) {
            }
        }
        puntos[iContador] = new Punto(10 + 10 + xxAux, 10 + 10 + yyAux, UPDWN);
        m_mpPlotterView.setArrPuntos(puntos);
    }

    class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {

            while (!bFin) {
                if (!isbProcesFin() && bEsprimeraVez) {
                    String linea = arrLineas.get(iContador);
                    try {
                        MensajePlot mpSalida = null;
                        mpSalida = ControlCalculo.transformarMensaje(linea);
                        if (mpSalida != null) {
                            timeInicial = new Date().getTime();                 // Tiempo inicial
                            timeOut = new Long(mpSalida.getMENSAJE_TIEMPO_EJECUCION());
                            String strError = comprobarErrores(mpSalida);
                            String strRes = "";
                            try {
                                if (!strError.equals("")) {
                                    strRes = " FICHERO " + mpSalida.getMENSAJE_NOM() + " " + strError + CTE_CAMBIO_LINEA_FIC;
                                } else {
                                    strRes = " FICHERO " + mpSalida.getMENSAJE_NOM() + " " + " VÁLIDO" + CTE_CAMBIO_LINEA_FIC;
                                }
                            } catch (Exception ex) {
                                System.err.println("Error en Thread: " + ex);
                            }
                            //
                            strRes = m_cb.enviarMissatge(mpSalida.toStringFile());
                            setRespuesta(strRes);
                            //
                            bEsprimeraVez = false;
                        }
                    } catch (Exception ex) {
                        System.err.println("Error: " + ex);
                        setbProcesFin(true);
                    }
                }
                timeActual = new Date().getTime();                 // Tiempo actual
                if (timeActual > timeInicial + timeOut + CTE_TIMEOUT_EXTRA) { // Se añade un TIEMPO EXTRA
                    // TIMEOUT acción finalizada
                    setbProcesFin(true);
                }
                if (isbProcesFin()) {
                    iContador++;
                    setbProcesFin(false);
                    bEsprimeraVez = true;
                }
                if (iContador >= arrLineas.size())
                    bFin = true;
                //
                publishProgress(iContador);
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            return " FICHERO " + "Fichero completado!" + CTE_CAMBIO_LINEA_FIC;
        }
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            txt.setText("");
            //
            if (!result.equals(""))
                tvResponse.setText(formatDate2String() + result + tvResponse.getText().toString());
        }
        @Override
        protected void onPreExecute() {
            txt.setText("En ejecución...");
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] < numLineasTotal)
                txt.setText("Ejecutando comando " +  (values[0] + 1) + " / " + arrLineas.size());
            progressBar.setProgress(values[0]);
            //
            if (!getRespuesta().equals("")) {
                tvResponse.setText(formatDate2String() + getRespuesta() + tvResponse.getText().toString());
                setRespuesta("");
            }
        }
    }

    private void inicializarAlertDialog() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt = (TextView) findViewById(R.id.output);
    }

    private String formatDate2String() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }

    private String comprobarErrores(MensajePlot pSalida) {
        String strError = "";
        if (!pSalida.getMENSAJE_RESULTADO_M1().equals("OK"))
            strError += "Error en la coordenada X";
        if (!pSalida.getMENSAJE_RESULTADO_M2().equals("OK")) {
            if (!strError.equals(""))
                strError += " - ";  // Guión entre mensajes de ERROR
            strError += "Error en la coordenada Y";
        }
        return strError;
    }

    private boolean conteDispositiuDefecte(List<String> p_lDevices, String pDeviceName) {
        for (String strDevice : p_lDevices) {
            if (strDevice.equals(pDeviceName))
                return true;
        }
        return false;
    }

    private ArrayList<String> obtenirFileNames() {
        //
        m_listaFiles = FilesDao.filesDirectori(CTE_PATH_FITXERS);
        Log.i("[FILES]","Nº Files: " + m_listaFiles.size());
        //
        return FilesDao.convertFiles2NamesString(m_listaFiles);
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
