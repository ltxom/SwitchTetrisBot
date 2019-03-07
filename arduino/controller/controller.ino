#include <LinkedList.h>
#include <Keyboard.h>

#define prefix "[Controller]"
#define keyPrefix "@"
#define suffix String("*")
#define splitter ","
#define lenghtOfRxTxData 40
#define keyPressDuration 40

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
        return this -> _command + splitter + String(this -> _commandNumber);
    }
    private:
        int _commandNumber;
    String _command;
};


LinkedList<KeyCommand*> toPressKeysList;
LinkedList<int> pressedCommandList;
void setup(){
  Serial.begin(19200);
  cPrintln("Init...");
  Serial1.begin(57600);
  Serial1.setTimeout(lenghtOfRxTxData);
  toPressKeysList = LinkedList<KeyCommand*>();
  pressedCommandList = LinkedList<int>();
  Keyboard.begin();

}

void loop(){
   cPrintln("\nNew loop...");
   while(toPressKeysList.size() > 0){
      KeyCommand * nextCommand = toPressKeysList.shift();
      int commandNumber = nextCommand->getCommandNumber();
      if(!isCommandPressed(commandNumber)){
        String command = (nextCommand -> getCommand());
      command.replace("f-1", "!");
      command.replace("ctrl-2", "@");
      char todo[command.length()];
      command.toCharArray(todo, command.length()+1);
      cPrintln("To Print: " + command);
      for(int i = 0; i < sizeof(todo); i++){
          if(todo[i] == '!'){
            cPrintln("Pressing: F1");
            Keyboard.press(KEY_F1);
            delay(keyPressDuration);
            Keyboard.release(KEY_F1);
          }else if(todo[i] == '@'){
            cPrintln("Pressing: Ctrl+2");
            Keyboard.press(KEY_LEFT_CTRL);
            Keyboard.press('2');
            delay(500);
            Keyboard.releaseAll();
          }else{
            cPrintln("Pressing: " + String(todo[i]));
            Keyboard.press(todo[i]);
            delay(keyPressDuration);
            Keyboard.release(todo[i]);
          }
          delay(keyPressDuration-20);
      }
         pressedCommandList.add(commandNumber);
         Serial1.print(keyPrefix + commandNumber + suffix);
      }
   }

   cPrintln("Waiting for data");
   while(!Serial1.available());//等待数据传送过来，若没有数据，一直等待
   String str = Serial1.readString();
   cPrintln("Fininshed reading");
   cPrintln("Raw: "+str);
   int prefixIndex = str.indexOf(keyPrefix);
   int suffixIndex = str.indexOf(suffix);
   String commandNumber = "";
   if(prefixIndex >= 0 && suffixIndex >= 0){
     str = str.substring(prefixIndex + sizeof(keyPrefix) - 1,suffixIndex);
     int splitterIndex = str.indexOf(splitter);
     commandNumber = str.substring(splitterIndex + 1);
     str = str.substring(0,splitterIndex);
     Serial.println(str + ":" + commandNumber);
     toPressKeysList.add(new KeyCommand(commandNumber.toInt(),str));
   }
   delay(1);
}

boolean isCommandPressed(int commandNumber){
  for(int i = 0; i < pressedCommandList.size(); i++){
    if(commandNumber == pressedCommandList.get(i))
      return true;
  }
  return false;
}

void cPrintln(String str){
  Serial.println(prefix + str + suffix);
}
