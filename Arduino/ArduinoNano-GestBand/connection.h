void read_client(){
  if(!app_client.connected()){
    reset_vars();
  }
  if(!app_client.available())return;
  String req = app_client.readStringUntil('|');
  String aux="";
  for(int i=0;i<req.length();i++){
    if(req[i]==';'){
      if(aux=="wifi"){
        protocol_read_wifi(req);
      }
      else if(aux=="send"){
        gb_send_data=true;
        gb_read_sensor = true;
        screen_change(1);
      }
      else if(aux=="send_wifi"){
        protocol_send_wifi();
      }
      else if(aux=="stop"){
        gb_send_data=false;
      }
      else if(aux=="restart"){
        initialize();
      }
      else if(aux=="profile"){
        protocol_read_profile(req);
      }
      return;
      }
      else aux+= req[i];
    }
  app_client.flush();
}


void wait_client(){
  if(!gb_client){
    WiFiClient client = server.available();
    if (!client) {return;}
    screen_change(0);
    app_client = client;
    gb_client=true;
  }
  else read_client();
}
