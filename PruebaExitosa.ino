// Definición de pines
const int ledPin1 = 2;
const int ledPin2 = 3;
const int ledPin3 = 4;
const int ledPin4 = 5;
const int buzzerPin = 6;

const int segmentPins[] = {7, 8, 9, 10, 11, 12, 13};
const int numOfDigits = 1;
int digits[numOfDigits] = {0};
const byte numCodes[] = {
  0b00111111, // 0
  0b00000110, // 1
  0b01011011, // 2
  0b01001111, // 3
  0b01100110, // 4
  0b01101101, // 5
  0b01111101, // 6
  0b00000111, // 7
  0b01111111, // 8
  0b01101111  // 9
};

void setup() {
  // Configuración de los pines como salida
  pinMode(ledPin1, OUTPUT);
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);
  pinMode(ledPin4, OUTPUT);
  pinMode(buzzerPin, OUTPUT);

  for (int i = 0; i < 7; i++) {
    pinMode(segmentPins[i], OUTPUT);
  }

  // Iniciar el puerto serie a 9600 bps
  Serial.begin(9600);

  displayNumber(digits[0]);
}
void loop() {
  if (Serial.available() > 0) {
    int valor = Serial.parseInt();

    if (valor >= 0 && valor <= 100) {
      encenderLedsPorPorcentaje(valor);
    } else if (valor == 101) {
      tocarBocina1Seg();
    } else if (valor == 102) {
      tocarBocinaCadaCuartoSeg();
    } else if (valor == 103) {
      increment();
      displayNumber(digits[0]);
    } else if (valor == 104) {
      decrement();
      displayNumber(digits[0]);
    }
  }
}

void encenderLedsPorPorcentaje(int porcentaje) {
  if (porcentaje == 0) {
    digitalWrite(ledPin1, LOW);
    digitalWrite(ledPin2, LOW);
    digitalWrite(ledPin3, LOW);
    digitalWrite(ledPin4, LOW);
  } else {
    digitalWrite(ledPin1, porcentaje >= 25 ? HIGH : LOW);
    digitalWrite(ledPin2, porcentaje >= 50 ? HIGH : LOW);
    digitalWrite(ledPin3, porcentaje >= 75 ? HIGH : LOW);
    digitalWrite(ledPin4, porcentaje == 100 ? HIGH : LOW);
  }
}

void displayNumber(int number) {
  for (int i = 0; i < 7; i++) {
    digitalWrite(segmentPins[i], bitRead(numCodes[number], i));
  }
}

void increment() {
  digits[0]++;
  if (digits[0] >= 10) {
    digits[0] = 0;
  }
}

void decrement() {
  digits[0]--;
  if (digits[0] < 0) {
    digits[0] = 9;
  }
} 
void tocarBocina1Seg() {
  tone(buzzerPin, 1000);
  delay(1000);
  noTone(buzzerPin);
}

void tocarBocinaCadaCuartoSeg() {
  for (int i = 0; i < 8; i++) {
    tone(buzzerPin, 1000);
    delay(250);
    noTone(buzzerPin);
    delay(250);
  }
}
