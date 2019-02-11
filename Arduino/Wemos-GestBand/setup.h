

void setup_OLED(){
  OLED.begin(SSD1306_SWITCHCAPVCC, 0x3c);
  OLED.clearDisplay();
  //Add stuff into the 'display buffer'
  OLED.setTextWrap(false);
  OLED.setTextSize(1);
  OLED.setTextColor(WHITE);
  //OLED.startscrollleft(0x00, 0x0F); //make display scroll 
  OLED.setCursor(0,0);
  OLED.display(); //output 'display buffer' to screen    
}

void setup_sensor(){
  //Wire.begin(sdaPin,sclPin);
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);
}

void setup_wifi_client(){
  wifi_access_point = false;
  server.begin();
  WiFi.mode(WIFI_STA);
}

void setup_wifi_server(){
      wifi_access_point = true;
      server.begin();
      WiFi.mode(WIFI_AP);
      WiFi.softAP(ssid_server, pass_server);
}


void setup_wifi(){
  WiFi.begin(ssid.c_str(), pass.c_str());
  counter(1200);
  while (WiFi.status() != WL_CONNECTED) {
    if(!counter(0) && !button_click()){
      if((counter_ticks+1)%400==0){
        clear_pixel(32,24,128-32,8);
        OLED.setCursor(32,24);
        OLED.print(ssid);
        OLED.display();
      }
      else if((counter_ticks+1)%100==0){
        OLED.print(".");
        OLED.display();
      }
      delay(10);
    }
    else{
      clear_pixel(32,24,128-32,8);
      OLED.setCursor(32,24);
      OLED.print("FAILED!");
      OLED.display();
      delay(2000);
      clear_pixel(32,24,128-32,8);
      OLED.setCursor(32,24);
      OLED.println("Creating Wifi...");
      OLED.display();
      delay(2000);
      setup_wifi_server();
      return;
    }
  }
  setup_wifi_client();
}
