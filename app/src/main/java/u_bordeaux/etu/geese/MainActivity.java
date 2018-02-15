package u_bordeaux.etu.geese;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView Iv;
    private Viewer viewer;

    private Bitmap bmap;

    private ImageButton b_gallery;
    private ImageButton b_camera;

    private Button b_sepia;

    private static int RESULT_LOAD_IMG = 0;
    private static int RESULT_CAMERA = 1;

    Image img;

    Matrix matrix = new Matrix();

    Float scale = 1f;
    ScaleGestureDetector SGD;

    protected void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode,resultCode,data);
        if(reqCode == RESULT_LOAD_IMG ) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri pathImg = data.getData();
                    final InputStream stream = getContentResolver().openInputStream(pathImg);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = false;
                    options.inMutable = true;
                    bmap = BitmapFactory.decodeStream(stream, null, options);
                    img= new Image(bmap);
                    Iv.setImageBitmap(bmap);
                } catch (FileNotFoundException e) {
                    Log.v("Image loading", "Unable to load Image : File not found");
                }
            }
        }
        if(reqCode == RESULT_CAMERA ) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap)data.getExtras().get("data");
                Iv.setImageBitmap(bmp);
            }

        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale,5f));
            matrix.setScale(scale,scale);
            Iv.setImageMatrix(matrix);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iv = (ImageView) findViewById(R.id.imageView);
        viewer = (Viewer) findViewById(R.id.viewer);

        b_gallery = (ImageButton) findViewById(R.id.B_gallery);
        b_camera = (ImageButton) findViewById(R.id.B_camera);

        b_sepia = (Button) findViewById(R.id.B_sepia);

        SGD = new ScaleGestureDetector(this, new ScaleListener());

        b_gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMG);
            }
        });

        b_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
            }
        });

        b_sepia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.toGray(bmap);
                img.sobel(bmap);
                img.gaussien(bmap,5);
            }
        });

    }
}