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

void reset_vars(){
  gb_client = false;
  gb_send_data = false;  
}

void setup_all(){
  reset_vars();
  setup_OLED();
  setup_sensor();  
}

void initialize(){
  reset_vars();
  read_configs();
  setup_controller();
}
  
