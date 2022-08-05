#include <Servo.h>

int iServoAcc = 0;
int iServoDir = 0;
int iServoSen = 0;
int iLuz1 = 0;
int iLuz2 = 0;
int iLuz3 = 0;
int iLuz4 = 0;
int iLuz5 = 0;
//
Servo servoAcc;   // servo PWM3 - Control accelerador - 0-90
Servo servoSen;   // servo PWM5 - Control sentido - 25 - 60
Servo servoDir;   // servo PWM6 - Control dirección - 10 - 70

void setup()
{
  int iServoAcc = 25;
  int iServoDir = 50;
  int iServoSen = 50;
  //
  servoAcc.attach(3);   // attaches the servo on pin 3 to the servo object
  servoSen.attach(5); // attaches the servo on pin 5 to the servo object
  servoDir.attach(6); // attaches the servo on pin 6 to the servo object
  //
  servoAcc.write(iServoAcc);
  servoSen.write(iServoSen);
  servoDir.write(iServoDir);
  // initialize the serial port:
  Serial.begin(9600);
}

void loop()
{
  if (Serial.available() > 0) {
    String datosSerial = Serial.readString();
    //
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
  }
  delay(10); // waits for the servo to get there
}

void accionarAccelerador() {
  // servo PWM3 - Control accelerador - 0-50
  int iServoAccPlaca = map(iServoAcc, 0, 100, 0, 90);
  servoAcc.write(iServoAccPlaca);  
  Serial.print("iServoAcc: ");
  Serial.print(iServoAccPlaca);
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
void descomponerDatosSerial(String pDatosSerial) {
  iServoAcc = pDatosSerial.substring(0,3).toInt();
  //
  iServoDir = pDatosSerial.substring(3,6).toInt();
  //
  iServoSen = pDatosSerial.substring(6,9).toInt();
  //
  iLuz1 = pDatosSerial.substring(9,10).toInt();
  iLuz2 = pDatosSerial.substring(10,11).toInt();
  iLuz3 = pDatosSerial.substring(11,12).toInt();
  iLuz4 = pDatosSerial.substring(12,13).toInt();
  iLuz5 = pDatosSerial.substring(13,14).toInt();
  //
  Serial.print("iServoAcc: ");
  Serial.print(iServoAcc);
  Serial.print(" - iServoDir: ");
  Serial.print(iServoDir);
  Serial.print(" - iServoSen: ");
  Serial.println(iServoSen);
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
