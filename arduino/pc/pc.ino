#include <LiquidCrystal.h>

#define prefix "[console]"
#define suffix "$"

LiquidCrystal lcd(7, 8, 9, 10, 11, 12);

void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);
  lcd.setCursor(0,0);
  cPrintln("Init...");
  pinMode(13, OUTPUT);
}

void loop() {
  cPrintln("Looping..."+ String(millis()));
  String c = Serial.readString();
  if(c!=""){
    c.trim();
    Serial.println(c);
    lcdPrint(c);
  }
  delay(5);
}

void cPrintln(String str){
  Serial.println(prefix + str + suffix);
}

void lcdPrint(String str){
  lcd.clear();
  int strLength = str.length();
  if(strLength > 16){
    if(strLength > 32)
      str = str.substring(0,32);
    lcd.setCursor(0,0);
    lcd.print(str.substring(0,16));
    lcd.setCursor(0,1);
    lcd.print(str.substring(16));
  }else{
    lcd.print(str);
  }
}
