#include <SoftwareSerial.h>
SoftwareSerial BTSerial(1, 0);
int led = 9;

void setup() {
  BTSerial.begin(9600);
  pinMode(led, OUTPUT);
}

void loop() {
  if(BTSerial.available()){
    char cmd = (char)BTSerial.read();
    if(cmd == '1'){
      digitalWrite(led, HIGH);
    } else if(cmd == '2'){
      digitalWrite(led, LOW);
    }
  }
}

