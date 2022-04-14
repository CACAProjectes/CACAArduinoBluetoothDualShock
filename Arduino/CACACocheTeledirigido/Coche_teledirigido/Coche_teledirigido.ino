/*
  CACA Coche Teledirigido

 */
// include the Servo library
#include <Servo.h>

// PWM
Servo servoPWM3;  // servo PWM3 - Control dirección
Servo servoPWM5;  // servo PWM5 - Control acelerador
Servo servoPWM6;  // servo PWM6 - Control cambio sentido
//
const int ledPinPrueba = 2;   // LED pin de la placa
/*
// SENSORES DE POSICIÓN
const int pinSENS7 =  7;       // Pin SENSOR - Posición de dirección
const int pinSENS8 =  8;       // Pin SENSOR - Posición del acelerador
const int pinSENS9 =  9;       // Pin SENSOR - Posición de cambio sentido
*/
// DATOS EN SERIE
const int pinSERIE_RST =  10;  // Pin SERIE RESET
const int pinSERIE_CLK =  16;  // Pin SERIE CLOCK
const int pinSERIE_DAT =  14;  // Pin SERIE DATA
//
int pos = 0;   // variable to hold the angle for the servo motor
int posPWM3 = 0;   // variable to hold the angle for the servo motor
int posPWM5 = 0;   // variable to hold the angle for the servo motor
int posPWM6 = 0;   // variable to hold the angle for the servo motor
int sentidoPWM3 = 0;   // variable to hold the angle for the servo motor
int sentidoPWM5 = 0;   // variable to hold the angle for the servo motor
int sentidoPWM6 = 0;   // variable to hold the angle for the servo motor
int contadorGeneral = 0;  // Contador general de tiempos

// the setup function runs once when you press reset or power the board
void setup() {
  // inicializar PINs IN-OUT
  pinMode(ledPinPrueba, OUTPUT);
  //
  servoPWM3.attach(3); // attaches the servo on pin 3 to the servo object
  servoPWM5.attach(5); // attaches the servo on pin 5 to the servo object
  servoPWM6.attach(6); // attaches the servo on pin 6 to the servo object
  /*
  pinMode(pinSENS7, INPUT);
  pinMode(pinSENS8, INPUT);
  pinMode(pinSENS9, INPUT);
  */  
  pinMode(pinSERIE_RST, OUTPUT);
  pinMode(pinSERIE_CLK, OUTPUT);
  pinMode(pinSERIE_DAT, OUTPUT);
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
}

void loop() {
  blinkLed();
  posicionarPWM3Direccion();
  posicionarPWM5Acelerador();
  posicionarPWM6Sentido();
  delay(15);
}

void blinkLed() {
  contadorGeneral++;
  if (contadorGeneral >= 0 &&  contadorGeneral < 50)
    digitalWrite(ledPinPrueba, HIGH);   // turn the LED on (HIGH is the voltage level)
  else if (contadorGeneral >= 50 &&  contadorGeneral < 100)
    digitalWrite(ledPinPrueba, LOW);    // turn the LED off by making the voltage LOW
  else 
    contadorGeneral = 0;
}
void pruebaOscilar() {  
  for (pos = 0; pos <= 180; pos += 1) { // goes from 0 degrees to 180 degrees
    // in steps of 1 degree
    servoPWM3.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
  for (pos = 180; pos >= 0; pos -= 1) { // goes from 180 degrees to 0 degrees
    servoPWM3.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }  
}
void posicionarPWM3Direccion() {
  if (sentidoPWM3 == 0) {
    // Sentido horario
    if (posPWM3 < 180) {
      posPWM3++;
      servoPWM3.write(posPWM3);
    }
    else if (posPWM3 >= 180) {
      posPWM3 = 180;
      servoPWM3.write(posPWM3);
      sentidoPWM3 = 1;
    }
  }
  else if (sentidoPWM3 == 1) {
    // Sentido anti-horario
    if (posPWM3 > 0) {
      posPWM3--;
      servoPWM3.write(posPWM3);
    }
    else if (posPWM3 <= 0) {
      posPWM3 = 0;
      servoPWM3.write(posPWM3);
      sentidoPWM3 = 0;
    }
  } 
}

void posicionarPWM5Acelerador() {
  if (sentidoPWM5 == 0) {
    // Sentido horario
    if (posPWM5 < 180) {
      posPWM5++;
      servoPWM5.write(posPWM5);
    }
    else if (posPWM5 >= 180) {
      posPWM5 = 180;
      servoPWM5.write(posPWM5);
      sentidoPWM5 = 1;
    }
  }
  else if (sentidoPWM5 == 1) {
    // Sentido anti-horario
    if (posPWM5 > 0) {
      posPWM5--;
      servoPWM5.write(posPWM5);
    }
    else if (posPWM5 <= 0) {
      posPWM5 = 0;
      servoPWM5.write(posPWM5);
      sentidoPWM5 = 0;
    }
  }
}
  
void posicionarPWM6Sentido() {
  if (sentidoPWM6 == 0) {
    // Sentido horario
    if (posPWM6 < 180) {
      posPWM6++;
      servoPWM6.write(posPWM6);
    }
    else if (posPWM6 >= 180) {
      posPWM6 = 180;
      servoPWM6.write(posPWM6);
      sentidoPWM6 = 1;
    }
  }
  else if (sentidoPWM6 == 1) {
    // Sentido anti-horario
    if (posPWM6 > 0) {
      posPWM6--;
      servoPWM6.write(posPWM6);
    }
    else if (posPWM6 <= 0) {
      posPWM6 = 0;
      servoPWM6.write(posPWM6);
      sentidoPWM6 = 0;
    }
  }
}
