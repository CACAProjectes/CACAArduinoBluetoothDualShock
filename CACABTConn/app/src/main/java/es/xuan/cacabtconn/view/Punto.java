package es.xuan.cacabtconn.view;

import java.io.Serializable;

public class Punto implements Serializable, Cloneable {
    float x;
    float y;
    boolean color;

    @Override
    public Punto clone() throws CloneNotSupportedException {
        return (Punto)super.clone();
    }

    public Punto(float x, float y, boolean pColor) {
        this.x = x;
        this.y = y;
        this.color = pColor;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
