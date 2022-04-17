package es.xuan.cacacontroller.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
/**
 * @author jcamposp
 *
 */
public class Utils implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void putValorSP(SharedPreferences p_spDades, String p_strKey, String p_strValor) {
		if (p_spDades != null) {
			Editor ed = p_spDades.edit();
			ed.putString(p_strKey, p_strValor);
			ed.commit();
		}
	}

	public static String getValorSP(SharedPreferences p_spDades, String p_strKey) {
		//To retrieve data from shared preference
		if (p_spDades != null)
			return p_spDades.getString(p_strKey, "");
		return "";
	}

	public static int convertString2Int(String pNum) {
		NumberFormat nf = NumberFormat.getInstance();		
		try {
			return nf.parse(pNum).intValue();
		} catch (ParseException e) {
		}
		return 0;
	}
	
	public static int calcularJornada(Calendar pCal) {
		/*
		 * 3>DISSABTES<7>FESTIUS AGOST<6>DISSABTES AGOST<5>FEINERS AGOST<4>FESTIUS<1>FEINERS LECTIUS<
		 */
		if (pCal.get(Calendar.MONTH) == Calendar.AUGUST) {
			// AGOST
			if (pCal.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY &&
					pCal.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY)		// 5>FEINERS AGOST
					return 5;												// 5>FEINERS AGOST
			else if (pCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)	// 6>DISSABTES AGOST
					return 6;												// 6>DISSABTES AGOST
			else if (esFestiu(pCal))										// 7>FESTIUS AGOST
					return 7;												// 7>FESTIUS AGOST
		}
		else if (esFestiu(pCal))											// 4>FESTIUS
			return 4;														// 4>FESTIUS
		else if (pCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)		// 3>DISSABTES - Calendar = 7
			return 3;														// 3>DISSABTES
		else if (pCal.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY &&
			pCal.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY)				// 1>FEINERS LECTIUS (NO AGOST)
			return 1;														// 1>FEINERS LECTIUS
		//
		return 1;															// 1>FEINERS LECTIUS (por defecto)
	}
	private static boolean esFestiu(Calendar pCal) {
		// TODO Afegir Calendari Anual amb els festius de Rub�
		return false;
	}
	
	public static String formatDataComplerta(Calendar pCal, String pIdioma) {
		String pattern = "EEEE, dd/MM/yyyy - HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale(pIdioma.toLowerCase(), pIdioma.toUpperCase()));
		return capitalize(simpleDateFormat.format(pCal.getTime()));
	}
	private static String capitalize(String str)
	{
	    if(str == null) return str;
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	public static String omplirHora(String pHora) {
		if (pHora.length() < 5) {
			return "0" + pHora;
		}
		return pHora;
	}
	public static String omplirMinuts(int pNum) {
		if (pNum < 10) {
			return "0" + pNum;
		}
		else if (pNum > 99) {
			return ">>";
		}
		return "" + pNum;
	}
	@SuppressWarnings("deprecation")
	public static void vibrar(Vibrator pVibrator, long pTempsVib) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			pVibrator.vibrate(VibrationEffect.createOneShot(pTempsVib, VibrationEffect.DEFAULT_AMPLITUDE));
		} else {
			pVibrator.vibrate(pTempsVib);
		}
	}

    public static String formatDataHora(Calendar pCalendar) {
		String pattern = "HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
		return capitalize(simpleDateFormat.format(pCalendar.getTime()));
    }

	private static int calcularDifTemps(ArrayList<String> pHores, int pActual, String pHoraActual, int pDiferencia) {
		/*
			pDiferencia -> -1	Hora anterior
			pDiferencia -> 0	Hora siguiente
			pDiferencia -> 1	Hora siguiente-siguiente
		 */
		if (pActual + pDiferencia < 0)
			return 0;
		String strHoraDif = pHores.get(pActual + pDiferencia);
		return calcularDifHores(pHoraActual, strHoraDif);
	}

	private static int calcularDifHores(String pHora, String pHoraDif) {
		String[] iHora = pHora.split(":");
		String[] iHoraDif = pHoraDif.split(":");
		int iRes = (Integer.parseInt(iHoraDif[0]) * 60 + Integer.parseInt(iHoraDif[1])) -
				(Integer.parseInt(iHora[0]) * 60 + Integer.parseInt(iHora[1]));
		return iRes;
	}

    public static int convertAnguloRotacion2Porcentaje(double pAngulo) {
		// Radians a %
		/*
		PI	  -> -100%
		3PI/4 -> -50%
		PI/2  -> 0%
		PI/4  -> 50%
		0	  -> 100%

		% = (PI()/2 - B$1) * 100 / PI() *2
		 */
		double res = (Math.PI/2 - pAngulo) * 100d / Math.PI * 2d;
		return (int)res;
    }
}
