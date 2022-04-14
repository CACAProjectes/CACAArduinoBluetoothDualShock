package es.xuan.cacabtconn.ctrl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class ControlCalculo  implements Serializable {
	private static final long serialVersionUID = 1L;

	// Mensaje Original Fichero
	private static String MENSAJE_NOM = "";
	private static String MENSAJE_UP_DOWN = "";
	private static String MENSAJE_X_DIST = "0";
	private static String MENSAJE_Y_DIST = "0";
	// Mensaje Compilado para Plotter
	private static String MENSAJE_M1_ANALOG = "0";			// Valores entre 0 y 255
	private static String MENSAJE_M2_ANALOG = "0";
	private static String MENSAJE_TIEMPO_EJECUCION = "0";
	//
	private static final BigDecimal TIEMPO_MILISEGUNDOS = new BigDecimal(1000).setScale(3, BigDecimal.ROUND_HALF_DOWN);
	// M1 - Coordenada X
	private static final BigDecimal M1_VEL_MAX = new BigDecimal(255).setScale(3, BigDecimal.ROUND_HALF_DOWN);	// Valores entre 0 y 255
	private static final BigDecimal M1_DIST_MAX = new BigDecimal(2).setScale(3, BigDecimal.ROUND_HALF_DOWN);	// en milisegundos
	private static final BigDecimal M1_VEL_MIN = new BigDecimal(230).setScale(3, BigDecimal.ROUND_HALF_DOWN);	// Valores entre 0 y 255
	private static final BigDecimal M1_DIST_MIN = new BigDecimal(0.3).setScale(3, BigDecimal.ROUND_HALF_DOWN);	// en milisegundos
	private static BigDecimal M1_TIEMPO_MIN = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_DOWN);		// Tiempo en recorrer DIST a VEL máxima	-> MENSAJE_X_DIST/M1_DIST_MAX
	// M2 - Coordenada Y
	private static final BigDecimal M2_VEL_MAX = new BigDecimal(255).setScale(3, BigDecimal.ROUND_HALF_DOWN);	// Valores entre 0 y 255
	private static final BigDecimal M2_DIST_MAX = new BigDecimal(2).setScale(3, BigDecimal.ROUND_HALF_DOWN);	// en milisegundos
	private static final BigDecimal M2_VEL_MIN = new BigDecimal(230).setScale(3, BigDecimal.ROUND_HALF_DOWN);	// Valores entre 0 y 255
	private static final BigDecimal M2_DIST_MIN = new BigDecimal(0.3).setScale(3, BigDecimal.ROUND_HALF_DOWN);	// en milisegundos
	private static BigDecimal M2_TIEMPO_MIN = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_DOWN);		// Tiempo en recorrer DIST a VEL máxima	-> MENSAJE_Y_DIST/M2_DIST_MAX
					
	private static DecimalFormat dfEntero = new DecimalFormat("#");
	private static DecimalFormat dfDecimal = new DecimalFormat("#.###");
	private static final String CTE_SEPARADOR_FIC = ";";

	public static MensajePlot transformarMensaje(String pLinea) throws ParseException {
		String[] strDatos = pLinea.split(CTE_SEPARADOR_FIC);
		//
		if (strDatos.length >= 4) {
			/*
				LINE;UP;189;0
				LINE;UP;0,1
				LINE;UP;-189;0
				LINE;UP;0,1
				LINE;UP;114;0
				LINE;DW;44;0
				LINE;UP;31;0
				LINE;UP;0,1
			 */
			MENSAJE_NOM = strDatos[0];
			MENSAJE_UP_DOWN = strDatos[1];
			MENSAJE_X_DIST = strDatos[2];
			MENSAJE_Y_DIST = strDatos[3];
		}
		else {
			System.err.println("TestPlotter - Número de parámetros incorrecto ");
			return null;
		}
		//
		if (MENSAJE_NOM.equals(FuncNoms.LINE.toString())) {
			//
			BigDecimal fMENSAJE_X_DIST = new BigDecimal(MENSAJE_X_DIST).setScale(3, BigDecimal.ROUND_HALF_DOWN);
			BigDecimal fMENSAJE_Y_DIST = new BigDecimal(MENSAJE_Y_DIST).setScale(3, BigDecimal.ROUND_HALF_DOWN);
			// Construir Línea
			M1_TIEMPO_MIN = fMENSAJE_X_DIST.divide(M1_DIST_MAX, BigDecimal.ROUND_HALF_DOWN).abs();
			boolean bM1Negativo = fMENSAJE_X_DIST.compareTo(new BigDecimal(0)) < 0;
			M2_TIEMPO_MIN = fMENSAJE_Y_DIST.divide(M2_DIST_MAX, BigDecimal.ROUND_HALF_DOWN).abs();
			boolean bM2Negativo = fMENSAJE_Y_DIST.compareTo(new BigDecimal(0)) < 0;
			if (M1_TIEMPO_MIN.compareTo(M2_TIEMPO_MIN) >= 0) {
				// M1 realiza el tiempo MAXIMO
				BigDecimal tiempoEjecucion = M1_TIEMPO_MIN.multiply(TIEMPO_MILISEGUNDOS);
				MENSAJE_TIEMPO_EJECUCION = dfDecimal.format(tiempoEjecucion);
				MENSAJE_M1_ANALOG = dfEntero.format(
						(bM1Negativo ? M1_VEL_MAX.negate() : M1_VEL_MAX));
				BigDecimal bgVelocM2 = calculaVelocMenor(false, M1_TIEMPO_MIN, fMENSAJE_Y_DIST);	// true = M1, false = M2
				boolean bRes = (bgVelocM2.compareTo(M2_VEL_MIN) >= 0 && bgVelocM2.compareTo(M2_VEL_MAX) <= 0);
				if (bgVelocM2.compareTo(M2_VEL_MIN) < 0)
					MENSAJE_M2_ANALOG = dfEntero.format(0);		// Sin movimiento
				else if (bgVelocM2.compareTo(M2_VEL_MAX) > 0)
					MENSAJE_M2_ANALOG = dfEntero.format(
						//M2_VEL_MAX);
						(bM2Negativo ? M2_VEL_MAX.negate() : M2_VEL_MAX));
				else
					MENSAJE_M2_ANALOG = dfEntero.format(
						(bM2Negativo ? bgVelocM2.negate() : bgVelocM2));
				return new MensajePlot(MENSAJE_NOM,
						MENSAJE_UP_DOWN,
						MENSAJE_M1_ANALOG,
						MENSAJE_M2_ANALOG,
						MENSAJE_TIEMPO_EJECUCION,
						true, 
						bRes
						);
			}
			else {
				// M1 realiza el tiempo MAXIMO
				BigDecimal tiempoEjecucion = M2_TIEMPO_MIN.multiply(TIEMPO_MILISEGUNDOS);
				MENSAJE_TIEMPO_EJECUCION = dfDecimal.format(tiempoEjecucion);
				MENSAJE_M2_ANALOG = dfEntero.format(
						(bM2Negativo ? M2_VEL_MAX.negate() : M2_VEL_MAX));
				BigDecimal bgVelocM1 = calculaVelocMenor(true, M2_TIEMPO_MIN, fMENSAJE_X_DIST); 	// true = M1, false = M2
				boolean bRes = (bgVelocM1.compareTo(M1_VEL_MIN) >= 0 && bgVelocM1.compareTo(M1_VEL_MAX) <= 0);
				if (bgVelocM1.compareTo(M1_VEL_MIN) < 0)
					MENSAJE_M1_ANALOG = dfEntero.format(0);		// Sin movimiento
				else if (bgVelocM1.compareTo(M1_VEL_MAX) > 0)
					MENSAJE_M1_ANALOG = dfEntero.format(
						(bM1Negativo ? M1_VEL_MAX.negate() : M1_VEL_MAX));
				else
					MENSAJE_M1_ANALOG = dfEntero.format(
						(bM1Negativo ? bgVelocM1.negate() : bgVelocM1));
				return new MensajePlot(MENSAJE_NOM,
						MENSAJE_UP_DOWN,
						MENSAJE_M1_ANALOG,
						MENSAJE_M2_ANALOG,
						MENSAJE_TIEMPO_EJECUCION,
						bRes,
						true
						);
			}				
		}
		else if (MENSAJE_NOM.equals(FuncNoms.RECT.toString())) {
			
		}
		else if (MENSAJE_NOM.equals(FuncNoms.DIAM.toString())) {

		}	
		return null;
	}

	private static BigDecimal calculaVelocMenor(boolean pIsM1, BigDecimal pTiempo, BigDecimal pDistancia) throws ParseException {
		if (pIsM1) {
			// Pendiente para M1
			BigDecimal fPendiente = M2_VEL_MAX.subtract(M2_VEL_MIN).divide(M2_DIST_MAX.subtract(M2_DIST_MIN), BigDecimal.ROUND_HALF_DOWN);
			// 	Y - M2_VEL_MIN = FPENDIENTE * ( X - M2_DIST_MIN)
			//	Y = FPENDIENTE * ( X - M2_DIST_MIN) + M2_VEL_MIN
			//	X = MENSAJE_Y_DIST / pTiempo
			BigDecimal fX = null;
			if (pTiempo.floatValue() == 0.0)
				fX = new BigDecimal(0.0f);
			else
				fX = pDistancia.divide(pTiempo, BigDecimal.ROUND_HALF_DOWN);
			BigDecimal fY = fX.subtract(M1_DIST_MIN).multiply(fPendiente).add(M1_VEL_MIN);
			return fY.setScale(0, BigDecimal.ROUND_HALF_DOWN);
		}
		else {
			// Pendiente para M2
			BigDecimal fPendiente = M1_VEL_MAX.subtract(M1_VEL_MIN).divide(M1_DIST_MAX.subtract(M1_DIST_MIN), BigDecimal.ROUND_HALF_DOWN);
			BigDecimal fX = null;
			if (pTiempo.floatValue() == 0.0)
				fX = new BigDecimal(0.0f);
			else
				fX = pDistancia.divide(pTiempo, BigDecimal.ROUND_HALF_DOWN);
			BigDecimal fY = fX.subtract(M2_DIST_MIN).multiply(fPendiente).add(M2_VEL_MIN);
			return fY.setScale(0, BigDecimal.ROUND_HALF_DOWN);
		}
	}

	public static MensajePlot transformarMensaje2Pantalla(String pLinea) {
		String[] strDatos = pLinea.split(CTE_SEPARADOR_FIC);
		//
		if (strDatos.length >= 4) {
			/*
				LINE;UP;189;0
				LINE;UP;0,1
				LINE;UP;-189;0
				LINE;UP;0,1
				LINE;UP;114;0
				LINE;DW;44;0
				LINE;UP;31;0
				LINE;UP;0,1
			 */
			MENSAJE_NOM = strDatos[0];
			MENSAJE_UP_DOWN = strDatos[1];
			MENSAJE_X_DIST = strDatos[2];
			MENSAJE_Y_DIST = strDatos[3];
		}
		else {
			System.err.println("TestPlotter - Número de parámetros incorrecto ");
			return null;
		}
		try {
			Number numXDist = dfEntero.parse(MENSAJE_X_DIST);
			Number numYDist = dfEntero.parse(MENSAJE_Y_DIST);
			MENSAJE_M1_ANALOG = dfEntero.format(numXDist.intValue() * 10);
			MENSAJE_M2_ANALOG = dfEntero.format(numYDist.intValue() * 10);
			MENSAJE_TIEMPO_EJECUCION = "0";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//
		return new MensajePlot(MENSAJE_NOM,
				MENSAJE_UP_DOWN,
				MENSAJE_M1_ANALOG,
				MENSAJE_M2_ANALOG,
				MENSAJE_TIEMPO_EJECUCION,
				true,
				true
		);
	}
}
