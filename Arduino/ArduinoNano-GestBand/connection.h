
void read_client() {
  if (!app_client.available())return;
  char c =  (char)app_client.read();
  data_protocol[data_protocol_i] = c;
  //Serial.println(c);
  data_protocol_i++;
  if (c == '|' || data_protocol_i==50) {
    Serial.println(data_protocol);
    protocol_run();
  }
}


void wait_client() {
  //Serial.println(digitalRead(btStatePin));
  if (digitalRead(btStatePin) == 1) {
    gb_client = true;
    //Serial.print("connected");
  }
  else {
    gb_client = false;
    //Serial.print("desconnected");
  }
  if (gb_client)read_client();
}
