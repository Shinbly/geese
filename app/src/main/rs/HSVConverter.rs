#pragma version(1)
#pragma rs java_package_name(u_bordeaux.etu.geese)


unsigned int RS_KERNEL rgbToHsv(unsigned int in, uint32_t x, uint32_t y) {

   int Red = (in >> 16) & 0xff;
   int Green = (in >>  8) & 0xff;
   int Blue = (in      ) & 0xff;
   float R = Red/255;
   float G = Green/255;
   float B = Blue/255;
   float Cmax= max(R,max(G,B));
   float Cmin= min(R,min(G,B));
   float delta = Cmax-Cmin;
   float hue;
   float sat;
   float val;

   if (delta == 0){
        hue = 0;
   }
   else{
       if (R == Cmax){
            hue = 60 * (fmod((float)((G-B)/delta),6.f));
       }
       else {
            if(G == Cmax){
                hue = 60 * (((B-R)/delta)+2 );
            }
            else {
                hue = 60 * (((R-G)/delta))+4;
            }
       }
   }
   if (Cmax == 0){
        sat = 0;
   }
   else{
        sat = 100* delta / Cmax;
   }
   val = Cmax*100;
   unsigned int out = (int)hue*1000000+ (int)sat * 1000 + (int)val;

   // out = ((int)hue & 0x1FF) << 16 | ((int)sat & 0x7F) << 8 | ((int)val & 0x7F);



  return out;
}