// #### OLED ####
#define OLED_RESET 0  // GPIO0
Adafruit_SSD1306 OLED(screenWidth,screenHeight,&Wire,OLED_RESET);

// #### BATERIA ####
const int8_t bat_max_charge = 110;//Porcento
const float bat_read_max = 4.2*0.49/3.3*1024;// 3.95V -> 100%
const float bat_read_min = 3.6*0.49/3.3*1024;//  3.60V - > 0%;
const float bat_min_por = bat_read_min/bat_read_max;
const float bat_range = 0.28;
int8_t buff_battery[100];
int8_t buff_battery_i;
int8_t charge_diff = 0;
int8_t battery_level = 255;
int8_t battery_level_show = 1;
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




// #### PROTOCOLO ####
bool gb_client = false;
bool gb_send_data = false;
bool gb_read_sensor = false;
String data_protocol;
// #### OUTROS ####
int button_tick;
int global_tick;
bool screen_off = false;
bool wifi_to_off = false;

// #### PROFILE ####
String profile = "";

// #### MENU ####
const String menu_title[] = {"Home","Sensor","App Control","Infos"};
