/*
  Blink SOS
*/ 
void setup() {
  pinMode(13, INPUT);
}

// the loop function runs over and over again forever
void loop() {
  //3 short flashes
   digitalWrite(13, HIGH);
  delay(500);
  digitalWrite(13, LOW);
  delay(300);
   digitalWrite(13, HIGH);
  delay(500);
  digitalWrite(13, LOW);
  delay(300);
   digitalWrite(13, HIGH);
  delay(500);
  digitalWrite(13, LOW);
  delay(300);
  
  //3 long flashes
   digitalWrite(13, HIGH);
  delay(1500);
  digitalWrite(13, LOW);
  delay(300);
   digitalWrite(13, HIGH);
  delay(1500);
  digitalWrite(13, LOW);
  delay(300);
   digitalWrite(13, HIGH);
  delay(1500);
  digitalWrite(13, LOW);
  delay(300);

  //3 short flashes
   digitalWrite(13, HIGH);
  delay(500);
  digitalWrite(13, LOW);
  delay(300);
   digitalWrite(13, HIGH);
  delay(500);
  digitalWrite(13, LOW);
  delay(300);
   digitalWrite(13, HIGH);
  delay(500);
  digitalWrite(13, LOW);
  delay(3000);
}
