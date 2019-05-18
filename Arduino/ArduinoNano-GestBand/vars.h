// #### OLED ####
#define OLED_RESET 0  // GPIO0
Adafruit_SSD1306 OLED(screenWidth,screenHeight,&Wire,OLED_RESET);

// #### BATERIA ####
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
//char buf[10];
//char buf2[256];
int16_t sensor_range = 500;




// #### PROTOCOLO ####
bool gb_client = false;
bool gb_send_data = false;
bool gb_read_sensor = false;
char data_protocol[50];
char data_protocol_action[10];
int8_t data_protocol_i = 0;
// #### OUTROS ####
int button_tick;
int global_tick;
bool screen_off = false;
bool wifi_to_off = false;

// #### PROFILE ####
String profile = "";

// #### MENU ####
const char menu_title[5][10] = {{"Home"},{"Sensor"},{"App"},{"Control"},{"Infos"}};
