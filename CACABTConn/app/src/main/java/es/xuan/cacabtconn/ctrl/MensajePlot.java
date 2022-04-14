package es.xuan.cacabtconn.ctrl;

import java.io.Serializable;

public class MensajePlot implements Serializable {
	private static final long serialVersionUID = 1L;
	private final static String CTE_SEPARADOR_FIC = ";";
	// Mensaje Compilado para Plotter
	private String MENSAJE_NOM = "";
	private String MENSAJE_UP_DOWN = "";
	private String MENSAJE_M1_ANALOG = "0";			// Valores entre 0 y 255
	private String MENSAJE_M2_ANALOG = "0";
	private String MENSAJE_TIEMPO_EJECUCION = "0";
	private String MENSAJE_RESULTADO_M1 = "OK";
	private String MENSAJE_RESULTADO_M2 = "OK";
	
	public MensajePlot(String mENSAJE_NOM2, String mMENSAJE_UP_DOWN2, String mENSAJE_M1_ANALOG2, String mENSAJE_M2_ANALOG2, String mENSAJE_TIEMPO_EJECUCION2, boolean bResM1, boolean bResM2) {
		setMENSAJE_NOM(mENSAJE_NOM2);
		setMENSAJE_UP_DOWN(mMENSAJE_UP_DOWN2);
		setMENSAJE_M1_ANALOG(mENSAJE_M1_ANALOG2);
		setMENSAJE_M2_ANALOG(mENSAJE_M2_ANALOG2);
		setMENSAJE_TIEMPO_EJECUCION(mENSAJE_TIEMPO_EJECUCION2);
		setMENSAJE_RESULTADO_M1((bResM1 ? "OK" : "KO"));
		setMENSAJE_RESULTADO_M2((bResM2 ? "OK" : "KO"));
	}

	public String getMENSAJE_UP_DOWN() {
		return MENSAJE_UP_DOWN;
	}

	public void setMENSAJE_UP_DOWN(String MENSAJE_UP_DOWN) {
		this.MENSAJE_UP_DOWN = MENSAJE_UP_DOWN;
	}

	public String getMENSAJE_RESULTADO_M1() {
		return MENSAJE_RESULTADO_M1;
	}


	public void setMENSAJE_RESULTADO_M1(String mENSAJE_RESULTADO_M1) {
		MENSAJE_RESULTADO_M1 = mENSAJE_RESULTADO_M1;
	}


	public String getMENSAJE_RESULTADO_M2() {
		return MENSAJE_RESULTADO_M2;
	}


	public void setMENSAJE_RESULTADO_M2(String mENSAJE_RESULTADO_M2) {
		MENSAJE_RESULTADO_M2 = mENSAJE_RESULTADO_M2;
	}


	public String getMENSAJE_NOM() {
		return MENSAJE_NOM;
	}
	public void setMENSAJE_NOM(String mENSAJE_NOM) {
		MENSAJE_NOM = mENSAJE_NOM;
	}
	public String getMENSAJE_M1_ANALOG() {
		return MENSAJE_M1_ANALOG;
	}
	public void setMENSAJE_M1_ANALOG(String mENSAJE_M1_ANALOG) {
		MENSAJE_M1_ANALOG = mENSAJE_M1_ANALOG;
	}
	public String getMENSAJE_M2_ANALOG() {
		return MENSAJE_M2_ANALOG;
	}
	public void setMENSAJE_M2_ANALOG(String mENSAJE_M2_ANALOG) {
		MENSAJE_M2_ANALOG = mENSAJE_M2_ANALOG;
	}
	public String getMENSAJE_TIEMPO_EJECUCION() {
		return MENSAJE_TIEMPO_EJECUCION;
	}
	public void setMENSAJE_TIEMPO_EJECUCION(String mENSAJE_TIEMPO_EJECUCION) {
		MENSAJE_TIEMPO_EJECUCION = mENSAJE_TIEMPO_EJECUCION;
	}
	@Override
	public String toString() {
		return "NOM: " + getMENSAJE_NOM() +
				" - UP/DOWN: " + getMENSAJE_UP_DOWN() +
				" - M1: " + getMENSAJE_M1_ANALOG() + "(" + getMENSAJE_RESULTADO_M1() + ")" + 
				" - M2: " + getMENSAJE_M2_ANALOG() + "(" + getMENSAJE_RESULTADO_M2() + ")" +
				" - T: " + getMENSAJE_TIEMPO_EJECUCION(); 
	}	
	public String toStringFile() {
		return getMENSAJE_NOM() + CTE_SEPARADOR_FIC +
				getMENSAJE_UP_DOWN() + CTE_SEPARADOR_FIC +
				getMENSAJE_M1_ANALOG() + CTE_SEPARADOR_FIC +
				getMENSAJE_RESULTADO_M1() + CTE_SEPARADOR_FIC +
				getMENSAJE_M2_ANALOG() + CTE_SEPARADOR_FIC +
				getMENSAJE_RESULTADO_M2() + CTE_SEPARADOR_FIC +
				getMENSAJE_TIEMPO_EJECUCION();
	}	
}
