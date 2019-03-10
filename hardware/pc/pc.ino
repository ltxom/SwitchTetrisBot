#include <LinkedList.h>
#include <LiquidCrystal.h>

#define prefix "[Console]"
#define keyPrefix "@"
#define suffix "*"
#define spliter ","
#define lengthOfRxTxData 40
#define keyPressDuration 30
#define transferTime 50

#define LEDARRAY_D 30
#define LEDARRAY_C 31
#define LEDARRAY_B 32
#define LEDARRAY_A 33
#define LEDARRAY_G 34
#define LEDARRAY_DI 35
#define LEDARRAY_CLK 36
#define LEDARRAY_LAT 37


class KeyCommand {
    public:
        KeyCommand(int commandNumber, String command) {
            this -> _commandNumber = commandNumber;
            this -> _command = command;
        }
    int getCommandNumber() {
        return this -> _commandNumber;
    }
    String getCommand() {
        return this -> _command;
    }
    String toString() {
        return this -> _command + spliter + String(this -> _commandNumber);
    }
    private:
        int _commandNumber;
    String _command;
};

LiquidCrystal lcd(7, 8, 9, 10, 11, 12);
LinkedList < KeyCommand * > commandList;

unsigned char Display_Buffer[2];
const unsigned char Word1[1][32] =
{
  0xFF,0xFF,0xFF,0xE1,0xC0,0x80,0x80,0x80,0xC0,0xE0,0xF0,0xF8,0xFC,0xFE,0xFF,0xFF,
  0xFF,0xFF,0xFF,0x87,0x03,0x01,0x01,0x01,0x03,0x07,0x0F,0x1F,0x3F,0x7F,0xFF,0xFF
};

void setup() {
    Serial.begin(115200);
    Serial1.begin(57600);
    Serial1.setTimeout(40);
    lcd.begin(16, 2);
    lcd.setCursor(0, 0);
    cPrintln("Init...");
    pinMode(13, OUTPUT);
    for(int i = 22; i <= 28; i++){
      pinMode(i, OUTPUT);
    }
    for(int i = 30; i <= 37; i++){
      pinMode(i, OUTPUT);
    }
    commandList = LinkedList < KeyCommand * > ();

}

int commandCounter = 1;
void loop() {
    Display(Word1);
    // 如果发送了指令
    if (Serial.available()) {
        String str = Serial.readString();
        cPrintln("Reading: \"" + str + "\"");
        if (str.indexOf(keyPrefix) >= 0 && str.indexOf(suffix) > 0) {
            KeyCommand * command = new KeyCommand(commandCounter++, str.substring(str.indexOf(keyPrefix) + 1, str.indexOf(suffix)));
           // commandList.add(command);
            String content = command->getCommand();
            lcdPrint(content);
            int commandSize = content.length();
            char charArr[commandSize];
            content.toCharArray(charArr, commandSize + 1);
            for(int i = 0; i< sizeof(charArr); i++){
              sendCommand(charArr[i]);
            }
        }
    }

}

void sendCommand(char command) {
  if(command == '1'){
    pressButton(22);
  }else if(command == '2'){
    pressButton(23);
  }else if(command == '3'){
    pressButton(24);
  }else if(command == '4'){
    pressButton(25);
  }else if(command == 'a'){
    pressButton(26);
  }else if(command == 'b'){
    pressButton(27);
  }else if(command == 'r'){
    pressButton(28);
  }
}

void pressButton(int pin){
    digitalWrite(pin, HIGH);
    delay(keyPressDuration);
    digitalWrite(pin, LOW);
    delay(keyPressDuration);
}

void cPrintln(String str) {
    Serial.println(prefix + str + suffix);
}

void lcdPrint(String str) {
    lcd.clear();
    int strLength = str.length();
    if (strLength > 16) {
        if (strLength > 32)
            str = str.substring(0, 32);
        lcd.setCursor(0, 0);
        lcd.print(str.substring(0, 16));
        lcd.setCursor(0, 1);
        lcd.print(str.substring(16));
    } else {
        lcd.print(str);
    }
}

void Display(const unsigned char dat[][32]){
  unsigned char i;
  for(i= 0;i < 16; i++){
    digitalWrite(LEDARRAY_G, HIGH);
    Display_Buffer[0] = dat[0][i];
    Display_Buffer[1] = dat[0][i+16];

    Send(Display_Buffer[1]);
    Send(Display_Buffer[0]);

    digitalWrite(LEDARRAY_LAT,HIGH);
    delayMicroseconds(1);

    Scan_Line(i);

    digitalWrite(LEDARRAY_G, LOW);

    delayMicroseconds(100);
  }
}

void Scan_Line(unsigned char m){
  switch(m){
    case 0:
      digitalWrite(LEDARRAY_D, LOW);
      digitalWrite(LEDARRAY_C, LOW);
      digitalWrite(LEDARRAY_B, LOW);
      digitalWrite(LEDARRAY_A, LOW);
      break;
    case 1:
      digitalWrite(LEDARRAY_D, LOW);
      digitalWrite(LEDARRAY_C, LOW);
      digitalWrite(LEDARRAY_B, LOW);
      digitalWrite(LEDARRAY_A, HIGH);
      break;
    case 2:
      digitalWrite(LEDARRAY_D, LOW);
      digitalWrite(LEDARRAY_C, LOW);
      digitalWrite(LEDARRAY_B, HIGH);
      digitalWrite(LEDARRAY_A, LOW);
      break;
    case 3:
      digitalWrite(LEDARRAY_D, LOW);
      digitalWrite(LEDARRAY_C, LOW);
      digitalWrite(LEDARRAY_B, HIGH);
      digitalWrite(LEDARRAY_A, HIGH);
      break;
    case 4:
      digitalWrite(LEDARRAY_D, LOW);
      digitalWrite(LEDARRAY_C, HIGH);
      digitalWrite(LEDARRAY_B, LOW);
      digitalWrite(LEDARRAY_A, LOW);
      break;
    case 5:
      digitalWrite(LEDARRAY_D, LOW);
      digitalWrite(LEDARRAY_C, HIGH);
      digitalWrite(LEDARRAY_B, LOW);
      digitalWrite(LEDARRAY_A, HIGH);
      break;
    case 6:
      digitalWrite(LEDARRAY_D, LOW);
      digitalWrite(LEDARRAY_C, HIGH);
      digitalWrite(LEDARRAY_B, HIGH);
      digitalWrite(LEDARRAY_A, LOW);
      break;
    case 7:
      digitalWrite(LEDARRAY_D, LOW);
      digitalWrite(LEDARRAY_C, HIGH);
      digitalWrite(LEDARRAY_B, HIGH);
      digitalWrite(LEDARRAY_A, HIGH);
      break;
    case 8:
      digitalWrite(LEDARRAY_D, HIGH);
      digitalWrite(LEDARRAY_C, LOW);
      digitalWrite(LEDARRAY_B, LOW);
      digitalWrite(LEDARRAY_A, LOW);
      break;
    case 9:
      digitalWrite(LEDARRAY_D, HIGH);
      digitalWrite(LEDARRAY_C, LOW);
      digitalWrite(LEDARRAY_B, LOW);
      digitalWrite(LEDARRAY_A, HIGH);
      break;
    case 10:
      digitalWrite(LEDARRAY_D, HIGH);
      digitalWrite(LEDARRAY_C, LOW);
      digitalWrite(LEDARRAY_B, HIGH);
      digitalWrite(LEDARRAY_A, LOW);
      break;
    case 11:
      digitalWrite(LEDARRAY_D, HIGH);
      digitalWrite(LEDARRAY_C, LOW);
      digitalWrite(LEDARRAY_B, HIGH);
      digitalWrite(LEDARRAY_A, HIGH);
      break;
    case 12:
      digitalWrite(LEDARRAY_D, HIGH);
      digitalWrite(LEDARRAY_C, HIGH);
      digitalWrite(LEDARRAY_B, LOW);
      digitalWrite(LEDARRAY_A, LOW);
      break;
    case 13:
      digitalWrite(LEDARRAY_D, HIGH);
      digitalWrite(LEDARRAY_C, HIGH);
      digitalWrite(LEDARRAY_B, LOW);
      digitalWrite(LEDARRAY_A, HIGH);
      break;
    case 14:
      digitalWrite(LEDARRAY_D, HIGH);
      digitalWrite(LEDARRAY_C, HIGH);
      digitalWrite(LEDARRAY_B, HIGH);
      digitalWrite(LEDARRAY_A, LOW);
      break;
    case 15:
      digitalWrite(LEDARRAY_D, HIGH);
      digitalWrite(LEDARRAY_C, HIGH);
      digitalWrite(LEDARRAY_B, HIGH);
      digitalWrite(LEDARRAY_A, HIGH);
      break;
    default:
      break;
  }
}

void Send(unsigned char dat){
  unsigned char i;
  digitalWrite(LEDARRAY_CLK, LOW);
  delayMicroseconds(1);
  digitalWrite(LEDARRAY_LAT, LOW);
  delayMicroseconds(1);

  for(int i = 0; i < 8; i++){
    if(dat&0x01){
      digitalWrite(LEDARRAY_DI, HIGH);
    }else{
      digitalWrite(LEDARRAY_DI, LOW);
    }

    delayMicroseconds(1);
    digitalWrite(LEDARRAY_CLK, HIGH);
    delayMicroseconds(1);
    digitalWrite(LEDARRAY_CLK, LOW);
    dat>>=1;

  }
}
