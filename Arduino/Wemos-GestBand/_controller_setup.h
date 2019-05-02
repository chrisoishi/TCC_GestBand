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
        clear_pixel(32,40,128-32,8);
        OLED.setCursor(32,40);
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
      clear_pixel(32,40,128-32,8);
      OLED.setCursor(32,40);
      OLED.print("FAILED!");
      OLED.display();
      delay(2000);
      clear_pixel(32,40,128-32,8);
      OLED.setCursor(32,40);
      OLED.println("Creating Wifi...");
      OLED.display();
      delay(2000);
      setup_wifi_server();
      return;
    }
  }
  setup_wifi_client();
}



void setup_controller(){
  screen_logo(16);
  setup_wifi();
}
