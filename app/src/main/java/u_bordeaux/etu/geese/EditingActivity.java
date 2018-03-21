package u_bordeaux.etu.geese;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.ShareActionProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class EditingActivity extends AppCompatActivity implements FragmentFilters.FragmentFiltersListener, FragmentEdit.FragmentEditListener {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    private ImageView imageView;

    private Bitmap bmp;
    Image img;
    Image preview;

    private Uri pathImg;


    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;

    private ImageButton brightness;
    private ImageButton contrast;
    private ImageButton hue;
    private Button gray;
    private Button sepia;

    private int cptImage =0;

    FragmentFilters fragmentFilters;
    FragmentEdit fragmentEdit;


    private String save(Bitmap bmp, String img_name){
        String root = Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DCIM + "/geese";
        File dir = new File(root);
        dir.mkdirs();
        String imgName = img_name+".jpg";
        File file = new File(dir,imgName);
        //Les lignes suivantes permettent que la photo soit tout de suite
        //affichée dans la galerie
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri);
        sendBroadcast(intent);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file.getAbsolutePath();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentFilters= new FragmentFilters();
        fragmentFilters.setListener(this);


        fragmentEdit= new FragmentEdit();
        fragmentEdit.setListener(this);


        adapter.addFragment(fragmentFilters, "Filters");
        adapter.addFragment(fragmentEdit, "Edit");

        viewPager.setAdapter(adapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Geese");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tablayoutid);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarid);
        viewPager = (ViewPager) findViewById(R.id.viewpagerid);

        imageView = (ImageView) findViewById(R.id.imageViewEditid);


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        pathImg = (Uri) intent.getParcelableExtra("pathBitmap");
        try{
            final InputStream stream = getContentResolver().openInputStream(pathImg);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            options.inMutable = true;
            bmp = BitmapFactory.decodeStream(stream, null, options);
            img = new Image(bmp);

            imageView.setImageBitmap(bmp);

        }catch (FileNotFoundException e){

        }
        SGD = new ScaleGestureDetector(this, new ScaleListener());





        imageView.setOnTouchListener(new View.OnTouchListener() {
            float downX, downY;
            int totalX, totalY;
            int scrollByX, scrollByY;
            int maxX = (int)(((img.getWidth()*scale) / 2) - (imageView.getWidth() / 2));
            int maxY = (int)(((img.getHeight()*scale) / 2) - (imageView.getHeight() / 2));
            // set scroll limits
            int maxLeft = (maxX * -1);
            int maxRight = maxX;
            int maxTop = (maxY * -1);
            int maxBottom = maxY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                SGD.onTouchEvent(event);
                float currentX, currentY;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        currentX = event.getX();
                        currentY = event.getY();
                        scrollByX = (int) (downX - currentX);
                        scrollByY = (int) (downY - currentY);

                        // scrolling to left side of image (pic moving to the right)
                        if (currentX > downX) {
                            if (totalX == maxLeft) {
                                scrollByX = 0;
                            }
                            if (totalX > maxLeft) {
                                totalX = totalX + scrollByX;
                            }
                            if (totalX < maxLeft) {
                                scrollByX = maxLeft - (totalX - scrollByX);
                                totalX = maxLeft;
                            }
                        }

                        // scrolling to right side of image (pic moving to the left)
                        if (currentX < downX) {
                            if (totalX == maxRight) {
                                scrollByX = 0;
                            }
                            if (totalX < maxRight) {
                                totalX = totalX + scrollByX;
                            }
                            if (totalX > maxRight) {
                                scrollByX = maxRight - (totalX - scrollByX);
                                totalX = maxRight;
                            }
                        }

                        // scrolling to top of image (pic moving to the bottom)
                        if (currentY > downY) {
                            if (totalY == maxTop) {
                                scrollByY = 0;
                            }
                            if (totalY > maxTop) {
                                totalY = totalY + scrollByY;
                            }
                            if (totalY < maxTop) {
                                scrollByY = maxTop - (totalY - scrollByY);
                                totalY = maxTop;
                            }
                        }

                        // scrolling to bottom of image (pic moving to the top)
                        if (currentY < downY) {
                            if (totalY == maxBottom) {
                                scrollByY = 0;
                            }
                            if (totalY < maxBottom) {
                                totalY = totalY + scrollByY;
                            }
                            if (totalY > maxBottom) {
                                scrollByY = maxBottom - (totalY - scrollByY);
                                totalY = maxBottom;
                            }
                        }
                        imageView.scrollBy(scrollByX, scrollByY);
                        downX = currentX;
                        downY = currentY;
                        break;
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void share(Uri uri){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");

        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                this.save(img.getBmp(),"img"+cptImage);
                cptImage++;
                return true;

            case R.id.action_share:
                share(this.pathImg);
                return true;

            case R.id.restore_image:
                img.restore();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFilterSelected(String TAG) {
        switch(TAG){
            case "brightness":
                Filters.brightness(img,50);
                break;
            case "contrast":
                Histogram.equalization(img);
                break;
            case "blur":
                Convolution.moyenneur(img,5);
                break;
            case "gray":
                Filters.toGray(img);
                break;
            case "sepia":
                Filters.sepia(img);
                break;
            case "hue":
                Filters.hue(img,(int)(Math.random()*360));
                break;
            case "egalization" :
                Histogram.equalization(img);
                break;
            case "linearExtention" :
                Histogram.linearExtension(img);

            default:
                Log.i("error ", "onFilterSelected: wrong tag");
                break;
        }
        imageView.invalidate();
    }

    @Override
    public void onPreviewSelected(String TAG, int progress) {
        preview.restore();
        switch(TAG){
            case "brightness":
                Filters.brightness(preview,progress);
                break;
            case "contrast":
                //Histogram.equalization(preview,progress);
                break;
            case "blur":
                Convolution.gaussien(preview,progress);
                break;
            case "hue":
                Filters.hue(preview,progress);
                break;
            default:
                Log.i("error ", "onFilterSelected: wrong tag");
                break;
        }
        imageView.invalidate();
        Log.i(TAG, "onPreviewSelected: "+progress);
    }

    @Override
    public void onPreviewStart() {
        preview = new Image(img.getPreview(imageView.getWidth(),imageView.getHeight()));
        imageView.setImageBitmap(preview.getBmp());
        imageView.invalidate();
        Log.i("creation", "onPreviewStart: preview created");

    }

    @Override
    public void onFilterSelected(String TAG, int progress) {

        switch(TAG){
            case "brightness":
                Filters.brightness(img,progress);
                break;
            case "contrast":
                //Histogram.equalization(img,progress);
                break;
            case "blur":
                Convolution.gaussien(img,progress);
                break;
            case "hue":
                Filters.hue(img,progress);
                break;
            default:
                Log.i("error ", "onFilterSelected: wrong tag");
                break;
        }
        imageView.setImageBitmap(img.getBmp());
        imageView.invalidate();
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);
        return true;
    }*/

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(((float)imageView.getHeight()/(float)img.getHeight())/1.3f, Math.min(scale,5f));
            matrix.setScale(scale,scale);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }
}
