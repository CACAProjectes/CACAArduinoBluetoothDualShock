// Include the Arduino Stepper Library
#include <Stepper.h>

// Number of steps per output rotation
const int pinA = 3;
const int pinB = 2;
const int pinC = 4;
const int pinD = 5;
const int pinSENSOR = 6;

String incomingString = "0";
int vSensor = 0;
int vSensorAux = 0;
int vContador = 0;
int vContadorPas = 0;
int vContadorPasMax = 0;
bool vSerialEscribir = true;

void setup()
{
  // initialize the serial port:
  Serial.begin(9600);
  //
  pinMode(pinA, OUTPUT);
  pinMode(pinB, OUTPUT);
  pinMode(pinC, OUTPUT);
  pinMode(pinD, OUTPUT);
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(pinSENSOR, INPUT);
  //
  motorParar();
  //
  motorIni();
  //
  contadoresIni();
}

void loop() 
{
  if (Serial.available() > 0) {
    // read the incoming string:
    String incomingString = Serial.readString();
    vContadorPasMax = incomingString.toInt();
  }
  if (vContadorPasMax > 0) {
    motorHalfStepSequence(1, vContadorPasMax);
    //
    Serial.println("Sensor: " + String(vSensor) + " - ContadorOptico: " + String(vContador) + 
        " - ContadorPasoMax: " + String(vContadorPasMax) + " - ContadorPaso: " + String(vContadorPas));
    vSerialEscribir = true;
  }
  else {
    if (vSerialEscribir) {
      motorParar();
      Serial.println("Motor Parado.");
    }
    vSerialEscribir = false;
  }
}

void motorParar() {
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, LOW);
}
void contadoresIni() {
  vContador = 0;
  vContadorPas = 0;
  vContadorPasMax = 0;
}
  
void sensor() {
    vSensor = digitalRead(pinSENSOR);
    if (vSensor != vSensorAux) {
      // Sólo cuenta cuando el SENSOR es 0
      if (vSensor == 0)
        vContador++;
      vSensorAux = vSensor;
    }  
}

bool pararMotor(int pNumPasMax) {
  vContadorPas++;
  if (vContadorPas>=pNumPasMax) {    
    contadoresIni(); 
    return true;   
  }
  return false;
}

void motorHalfStepSequence(int pDelay, int pNumPasMax) {
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, HIGH);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  sensor();
  if (pararMotor(pNumPasMax))
    return;
  delay(pDelay);
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  sensor();
  if (pararMotor(pNumPasMax))
    return;
  delay(pDelay);
  
  digitalWrite(pinA, HIGH);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  sensor();
  if (pararMotor(pNumPasMax))
    return;
  delay(pDelay);
  digitalWrite(pinA, HIGH);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, LOW);
  sensor();
  if (pararMotor(pNumPasMax))
    return;
  delay(pDelay);
  
  digitalWrite(pinA, HIGH);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, HIGH);
  digitalWrite(pinD, LOW);
  sensor();
  if (pararMotor(pNumPasMax))
    return;
  delay(pDelay);
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, HIGH);
  digitalWrite(pinD, LOW);
  sensor();
  if (pararMotor(pNumPasMax))
    return;
  delay(pDelay);
  
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, HIGH);
  digitalWrite(pinC, HIGH);
  digitalWrite(pinD, LOW);
  sensor();
  if (pararMotor(pNumPasMax))
    return;
  delay(pDelay);
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, HIGH);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, LOW);
  sensor();
  if (pararMotor(pNumPasMax))
    return;
  delay(pDelay);
}

void motorIni() {
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, LOW);
}

void motorHalfStepSequence(int pDelay) {
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, HIGH);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  sensor();
  delay(pDelay);
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  sensor();
  delay(pDelay);
  
  digitalWrite(pinA, HIGH);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  sensor();
  delay(pDelay);
  digitalWrite(pinA, HIGH);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, LOW);
  sensor();
  delay(pDelay);
  
  digitalWrite(pinA, HIGH);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, HIGH);
  digitalWrite(pinD, LOW);
  sensor();
  delay(pDelay);
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, HIGH);
  digitalWrite(pinD, LOW);
  sensor();
  delay(pDelay);
  
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, HIGH);
  digitalWrite(pinC, HIGH);
  digitalWrite(pinD, LOW);
  sensor();
  delay(pDelay);
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, HIGH);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, LOW);
  sensor();
  delay(pDelay);
}
void motorFullStepSequence(int pDelay) {
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, HIGH);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  delay(pDelay);
  digitalWrite(pinA, LOW);
  digitalWrite(pinB, HIGH);
  digitalWrite(pinC, HIGH);
  digitalWrite(pinD, LOW);
  delay(pDelay);  
  digitalWrite(pinA, HIGH);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  delay(pDelay);
  digitalWrite(pinA, HIGH);
  digitalWrite(pinB, LOW);
  digitalWrite(pinC, LOW);
  digitalWrite(pinD, HIGH);
  delay(pDelay);
}
