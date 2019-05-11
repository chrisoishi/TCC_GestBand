#include <SoftwareSerial.h>

// #### PINOS ####
const int batteryPin = A3;
const int buttonPin = 8;
const int redPin = 9;
const int greenPin = 10;
const int bluePin = 11;
const int btStatePin = 3;


// #### OLED ####
int screenWidth = 128;
int screenHeight = 32;

// #### WIFI ####
SoftwareSerial app_client(4,5);
