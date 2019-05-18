void battery_read() {
  const int8_t bat_max_charge = 110;//Porcento
  const float bat_read_max = 4.2 * 0.49 / 3.3 * 1024; // 3.95V -> 100%
  const float bat_read_min = 3.6 * 0.49 / 3.3 * 1024; //  3.60V - > 0%;
  const float bat_min_por = bat_read_min / bat_read_max;
  const float bat_range = 0.28;

  float t = analogRead(batteryPin);
  float por = (t - bat_read_min) / (bat_read_max - bat_read_min);
  int t2 = por * 100;
  if (buff_battery_i == 100) {
    buff_battery_i = 0;
    int bmin = 255;
    int bmax = 0;
    for (int i = 0; i < 100; i++) {
      if (buff_battery[i] > bmax) bmax = buff_battery[i];
      if (buff_battery[i] < bmin) bmin = buff_battery[i];
    }
    //m = m/100;
    if (!is_charging) {
      if (bmin > battery_level) {
        if (!wifi_to_off)is_charging = true;
        else {
          wifi_to_off = false;
          battery_level = bmin;
        }
        charge_diff = bmax - battery_level;
      }
      else if (bmax < battery_level  && ! button_click()) {
        battery_level = bmax;
        battery_level_show = bmax;
        if (battery_level_show > 100)battery_level_show = 100;
      }

    }
    else {
      if (bmax + 5 < battery_level) {
        is_charging = false;
        rgb_fade_color(0, 0, 0, 5);
      }
      else if (bmax > battery_level  && ! button_click()) {
        battery_level = bmax;
        por = (bmax - charge_diff) * 100 / bat_max_charge;
        battery_level_show = por;
        if (battery_level_show > 100)battery_level_show = 100;
      }

    }
  }
  else {
    buff_battery[buff_battery_i] = t2;
    buff_battery_i++;
  }
}
