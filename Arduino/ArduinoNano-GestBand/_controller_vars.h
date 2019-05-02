#include <ESP8266WiFi.h>
#include <WiFiClient.h>

// #### PINOS ####
const int batteryPin = A0;
const int buttonPin = D3;
const int redPin = D5;
const int greenPin = D6;
const int bluePin = D7;


// #### OLED ####
int screenWidth = 128;
int screenHeight = 64;

// #### WIFI ####
WiFiClient app_client;
WiFiServer server(3322);
