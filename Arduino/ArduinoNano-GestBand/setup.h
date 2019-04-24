int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;

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
