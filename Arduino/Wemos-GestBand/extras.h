bool counter(int t){
  if(t>0){
    counter_ticks = t;
    return false;
  }
  else{
    counter_ticks--;
    return counter_ticks==0?true:false;
  }
}
