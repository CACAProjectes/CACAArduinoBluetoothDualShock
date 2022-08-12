#include <Servo.h>

int iServoBase = 0;
int iServoBrazo = 0;
//
Servo servoBase;    // servo PWM7 - Control base - 000 _ 180
Servo servoBrazo;   // servo PWM8 - Control brazo -> 000 _ 180
//
String datosSerial;
int contadorDatosSerial;

void setup()
{
  iServoBase = 0;   // Posición inicial - Hacia delante
  iServoBrazo = 180; // Posición inicial - Hacia delante
  //
  servoBase.attach(7);   // attaches the servo on pin 7 to the servo object
  servoBrazo.attach(8);   // attaches the servo on pin 8 to the servo object
  //
  servoBase.write(iServoBase);
  servoBrazo.write(iServoBrazo);
  // initialize the serial port:
  Serial.begin(115200);
}

void loop()
{
  if (Serial.available() > 0) {
    String datosSerial = Serial.readString();
      //
      descomponerDatosSerial(datosSerial);
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
  }
  delay(10); // waits for the servo to get there
}

void accionarBase() {
  // servo PWM3 - Control accelerador - 0-50
  int iServoBasePlaca = map(iServoBase, 0, 100, 0, 180);
  servoBase.write(iServoBasePlaca);  
}
void accionarBrazo() {
  // servo PWM6 - Control dirección - 10 - 70
  int iServoBrazoPlaca = map(iServoBrazo, 0, 100, 0, 180);
  servoBrazo.write(iServoBrazoPlaca);  
}

void descomponerDatosSerial(String pDatosSerial) {
  iServoBase = pDatosSerial.substring(0,3).toInt();
  //
  iServoBrazo = pDatosSerial.substring(3,6).toInt();
  //
  Serial.print("iServoBase: ");
  Serial.print(iServoBase);
  Serial.print(" - iServoBrazo: ");
  Serial.println(iServoBrazo);
}
