#pragma version(1)
#pragma rs java_package_name(u_bordeaux.etu.geese)


rs_allocation picture;
double * filter;
int fside;
int imgWidth;
int imgHeight;


unsigned int RS_KERNEL convolution(unsigned int in, uint32_t x, uint32_t y) {
    unsigned int out = in;
    double coeff;
    int pos;
    float R = 0;
    float G = 0;
    float B = 0;
    double div = 0.0f;
    for (int i = -fside; i <= fside; i++) {
        for (int j = -fside; j <= fside; j++) {
            if (x >= fside && (x + fside)<imgWidth && y >= fside && (y + fside) < imgHeight){
                coeff = filter[j+fside+((i+fside)*(fside*2+1))];
                pos = rsGetElementAt_uint(picture, x+j, y+i);
                R += ((pos >> 16) & 0xff)*coeff;
                G += ((pos >>  8) & 0xff)*coeff;
                B += ((pos      ) & 0xff)*coeff;
                div += coeff;
            }
            else{
                R = ((in >> 16) & 0xff);
                G = ((in >>  8) & 0xff);
                B = ((in      ) & 0xff);
            }

        }
    }
    if (div != 0.0f && div != 1.0){
        R = R/div;
        G = G/div;
        B = B/div;
    }
    R = fmax(0.f,fmin(255.f,R));
    G = fmax(0.f,fmin(255.f,G));
    B = fmax(0.f,fmin(255.f,B));


    out = ((int)(R) & 0xff) << 16 | ((int)(G) & 0xff) << 8 | ((int)(B) & 0xff);

    return out;
}
