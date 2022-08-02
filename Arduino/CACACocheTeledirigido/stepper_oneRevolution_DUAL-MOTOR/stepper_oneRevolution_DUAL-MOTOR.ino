
/*
 Stepper Motor Control - one revolution

 This program drives a unipolar or bipolar stepper motor.
 The motor is attached to digital pins 8 - 11 of the Arduino.

 The motor should revolve one revolution in one direction, then
 one revolution in the other direction.


 Created 11 Mar. 2007
 Modified 30 Nov. 2009
 by Tom Igoe

 */

#include <Stepper.h>

const int stepsPerRevolution = 200;  // change this to fit the number of steps per revolution
// for your motor
int velocidadHor = 0;
int velocidadVer = 0;
// initialize the stepper library on pins 2 through 5:
Stepper myStepperHor(stepsPerRevolution, 2, 3, 4, 5);
// initialize the stepper library on pins 6 through 9:
Stepper myStepperVer(stepsPerRevolution, 6, 7, 8, 9);

void setup() {
  // set the speed at 60 rpm:
  myStepperHor.setSpeed(0);
  myStepperVer.setSpeed(0);
  // initialize the serial port:
  Serial.begin(9600);
}

void loop() {

  if (Serial.available() > 0) {
    motorPararHor();
    motorPararVer();
    String arrDades = Serial.readString();
    velocidadHor = arrDades.split(";")[0].toInt();
    velocidadVer = arrDades.split(";")[1].toInt();
    myStepperHor.setSpeed(velocidadHor);
    myStepperVer.setSpeed(velocidadVer);
  }
  myStepperHor.step(1);
  myStepperVer.step(1);
}

void motorPararVer() {
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
}
void motorPararHor() {
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
}
