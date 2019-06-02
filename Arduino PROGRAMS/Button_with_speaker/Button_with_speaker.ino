// constants won't change. They're used here to set pin numbers:
const int buttonPin = 2;     // the number of the pushbutton pin
const int buttonPin_2 = 3;
const int ledPin =  13;      // the number of the LED pin
const int speakerPin = 11;   //speaker pin
const int speakerPin_2 = 10;

// variables will change:
int buttonState = 0;         // variable for reading the pushbutton status
int button_2State = 0;
int n = 50;
int x = 0;

void setup() {
  pinMode(ledPin, OUTPUT);
  pinMode(speakerPin, OUTPUT);
  pinMode(speakerPin_2, OUTPUT);
  
  pinMode(buttonPin, INPUT);
  pinMode(buttonPin_2, INPUT);

}

void loop() {
  
  buttonState = digitalRead(buttonPin);
  button_2State = digitalRead(buttonPin_2);

  if (buttonState == HIGH) {
    digitalWrite(ledPin, HIGH);
      tone(speakerPin,n);
      delay(350);
      n = (n + 30);
      if(n > 120) n = 50;
    
  } else {
    // turn LED off:
    noTone(speakerPin);
    n = 50;
    digitalWrite(ledPin, LOW);
  }

  if(button_2State == HIGH)
  {
    if(x == 3)
    {
      tone(speakerPin_2, 330, 750);
      x = 0;
    }
    else
    {
     x++;
     tone(speakerPin_2, 420, 500);
    }
  }
}
