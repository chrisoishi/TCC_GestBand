#include <EEPROM.h>
void ROM_save(int pos, int leng,String str){
    for(int i=pos;i<pos+leng;i++){
      if(i<pos+str.length())EEPROM.write(i, str[i-pos]);
      else EEPROM.write(i, ';');
    }
    EEPROM.commit();
}
String ROM_read(int pos, int leng){
    String buff;
    char c;
    for (int i = pos; i < pos+leng; i++) {
      c = char(EEPROM.read(i));
      if(c!=';')buff+=c;
      else break;
    }
    return buff;  
}

void read_configs(){
    ssid = ROM_read(0,32);
    pass = ROM_read(32,64);
}
void save_configs(){
    ROM_save(0,32,ssid);
    ROM_save(32,64,pass);
}
