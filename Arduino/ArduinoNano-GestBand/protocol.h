

// ############################################################################################
// SEND
// ############################################################################################

void protocol_send_wifi() {}

void protocol_send_data_sensor() {
  if (gb_send_data) {
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


void protocol_read_wifi(String req) { }


void protocol_read_profile(String req) {
  String aux;
  int data_item = 0;
  for (int i = 0; i < req.length(); i++) {
    if (req[i] == ';') {
      if (data_item == 1) {
        profile = aux;
        Serial.println(req);
        Serial.println(aux);
        return;
      }
      data_item++;
      aux = "";
    }
    else aux += req[i];
  }
}

String protocol_clear() {
  for (int i = 0; i < sizeof(data_protocol) / sizeof(data_protocol[0]); i++) {
    data_protocol[i] = 0;
  }
  for (int i = 0; i < sizeof(data_protocol_action) / sizeof(data_protocol_action[0]); i++) {
    data_protocol_action[i] = 0;
  }
  data_protocol_i = 0;
}

void protocol_read() {
  for (int i = 0; i < 20; i++) {
    if (data_protocol[i] == ';') {
      return;
    }
    else data_protocol_action[i] = data_protocol[i];
  }
}

String protocol_run() {
  protocol_read();
  Serial.println(data_protocol_action);
  if (strstr(data_protocol_action,"send") != NULL) {
    Serial.print("dgdsg");
    gb_send_data = true;
    gb_read_sensor = true;
    screen_change(1);
  }
  else if (strstr(data_protocol_action,"stop") != NULL) {
    gb_send_data = false;
  }
//  else if (data_protocol_action == "restart") {
//    initialize();
//  }
//  else if (data_protocol_action == "profile") {
//    protocol_read_profile(data_protocol);
//  }
  protocol_clear();
}
