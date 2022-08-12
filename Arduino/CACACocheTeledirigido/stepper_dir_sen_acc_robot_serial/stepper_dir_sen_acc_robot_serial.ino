#include <Servo.h>

int iServoAcc = 0;
int iServoDir = 30;
int iServoSen = 25;
int iServoBase = 90;   // Posición inicial - Hacia delante
int iServoBrazo = 90; // Posición inicial - Hacia delante

int iLuz1 = 0;
int iLuz2 = 0;
int iLuz3 = 0;
int iLuz4 = 0;
int iLuz5 = 0;
//
Servo servoAcc;   // servo PWM3 - Control accelerador - 0-90
Servo servoSen;   // servo PWM5 - Control sentido - 25 - 60
Servo servoDir;   // servo PWM6 - Control dirección - 10 - 70
Servo servoBase;  // servo PWM7 - Control base - 000 - 180
Servo servoBrazo; // servo PWM8 - Control brazo - 000 - 180
//
String datosSerial;
int contadorDatosSerial;

void setup()
{
  /*
  iServoAcc = 25;
  iServoDir = 50;
  iServoSen = 50;
  iServoBase = 90;   // Posición inicial - Hacia delante
  iServoBrazo = 90; // Posición inicial - Hacia delante
  */
  //
  servoAcc.attach(3);   // attaches the servo on pin 3 to the servo object
  servoSen.attach(5);   // attaches the servo on pin 5 to the servo object
  servoDir.attach(6);   // attaches the servo on pin 6 to the servo object
  servoBase.attach(7);  // attaches the servo on pin 7 to the servo object
  servoBrazo.attach(8); // attaches the servo on pin 8 to the servo object
  //
  servoAcc.write(iServoAcc);
  servoSen.write(iServoSen);
  servoDir.write(iServoDir);
  servoBase.write(iServoBase);
  servoBrazo.write(iServoBrazo);
  // initialize the serial port:
  Serial.begin(115200);
  Serial1.begin(115200);
  //
  datosSerial = "";
  contadorDatosSerial = 0;
  //
  Serial1.println("ACK");
}

void loop()
{
  if (Serial1.available() > 0) {
    /*
    Serial.println("Inicio");
    String datosSerial = Serial1.readString();
    Serial.println(datosSerial);
    Serial.println("Fin");
    */
    //
    char inChar = Serial1.read();
    if (inChar == 13 || inChar == 10 || contadorDatosSerial > 20) {
      /*
      int ii = datosSerial.length() - 20;
      Serial.println("+"+datosSerial+"+");
      datosSerial = datosSerial.substring(ii);
      Serial.println("-"+datosSerial+"-");
      */
      descomponerDatosSerial(datosSerial);
      //
      if (iServoAcc >= 0 && iServoAcc <= 100) {
        accionarAccelerador();      
      }    
      //
      if (iServoDir >= 0 && iServoDir <= 100) {
        accionarDireccion();      
      }  
      //
      if (iServoSen >= 0 && iServoSen <= 100) {
        accionarSentido();      
      } 
      //
      if (iServoBase >= 0 && iServoBase <= 100) {
        accionarBase();      
      }    
      //
      if (iServoBrazo >= 0 && iServoBrazo <= 100) {
        accionarBrazo();      
      }  
      //
      datosSerial = ""; 
      contadorDatosSerial = 0;
      //
      Serial1.println("ACK");
    }
    else {
      datosSerial.concat(inChar);
      contadorDatosSerial++;
    }    
  }
  delay(10); // waits for the servo to get there
}

void accionarAccelerador() {
  // servo PWM3 - Control accelerador - 0-50
  int iServoAccPlaca = map(iServoAcc, 0, 100, 0, 90);
  servoAcc.write(iServoAccPlaca);  
}
void accionarDireccion() {
  // servo PWM6 - Control dirección - 10 - 70
  int iServoDirPlaca = map(iServoDir, 0, 100, 10, 70);
  servoDir.write(iServoDirPlaca);  
}
void accionarSentido() {
  // servo PWM5 - Control sentido - 25 - 60
  int iServoSenPlaca = map(iServoSen, 0, 100, 25, 60);
  servoSen.write(iServoSenPlaca);  
}
void accionarBase() {
  // servo PWM7 - Control base - 0-180
  int iServoBasePlaca = map(iServoBase, 0, 100, 0, 180);
  servoBase.write(iServoBasePlaca);  
}
void accionarBrazo() {
  // servo PWM8 - Control base - 0 - 180
  int iServoBrazoPlaca = map(iServoBrazo, 0, 100, 0, 180);
  servoBrazo.write(iServoBrazoPlaca);  
}
void descomponerDatosSerial(String pDatosSerial) {
  iServoAcc = pDatosSerial.substring(0,3).toInt();
  //
  iServoDir = pDatosSerial.substring(3,6).toInt();
  //
  iServoSen = pDatosSerial.substring(6,9).toInt();
  //
  iServoBase = pDatosSerial.substring(9,12).toInt();
  //
  iServoBrazo = pDatosSerial.substring(12,15).toInt();
  //
  iLuz1 = pDatosSerial.substring(15,16).toInt();
  iLuz2 = pDatosSerial.substring(16,17).toInt();
  iLuz3 = pDatosSerial.substring(17,18).toInt();
  iLuz4 = pDatosSerial.substring(18,19).toInt();
  iLuz5 = pDatosSerial.substring(19,20).toInt();
  //
  Serial.print("iServoAcc: ");
  Serial.print(iServoAcc);
  Serial.print(" - iServoDir: ");
  Serial.print(iServoDir);
  Serial.print(" - iServoSen: ");
  Serial.print(iServoSen);
  Serial.print(" - iServoBase: ");
  Serial.print(iServoBase);
  Serial.print(" - iServoBrazo: ");
  Serial.println(iServoBrazo);
  //
  Serial.print("iLuz1: ");
  Serial.print(iLuz1);
  Serial.print(" - iLuz2: ");
  Serial.print(iLuz2);
  Serial.print(" - iLuz3: ");
  Serial.print(iLuz3);
  Serial.print(" - iLuz4: ");
  Serial.print(iLuz4);
  Serial.print(" - iLuz5: ");
  Serial.println(iLuz5);
}
