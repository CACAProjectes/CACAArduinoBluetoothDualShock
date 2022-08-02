/**
 * @author jcamposp
 *
 */
	public class MotorStepper {

	/**
	 * @param args
	 */
		private final static int LOW = 0;
		private final static int HIGH = 1;
		private final static int arrayHalfStepSequence[][] = {
				{LOW, HIGH, LOW, HIGH},
				{LOW, LOW, LOW, HIGH},
				{HIGH, LOW , LOW, HIGH},
				{HIGH, LOW, LOW, LOW},
				{HIGH, LOW, HIGH, LOW},
				{LOW, LOW, HIGH, LOW},
				{LOW, HIGH, HIGH, LOW},
				{LOW, HIGH, LOW, LOW}
		};
		
		private static int punteroMotor1 = 0;
	
	public static void main(String[] args) {

		for (int i=0;i<16;i++) {
			
			int iPaso = i % 8;
			System.out.println("Paso:" + iPaso + " - " + escribirPaso(arrayHalfStepSequence[iPaso]));
			
		}
		
	}

	private static String escribirPaso(int[] pPaso) {
		return (pPaso[0] == 0 ? "LOW" : "HIGH") + " - " +
				(pPaso[1] == 0 ? "LOW" : "HIGH") + " - " +
				(pPaso[2] == 0 ? "LOW" : "HIGH") + " - " +
				(pPaso[3] == 0 ? "LOW" : "HIGH") ;
	}
}
