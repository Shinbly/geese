package u_bordeaux.etu.geese;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.pm.PackageManager;

public class MainActivity extends AppCompatActivity {

    private ImageButton b_gallery;
    private ImageButton b_camera;

    private static int RESULT_LOAD_IMG = 0;
    private static int RESULT_CAMERA = 1;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private Uri uriCamera;

    String mCurrentPhotoPath;

    Context context;

    protected void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            if (reqCode == RESULT_LOAD_IMG) {
                    final Uri pathImg = data.getData();

                    Intent intent = new Intent(MainActivity.this, EditingActivity.class);
                    intent.putExtra("pathBitmap",pathImg);
                    startActivity(intent);
            }
            if (reqCode == RESULT_CAMERA) {
                    galleryAddPic();

                    Intent intent = new Intent(MainActivity.this, EditingActivity.class);
                    intent.putExtra("pathBitmap",uriCamera);
                    startActivity(intent);
            }
        }
    }

    private void savePerm(){
        if(Build.VERSION.SDK_INT >= 23) {
            int hasWriteGalleryPermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteGalleryPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM + "/geese");
        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanItent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanItent.setData(contentUri);
        this. sendBroadcast(mediaScanItent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_gallery = (ImageButton) findViewById(R.id.B_gallery);
        b_camera = (ImageButton) findViewById(R.id.B_camera);


        context = getApplicationContext();

        savePerm();

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

                if(intent.resolveActivity(getPackageManager()) != null){
                    File photoFile = null;

                    try{
                        photoFile = createImageFile();

                    }catch (IOException e){

                    }

                    if(photoFile != null){
                        uriCamera = FileProvider.getUriForFile(context,"u_bordeaux.etu.geese.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCamera);
                        startActivityForResult(intent, RESULT_CAMERA);
                    }
                }
            }
        });
    }
}