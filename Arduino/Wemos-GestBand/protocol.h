// ############################################################################################
// SEND
// ############################################################################################

void protocol_send_wifi(){
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Sending wifi configs"); 
  OLED.display(); 
  sprintf(buf2,"wifi;ssid:%s;pass:%s;|",ssid.c_str(),pass.c_str());
  app_client.print(buf2); 
}

void protocol_send_data_sensor(){
  if(gb_send_data){   
      app_client.print("data;A:");
      app_client.print(AcX);
      app_client.print(":");
      app_client.print(AcY);
      app_client.print(":");
      app_client.print(AcZ);
      app_client.print("|");
      app_client.print("data;G:");
      app_client.print(GyX);
      app_client.print(":");
      app_client.print(GyY);
      app_client.print(":");
      app_client.print(GyZ);
      app_client.print("|");
   }
 }


// ############################################################################################
// READ
// ############################################################################################


void protocol_read_wifi(String req){
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Saving wifi...");
  OLED.display();
  String aux;
  int data_item=0;
  for(int i=0;i<req.length();i++){
    if(req[i]==';'){
      if(data_item==1)ssid=aux;
      else if(data_item==2)pass=aux;
      data_item++;
      aux="";
    }
    else aux+= req[i];
  }
  save_configs();
  delay(1000);
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Wifi Saved!"); 
  OLED.display(); 
}


void protocol_read_profile(String req){
  String aux;
  int data_item=0;
  for(int i=0;i<req.length();i++){
    if(req[i]==';'){
      if(data_item==1){
        profile=aux;
        Serial.println(req);
        Serial.println(aux);
        return;
      }
      data_item++;
      aux="";
    }
    else aux+= req[i];
  }
}
