void setup() {
  Serial.begin(115200);
  Serial1.begin(115200);
  Serial.print("Test"); 
}

void loop() {
// read from port 1, send to port 0:
  if (Serial1.available()) {
    //char inByte = Serial1.read();
    String datosSerial = Serial1.readString();
    //Serial.write(inByte);
    Serial.println(datosSerial);
  }
  delay(100);
}
/*
void setup()
{
 Serial.begin(115200);
 Serial1.begin(115200);
}

unsigned long previousTime = 0;
int counter = 0;

void loop() {
 
 unsigned long currentTime = millis();
 if ( currentTime - previousTime > 60 ) {
   counter = counter + 1;
   String data;
   data.concat("counter: ");
   data.concat(counter);
   data.concat(",");
   data.concat("time: ");
   data.concat(currentTime);
   data.concat("\r\n");
   Serial.write(data.c_str());
   Serial1.write(data.c_str());
   previousTime = currentTime;
 }
}
*/
