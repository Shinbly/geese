package u_bordeaux.etu.geese;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class MainActivity extends AppCompatActivity {

    private ImageView Iv;
    private Viewer viewer;

    private Uri imageUri;
    private Animator currentAnimator;
    private int longAnimationDuration;

    private Bitmap bmap;

    private ImageButton b_gallery;
    private ImageButton b_camera;

    private Button b_sepia;

    private static int RESULT_LOAD_IMG = 0;
    private static int RESULT_CAMERA = 1;

    Image img;

    protected void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            if (reqCode == RESULT_LOAD_IMG) {
                try {
                    imageUri = data.getData();
                    final InputStream stream = getContentResolver().openInputStream(imageUri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = true;
                    options.inMutable = true;
                    bmap = BitmapFactory.decodeStream(stream, null, options);
                    img = new Image(bmap);
                    viewer.setImageBitmap(bmap);

                } catch (FileNotFoundException e) {
                    Log.v("Image loading", "Unable to load Image : File not found");
                }
            }
            if (reqCode == RESULT_CAMERA) {
                bmap = (Bitmap) data.getExtras().get("data");
                img= new Image(bmap);
                viewer.setImageBitmap(bmap);
            }
        }
    }


    private void zoomImageFromThumb(){

        if(currentAnimator != null){
            currentAnimator.cancel();
        }

            Rect startBounds = new Rect();
            Rect finalBounds = new Rect();
            Point globalOffset = new Point();

            viewer.getGlobalVisibleRect(startBounds);
            findViewById(R.id.container).getGlobalVisibleRect(finalBounds,globalOffset);
            startBounds.offset(-globalOffset.x, -globalOffset.y);
            finalBounds.offset(-globalOffset.x, -globalOffset.y);

            float startScale;
            if((float) finalBounds.width()/finalBounds.height()>
                    (float) startBounds.width()/startBounds.height()){

                startScale = (float) startBounds.height() / finalBounds.height();

                float startWidth = (float) startScale * finalBounds.width();
                float deltaWidth = (startWidth - startBounds.width()) / 2;

                startBounds.left -= deltaWidth;
                startBounds.right += deltaWidth;
            }else{
                startScale = (float) startBounds.width() / finalBounds.width();

                float startHeight = (float) startScale * finalBounds.height();
                float deltaHeight = (startHeight - startBounds.height()) /2;

                startBounds.top -= deltaHeight;
                startBounds.bottom += deltaHeight;
            }

            Iv.setAlpha(0f);
            viewer.setVisibility(View.VISIBLE);

            viewer.setPivotX(0f);
            viewer.setPivotY(0f);

            AnimatorSet set = new AnimatorSet();
            set
                    .play(ObjectAnimator.ofFloat(viewer, View.X, startBounds.left, finalBounds.left))
                    .with(ObjectAnimator.ofFloat(viewer, View.Y, startBounds.top, finalBounds.top))
                    .with(ObjectAnimator.ofFloat(viewer, View.SCALE_X, startScale, 1f))
                    .with(ObjectAnimator.ofFloat(viewer, View.SCALE_Y, startScale, 1f));
            set.setDuration(longAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);

                    currentAnimator = null;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    currentAnimator = null;
                }
            });
            set.start();
            currentAnimator = set;
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

        longAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);

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
                Convolution.sobel(img);
            }
        });

        Iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                zoomImageFromThumb();

                return true;
            }
        });

    }
}