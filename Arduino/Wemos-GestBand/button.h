
bool button_press;

bool button_click(){
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

void button(){
    button_down();
    if(button_up()){
      if(button_tick<50){
        screen_wake();
        screen_next();
        
      }
      button_tick = 0;
    }
    if(button_tick>128){
      screen_shutdown();
    }
    if(button_press)button_tick++;
}
