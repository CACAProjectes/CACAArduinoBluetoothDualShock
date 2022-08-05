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
Servo servoAcc; // create servo object to control a servo - Valors: 0-90

void setup()
{
  int iServoAcc = 50;
  int iServoDir = 50;
  int iServoSen = 50;
  // initialize the serial port:
  Serial.begin(9600);
  //
  servoAcc.attach(3);   // attaches the servo on pin 3 to the servo object
  
}

void loop()
{
  if (Serial.available() > 0) {
    String dadaSerial = Serial.readString();
    //
    iServoAcc = dadaSerial.substring(0,3).toInt();
    //
    iServoDir = dadaSerial.substring(3,6).toInt();
    //
    iServoSen = dadaSerial.substring(6,9).toInt();
    //
    iLuz1 = dadaSerial.substring(9,10).toInt();
    iLuz2 = dadaSerial.substring(10,11).toInt();
    iLuz3 = dadaSerial.substring(11,12).toInt();
    iLuz4 = dadaSerial.substring(12,13).toInt();
    iLuz5 = dadaSerial.substring(13,14).toInt();
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
    //
    int iServoAccPlaca = map(iServoAcc, 0, 100, 30, 60);
    Serial.print("iServoAccPlaca: ");
    Serial.println(iServoAccPlaca);
    if (iServoAcc >= 0 &&
       iServoAcc <= 100) {
      servoAcc.write(iServoAccPlaca);
    }    
  }
  delay(15); // waits for the servo to get there
}