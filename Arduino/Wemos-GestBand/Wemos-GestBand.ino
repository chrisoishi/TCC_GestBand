#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include<Wire.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <EEPROM.h>
#include "vars.h"
#include "extras.h";
#include "ROM.h"
#include "bitmaps.h"
#include "RGB.h"
#include "Sensor.h"
#include "screens.h"
#include "button.h"
#include "setup.h"



// ############################################################################################
// INICIALIZACAO
// ############################################################################################


void setup() {
  Serial.begin(115200);
  EEPROM.begin(512);
  delay(10);
  setup_OLED();
  setup_sensor();  
  delay(10);
  initialize();
}

void init_configs(){
  gb_client = false;
  gb_send_data = false;  
}

void initialize(){
  init_configs();
  read_configs();
  screen_logo();
  setup_wifi();
}



void read_configs(){
    ssid = ROM_read(0,32);
    pass = ROM_read(32,64);
}
void save_configs(){
    ROM_save(0,32,ssid);
    ROM_save(32,64,pass);
}

// ############################################################################################
// CONEXAO
// ############################################################################################

void wait_client(){
  if(!gb_client){
    Serial.println("wait");
    WiFiClient client = server.available();
    if (!client) {return;}
    screen_change(0);
    app_client = client;
    gb_client=true;
  }
  else read_client();
}

void read_client(){
  if(!app_client.connected()){
    init_configs();
    screen_change(0);
  }
  if(!app_client.available())return;
  String req = app_client.readStringUntil('|');
  String aux="";
  for(int i=0;i<req.length();i++){
    if(req[i]==';'){
      if(aux=="wifi"){
        read_wifi(req);
      }
      else if(aux=="send"){
        gb_send_data=true;
        gb_read_sensor = true;
        screen_change(1);
      }
      else if(aux=="send_wifi"){
        send_wifi();
      }
      else if(aux=="stop"){
        gb_send_data=false;
      }
      else if(aux=="restart"){
        initialize();
      }
      return;
      }
      else aux+= req[i];
    }
  app_client.flush();
}

// ############################################################################################
// FUNCOES DO PROTOCOLO 
// ############################################################################################

void send_wifi(){
   OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Sending wifi configs"); 
  OLED.display(); 
  sprintf(buf2,"wifi;ssid:%s;pass:%s;|",ssid.c_str(),pass.c_str());
  app_client.print(buf2); 
}

void read_wifi(String req){
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Saving wifi...");
  OLED.display();
  String aux;
  int data_item=0;
  for(int i=0;i<req.length();i++){
    if(req[i]==';'){
      if(data_item==1)ssid=aux;
      else if(data_item==2)pass=aux;
      data_item++;
      aux="";
    }
    else aux+= req[i];
  }
  save_configs();
  delay(1000);
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Wifi Saved!"); 
  OLED.display(); 
}


// ############################################################################################
// SENSOR 
// ############################################################################################

void send_data(){
  if(gb_send_data){   
    if(AcX != 0 || AcY != 0 || AcZ != 0){ 
      sprintf(buf,"data;A:%d:%d:%d|",AcX/500,AcY/500,AcZ/500);
      app_client.print(buf);
       sprintf(buf,"data;G:%d:%d:%d|",GyX/500,GyY/500,GyZ/500);
      app_client.print(buf);
    }
   }
 }

// ############################################################################################
// BATERIA
// ############################################################################################

 void battery_read(){
      float t = analogRead(batteryPin);
      float por = (t-bat_read_min)/(bat_read_max-bat_read_min);
      int t2 = por*100;
      if(buff_battery_i == 100){
        buff_battery_i = 0;
        int bmin = 255;
        int bmax = 0;
        for(int i=0;i<100;i++){
          if(buff_battery[i] > bmax) bmax = buff_battery[i];
          if(buff_battery[i] < bmin) bmin = buff_battery[i];
        }
        //m = m/100;
        if(!is_charging){
            if(bmin>battery_level){
              if(!wifi_to_off)is_charging = true;
              else{
                wifi_to_off = false;
                battery_level = bmin;
              }
              charge_diff = bmax-battery_level;
            } 
            else if(bmax<battery_level  && ! button_click()){
              battery_level = bmax;
              battery_level_show = bmax;
              if(battery_level_show>100)battery_level_show=100;
            }

        }
        else{
            if(bmax+5<battery_level){
              is_charging = false;
              rgb_fade_color(0,0,0,5);
            } 
            else if(bmax>battery_level  && ! button_click()){
              battery_level = bmax;
              por = (bmax-charge_diff)*100/bat_max_charge;
              battery_level_show = por;
              if(battery_level_show>100)battery_level_show=100;
            }

        }
      }
      else{
        buff_battery[buff_battery_i] = t2; 
        buff_battery_i++;
      }
 }

// ############################################################################################
// LOOP
// ############################################################################################




void loop() {
  wait_client();
  read_sensor();
  send_data();
  battery_read();
  button();
  rgb();
  screen();
  delay(5);
  global_tick++;
  if(global_tick==10000)global_tick=0;
}
