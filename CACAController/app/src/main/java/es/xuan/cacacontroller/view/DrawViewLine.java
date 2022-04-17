package es.xuan.cacacontroller.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import es.xuan.cacacontroller.ControlMandoJPActivity;
import es.xuan.cacacontroller.utils.Utils;

public class DrawViewLine extends View {
    private Paint paint = new Paint();
    private Paint paintFondo = new Paint();
    private Paint paintFondoAtencion = new Paint();
    private static final double CTE_X1 = 767;
    private static final double CTE_Y1 = 310;
    private static double CTE_RADIO = 300;
    private static double CTE_RADIO_PEQ = 10;
    private static final float CTE_GROSOR_ANCHO = 10f;
    private static final float CTE_GROSOR_FINO = 2f;
    private double anguloRotacion;
    private static final int CTE_COLOR_LINIA = Color.BLACK;
    private static final int CTE_COLOR_LINIA_FONDO = Color.GRAY;
    private static final int CTE_COLOR_TEXTO_ATENCION = Color.RED;
    private static final float CTE_TEXT_SIZE = 48f;
    private static final float CTE_MARGEN_INFERIOR = 50f;
    private static final float CTE_MITAD = 3f;

    private void init() {
        Typeface typeface = Typeface.DEFAULT_BOLD;
        // Aguja
        paint.setColor(CTE_COLOR_LINIA);
        paint.setStrokeWidth(CTE_GROSOR_ANCHO);
        paint.setTextSize(CTE_TEXT_SIZE);
        paint.setTypeface(typeface);
        // Coordenadas
        paintFondo.setColor(CTE_COLOR_LINIA_FONDO);
        paintFondo.setStrokeWidth(CTE_GROSOR_FINO);
        paintFondo.setTextSize(CTE_TEXT_SIZE);
        // Marcha atrás
        paintFondoAtencion.setTextSize(CTE_TEXT_SIZE);
        paintFondoAtencion.setColor(CTE_COLOR_TEXTO_ATENCION);
        paintFondoAtencion.setStrokeWidth(CTE_GROSOR_ANCHO);
        paintFondoAtencion.setTypeface(typeface);
    }

    public DrawViewLine(Context context) {
        super(context);
        init();
    }

    public DrawViewLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawViewLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public double getAnguloRotacion() {
        // En radianes -PI y PI
        return anguloRotacion;
    }

    public void setAnguloRotacion(double anguloRotacion) {
        // En radianes -PI y PI
        this.anguloRotacion = anguloRotacion;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //
        double vX2 = CTE_RADIO * Math.cos(getAnguloRotacion()) + CTE_X1;
        double vY2 = -CTE_RADIO * Math.sin(getAnguloRotacion()) + CTE_Y1;
        //  Coordenadas fijas
        canvas.drawLine((float)(CTE_X1 - CTE_RADIO), (float)CTE_Y1, (float)(CTE_X1 + CTE_RADIO), (float)CTE_Y1, paintFondo);
        canvas.drawLine((float)CTE_X1, (float)CTE_Y1, (float)CTE_X1, (float)(CTE_Y1 - CTE_RADIO), paintFondo);
        //
        int angRot = Utils.convertAnguloRotacion2Porcentaje(getAnguloRotacion());
        //
        String text = "" + angRot + "%";
        if (angRot < 0) {
            // Marcha atrás en rojo
            String textMA = "MARCHA ATRÁS";
            paint = paintFondoAtencion;
            // MARCHA ATRÁS
            canvas.drawText(textMA, (float) CTE_X1 / CTE_MITAD, (float) CTE_Y1 + CTE_MARGEN_INFERIOR, paint);
        }
         // PORCENTAJE
        canvas.drawText(text, (float) CTE_X1 - (text.length() * CTE_TEXT_SIZE / CTE_MITAD), (float) CTE_Y1 + CTE_MARGEN_INFERIOR, paint);
        // Aguja en posición
        canvas.drawCircle((float)CTE_X1, (float)CTE_Y1, (float)CTE_RADIO_PEQ, paint);
        canvas.drawLine((float)CTE_X1, (float)CTE_Y1, (float)vX2, (float)vY2, paint);
        //canvas.drawCircle((float)vX2, (float)vY2, (float)CTE_RADIO_PEQ, paint);
        //
        postInvalidate(); // Indicate view should be redrawn
    }

}
