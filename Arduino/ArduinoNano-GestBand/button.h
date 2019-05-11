bool button_press;

bool button_click(){
    //Serial.println(digitalRead(buttonPin));
    if(digitalRead(buttonPin)==0){
      return true;
    }
    return false;
}

bool button_up(){
    if(button_tick>5 && button_press){
      if(!button_click()){
        rgb_fade_color(0,0,0,5);
        button_press = false;
        return true;
      }
    }
    return false;
}

void button_down(){
    if(button_click()){
      if(!button_press){
        rgb_fade_random(255);
        button_press = true;
        button_tick=0;
      }
    }
}

// ############################################################################################
// ACTIONS BUTTON
// ############################################################################################
void button_action_click(){
  screen_wake();
        if(menu_active == -1){
          if(screen_active != 2){
             menu_active = screen_active;
          }
          else {
            if(gb_client)app_client.print("profile;|");
            else menu_active = screen_active;
          }
        }
        else{
          menu_next();
        }

}

void button_action_pressed(){
  if(screen_active != 2)screen_shutdown();
      else menu_active = 0;
}

void button(){
    button_down();
    if(button_up()){
      if(button_tick<50)button_action_click();
      button_tick = 0;
    }
    if(button_tick>128)button_action_pressed();
    if(button_press)button_tick++;
}
