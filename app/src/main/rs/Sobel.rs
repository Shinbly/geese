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
    float G = 0;
    float B = 0;
    float R2 = 0;
    float G2 = 0;
    float B2 = 0;

    for (int i = -side; i <= side; i++) {
        for (int j = -side; j <= side; j++) {
            if (x >= side && (x+side)<=imgWidth && y >= side && (y +side) <= imgHeight){
                coeff = mask_v[((j+side)*fSize)+i+side];
                out = rsGetElementAt_uint(picture, x+i, y+j);
                R += ((out >> 16) & 0xff)*coeff;
                G += ((out >>  8) & 0xff)*coeff;
                B += ((out      ) & 0xff)*coeff;

                coeff = mask_h[((j+side)*fSize)+i+side];
                out = rsGetElementAt_uint(picture, x+i, y+j);
                R2 += ((out >> 16) & 0xff)*coeff;
                G2 += ((out >>  8) & 0xff)*coeff;
                B2 += ((out      ) & 0xff)*coeff;

                /*R = sqrt((R*R)+(R2*R2));
                G = sqrt((G*G)+(G2*G2));
                B = sqrt((B*B)+(B2*B2));
                R = fmax(R,0);
                R = fmin(R,255);
                G = fmax(G,0);
                G = fmin(G,255);
                B = fmax(B,0);
                B = fmin(B,255);*/
            }
        }
    }
    out = ((int)R & 0xff) << 16 | ((int)G & 0xff) << 8 | ((int)B & 0xff);
    return out;
}
