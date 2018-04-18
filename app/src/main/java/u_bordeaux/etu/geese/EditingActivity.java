package u_bordeaux.etu.geese;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
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
    private boolean working;


    private ImageView imageView;

    private Bitmap bmp;
    Image img;
    Image preview;

    private Uri pathImg;


    Float scale = 1f;
    ScaleGestureDetector SGD;


    private int cptImage =0;

    FragmentFilters fragmentFilters;
    FragmentEdit fragmentEdit;

    ViewPagerAdapter adapter;

    public Context context;


    public Uri getPathImg(){
        return pathImg;
    }

    public Context getContext(){
        return context;
    }

    public TabLayout getTabLayout(){return tabLayout;}

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

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
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
        context = getApplicationContext();


        //On recupere l'image depuis la premiere activite
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


        Bundle bundle = new Bundle();
        bundle.putParcelable("bmp",pathImg);
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
        Log.i(TAG, "onFilterSelected: ");
        filterSelection(TAG,-1,img);
    }

    @Override
    public void onPreviewSelected(String TAG, int progress) {
        filterSelection(TAG, progress, preview);
    }

    @Override
    public void onPreviewStart() {
        preview = new Image(img.getPreview(imageView.getWidth(),imageView.getHeight()));
        imageView.setImageBitmap(preview.getBmp());
    }

    @Override
    public void onFilterSelected(String TAG, int progress) {
        filterSelection(TAG,progress,img);

    }

    public void filterSelection(String TAG, int progress, Image img){
        if (working == false){
            working = true;

            if (img == preview){
                preview.restore();
            }
            taskFilters exec = new taskFilters(TAG, progress, img);
            exec.execute();
        }


    }

    private class taskFilters extends AsyncTask{
        private String TAG;
        private int progress;
        private Image img;

        public taskFilters (String TAG, int progress, Image img){
            this.TAG = TAG;
            this.progress = progress;
            this.img = img;
        }
        @Override
        protected Object doInBackground(Object... objects) {
            switch(TAG){
                case "brightness":
                    Filters.brightness(img,progress);
                    break;
                case "saturation":
                    Filters.saturationRs(img,progress,context);
                    break;
                case "contrast":
                    Filters.contrast(img,progress);
                    break;
                case "blur":
                    Convolution.gaussien(img,progress,context);
                    break;
                case "hue":
                    Filters.hueRs(img,progress,context);
                    break;
                case "gray":
                    Filters.toGray(img);
                    break;
                case "sepia":
                    Filters.sepia(img);
                    break;
                case "egalization" :
                    Histogram.equalization(img);
                    break;
                case "linearExtention" :
                    Histogram.linearExtension(img);
                    break;
                case "negative" :
                    Filters.negative(img);
                    break;
                case "sobel" :
                    Convolution.sobelRS(img,context);
                    break;
                case "laplacien" :
                    Convolution.laplacien(img,context);
                    break;
                case "cancel":
                    break;
            }
            Log.i(TAG, "in doInBackground: ");
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            imageView.setImageBitmap(img.getBmp());
            imageView.invalidate();
            working = false;
            Log.i(TAG, "done onPostExecute: ");
        }
    }
}
