package u_bordeaux.etu.geese;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView Iv;
    private ImageButton b_gallery;
    private ImageButton b_camera;
    private int RESULT_LOAD_IMG = 0;
    private int RESULT_CAMERA = 1;

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
                    Bitmap img = BitmapFactory.decodeStream(stream, null, options);
                    Iv.setImageBitmap(img);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iv = (ImageView) findViewById(R.id.imageView);
        b_gallery = (ImageButton) findViewById(R.id.B_gallery);
        b_camera = (ImageButton) findViewById(R.id.B_camera);


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



    }
}