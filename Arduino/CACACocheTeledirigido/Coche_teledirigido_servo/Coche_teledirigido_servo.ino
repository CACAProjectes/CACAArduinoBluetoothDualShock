// include the Servo library
#include <Servo.h>

// PWM
Servo servoPWM3Acc;  // servo PWM3 - Control accelerador - 0-90
Servo servoPWM5Sen;  // servo PWM5 - Control sentido - 25 - 60
Servo servoPWM6Dir;  // servo PWM6 - Control dirección - 10 - 70

void setup() {
  //
  servoPWM3Acc.attach(3); // attaches the servo on pin 3 to the servo object
  servoPWM5Sen.attach(5); // attaches the servo on pin 5 to the servo object
  servoPWM6Dir.attach(6); // attaches the servo on pin 6 to the servo object
  //
  pinMode(3, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  //
  servoPWM3Acc.write(70);
  servoPWM5Sen.write(50);
  servoPWM6Dir.write(50);
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
}

void loop() {
  if (Serial.available() > 0) {
    String dada = Serial.readString();
    servoPWM5Sen.write(dada.toInt());
    //servoPWM3Acc.write(dada.toInt());
    //servoPWM6Dir.write(dada.toInt());
    Serial.print("Posición: " + dada);
    delay(15);    
  }
}
