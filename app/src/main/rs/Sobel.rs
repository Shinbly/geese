#pragma version(1)
#pragma rs java_package_name(u_bordeaux.etu.geese)


rs_allocation picture;
double mask_v[]= {-1,0,1,-2,0,2,-1,0,1};
double mask_h[]= {-1,-2,-1,0,0,0,1,2,1};
int fSize = 3;
int imgWidth;
int imgHeight;


unsigned int RS_KERNEL sobel(unsigned int in, uint32_t x, uint32_t y) {
    unsigned int out;
    double coeff;
    int side = fSize/2;
    float R = 0;
    float R2 = 0;

    for (int i = -side; i <= side; i++) {
        for (int j = -side; j <= side; j++) {
            if (x >= side && (x + side)<imgWidth && y >= side && (y + side) < imgHeight){

                coeff = mask_v[((j + side) * fSize) + i + side];
                out = rsGetElementAt_uint(picture, x + i, y + j);
                R += ((out >> 16) & 0xff) * coeff;

                coeff = mask_h[((j + side) * fSize) + i + side];
                R2 += ((out >> 16) & 0xff) * coeff;




            }
        }
    }
    R = sqrt((R * R) + (R2 * R2));
    R = fmin(R,255);
    out = ((int)R & 0xff) << 16 | ((int)R & 0xff) << 8 | ((int)R & 0xff);
    return out;
}
