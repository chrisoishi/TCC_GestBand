#include <SoftwareSerial.h>

// #### PINOS ####
const int8_t batteryPin = A2;
const int8_t buttonPin = 7;
const int8_t redPin = 9;
const int8_t greenPin = 10;
const int8_t bluePin = 11;
const int8_t btStatePin = 2;


// #### OLED ####
int8_t screenWidth = 128;
int8_t screenHeight = 32;

// #### WIFI ####
SoftwareSerial app_client(4,5);
