int tick_top_bar = 0;
int tick_screen = 0;
int tick_menu = 0;
int screen_active = 0;
int menu_active = -1;
bool screen_refresh = true;
bool screen_update = false;
bool onInit = false;


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

void text_center_offset(int offset,int y,String text){
    int x = (128-(text.length()*6)-offset)/2;
    OLED.setCursor(x+offset,y);
    OLED.print(text);
}


void screen_change(int s){
  screen_active = s;
  screen_refresh = true;
  tick_screen = 0;
  gb_read_sensor = false;
  onInit = true;
}

void menu_next(){
  menu_active++;
  tick_menu = 0;
  if(menu_active==3)menu_active=0;
  screen_change(screen_active);
}
void menu_await(){
  if(menu_active > -1){
    if(tick_menu>=15){
       screen_change(menu_active);
       menu_active = -1;
       tick_menu = 0;
    }
    else tick_menu++;
    Serial.println(tick_menu);
  }
}

void screen_shutdown(){
  OLED.clearDisplay();
  screen_off = true;
  wifi_to_off = true;
  OLED.display();
  if(!gb_send_data){
    controller_economy(true);
    rgb_set_color(0,0,0);
    delay(1000);
  }
}
void screen_wake(){
   if(screen_off){
     screen_off = false;
     controller_economy(false);
   }
}
// ############################################################################################
// SCREENS
// ############################################################################################

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



void screen_logo(int y){
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.drawBitmap(0, y, bmp_gestband, 128, 32, 1);
  //OLED.display();
}

void screen_menu(){
  int y = (screenHeight-24)/2;
  OLED.clearDisplay();
   OLED.setCursor(0,0);
   if(menu_active==0)OLED.drawBitmap(10, y, icon_home, 24,24, 1);
   else if(menu_active==1)OLED.drawBitmap(10, y, icon_gesture, 24,24, 1);
   else if(menu_active==2)OLED.drawBitmap(10, y, icon_profile, 24,24, 1);
   else if(menu_active==3)OLED.drawBitmap(10, y, icon_info, 24,24, 1);
   text_center_offset(34,y+10,menu_title[menu_active]);
}

void screen_connect(){
  clear_pixel(0,8,128,screenHeight-8);
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
  clear_pixel(0,8,128,screenHeight-8);
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
void screen_profile(){
  clear_pixel(0,8,128,screenHeight-8);
  if(gb_client){
    int y = (screenHeight-8-20)/2;
    if(onInit){
      app_client.print("profile;|"); 
      onInit = false;
    }
    text_center(y,"Controlling"); 
    text_center(y+10,profile);   
  }
  else{
    int y = (screenHeight-16)/2;
    text_center(y,"First, connect to app"); 
  }
}



// ############################################################################################
// LOOP
// ############################################################################################

void screen(){
  if(!screen_off){
    if(tick_screen%15==0){
      if(menu_active==-1){
        screen_top_bar();
        if(screen_refresh){
          if(screen_active==0)screen_connect();
          else if(screen_active==1)screen_sensor();
          else if(screen_active==2)screen_profile();
        }
      }
      else screen_menu();
      menu_await();
      OLED.display();
    }

  }
  tick_screen++;
  if(tick_screen==2000)tick_screen=0;
}
