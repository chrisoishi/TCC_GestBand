#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include<Wire.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <EEPROM.h>

// #### OLED ####
#define OLED_RESET 0  // GPIO0
Adafruit_SSD1306 OLED(OLED_RESET);


// #### Sensores ####
const int sclPin = D3;
const int sdaPin = D4;
const int MPU_addr=0x68;  // I2C address of the MPU-6050
int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;
//int16_t AcX2,AcY2,AcZ2,Tmp2,GyX2,GyY2,GyZ2;
char buf[10];

char buf2[256];



//SSID of your network
String ssid = ""; //SSID of your Wi-Fi router
String pass = ""; //Password of your Wi-Fi router
String ip = "";
char ssid_server[] = "GestBand"; //SSID of your Wi-Fi router
char pass_server[] = "inyourcontrol"; //Password of your Wi-Fi router
WiFiClient app_client;
WiFiServer server(3322);

bool gb_client = false;
bool gb_send_data = false;

void read_configs(){
    for (int i = 0; i < 32; ++i) {
      ssid += char(EEPROM.read(i));
    }
    for (int i = 32; i < 96; ++i) {
      pass += char(EEPROM.read(i));
    }
}


void save_configs(){
    for(int i=0;i<ssid.length();i++){
      Serial.print(ssid[i]);
      EEPROM.write(i, ssid[i]);
    }
    for(int i=0;i<pass.length();i++){
      EEPROM.write(i+32, pass[i]);
    }
    EEPROM.commit();
}

void setup_wifi(){
  gb_client = false;
  gb_send_data = false;
  int wifi_wait = 0;
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Connecting to wifi");
  OLED.println(ssid);
  OLED.display();

  WiFi.begin(ssid.c_str(), pass.c_str());

  while (WiFi.status() != WL_CONNECTED) {
    if(wifi_wait < 2){
      delay(1000);
      OLED.print(".");
      OLED.display();
      wifi_wait++;
    }
    else{
      OLED.clearDisplay();
      OLED.setCursor(0,0);
      OLED.println("Connecting failed");
      OLED.println("Creating Wifi...");
      OLED.display();
      delay(2000);
      setup_server();
      return;
    }

  }
  server.begin();
  OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Gestband");
  OLED.print("IP:");
  OLED.println( WiFi.localIP());
  OLED.println("Wait Connection...");
}
void setup_server(){
      OLED.clearDisplay();
      server.begin();
      OLED.setCursor(0,0);
      OLED.println("Server Gestband");
      WiFi.mode(WIFI_AP);
      WiFi.softAP(ssid_server, pass_server);
      OLED.print("IP:");
      OLED.println( WiFi.softAPIP());
      OLED.println("Wait Connection...");
      OLED.display();
}
void wait_client(){
  if(!gb_client){
    Serial.println("wait");
    WiFiClient client = server.available();
    if (!client) {return;}
    OLED.clearDisplay();
    OLED.setCursor(0,0);
    OLED.println("App Connected");
    OLED.display();
    app_client = client;
    gb_client=true;
  }
  else read_client();
}

void read_client(){
  if(!app_client.connected())setup_wifi();
  if(!app_client.available())return;
  String req = app_client.readStringUntil('|');
  String aux="";
  for(int i=0;i<req.length();i++){
    if(req[i]==';'){
      if(aux=="wifi"){
        read_wifi(req);
      }
      else if(aux=="send"){
        gb_send_data=true;
      }
      else if(aux=="send_wifi"){
        send_wifi();
      }
      else if(aux=="stop"){
        gb_send_data=false;
      }
      else if(aux=="restart"){
        setup_wifi();
      }
      return;
      }
      else aux+= req[i];
    }
  app_client.flush();
}

void read_wifi(String req){
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
void send_wifi(){
   OLED.clearDisplay();
  OLED.setCursor(0,0);
  OLED.println("Sending wifi configs"); 
  OLED.display(); 
  sprintf(buf2,"wifi;ssid:%s;pass:%s;|",ssid.c_str(),pass.c_str());
  app_client.print(buf2); 
}

void setup_OLED(){
  OLED.begin(SSD1306_SWITCHCAPVCC, 0x3c);
  OLED.clearDisplay();
 
  //Add stuff into the 'display buffer'
  OLED.setTextWrap(false);
  OLED.setTextSize(1);
  OLED.setTextColor(WHITE);
  //OLED.startscrollleft(0x00, 0x0F); //make display scroll 
  OLED.setCursor(0,0);
  
  OLED.display(); //output 'display buffer' to screen    
}

void setup_sensor(){
  //Wire.begin(sdaPin,sclPin);
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);
}

void send_data(){
  if(gb_send_data){
    Wire.beginTransmission(MPU_addr);
    Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
    Wire.endTransmission(false);
    Wire.requestFrom(MPU_addr,14,true);  // request a total of 14 registers
    AcX=Wire.read()<<8|Wire.read();  // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)    
    AcY=Wire.read()<<8|Wire.read();  // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
    AcZ=Wire.read()<<8|Wire.read();  // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
    Tmp=Wire.read()<<8|Wire.read();  // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
    GyX=Wire.read()<<8|Wire.read();  // 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
    GyY=Wire.read()<<8|Wire.read();  // 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
    GyZ=Wire.read()<<8|Wire.read();  // 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)
  
    
    if(AcX != 0 || AcY != 0 || AcZ != 0){ 
      OLED.clearDisplay();
      OLED.setCursor(0,5);
  
      OLED.println(AcX/500);
      OLED.println(AcY/500);
      OLED.println(AcZ/500);
  
      sprintf(buf,"data;A:%d:%d:%d|",AcX/500,AcY/500,AcZ/500);
      app_client.print(buf);
       sprintf(buf,"data;G:%d:%d:%d|",GyX/500,GyY/500,GyZ/500);
      app_client.print(buf);
     // OLED.print(" | Tmp = "); OLED.println(Tmp/340.00+36.53);  //equation for temperature in degrees C from datasheet
      OLED.display();
      delay(10);
  
    }
   }
 }

void setup() {
  EEPROM.begin(512);
  delay(10);
  read_configs();
  Serial.begin(115200);
  setup_OLED();
  setup_sensor();  
  delay(10);
  setup_wifi();
}

void loop() {
  wait_client();
  send_data();
}
