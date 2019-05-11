#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include<Wire.h>
#include "includes/bitmaps.h"
#include "includes/extras.h";
#include "_controller_vars.h"
#include "vars.h"
#include "_controller_functions.h"

#include "ROM.h"
#include "RGB.h"
#include "Sensor.h"
#include "screens.h"
#include "button.h"
#include "_controller_setup.h"
#include "setup.h"
#include "Protocol.h"
#include "connection.h"
#include "battery.h"


// ############################################################################################
// INICIALIZACAO
// ############################################################################################


void setup() {
  Serial.begin(115200);
  //EEPROM.begin(512);
  app_client.begin(115200);
  delay(10);
  setup_all();
  delay(10);
  initialize();
  
}

void loop() {
  wait_client();
  read_sensor();
  protocol_send_data_sensor();
  battery_read();
  button();
  rgb();
  screen();
  delay(5);
  global_tick++;
  if(global_tick==10000)global_tick=0;
}
