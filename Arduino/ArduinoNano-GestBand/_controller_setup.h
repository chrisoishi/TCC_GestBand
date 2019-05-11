void setup_controller(){
  pinMode(batteryPin,INPUT);
  pinMode(buttonPin,INPUT);
  digitalWrite(buttonPin,HIGH);
  //pinMode(redPin,OUTPUT);
  //pinMode(greenPin,OUTPUT);
  //pinMode(bluePin,OUTPUT);
  pinMode(btStatePin,INPUT);
  digitalWrite(btStatePin,LOW);
  //screen_logo(0);
  OLED.setCursor(0,0);
  OLED.print("dsgsgsd");
  OLED.display();
  delay(1000);
  //setup_wifi();
}
