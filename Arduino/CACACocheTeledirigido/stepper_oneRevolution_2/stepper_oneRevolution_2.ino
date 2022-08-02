
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
int velocidad = 100;
// initialize the stepper library on pins 8 through 11:
Stepper myStepper(stepsPerRevolution, 2, 3, 4, 5);

void setup() {
  // set the speed at 60 rpm:
  myStepper.setSpeed(velocidad);
  // initialize the serial port:
  Serial.begin(9600);
}

void loop() {

  if (Serial.available() > 0) {
    motorParar();
    String incomingString = Serial.readString();
      velocidad = incomingString.toInt();
      myStepper.setSpeed(velocidad);
  }
  myStepper.step(1);
}

void motorParar() {
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
}