#include <Servo.h>

int iServoAcc = 0;
int iServoDir = 30;
int iServoSen = 25;
int iServoBase = 90;          // Posición inicial - Hacia delante
int iServoBrazo = 90;         // Posición inicial - Hacia delante
int iServoAccPlaca = 0;
int iServoDirPlaca = 0;
int iServoSenPlaca = 0;
int iServoBasePlaca = 0;      // Posición inicial - Hacia delante
int iServoBrazoPlaca = 0;     // Posición inicial - Hacia delante
int iServoAccPlacaAnt = 0;
int iServoDirPlacaAnt = 0;
int iServoSenPlacaAnt = 0;
int iServoBasePlacaAnt = 0;   // Posición inicial - Hacia delante
int iServoBrazoPlacaAnt = 0;  // Posición inicial - Hacia delante
//
//Pin connected to ST_CP of 74HC595
int pinSERIE_LCH = 14; 
//Pin connected to SH_CP of 74HC595
int pinSERIE_CLK = 10; 
////Pin connected to DS of 74HC595
int pinSERIE_DAT = 16; 
//
int iLuz1 = 0;  // BLANCO
int iLuz2 = 0;  // AZUL
int iLuz3 = 0;  // ROJO
int iLuz4 = 0;  // AMARILLO1
int iLuz5 = 0;  // AMARILLO2
int iLuz1Ant = 0;  // BLANCO
int iLuz2Ant = 0;  // AZUL
int iLuz3Ant = 0;  // ROJO
int iLuz4Ant = 0;  // AMARILLO1
int iLuz5Ant = 0;  // AMARILLO2
int iLuzGeneral = B00000000; //Completo: B00011111 VACIO - VACIO - VACIO - BLANCO - AZUL - ROJO - AMARILLO1 - AMARILLO2
//
Servo servoAcc;   // servo PWM3 - Control accelerador - 0-90
Servo servoSen;   // servo PWM5 - Control sentido - 25 - 60
Servo servoDir;   // servo PWM6 - Control dirección - 10 - 70
Servo servoBase;  // servo PWM7 - Control base - 000 - 180
Servo servoBrazo; // servo PWM8 - Control brazo - 000 - 180
//
String datosSerial;

void setup()
{
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
  //  LUCES
  pinMode(pinSERIE_LCH, OUTPUT);
  pinMode(pinSERIE_CLK, OUTPUT);
  pinMode(pinSERIE_DAT, OUTPUT);
  // LEDS
  shiftDato(iLuzGeneral); // VACIO - VACIO - VACIO - BLANCO - AZUL - ROJO - AMARILLO1 - AMARILLO2
  // initialize the serial port
  //Serial.begin(9600);
  Serial1.begin(115200);
  //  ACK
  Serial1.println("ACK");
}

void loop()
{
  if (Serial1.available() > 0) {
    //
    char inChar = Serial1.read(); // A10005000005005099999
    if (inChar == 'A') {
      //Serial.print(inChar);
      // Inicio MENSAJE
      datosSerial = "";
      for (int i=0;i<20;i++) {
        inChar = Serial1.read();
        if (inChar >= '0' && inChar <= '9')
          datosSerial.concat(inChar);
      }
      //
      //Serial.println(datosSerial);
      if (datosSerial.length() == 20) {
        //
        descomponerDatosSerial(datosSerial);
        //
        accionarAccelerador();      
        accionarDireccion();      
        accionarSentido();      
        accionarBase();      
        accionarBrazo();   
        //
        encenderLuces();
        //  ACK
        //Serial1.println("ACK");   
      }
    }
  }
  else
    delay(10); // waits for the servo to get there
}
void shiftDato(int pNum) {
  //take the latch pin low so the LEDs will light down:
  digitalWrite(pinSERIE_LCH, LOW);
  // shift out the bits:
  shiftOut(pinSERIE_DAT, pinSERIE_CLK, LSBFIRST, pNum);
  //take the latch pin high so the LEDs will light up:
  digitalWrite(pinSERIE_LCH, HIGH);
}
void accionarAccelerador() {
  // servo PWM3 - Control accelerador - 0-50
  iServoAccPlaca = map(iServoAcc, 0, 100, 0, 90);
  if (iServoAccPlacaAnt != iServoAccPlaca || iServoAccPlaca == 0)
    servoAcc.write(iServoAccPlaca);  
}
void accionarDireccion() {
  // servo PWM6 - Control dirección - 10 - 70
  iServoDirPlaca = map(iServoDir, 0, 100, 10, 70);
  if (iServoDirPlacaAnt != iServoDirPlaca)
    servoDir.write(iServoDirPlaca);  
}
void accionarSentido() {
  // servo PWM5 - Control sentido - 25 - 60
  iServoSenPlaca = map(iServoSen, 0, 100, 25, 60);
  if (iServoSenPlacaAnt != iServoSenPlaca)
    servoSen.write(iServoSenPlaca);  
}
void accionarBase() {
  // servo PWM7 - Control base - 0-180
  iServoBasePlaca = map(iServoBase, 0, 100, 0, 180);
  if (iServoBasePlacaAnt != iServoBasePlaca)
    servoBase.write(iServoBasePlaca);  
}
void accionarBrazo() {
  // servo PWM8 - Control base - 0 - 180
  iServoBrazoPlaca = map(iServoBrazo, 0, 100, 0, 180);
  if (iServoBrazoPlacaAnt != iServoBrazoPlaca)
    servoBrazo.write(iServoBrazoPlaca);  
}
void descomponerDatosSerial(String pDatosSerial) {
  //
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
}
void encenderLuces() {
  //Completo: B00011111 VACIO - VACIO - VACIO - BLANCO - AZUL - ROJO - AMARILLO1 - AMARILLO2
  if (iLuz1 == iLuz1Ant &&
    iLuz2 == iLuz2Ant &&
    iLuz3 == iLuz3Ant &&
    iLuz4 == iLuz4Ant &&
    iLuz5 == iLuz5Ant)  // Sin cambios
    return;
  // LED BLANCO  
  if (iLuz1 == 1)       // Encender
    iLuzGeneral = iLuzGeneral | B00010000;
  else if (iLuz1 == 0)  // Apagar
    iLuzGeneral = iLuzGeneral & B00001111;
  // LED AZUL  
  if (iLuz2 == 1)       // Encender
    iLuzGeneral = iLuzGeneral | B00001000;
  else if (iLuz2 == 0)  // Apagar
    iLuzGeneral = iLuzGeneral & B00010111;
  // LED ROJO  
  if (iLuz3 == 1)       // Encender
    iLuzGeneral = iLuzGeneral | B00000100;
  else if (iLuz3 == 0)  // Apagar
    iLuzGeneral = iLuzGeneral & B00011011;
  // LED AMARILLO1  
  if (iLuz4 == 1)       // Encender
    iLuzGeneral = iLuzGeneral | B00000010;
  else if (iLuz4 == 0)  // Apagar
    iLuzGeneral = iLuzGeneral & B00011101;
  // LED AMARILLO2  
  if (iLuz5 == 1)       // Encender
    iLuzGeneral = iLuzGeneral | B00000001;
  else if (iLuz5 == 0)  // Apagar
    iLuzGeneral = iLuzGeneral & B00011110;
  //
  shiftDato(iLuzGeneral); // VACIO - VACIO - VACIO - BLANCO - AZUL - ROJO - AMARILLO1 - AMARILLO2    
}
