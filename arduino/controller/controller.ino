#include <LinkedList.h>
#include <Keyboard.h>

#define prefix "[Controller]"
#define keyPrefix "@"
#define suffix "*"
#define splitter ","
#define lenghtOfRxTxData 40
#define keyPressDuration 10
LinkedList<String> toPressKeysList;

void setup() {
    Serial.begin(19200);
    cPrintln("Init...");
    Serial1.begin(57600);
    Serial1.setTimeout(lenghtOfRxTxData);
    toPressKeysList = LinkedList < String > ();
    Keyboard.begin();
}

void loop() {
    cPrintln("\nNew loop...");
    while (toPressKeysList.size() > 0) {
        String nextCommand = toPressKeysList.shift();
        char todo[nextCommand.length()];
        nextCommand.toCharArray(todo, nextCommand.length() + 1);
        cPrintln("To Print: " + nextCommand);
        for (int i = 0; i < sizeof(todo); i++) {
            cPrintln("Pressing: " + String(todo[i]));
            Keyboard.press(todo[i]);
            delay(keyPressDuration);
            Keyboard.release(todo[i]);
        }
    }

    cPrintln("Waiting for data");
    while (!Serial1.available()); //等待数据传送过来，若没有数据，一直等待
    cPrintln("Reading data");

    String str = Serial1.readString();
    cPrintln("Fininshed reading");
    cPrintln("Raw: " + str);
    int prefixIndex = str.indexOf(keyPrefix);
    int suffixIndex = str.indexOf(suffix);
    String commandNumber = "";
    if (prefixIndex >= 0 && suffixIndex >= 0) {
        str = str.substring(prefixIndex + sizeof(keyPrefix) - 1, suffixIndex);
        int splitterIndex = str.indexOf(splitter);
        commandNumber = str.substring(splitterIndex + 1);
        str = str.substring(0, splitterIndex);
        Serial.println(str + ":" + commandNumber);
        toPressKeysList.add(str);
        Serial1.print(keyPrefix + commandNumber + suffix);
    }
    delay(1);
}

void cPrintln(String str) {
    Serial.println(prefix + str + suffix);
}