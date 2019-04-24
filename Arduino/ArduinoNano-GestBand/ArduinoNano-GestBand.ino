#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include<Wire.h>
//#include <ESP8266WiFi.h>
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
//#include <ArduinoOTA.h>



// ############################################################################################
// INICIALIZACAO
// ############################################################################################


void setup() {
  Serial.begin(115200);
  //EEPROM.begin(512);
  //ArduinoOTA.begin();
  //delay(10);
  setup_OLED();
  //setup_sensor();  
  //delay(10);
  initialize();
  Serial.println("sdgdsg");
}

void init_configs(){
  gb_client = false;
  gb_send_data = false;  
}

void initialize(){
  //init_configs();
  //read_configs();
  screen_logo();
  //setup_wifi();
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
}

void read_client(){

}

// ############################################################################################
// FUNCOES DO PROTOCOLO 
// ############################################################################################

void send_wifi(){
}

void read_wifi(String req){
}


// ############################################################################################
// SENSOR 
// ############################################################################################

void send_data(){

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
  //ArduinoOTA.handle();
  //wait_client();
  //read_sensor();
  //send_data();
  //battery_read();
  //button();
  //rgb();
  //screen();
  //delay(5);
  //global_tick++;
  //if(global_tick==10000)global_tick=0;
}
