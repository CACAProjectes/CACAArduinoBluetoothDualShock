package es.xuan.cacacontroller.view;

import java.io.Serializable;

public class BotonesMandos implements Serializable {

    // Botones
    private boolean BUTTON_X = false;
    private boolean BUTTON_A = false;
    private boolean BUTTON_Y = false;
    private boolean BUTTON_B = false;
    private boolean BUTTON_R1 = false;
    private boolean BUTTON_L1 = false;
    // Botones en cruz
    private float AXIS_HAT_X = 0;
    private float AXIS_HAT_Y = 0;
    private float DPAD_UP = 0;
    private float DPAD_DOWN = 0;
    private float DPAD_LEFT = 0;
    private float DPAD_RIGHT = 0;
    private float DPAD_CENTER = 0;
    //Joystick izquierdo
    private float AXIS_X = 0;
    private float AXIS_Y = 0;
    private boolean BUTTON_THUMBL = false;
    //Joystick derecho
    private float AXIS_Z = 0;
    private float AXIS_RZ = 0;
    private boolean BUTTON_THUMBR = false;
    // Acelerador
    private float AXIS_RTRIGGER = 0;
    private float AXIS_THROTTLE = 0;
    // Freno
    private float AXIS_LTRIGGER = 0;
    private float AXIS_BRAKE = 0;
    //

    public boolean isBUTTON_X() {
        return BUTTON_X;
    }

    public void setBUTTON_X(boolean BUTTON_X) {
        this.BUTTON_X = BUTTON_X;
    }

    public boolean isBUTTON_A() {
        return BUTTON_A;
    }

    public void setBUTTON_A(boolean BUTTON_A) {
        this.BUTTON_A = BUTTON_A;
    }

    public boolean isBUTTON_Y() {
        return BUTTON_Y;
    }

    public void setBUTTON_Y(boolean BUTTON_Y) {
        this.BUTTON_Y = BUTTON_Y;
    }

    public boolean isBUTTON_B() {
        return BUTTON_B;
    }

    public void setBUTTON_B(boolean BUTTON_B) {
        this.BUTTON_B = BUTTON_B;
    }

    public boolean isBUTTON_R1() {
        return BUTTON_R1;
    }

    public void setBUTTON_R1(boolean BUTTON_R1) {
        this.BUTTON_R1 = BUTTON_R1;
    }

    public boolean isBUTTON_L1() {
        return BUTTON_L1;
    }

    public void setBUTTON_L1(boolean BUTTON_L1) {
        this.BUTTON_L1 = BUTTON_L1;
    }

    public float getAXIS_HAT_X() {
        return AXIS_HAT_X;
    }

    public void setAXIS_HAT_X(float AXIS_HAT_X) {
        this.AXIS_HAT_X = AXIS_HAT_X;
    }

    public float getAXIS_HAT_Y() {
        return AXIS_HAT_Y;
    }

    public void setAXIS_HAT_Y(float AXIS_HAT_Y) {
        this.AXIS_HAT_Y = AXIS_HAT_Y;
    }

    public float getDPAD_UP() {
        return DPAD_UP;
    }

    public void setDPAD_UP(float DPAD_UP) {
        this.DPAD_UP = DPAD_UP;
    }

    public float getDPAD_DOWN() {
        return DPAD_DOWN;
    }

    public void setDPAD_DOWN(float DPAD_DOWN) {
        this.DPAD_DOWN = DPAD_DOWN;
    }

    public float getDPAD_LEFT() {
        return DPAD_LEFT;
    }

    public void setDPAD_LEFT(float DPAD_LEFT) {
        this.DPAD_LEFT = DPAD_LEFT;
    }

    public float getDPAD_RIGHT() {
        return DPAD_RIGHT;
    }

    public void setDPAD_RIGHT(float DPAD_RIGHT) {
        this.DPAD_RIGHT = DPAD_RIGHT;
    }

    public float getAXIS_X() {
        return AXIS_X;
    }

    public void setAXIS_X(float AXIS_X) {
        this.AXIS_X = AXIS_X;
    }

    public float getAXIS_Y() {
        return AXIS_Y;
    }

    public void setAXIS_Y(float AXIS_Y) {
        this.AXIS_Y = AXIS_Y;
    }

    public boolean isBUTTON_THUMBL() {
        return BUTTON_THUMBL;
    }

    public void setBUTTON_THUMBL(boolean BUTTON_THUMBL) {
        this.BUTTON_THUMBL = BUTTON_THUMBL;
    }

    public float getAXIS_Z() {
        return AXIS_Z;
    }

    public void setAXIS_Z(float AXIS_Z) {
        this.AXIS_Z = AXIS_Z;
    }

    public float getAXIS_RZ() {
        return AXIS_RZ;
    }

    public void setAXIS_RZ(float AXIS_RZ) {
        this.AXIS_RZ = AXIS_RZ;
    }

    public boolean isBUTTON_THUMBR() {
        return BUTTON_THUMBR;
    }

    public void setBUTTON_THUMBR(boolean BUTTON_THUMBR) {
        this.BUTTON_THUMBR = BUTTON_THUMBR;
    }

    public float getAXIS_RTRIGGER() {
        return AXIS_RTRIGGER;
    }

    public void setAXIS_RTRIGGER(float AXIS_RTRIGGER) {
        this.AXIS_RTRIGGER = AXIS_RTRIGGER;
    }

    public float getAXIS_THROTTLE() {
        return AXIS_THROTTLE;
    }

    public void setAXIS_THROTTLE(float AXIS_THROTTLE) {
        this.AXIS_THROTTLE = AXIS_THROTTLE;
    }

    public float getAXIS_LTRIGGER() {
        return AXIS_LTRIGGER;
    }

    public void setAXIS_LTRIGGER(float AXIS_LTRIGGER) {
        this.AXIS_LTRIGGER = AXIS_LTRIGGER;
    }

    public float getAXIS_BRAKE() {
        return AXIS_BRAKE;
    }

    public void setAXIS_BRAKE(float AXIS_BRAKE) {
        this.AXIS_BRAKE = AXIS_BRAKE;
    }

    public void setDPAD_CENTER(float DPAD_CENTER) {
        this.DPAD_CENTER = DPAD_CENTER;
    }
    public float getDPAD_CENTER() {
        return DPAD_CENTER;
    }
}
