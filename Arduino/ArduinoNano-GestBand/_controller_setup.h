void setup_controller(){
  pinMode(batteryPin,INPUT);
  pinMode(buttonPin,INPUT);
  digitalWrite(buttonPin,HIGH);
  //pinMode(redPin,OUTPUT);
  //pinMode(greenPin,OUTPUT);
  //pinMode(bluePin,OUTPUT);
  pinMode(btStatePin,INPUT);
  //digitalWrite(btStatePin,HIGH);
  //screen_logo(0);
  text_center(12,"GestBand");
  OLED.display();
  delay(1000);
  //setup_wifi();
}
