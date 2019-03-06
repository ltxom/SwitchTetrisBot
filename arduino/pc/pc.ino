#include <LinkedList.h>
#include <LiquidCrystal.h>

#define prefix "[Console]"
#define keyPrefix "@"
#define suffix "*"
#define spliter ","
#define lengthOfRxTxData 40
#define keyPressDuration 10
#define transferTime 50
class KeyCommand {
    public:
        KeyCommand(int commandNumber, String command) {
            this - > _commandNumber = commandNumber;
            this - > _command = command;
        }
    int getCommandNumber() {
        return this - > _commandNumber;
    }
    String getCommand() {
        return this - > _command;
    }
    String toString() {
        return this - > _command + spliter + String(this - > _commandNumber);
    }
    private:
        int _commandNumber;
    String _command;
};

LiquidCrystal lcd(7, 8, 9, 10, 11, 12);
LinkedList < KeyCommand * > commandList;

void setup() {
    Serial.begin(115200);
    Serial1.begin(57600);
    Serial1.setTimeout(40);
    lcd.begin(16, 2);
    lcd.setCursor(0, 0);
    cPrintln("Init...");
    pinMode(13, OUTPUT);
    commandList = LinkedList < KeyCommand * > ();
}

int commandCounter = 1;
void loop() {
    // 如果发送了指令
    if (Serial.available()) {
        String str = Serial.readString();
        cPrintln("Reading: \"" + str + "\"");
        if (str.indexOf(keyPrefix) >= 0 && str.indexOf(suffix) > 0) {
            KeyCommand * command = new KeyCommand(commandCounter++, str.substring(str.indexOf(keyPrefix) + 1, str.indexOf(suffix)));
            commandList.add(command);
        }
    }

    // 如果有未处理的指令
    if (commandList.size() > 0) {
        KeyCommand * nextCommand = commandList.get(0);
        Serial.println(nextCommand - > toString());
        Serial1.print(keyPrefix + nextCommand - > toString() + suffix);

        // 等待输出
        int commandLength = (nextCommand - > getCommand()).length();
        delay(keyPressDuration * (commandLength) + transferTime);

        // 如果有返回的状态
        if (Serial1.available()) {
            String str = Serial1.readString();
            if (str.indexOf(keyPrefix) >= 0 && str.indexOf(suffix) > 0) {
                str = str.substring(str.indexOf(keyPrefix) + 1, str.indexOf(suffix));
                // 从队列中将该编号命令移除
                int commandNumber = str.toInt();
                for (int i = 0; i < commandList.size(); i++) {
                    if (commandList.get(i) - > getCommandNumber() == commandNumber) {

                        cPrintln("Command completed: " + commandList.get(i) - > toString());
                        lcdPrint("Return: " + String(commandNumber));
                        commandList.remove(i);
                        break;
                    }
                }
            }
        }

    }

}

void sendKeyCommand(String command) {

}
void loop2() {
    cPrintln("Looping..." + String(millis()));
    String c = Serial.readString();
    if (c != "") {
        c.trim();
        Serial.println(c);
        lcdPrint(c);
    }
    delay(5);
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