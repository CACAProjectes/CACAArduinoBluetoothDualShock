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
    private Paint paintFondoAtencionAtras = new Paint();
    private static final double CTE_X1 = 767;
    private static final double CTE_Y1 = 410;
    private static double CTE_RADIO = 300;
    private static double CTE_RADIO_FONDO_MIN = 250;
    private static double CTE_RADIO_FONDO_MAX = 350;
    private static double CTE_RADIO_PEQ = 10;
    private static double CTE_ANG_INI = Math.PI * 3 / 4;        // 135º
    private static double CTE_ANG_MEDIO = Math.PI * 375 / 1000; // 67,5º
    private static double CTE_ANG_FIN = 0;                      // 0º
    private static double CTE_ANG_ATRAS = Math.PI;              // 180º
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
        // Marcha atrás FONDO
        paintFondoAtencionAtras.setTextSize(CTE_TEXT_SIZE);
        paintFondoAtencionAtras.setColor(CTE_COLOR_TEXTO_ATENCION);
        paintFondoAtencionAtras.setStrokeWidth(CTE_GROSOR_FINO);
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
        //  Coordenadas fijas
        //  Calcular los ángulos INICIAL
        double vX2min = CTE_RADIO_FONDO_MIN * Math.cos(CTE_ANG_INI) + CTE_X1;
        double vY2min = -CTE_RADIO_FONDO_MIN * Math.sin(CTE_ANG_INI) + CTE_Y1;
        double vX2max = CTE_RADIO_FONDO_MAX * Math.cos(CTE_ANG_INI) + CTE_X1;
        double vY2max = -CTE_RADIO_FONDO_MAX * Math.sin(CTE_ANG_INI) + CTE_Y1;
        canvas.drawLine((float)vX2min, (float)vY2min, (float)vX2max, (float)vY2max, paintFondo);
        canvas.drawText("0%", (float)vX2max - 35, (float)vY2max - 10, paintFondo);
        //  Calcular los ángulos MEDIO
        vX2min = CTE_RADIO_FONDO_MIN * Math.cos(CTE_ANG_MEDIO) + CTE_X1;
        vY2min = -CTE_RADIO_FONDO_MIN * Math.sin(CTE_ANG_MEDIO) + CTE_Y1;
        vX2max = CTE_RADIO_FONDO_MAX * Math.cos(CTE_ANG_MEDIO) + CTE_X1;
        vY2max = -CTE_RADIO_FONDO_MAX * Math.sin(CTE_ANG_MEDIO) + CTE_Y1;
        canvas.drawLine((float)vX2min, (float)vY2min, (float)vX2max, (float)vY2max, paintFondo);
        canvas.drawText("50%", (float)vX2max - 40, (float)vY2max - 5, paintFondo);
        //  Calcular los ángulos FINAL
        vX2min = CTE_RADIO_FONDO_MIN * Math.cos(CTE_ANG_FIN) + CTE_X1;
        vY2min = -CTE_RADIO_FONDO_MIN * Math.sin(CTE_ANG_FIN) + CTE_Y1;
        vX2max = CTE_RADIO_FONDO_MAX * Math.cos(CTE_ANG_FIN) + CTE_X1;
        vY2max = -CTE_RADIO_FONDO_MAX * Math.sin(CTE_ANG_FIN) + CTE_Y1;
        canvas.drawLine((float)vX2min, (float)vY2min, (float)vX2max, (float)vY2max, paintFondo);
        canvas.drawText("100%", (float)vX2max + 5, (float)vY2max, paintFondo);
        //  Calcular los ángulos ATRAS
        vX2min = CTE_RADIO_FONDO_MIN * Math.cos(CTE_ANG_ATRAS) + CTE_X1;
        vY2min = -CTE_RADIO_FONDO_MIN * Math.sin(CTE_ANG_ATRAS) + CTE_Y1;
        vX2max = CTE_RADIO_FONDO_MAX * Math.cos(CTE_ANG_ATRAS) + CTE_X1;
        vY2max = -CTE_RADIO_FONDO_MAX * Math.sin(CTE_ANG_ATRAS) + CTE_Y1;
        canvas.drawLine((float)vX2min, (float)vY2min, (float)vX2max, (float)vY2max, paintFondoAtencionAtras);
        canvas.drawText("-100%", (float)vX2max - 150, (float)vY2max, paintFondoAtencionAtras);
        //
        double angRotRadianes = Utils.convert01ToAngulo(getAnguloRotacion());
        int angRotPorcentaje = Utils.convert01ToPorcentaje(getAnguloRotacion());
        //
        String text = "" + angRotPorcentaje + "%";
        if (angRotRadianes < 0) {
            // Marcha atrás en rojo
            String textMA = "MARCHA ATRÁS";
            paint = paintFondoAtencion;
            // MARCHA ATRÁS
            canvas.drawText(textMA, (float) CTE_X1 / CTE_MITAD, (float) CTE_Y1 + CTE_MARGEN_INFERIOR, paint);
        }
         // PORCENTAJE
        canvas.drawText(text, (float) CTE_X1 - (text.length() * CTE_TEXT_SIZE / CTE_MITAD), (float) CTE_Y1 + CTE_MARGEN_INFERIOR, paint);
        // Aguja en posición
        double vX2 = CTE_RADIO * Math.cos(angRotRadianes) + CTE_X1;
        double vY2 = -CTE_RADIO * Math.sin(angRotRadianes) + CTE_Y1;
        canvas.drawCircle((float)CTE_X1, (float)CTE_Y1, (float)CTE_RADIO_PEQ, paint);
        canvas.drawLine((float)CTE_X1, (float)CTE_Y1, (float)vX2, (float)vY2, paint);
        //
        postInvalidate(); // Indicate view should be redrawn
    }

}
