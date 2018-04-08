#pragma version(1)
#pragma rs java_package_name(u_bordeaux.etu.geese)


rs_allocation picture;
double * filter;
int fSize;
int imgWidth;
int imgHeight;


unsigned int RS_KERNEL convolution(unsigned int in, uint32_t x, uint32_t y) {
    unsigned int out;
    double coeff;
    int side = fSize/2;
    float R = 0;
    float G = 0;
    float B = 0;
    float div = 0.0f;
    for (int i = -side; i <= side; i++) {
        for (int j = -side; j <= side; j++) {
            if (x >= side && (x+side)<=imgWidth && y >= side && (y +side) <= imgHeight){
                coeff = filter[((j+side)*fSize)+i+side];
                out = rsGetElementAt_uint(picture, x+i, y+j);
                R += ((out >> 16) & 0xff)*coeff;
                G += ((out >>  8) & 0xff)*coeff;
                B += ((out      ) & 0xff)*coeff;
                div += coeff;
            }
        }
    }
    if (div == 0){div = 1;}
    out = ((int)(R/div) & 0xff) << 16 | ((int)(G/div) & 0xff) << 8 | ((int)(B/div) & 0xff);
    return out;
}
