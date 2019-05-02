int r_set,g_set,b_set;
int r_aux,g_aux,b_aux;
int r_dir,g_dir,b_dir;
int fade_speed = 1;

void rgb_set_color(int r,int g,int b){
    r_set = r;
    g_set = g;
    b_set = b;
    r_aux = r;
    g_aux = g;
    b_aux = b;
    analogWrite(redPin,r);
    analogWrite(greenPin,g);
    analogWrite(bluePin,b);
}

void rgb_fade_color(int r,int g,int b,int s){
    r_set = r;
    g_set = g;
    b_set = b;
   fade_speed = s;

    if(r_set<r_aux)r_dir = -1;
    else r_dir = 1;
    if(g_set<g_aux)g_dir = -1;
    else g_dir = 1;
    if(b_set<b_aux)b_dir = -1;
    else b_dir = 1;

   
}

void rgb_fade_random(int s){
        int r,g,b;
        do{
        r = random(0,5)*50;
        g = random(0,5)*50;
        b = random(0,5)*50;
        }while(r+g+b<150);
        rgb_fade_color(r,g,b,s);
}

bool rgb_fade_loop(){
    if(r_set!=r_aux){
      r_aux+=fade_speed*r_dir;
      if(r_dir==1 && r_aux>r_set || r_dir==-1 && r_aux<r_set)r_aux = r_set;
      analogWrite(redPin,r_aux);
    }
    if(g_set!=g_aux){
      g_aux+=fade_speed*g_dir;
      if(g_dir==1 && g_aux>g_set || g_dir==-1 && g_aux<g_set)g_aux = g_set;
      analogWrite(greenPin,g_aux);
    }
    if(b_set!=b_aux){
      b_aux+=fade_speed*b_dir;
      if(b_dir==1 && b_aux>b_set || b_dir==-1 && b_aux<b_set)b_aux = b_set;
      analogWrite(bluePin,b_aux);
    }

}

void rgb_show_battery(int s){
   float por = (float)battery_level_show/100;
   float r = (1-por)*255;
   float g = por*255;
   rgb_fade_color(r,g,0,s);
}

void rgb_alert(){
    if(!is_charging){
      if(battery_level_show==0){
          if(global_tick%100==0)rgb_fade_color(255,0,0,10);
          else if(global_tick%50==0)rgb_set_color(0,0,0);
      }
      else if(!gb_client && !screen_off){
          if(global_tick%200==0)rgb_fade_color(255,200,0,10);
          else if(global_tick%100==0)rgb_fade_color(0,0,0,20);
      }
      else if(screen_off){
          int t = global_tick%1500;
          if(t==200)rgb_fade_color(0,0,0,2);
          else if(t==0)rgb_show_battery(2);
      }
      else{
        if(global_tick%100==0)rgb_fade_color(0,0,0,20);
      }
    }
    else{
        if(global_tick%100==0)rgb_show_battery(2);
    }
}

void rgb(){
    rgb_fade_loop();
    rgb_alert();
}
