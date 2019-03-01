// #### PINOS ####
const int batteryPin = A0;
//const int sclPin = D1;
//const int sdaPin = D2;
const int buttonPin = D3;
const int redPin = D5;
const int greenPin = D6;
const int bluePin = D7;

// #### OLED ####
#define OLED_RESET 0  // GPIO0
Adafruit_SSD1306 OLED(128,64,&Wire,OLED_RESET);

// #### BATERIA ####
const int bat_max_charge = 110;//Porcento
const float bat_read_max = 4.2*0.49/3.3*1024;// 3.95V -> 100%
const float bat_read_min = 3.6*0.49/3.3*1024;//  3.60V - > 0%;
const float bat_min_por = bat_read_min/bat_read_max;
const float bat_range = 1-bat_min_por;
int buff_battery[100];
int buff_battery_i;
int charge_diff = 0;
int battery_level = 255;
int battery_level_show = 1;
bool is_charging;
bool deep_sleep = false;
// #### LED RGB ####



// #### SENSORES ####
const int MPU_addr=0x68;  // I2C address of the MPU-6050
int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;
//int16_t AcX2,AcY2,AcZ2,Tmp2,GyX2,GyY2,GyZ2;
char buf[10];
char buf2[256];
int sensor_range = 500;

// #### WIFI ####
String ssid = ""; //SSID of your Wi-Fi router
String pass = ""; //Password of your Wi-Fi router
String ip = "";
char ssid_server[] = "GestBand"; //SSID of your Wi-Fi router
char pass_server[] = "inyourcontrol"; //Password of your Wi-Fi router
WiFiClient app_client;
WiFiServer server(3322);
bool wifi_access_point;

// #### PROTOCOLO ####
bool gb_client = false;
bool gb_send_data = false;
bool gb_read_sensor = false;

// #### OUTROS ####
int counter_ticks;
int button_tick;
int global_tick;
bool screen_off = false;
bool wifi_to_off = false;
