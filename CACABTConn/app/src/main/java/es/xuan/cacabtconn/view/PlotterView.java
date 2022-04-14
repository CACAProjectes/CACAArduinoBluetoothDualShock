package es.xuan.cacabtconn.view;

import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
/**
 * Created by jcamposp on 18/11/2016.
 */
public class PlotterView extends View {
    private static final int CTE_MAX_ARRAY_PUNTOS = 100;
    private Paint drawPaint;
    int xPosicion = 200;
    int yPosicion = 200;
    boolean bEscriure = false;
    Punto[] m_arrPuntos = new Punto[CTE_MAX_ARRAY_PUNTOS];
    int m_iContadorGlobal = 0;
    float m_xPos = 0;
    float m_yPos = 0;

    public Punto[] getArrPuntos() {
        return m_arrPuntos;
    }

    public void setArrPuntos(Punto[] m_arrPuntos) {
        this.m_arrPuntos = m_arrPuntos;
    }

    public boolean isbEscriure() {
        return bEscriure;
    }

    public void setbEscriure(boolean bEscriure) {
        this.bEscriure = bEscriure;
    }

    public int getxPosicion() {
        return xPosicion;
    }

    public void setxPosicion(int xPosicion) {
        this.xPosicion = xPosicion;
    }

    public int getyPosicion() {
        return yPosicion;
    }

    public void setyPosicion(int yPosicion) {
        this.yPosicion = yPosicion;
    }

    //
    public PlotterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
        //arrPuntos = new ArrayList<Punto>();
    }
    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(0);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }
    // Draws the path created during the touch events
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPaint=new Paint();
        // Lineas verticales
        drawPaint.setColor(Color.WHITE);
        canvas.drawRect(20,20,40,580, drawPaint);
        canvas.drawRect(550,20,570,580, drawPaint);
        /*
            yEscala = 560 / 200
            y = yEscala * yPosicion + Pto. Medio ->
         */
        float escala = (float)520 /(float)200;
        float yPos = escala * (float)getyPosicion() +  (float)560 / (float)2;
        escala = (float)450 /(float)200;
        float xPos = escala * (float)getxPosicion() + (float)560 / (float)2;
        //  Lista de puntos - recorrido
        //arrPuntos.add(new Punto(10 + 10 + xPos, 10 + 10 + yPos, bEscriure));
        if (xPos != m_xPos || yPos != m_yPos)
            m_arrPuntos[(m_iContadorGlobal++) % CTE_MAX_ARRAY_PUNTOS] = new Punto(10 + 10 + xPos, 10 + 10 + yPos, isbEscriure());
        m_xPos = xPos;
        m_yPos = yPos;
        //
        escribirPuntos(canvas);
        // Linea horizontal
        drawPaint.setColor(Color.YELLOW);
        canvas.drawRect(10, 10 + (int)yPos,580, 30 + (int)yPos, drawPaint);
        // Punt llapis
        if  (isbEscriure())
            drawPaint.setColor(Color.GREEN);
        else
            drawPaint.setColor(Color.RED);
        canvas.drawCircle(10 + 10 + xPos,  10 + 10 + yPos, 20, drawPaint);
        //
        refrescarDraw();
    }
    /*
    private void escribirPuntos(Canvas pCanvas) {
        int iContador = 0;
        Punto pAux = null;
        for (Punto punto : m_arrPuntos) {
            if (punto == null)
                break;
            if (iContador++ > 0) {
                if  (punto.color)
                    drawPaint.setColor(Color.WHITE);
                else
                    drawPaint.setColor(Color.BLACK);
                pCanvas.drawLine(pAux.getX(), pAux.getY(), punto.getX(), punto.getY(), drawPaint);
            }
            try {
                pAux = punto.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }
    */
    public void escribirPuntos(Canvas pCanvas) {
        Punto pAux = null;
        float yPosAux = 0;
        float xPosAux = 0;
        for (Punto punto : m_arrPuntos) {
            if (punto == null)
                break;
            float escala = (float)520 /(float)200;
            float yPos = escala * (float)punto.getY() +  (float)560 / (float)2;
            escala = (float)450 /(float)200;
            float xPos = escala * (float)punto.getX() + (float)560 / (float)2;
            //
            if (yPosAux == 0 && xPosAux == 0) {
                yPosAux = yPos;
                xPosAux = xPos;
            }
            if  (punto.color)
                drawPaint.setColor(Color.WHITE);
            else
                drawPaint.setColor(Color.BLACK);
            pCanvas.drawLine(xPosAux, yPosAux, xPos, yPos, drawPaint);
            //
            yPosAux = yPos;
            xPosAux = xPos;
        }
    }

    private void refrescarDraw() {
        postInvalidate(); // Indicate view should be redrawn
    }

    public void iniArrPuntos() {
        m_arrPuntos = new Punto[CTE_MAX_ARRAY_PUNTOS];
    }
}

