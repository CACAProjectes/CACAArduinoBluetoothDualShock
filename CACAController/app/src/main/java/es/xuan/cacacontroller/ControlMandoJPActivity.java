package es.xuan.cacacontroller;

import static es.xuan.cacacontroller.view.Dpad.CENTER;
import static es.xuan.cacacontroller.view.Dpad.DOWN;
import static es.xuan.cacacontroller.view.Dpad.UP;
import static es.xuan.cacacontroller.view.Dpad.LEFT;
import static es.xuan.cacacontroller.view.Dpad.RIGHT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import es.xuan.cacacontroller.dev.ControlBluetooth;
import es.xuan.cacacontroller.dev.DeviceBT;
import es.xuan.cacacontroller.utils.Utils;
import es.xuan.cacacontroller.view.BotonesMandos;
import es.xuan.cacacontroller.view.Dpad;
import es.xuan.cacacontroller.view.DrawViewLine;

public class ControlMandoJPActivity extends AppCompatActivity {

    private DrawViewLine mDrawViewLine;
    private Dpad dpad = null;
    private static int CTE_DPAD_NULL = -1;
    private static BotonesMandos mBotonesMando = null;
    private ImageView mIntermitentIzq = null;
    private ImageView mIntermitentDer = null;
    private ImageView mIntermitentBackIzq = null;
    private ImageView mIntermitentBackDer = null;
    private ImageView mSirena = null;
    private ImageView mSirenaBack = null;
    private ImageView mLucesEmergencia = null;
    private ImageView mLucesEmergenciaBack = null;
    private ImageView mLuces = null;
    private ImageView mLucesBack = null;
    private TextView mGradosDireccion = null;
    private float mAnguloDireccionAnt = 0f;
    //
    private static final long CTE_VIBRATION_MS = 50;
    private Vibrator mVibr = null;
    //
    private static final String CTE_NAME_H3 = "H3";
    private ControlBluetooth m_cb = null;
    private ArrayList<DeviceBT> m_listaDev = null;
    private String[] mMensajeBT = new String[20];
    private long mTimeActual = 0;
    private long mTimeAnterior = 0;
    private String mStrContAnt = "";
    String mConcatenarControl = "";

    /*
      0 - iServoAcc = pDatosSerial.substring(0,3).toInt();
      //
      1 - iServoDir = pDatosSerial.substring(3,6).toInt();
      //
      2 - iServoSen = pDatosSerial.substring(6,9).toInt();
      //
      3 - iServoBase = pDatosSerial.substring(9,12).toInt();
      //
      4 - iServoBrazo = pDatosSerial.substring(12,15).toInt();
      //
      5 - iLuz1 = pDatosSerial.substring(15,16).toInt();    Luces de posición
      6 - iLuz2 = pDatosSerial.substring(16,17).toInt();    Sirena azul
      7 - iLuz3 = pDatosSerial.substring(17,18).toInt();    Sirena rojo
      8 - iLuz4 = pDatosSerial.substring(18,19).toInt();    Intermitente izquierdo
      9 - iLuz5 = pDatosSerial.substring(19,20).toInt();    Intermitente derecho
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_mando_jp);
        //
        mantenerPantallaEncendida();
        //
        inicializar();
        //
        inicialitzarBT(CTE_NAME_H3);
    }

    private void enviarMensaje(String pMensaje) {
        Log.d("[BLUETOOTH-H3]","Mensaje: " + pMensaje);
        String strRes = getString(R.string.desconectado);
        if (m_cb != null) {
            strRes = m_cb.enviarMissatge(pMensaje);
        }
    }

    private void rotarRobot() {
        // 0º a 180º -> -1 a 1
        float fValorBase = mBotonesMando.getAXIS_Z();       // -1 a 1
        float fValorBrazo = mBotonesMando.getAXIS_RZ();     // -1 a 1
        //  -1 to 1 -> 0 to 100
        mMensajeBT[3] = "" + (100 - new Float((fValorBase + 1) * 50).intValue());
        mMensajeBT[4] = "" + (100 - new Float((fValorBrazo + 1) * 50).intValue());
    }

    private void rotarRuedas() {
        // -30º a 30º -> -1 a 1
        float fValor = mBotonesMando.getAXIS_X();   // -1 a 1
        float pAnguloIni = mAnguloDireccionAnt;
        float pAnguloFin = (float)Utils.convert01ToPorcentajeDireccion(fValor);
        //
        ImageView imageView1 = (ImageView)findViewById(R.id.ivC1);
        rotarRuedas(imageView1, pAnguloIni, pAnguloFin);
        ImageView imageView2 = (ImageView)findViewById(R.id.ivC3);
        rotarRuedas(imageView2, pAnguloIni, pAnguloFin);
        //
        mGradosDireccion.setText("" + (int)pAnguloFin + "º");
        //
        mAnguloDireccionAnt = pAnguloFin;
        //  -1 to 1 -> 0 to 100
        int velocidad = (100 - new Float((fValor + 1) * 50).intValue());
        velocidad = (velocidad / 5) * 5;
        mMensajeBT[1] = "" + velocidad;
    }

    private void rotarRuedas(ImageView pImageView, float pAnguloIni, float pAnguloFin) {
        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(pAnguloIni, pAnguloFin,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(500);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);

        pImageView.startAnimation(animSet);
    }
    private void acelerarFrenar() {
        //  Acelerador - Freno
        if (mDrawViewLine == null)
            mDrawViewLine = (DrawViewLine)findViewById(R.id.viewMedioCirculo);
        mDrawViewLine.setAnguloRotacion(mBotonesMando.getAXIS_RTRIGGER() - mBotonesMando.getAXIS_LTRIGGER());
        //  Aceleración + frenado
        int velocidad = new Float(Math.abs(mBotonesMando.getAXIS_RTRIGGER() - mBotonesMando.getAXIS_LTRIGGER()) * 100).intValue();
        velocidad = (velocidad / 5) * 5;
        mMensajeBT[0] = "" + velocidad;
        // Sentido de la marcha
        if (mBotonesMando.getAXIS_RTRIGGER() > (mBotonesMando.getAXIS_LTRIGGER() - 0.20))
            mMensajeBT[2] = "0";
        else
            mMensajeBT[2] = "100";

    }
    private void dibujarVista() {
        // Inicializar valors
        mMensajeBT[5] = "9";    // Sin valor
        mMensajeBT[6] = "9";    // Sin valor
        mMensajeBT[7] = "9";    // Sin valor
        mMensajeBT[8] = "9";    // Sin valor
        mMensajeBT[9] = "9";    // Sin valor
        //  Dirección ruedas
        rotarRuedas();
        //  Acelerador - Freno
        acelerarFrenar();
        //  Robot - Base - Brazo
        rotarRobot();
        //  Intermitente izquierdo
        if (mBotonesMando.isBUTTON_L1()) {
            if (mIntermitentIzq.getVisibility() == View.VISIBLE) {
                mIntermitentIzq.setVisibility(View.GONE);
                mIntermitentBackIzq.setVisibility(View.VISIBLE);
                mMensajeBT[8] = "0";
            }
            else {
                mIntermitentIzq.setVisibility(View.VISIBLE);
                mIntermitentBackIzq.setVisibility(View.GONE);
                mMensajeBT[8] = "1";
            }
        }
        //  Intermitente derecho
        if (mBotonesMando.isBUTTON_R1()) {
            if (mIntermitentDer.getVisibility() == View.VISIBLE) {
                mIntermitentDer.setVisibility(View.GONE);
                mIntermitentBackDer.setVisibility(View.VISIBLE);
                mMensajeBT[9] = "0";
            }
            else {
                mIntermitentDer.setVisibility(View.VISIBLE);
                mIntermitentBackDer.setVisibility(View.GONE);
                mMensajeBT[9] = "1";
            }
        }
        //  Luces de emergencia
        if (mBotonesMando.getDPAD_UP() == UP) {
            if (mSirena.getVisibility() == View.VISIBLE) {
                mSirena.setVisibility(View.GONE);
                mSirenaBack.setVisibility(View.VISIBLE);
            }
            else {
                mSirena.setVisibility(View.VISIBLE);
                mSirenaBack.setVisibility(View.GONE);
            }
        }
        //  Sirena
        if (mBotonesMando.getDPAD_DOWN() == DOWN) {
            if (mLucesEmergencia.getVisibility() == View.VISIBLE) {
                mLucesEmergencia.setVisibility(View.GONE);
                mLucesEmergenciaBack.setVisibility(View.VISIBLE);
            }
            else {
                mLucesEmergencia.setVisibility(View.VISIBLE);
                mLucesEmergenciaBack.setVisibility(View.GONE);
            }
        }
        //  Luces
        if (mBotonesMando.getDPAD_LEFT() == LEFT) {
            if (mLuces.getVisibility() == View.VISIBLE) {
                mLuces.setVisibility(View.GONE);
                mLucesBack.setVisibility(View.VISIBLE);
                mMensajeBT[5] = "0";
            }
            else {
                mLuces.setVisibility(View.VISIBLE);
                mLucesBack.setVisibility(View.GONE);
                mMensajeBT[5] = "1";
            }
        }
        mConcatenarControl = concatenarControles();
        mTimeActual = System.currentTimeMillis();
        if (mTimeActual - mTimeAnterior > 100) {
            if (!mConcatenarControl.equals(mStrContAnt)) {
                enviarMensaje("A" + mConcatenarControl);
                mStrContAnt = mConcatenarControl;
            }
            mTimeAnterior = mTimeActual;
        }
        else {  // Comprobar si está parado
            if (mConcatenarControl.startsWith("000")) {
                enviarMensaje("A" + mConcatenarControl);
                Log.d("[BLUETOOTH]","Parar actuadores!");
            }
        }
    }

    private String concatenarControles() {
        return Utils.completarCeros(mMensajeBT[0], 3) +
                Utils.completarCeros(mMensajeBT[1], 3) +
                Utils.completarCeros(mMensajeBT[2], 3) +
                Utils.completarCeros(mMensajeBT[3], 3) +
                Utils.completarCeros(mMensajeBT[4], 3) +
                mMensajeBT[5] +
                mMensajeBT[6] +
                mMensajeBT[7] +
                mMensajeBT[8] +
                mMensajeBT[9];
    }

    private void inicializar() {
        //
        mVibr = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //
        ImageView ivCerrar = (ImageView)findViewById(R.id.ivD3Cerrar);
        ivCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Utils.vibrar(mVibr, CTE_VIBRATION_MS);
                //
                finish();
            }
        });
        //
        mBotonesMando = new BotonesMandos();
        //
        dpad = new Dpad();
        mBotonesMando.setDPAD_UP(CTE_DPAD_NULL);
        mBotonesMando.setDPAD_LEFT(CTE_DPAD_NULL);
        mBotonesMando.setDPAD_RIGHT(CTE_DPAD_NULL);
        mBotonesMando.setDPAD_DOWN(CTE_DPAD_NULL);
        mBotonesMando.setDPAD_CENTER(CTE_DPAD_NULL);
        //
        escribirGameControllerIds();
        // Cargar el layout despues de arrancar la apliación
        final LinearLayout layout = (LinearLayout) findViewById(R.id.llCirculo);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dibujarVista();
            }
        });
        mIntermitentIzq = (ImageView)findViewById(R.id.ivIntermitenteIzq);
        mIntermitentIzq.setVisibility(View.GONE);
        mIntermitentBackIzq = (ImageView)findViewById(R.id.ivIntermitenteIzqBack);
        mIntermitentBackIzq.setVisibility(View.VISIBLE);
        //
        mIntermitentDer = (ImageView)findViewById(R.id.ivIntermitenteDer);
        mIntermitentDer.setVisibility(View.GONE);
        mIntermitentBackDer = (ImageView)findViewById(R.id.ivIntermitenteDerBack);
        mIntermitentBackDer.setVisibility(View.VISIBLE);
        //
        mSirena = (ImageView)findViewById(R.id.ivSirena);
        mSirena.setVisibility(View.GONE);
        mSirenaBack = (ImageView)findViewById(R.id.ivSirenaBack);
        mSirenaBack.setVisibility(View.VISIBLE);
        //
        mLucesEmergencia = (ImageView)findViewById(R.id.ivD2);
        mLucesEmergencia.setVisibility(View.GONE);
        mLucesEmergenciaBack = (ImageView)findViewById(R.id.ivD2Back);
        mLucesEmergenciaBack.setVisibility(View.VISIBLE);
        //
        mLuces = (ImageView)findViewById(R.id.ivLuces);
        mLuces.setVisibility(View.GONE);
        mLucesBack = (ImageView)findViewById(R.id.ivLucesBack);
        mLucesBack.setVisibility(View.VISIBLE);
        //
        mGradosDireccion = (TextView)findViewById(R.id.tvDireccionGrados);
    }

    private void escribirGameControllerIds() {
        ArrayList<Integer> listGCIds = getGameControllerIds();
        for (Integer element : listGCIds) {
            System.out.println("Game Controller Id: " + element.toString());
        }
    }

    private void mantenerPantallaEncendida() {
        /* This code together with the one in onDestroy()
         * will make the screen be always on until this Activity gets destroyed.
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    /*
        Eventos Controller JPAD y JOYSTICK
     */
    public ArrayList<Integer> getGameControllerIds() {
        ArrayList<Integer> gameControllerDeviceIds = new ArrayList<Integer>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }
        return gameControllerDeviceIds;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = false;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                //
                if (keyCode == KeyEvent.KEYCODE_BUTTON_A)
                    mBotonesMando.setBUTTON_A(true);    // Cruz presionada
                if (keyCode == KeyEvent.KEYCODE_BUTTON_B)
                    mBotonesMando.setBUTTON_B(true);    // Circulo presionada
                if (keyCode == KeyEvent.KEYCODE_BUTTON_X)
                    mBotonesMando.setBUTTON_X(true);    // Cuadrado presionada
                if (keyCode == KeyEvent.KEYCODE_BUTTON_Y)
                    mBotonesMando.setBUTTON_Y(true);    // Triangulo presionada
                //
                if (keyCode == KeyEvent.KEYCODE_BUTTON_THUMBL)
                    mBotonesMando.setBUTTON_THUMBL(true);    // Joystick izquierdo presionada
                if (keyCode == KeyEvent.KEYCODE_BUTTON_THUMBR)
                    mBotonesMando.setBUTTON_THUMBR(true);    // Joystick derecho presionada
                //
                if (keyCode == KeyEvent.KEYCODE_BUTTON_L1)
                    mBotonesMando.setBUTTON_L1(true);    // Botón delantero izquierdo
                if (keyCode == KeyEvent.KEYCODE_BUTTON_R1)
                    mBotonesMando.setBUTTON_R1(true);    // Botón delantero derecho
            }
            dibujarVista();
            if (handled) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = false;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                //
                if (keyCode == KeyEvent.KEYCODE_BUTTON_A)
                    mBotonesMando.setBUTTON_A(false);    // Cruz presionada
                if (keyCode == KeyEvent.KEYCODE_BUTTON_B)
                    mBotonesMando.setBUTTON_B(false);    // Circulo presionada
                if (keyCode == KeyEvent.KEYCODE_BUTTON_X)
                    mBotonesMando.setBUTTON_X(false);    // Cuadrado presionada
                if (keyCode == KeyEvent.KEYCODE_BUTTON_Y)
                    mBotonesMando.setBUTTON_Y(false);    // Triangulo presionada
                //
                if (keyCode == KeyEvent.KEYCODE_BUTTON_THUMBL)
                    mBotonesMando.setBUTTON_THUMBL(false);    // Joystick izquierdo presionada
                if (keyCode == KeyEvent.KEYCODE_BUTTON_THUMBR)
                    mBotonesMando.setBUTTON_THUMBR(false);    // Joystick derecho presionada
                //
                if (keyCode == KeyEvent.KEYCODE_BUTTON_L1)
                    mBotonesMando.setBUTTON_L1(false);    // Botón delantero izquierdo
                if (keyCode == KeyEvent.KEYCODE_BUTTON_R1)
                    mBotonesMando.setBUTTON_R1(false);    // Botón delantero derecho
            }
            dibujarVista();
            if (handled) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        // Check if this event if from a D-pad and process accordingly.
        if (dpad.isDpadDevice(event)) {
            int press = dpad.getDirectionPressed(event);
            if (press == -1) {
                // Key UP
                if (mBotonesMando.getDPAD_LEFT() != CTE_DPAD_NULL)
                    mBotonesMando.setDPAD_LEFT(CTE_DPAD_NULL);
                if (mBotonesMando.getDPAD_RIGHT() != CTE_DPAD_NULL)
                    mBotonesMando.setDPAD_RIGHT(CTE_DPAD_NULL);
                if (mBotonesMando.getDPAD_DOWN() != CTE_DPAD_NULL)
                    mBotonesMando.setDPAD_DOWN(CTE_DPAD_NULL);
                if (mBotonesMando.getDPAD_UP() != CTE_DPAD_NULL)
                    mBotonesMando.setDPAD_UP(CTE_DPAD_NULL);
                if (mBotonesMando.getDPAD_CENTER() != CTE_DPAD_NULL)
                    mBotonesMando.setDPAD_CENTER(CTE_DPAD_NULL);
            }
            else {
                // Key DOWN
                switch (press) {
                    case LEFT:
                        // Do something for LEFT direction press
                        mBotonesMando.setDPAD_LEFT(LEFT);
                        break;
                    case RIGHT:
                        // Do something for RIGHT direction press
                        mBotonesMando.setDPAD_RIGHT(RIGHT);
                        break;
                    case UP:
                        // Do something for UP direction press
                        mBotonesMando.setDPAD_UP(UP);
                        break;
                    case DOWN:
                        // Do something for UP direction press
                        mBotonesMando.setDPAD_DOWN(DOWN);
                        break;
                    case CENTER:
                        // Do something for UP direction press
                        mBotonesMando.setDPAD_CENTER(CENTER);
                        break;
                    default:
                        break;
                }
                //dibujarVista();
                //return true;
            }
        }
        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {
            // Process all historical movement samples in the batch
            final int historySize = event.getHistorySize();
            // Process the movements starting from the
            // earliest historical position in the batch
            for (int i = 0; i < historySize; i++) {
                // Process the event at historical position i
                processJoystickInput(event, i);
            }
            // Process the current movement sample in the batch (position -1)
            processJoystickInput(event, -1);
            //dibujarVista();
            //return true;
        }
        dibujarVista();
        return super.onGenericMotionEvent(event);
    }


    private void processJoystickInput(MotionEvent event,
                                      int historyPos) {

        InputDevice mInputDevice = event.getDevice();

        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float lx = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_X, historyPos);
        mBotonesMando.setAXIS_X(lx);

        float rx = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_Z, historyPos);
        mBotonesMando.setAXIS_Z(rx);

        float ly = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_Y, historyPos);
        mBotonesMando.setAXIS_Y(ly);

        float ry = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_RZ, historyPos);
        mBotonesMando.setAXIS_RZ(ry);

        float rTR = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_RTRIGGER, historyPos);
        mBotonesMando.setAXIS_RTRIGGER(rTR);
        float lTR = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_LTRIGGER, historyPos);
        mBotonesMando.setAXIS_LTRIGGER(lTR);
        float thr = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_THROTTLE, historyPos);
        mBotonesMando.setAXIS_THROTTLE(thr);
        float bra = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_BRAKE, historyPos);
        mBotonesMando.setAXIS_BRAKE(bra);
    }

    private static float getCenteredAxis(MotionEvent event,
                                         InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range =
                device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value =
                    historyPos < 0 ? event.getAxisValue(axis) :
                            event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

    private void inicialitzarBT(String p_strDispName) {
        //
        if (m_cb == null)
            m_cb = new ControlBluetooth(this);
        //
        m_listaDev = m_cb.listDevicesBT();
        //
        DeviceBT devBT = cercarPropsDevice(m_listaDev, p_strDispName);
        if (devBT != null)
            m_cb.inicialitzarBT(devBT.getMAC(), devBT.getUUID());
        else
            m_cb = null;
    }

    private DeviceBT cercarPropsDevice(ArrayList<DeviceBT> plistaDev, String pDeviceName) {
        for (DeviceBT devBT : plistaDev) {
            if (devBT.getName().equals(pDeviceName))
                return devBT;
        }
        return null;
    }
}