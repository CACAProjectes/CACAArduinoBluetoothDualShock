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
  motorIni();
}

void loop() 
{
  motorHalfStepSequence(1);
  //
  Serial.println("Sensor: " + String(vSensor) + " - Contador: " + String(vContador));
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
