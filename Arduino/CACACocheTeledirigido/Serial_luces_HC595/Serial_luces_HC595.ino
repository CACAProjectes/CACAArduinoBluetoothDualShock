//
//Pin connected to ST_CP of 74HC595
int pinSERIE_LCH = 14; //9;
//Pin connected to SH_CP of 74HC595
int pinSERIE_CLK = 10; //12;
////Pin connected to DS of 74HC595
int pinSERIE_DAT = 16; //11;

void setup() {
  //
  pinMode(pinSERIE_LCH, OUTPUT);
  pinMode(pinSERIE_CLK, OUTPUT);
  pinMode(pinSERIE_DAT, OUTPUT);
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
}

void loop() {
  /*
  // AZUL
  shiftDato(B00100000);
  delay(300);
  // BLANCO
  shiftDato(B00000000);
  delay(150);
  // AZUL
  shiftDato(B00100000);
  delay(300);
  // ROJO
  shiftDato(B00001000);
  delay(500);
  */
  // LEDS
  shiftDato(B00011111); // VACIO - VACIO - VACIO - BLANCO - AZUL - ROJO - AMARILLO1 - AMARILLO2
  delay(500);
  shiftDato(B00010101); // VACIO - VACIO - VACIO - BLANCO - AZUL - ROJO - AMARILLO1 - AMARILLO2
  delay(500);
  shiftDato(B00001010); // VACIO - VACIO - VACIO - BLANCO - AZUL - ROJO - AMARILLO1 - AMARILLO2
  delay(500);
  
}
void shiftDato(int pNum) {
  //take the latch pin low so the LEDs will light down:
  digitalWrite(pinSERIE_LCH, LOW);
  // shift out the bits:
  shiftOut(pinSERIE_DAT, pinSERIE_CLK, LSBFIRST, pNum);
  //take the latch pin high so the LEDs will light up:
  digitalWrite(pinSERIE_LCH, HIGH);
}
