package es.xuan.cacacontroller;

import static es.xuan.cacacontroller.view.Dpad.CENTER;
import static es.xuan.cacacontroller.view.Dpad.DOWN;
import static es.xuan.cacacontroller.view.Dpad.UP;
import static es.xuan.cacacontroller.view.Dpad.LEFT;
import static es.xuan.cacacontroller.view.Dpad.RIGHT;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import es.xuan.cacacontroller.view.BotonesMandos;
import es.xuan.cacacontroller.view.Dpad;
import es.xuan.cacacontroller.view.DrawViewLine;

public class ControlMandoJPActivity extends AppCompatActivity {

    private DrawViewLine mDrawViewLine;
    private Dpad dpad = null;
    private static BotonesMandos mBotonesMando = null;
    private ImageView mIntermitentIzq = null;
    private ImageView mIntermitentDer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_mando_jp);
        //
        mantenerPantallaEncendida();
        //
        inicializar();
    }

    private void dibujarVista() {
        if (mDrawViewLine == null)
            mDrawViewLine = (DrawViewLine)findViewById(R.id.viewMedioCirculo);
        //  Acelerador - Freno
        mDrawViewLine.setAnguloRotacion(mBotonesMando.getAXIS_RTRIGGER() - mBotonesMando.getAXIS_LTRIGGER());
        //  Intermitente izquierdo
        if (mBotonesMando.isBUTTON_L1())
            mIntermitentIzq.setVisibility(mIntermitentIzq.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        //  Intermitente derecho
        if (mBotonesMando.isBUTTON_R1())
            mIntermitentDer.setVisibility(mIntermitentDer.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
    }

    private void inicializar() {
        //
        mBotonesMando = new BotonesMandos();
        //
        dpad = new Dpad();
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
        mIntermitentIzq.setVisibility(View.INVISIBLE);
        mIntermitentDer = (ImageView)findViewById(R.id.ivIntermitenteDer);
        mIntermitentDer.setVisibility(View.INVISIBLE);
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
            switch (press) {
                case LEFT:
                    // Do something for LEFT direction press
                    mBotonesMando.setDPAD_LEFT(LEFT);
                case RIGHT:
                    // Do something for RIGHT direction press
                    mBotonesMando.setDPAD_RIGHT(RIGHT);
                case UP:
                    // Do something for UP direction press
                    mBotonesMando.setDPAD_UP(UP);
                case DOWN:
                    // Do something for UP direction press
                    mBotonesMando.setDPAD_DOWN(DOWN);
                case CENTER:
                    // Do something for UP direction press
                    mBotonesMando.setDPAD_CENTER(CENTER);
                default:
                    break;
            }
            if (press > -1) {
                dibujarVista();
                return true;
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
            dibujarVista();
            return true;
        }
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
}