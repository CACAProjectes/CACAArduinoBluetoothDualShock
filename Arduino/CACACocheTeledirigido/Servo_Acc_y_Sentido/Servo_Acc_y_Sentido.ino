/*
 Controlling a servo position using a potentiometer (variable resistor)
 by Michal Rinott <http://people.interaction-ivrea.it/m.rinott>

 modified on 8 Nov 2013
 by Scott Fitzgerald
 http://www.arduino.cc/en/Tutorial/Knob
*/

#include <Servo.h>

Servo servoAcc; // create servo object to control a servo - Valors: 0-90
Servo servoSen; // create servo object to control a servo - Valors: 25-60

void setup() {
  // initialize the serial port:
  Serial.begin(9600);
  //
  servoAcc.attach(3);   // attaches the servo on pin 3 to the servo object
  servoAcc.write(50);   // Punto medio - 50
  servoSen.attach(5);   // attaches the servo on pin 5 to the servo object
  servoSen.write(50);   // Punto medio - 50 
}

void loop() {
  // 0 - 100
  if (Serial.available() > 0) {
    String dada = Serial.readString();
    servoAcc.write(dada.toInt());                  // sets the servo position according to the scaled value
    servoSen.write(dada.toInt());                  // sets the servo position according to the scaled value
    //
    Serial.print("Posición: " + dada);
  }
  delay(15);                           // waits for the servo to get there
}
