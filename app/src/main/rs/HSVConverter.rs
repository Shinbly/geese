#pragma version(1)
#pragma rs java_package_name(u_bordeaux.etu.geese)


unsigned int RS_KERNEL rgbToHsv(unsigned int in, uint32_t x, uint32_t y) {
  int R = ((in >> 16) & 0xff);
  int G = ((in >>  8) & 0xff);
  int B = ((in      ) & 0xff);
  int Cmax= max(R,max(G,B));
  int Cmin= min(R,min(G,B));
  float delta = Cmax-Cmin;
  float hue;
  float sat;
  float val;

  if (delta == 0){
    hue = 0;
  }
  else{
    if (R == Cmax){
      hue = G-B;
      hue = (float)hue/(float)delta;
      hue = fmod(hue,6.0f);
    }
    else{
      if(G == Cmax){
        hue = B-R;
        hue = (float)hue/(float)delta;
        hue+= 2.0f;
      }
      else{
        if (B == Cmax) {
            hue = R-G;
            hue = (float)hue/(float)delta;
            hue+= 4.0f;
        }
      }
    }
    hue*=60.f;
  }
  if (Cmax == 0){
    sat = 0;
  }
  else{
    sat = (float)(100.f*(float)delta/(float)Cmax);
  }
  val = (float)((float)Cmax*100.f/255.f);
  hue= (int)(hue*5);
  val = (int)(val*5);
  sat = (int)(sat*5);
  unsigned int out = hue*1000000+ sat * 1000 + val;
   // out = ((int)hue & 0x1FF) << 16 | ((int)sat & 0x7F) << 8 | ((int)val & 0x7F);
  return out;
}
