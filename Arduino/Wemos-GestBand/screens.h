

int tick_top_bar = 0;
int tick_screen = 0;
int screen_active = 0;
bool screen_refresh = true;
bool screen_update = false;


void clear_pixel(int x,int y,int width,int height){
  for(int i=x;i<x+width;i++){
    for(int j=y;j<y+height;j++){
      OLED.drawPixel(i, j, BLACK); 
    }
  }
}


void draw(int x,int y,int width,int height){
  for(int i=x;i<x+width;i++){
    for(int j=y;j<y+height;j++){
      OLED.drawPixel(i, j, WHITE); 
    }
  }
}

void text_center(int y,String text){
    int x = (128-(text.length()*6))/2;
    OLED.setCursor(x,y);
    OLED.print(text);
}


void screen_change(int s){
  screen_active = s;
  screen_refresh = true;
  tick_screen = 0;
  gb_read_sensor = false;
}

void screen_next(){
  screen_active++;
  if(screen_active==2)screen_active=0;
  screen_change(screen_active);
}

void screen_shutdown(){
  OLED.clearDisplay();
  screen_off = true;
  wifi_to_off = true;
  OLED.display();
  if(!gb_send_data){
    WiFi.forceSleepBegin();
    rgb_set_color(0,0,0);
    delay(1000);
  }
}
void screen_wake(){
   if(screen_off){
          screen_off = false;
          WiFi.forceSleepWake();
        if(!gb_client){
             if(!wifi_access_point){
                //WiFi.mode(WIFI_STA);
                WiFi.begin(ssid.c_str(), pass.c_str());
             }
             else{
                //WiFi.mode(WIFI_AP);
                WiFi.softAP(ssid_server, pass_server);
             }
        }
   }
}

void screen_logo(){
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.drawBitmap(0, 16, bmp_gestband, 128, 32, 1);
  //OLED.display();
}

void screen_top_bar(){
  clear_pixel(0,0,128,8);
  OLED.setCursor(0,0);
  if(button_tick<50){
  if(!gb_client){
    if(tick_top_bar<5){
      draw(tick_top_bar,0,3,3);
      draw(5-tick_top_bar%5,5,3,3);
      OLED.setCursor(9,0);
      OLED.print(" Waiting");
    }
    else if(tick_top_bar<10){
      draw(5,tick_top_bar%5,3,3);
      draw(0,5-tick_top_bar%5,3,3);
      OLED.setCursor(9,0);
      OLED.print(" Connection");
    }
  }
  else if(gb_send_data){
    if(tick_top_bar<5){
      draw(tick_top_bar,0,3,3);
      draw(5-tick_top_bar%5,5,3,3);
    }
    else if(tick_top_bar<10){
      draw(5-tick_top_bar%5,0,3,3);
      draw(tick_top_bar%5,5,3,3);
    }
    OLED.setCursor(9,0);
    OLED.print(" Sending");
  }
  else{
    if(tick_top_bar<5){
      draw(0,tick_top_bar,3,3);
      draw(5,tick_top_bar,3,3);
    }
    else if(tick_top_bar<10){
      draw(0,5-tick_top_bar%5,3,3);
      draw(5,5-tick_top_bar%5,3,3);
    }
    OLED.setCursor(9,0);
    OLED.print(" Connected");
  }
  
  
  if(is_charging)OLED.drawBitmap(105, 0, bmp_charge, 8, 8, 1);
  if(battery_level_show>0 || tick_top_bar%2==0){
      OLED.drawBitmap(112, 0, bmp_battery, 16, 8, 1);
      draw(117,1,battery_level_show/10,6);
  }

  tick_top_bar++;
  tick_top_bar=tick_top_bar%10;
  //OLED.display();
  }
  else{
    draw(0,0,button_tick,8);
  }
  screen_update = true;
}

void screen_ip(){
  clear_pixel(0,8,128,56);
  
  if(wifi_access_point){
    text_center(27,"WiFi: GestBand");
    text_center(37,"GB IP:"+WiFi.softAPIP().toString());
  }
  else {
    if(!gb_client)text_center(27,"Connect APP to");
    else text_center(27,"GestBand");
    text_center(37,"IP:"+WiFi.localIP().toString());
  }
  screen_refresh = false;
  //OLED.display();
}

void screen_sensor(){
  gb_read_sensor = true;
  screen_update = true;
  clear_pixel(0,8,128,56);
  OLED.setCursor(0,27);
  OLED.print("Ac:");
  OLED.setCursor(20,27);
  OLED.print(AcX);
  OLED.setCursor(50,27);
  OLED.print(AcY);
  OLED.setCursor(80,27);
  OLED.print(AcZ);
  
  OLED.setCursor(0,37);
  OLED.print("Gy:");
  OLED.setCursor(20,37);
  OLED.print(GyX);
  OLED.setCursor(50,37);
  OLED.print(GyY);
  OLED.setCursor(80,37);
  OLED.print(GyZ);
  
  //OLED.display();
  //screen_refresh = false;
  
}

void screen(){
  if(!screen_off){
    if(tick_screen%15==0){
      screen_top_bar();
      if(screen_refresh){
        if(screen_active==0)screen_ip();
        else if(screen_active==1)screen_sensor();
      }
      OLED.display();
    }

  }
  tick_screen++;
  if(tick_screen==2000)tick_screen=0;
}
