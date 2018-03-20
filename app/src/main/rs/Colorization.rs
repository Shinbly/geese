#pragma version(1)
#pragma rs java_package_name(u_bordeaux.etu.geese)

float colorizationValue = 0.0f;


unsigned int RS_KERNEL Hue(unsigned int in, uint32_t x, uint32_t y) {

  unsigned int out = in;

    //RGB to HSV
  int R = ((in >> 16) & 0xff);
  int G = ((in >>  8) & 0xff);
  int B = ((in      ) & 0xff);

  float fR = (float)R/255.0f;
  float fG = (float)G/255.0f;
  float fB = (float)B/255.0f;
  float Cmax= fmax(fR,fmax(fG,fB));
  float Cmin= fmin(fR,fmin(fG,fB));
  float delta = Cmax-Cmin;
  float hue = 0.0f;
  float sat = 0.0f;
  float val = 0.0f;

  if (delta == 0.0f){
    hue = 0.0f;
  }

  else if (fR == Cmax){
      hue = fG-fB;
      hue = hue/delta;
      hue = fmod(hue,6.0f);
    }
    else if(fG == Cmax){
        hue = fB-fR;
        hue = hue/delta;
        hue+= 2.0f;

      }
      else if (fB == Cmax) {
            hue = fR-fG;
            hue = hue/delta;
            hue+= 4.0f;

        }

hue=  hue * 60.f;

  if (Cmax == 0.0f){
    sat = 0.0f;
  }
  else{
    sat =(delta/Cmax);
  }

  val = Cmax;

  //process
  hue = colorizationValue;


  // HSV to RGB
  float C = val*sat;
  float X = C *(1.0f- fabs(fmod((hue/60.0f),2.0f)-1.0f));
  float m = val-C;

  if (hue<60.0f){
    fR=C;
    fG=X;
    fB=0.0f;

  }
  else if (hue<120.0f){
        fR=X;
        fG=C;
        fB=0.0f;
    }
    else if (hue<180.0f){
            fR=0.0f;
            fG=C;
            fB=X;
        }
        else if (hue<240.0f){
                fR=0.0f;
                fG=X;
                fB=C;
            }
            else if (hue<300.0f){
                    fR=X;
                    fG=0.0f;
                    fB=C;
                }
                else if (hue<360.0f){
                        fR=C;
                        fG=0.0f;
                        fB=X;
                    }

  R=(int)((float)(fR+m)*255.0f);
  G=(int)((float)(fG+m)*255.0f);
  B=(int)((float)(fB+m)*255.0f);

  out = (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
  return out;
}
