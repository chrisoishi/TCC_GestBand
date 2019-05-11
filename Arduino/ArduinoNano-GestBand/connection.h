
void read_client(){
////  if(!app_client.connected()){
////    reset_vars();
////  }
//  if(!app_client.available())return;
//  Serial.print("gsdg");
//  char c = (char)app_client.read();
//  data_protocol += c;
//  if(c == '|'){
//  String aux="";
////  for(int i=0;i<data_protocol.length();i++){
////    if(data_protocol[i]==';'){
////      if(aux=="wifi"){
////        //protocol_read_wifi();
////      }
////      else if(aux=="send"){
////        gb_send_data=true;
////        gb_read_sensor = true;
////        screen_change(1);
////      }
////      else if(aux=="send_wifi"){
////        //protocol_send_wifi();
////      }
////      else if(aux=="stop"){
////        gb_send_data=false;
////      }
////      else if(aux=="restart"){
////        initialize();
////      }
////      else if(aux=="profile"){
////        protocol_read_profile(data_protocol);
////      }
////      data_protocol = "";
////      return;
////      }
////      else aux+= data_protocol[i];
////    }
  //app_client.flush();
 // }
}


void wait_client(){
  if(digitalRead(btStatePin) == 1){
      gb_client = true;
      //Serial.print("connected");
  }
  else {
    gb_client = false;
    //Serial.print("desconnected");
  }
  if(gb_client)read_client();
}
