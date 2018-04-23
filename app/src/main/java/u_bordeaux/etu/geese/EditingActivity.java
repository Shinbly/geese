package u_bordeaux.etu.geese;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Class EditingActivity, handles all the actions that can be applied on the image
 */

public class EditingActivity extends AppCompatActivity implements FragmentFilters.FragmentFiltersListener, FragmentEdit.FragmentEditListener {

    private TabLayout tabLayout;


    private ViewPager viewPager;


    /**
     * boolean working, set to true when an asynckTask is set to treat a filter and set to false
     * when the work is done
     */
    private boolean working;


    /**
     * the viewer on which the image is displayed
     */
    private Viewer imageView;


    private Bitmap bmp;


    private Image img;

    /**
     * An image which fit the size of the Viewer. It's used to show the result of a filter using the
     * seekbar before it's applied on the whole image. It's a memory and compute gain.
     */
    private Image preview;


    private Uri pathImg;


    private String pathDirectorySave = Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DCIM + "/geese";


    private String mCurrentPhotoPath;


    private FragmentFilters fragmentFilters;
    private FragmentEdit fragmentEdit;


    private ViewPagerAdapter adapter;


    public Context context;

    private ProgressBar progressBar;




    /**
     * Getter for the path of the image display
     * @return the path of the image
     */
    public Uri getPathImg(){
        return pathImg;
    }


    /**
     * Getter for the context
     * @return the context of the application
     */
    public Context getContext(){
        return context;
    }


    /**
     * Getter for the tabLayout
     * @return the tabLayout which handles the fragments
     */
    public TabLayout getTabLayout(){return tabLayout;}




    /**
     * Method createImageFile
     * Creates an image file in the directory of the application and gives it a name based on the
     * current date
     * @return the image file
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = new File(pathDirectorySave);
        storageDir.mkdirs();
        File image = new File(storageDir,imageFileName+".jpg");

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }




    /**
     * Method save
     * Save the bitmap with all the modifications that have been applied on it
     * @param bmp the bitmap to save
     */
    private void save(Bitmap bmp){
        boolean saveDone = false;

        File file = null;

        try{
            file = createImageFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(file != null){

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
                        saveDone = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Allows that the image is immediatly displayed in the gallery
        pathImg = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,pathImg);
        sendBroadcast(intent);

        if(saveDone){
            Toast.makeText(context,"Image saved",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"An error occured",Toast.LENGTH_LONG).show();
        }
    }




    /**
     * Method setupViewPager
     * Organized the fragments in the adapter
     * @param viewPager
     */
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




    /**
     * Method decodeSampleBitmapFromStream
     * Return a bitmap smaller than the one passed with the stream. The original bitmap is not
     * charged into the memory, so it doesn't overload it
     * @param stream the stream which "contains" the bitmap
     * @param streamS the same stream used to decode the smaller bitmap
     * @return a smaller version of the bitmap
     */
    public Bitmap decodeSampledBitmapFromStream(InputStream stream, InputStream streamS) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream,null,options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        int nbPixelsOrig = imageHeight*imageWidth;
        int nbPixelsAllow = 1920*1080;

        float ratioSize = nbPixelsOrig/nbPixelsAllow;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, (int)(imageWidth/Math.sqrt(ratioSize)), (int)(imageHeight/Math.sqrt(ratioSize)));

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inScaled = true;
        options.inMutable = true;

        return BitmapFactory.decodeStream(streamS,null,options);
    }




   /**
    * Method calculateInSampleSize
    * Computes the reduction factor to apply to the bitmap to reduce it based on the width and the
    * height given in parameters
    * @param options the option of the Bitmap factory, to retrieve the width and height of the
    *                original bitmp
    * @param reqWidth the desired width of the resized bitmap
    * @param reqHeight the desired height of the resized bitmap
    * @return the reduction factor
    */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }




    /**
     * Method share
     * Allows to share the image with an other application
     * @param uri
     */
    private void share(Uri uri){
        save(bmp);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }






    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Geese");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tablayoutid);
        viewPager = (ViewPager) findViewById(R.id.viewpagerid);

        imageView = (Viewer) findViewById(R.id.imageViewEditid);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        context = getApplicationContext();


        //Retrieves the image from the MainActivity
        Intent intent = getIntent();
        pathImg = (Uri) intent.getParcelableExtra("pathBitmap");
        try{
            final InputStream stream = getContentResolver().openInputStream(pathImg);
            final InputStream streamS = getContentResolver().openInputStream(pathImg);

            bmp = decodeSampledBitmapFromStream(stream, streamS);

            imageView.setImageBitmap(bmp);
            img = new Image(bmp);


        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        //Bundle which will be send to the FragmentFilters for the thumbnails
        Bundle bundle = new Bundle();
        bundle.putParcelable("bmp",pathImg);
    }


    @Override
    protected void onStop() {
        super.onStop();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                this.save(img.getBmp());
                return true;

            case R.id.action_share:
                share(pathImg);
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
        filterSelection(TAG,-1,img);
    }





    @Override
    public void onPreviewSelected(String TAG, int progress) {
        filterSelection(TAG, progress, preview);
    }





    @Override
    public void onPreviewStart() {
        preview = new Image(img.getPreview(imageView.getWidth(), imageView.getHeight()));
        imageView.setImageBitmap(preview.getBmp());
    }





    @Override
    public void onFilterSelected(String TAG, int progress) {
        filterSelection(TAG,progress,img);

    }




    /**
     * Method filterSelection
     * Uses on the changes of the seekbar, restores the preview and applies the filter with the
     * new seekbar value
     * @param TAG the tag of the choosen filter
     * @param progress the value get with the seekbar
     * @param img the image on which apply the filter
     */
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






    /**
     * Inner class taskFilters, allows to execute the selected filter in an other thread
     * than the UIthread
     */
    private class taskFilters extends AsyncTask{
        private String TAG;
        private int progress;
        private Image img;




        /**
         * Constructor
         * @param TAG
         * @param progress
         * @param img
         */
        public taskFilters (String TAG, int progress, Image img){
            this.TAG = TAG;
            this.progress = progress;
            this.img = img;
        }




        @Override
        protected Object doInBackground(Object... objects) {
            switch(TAG){
                case "brightness" :
                    Filters.brightness(img, progress);
                    break;

                case "saturation" :
                    Filters.saturationRs(img, progress, context);
                    break;

                case "colorization" :
                    Filters.colorization(img, progress, context);
                    break;

                case "contrast" :
                    Filters.contrast(img, progress);
                    break;

                case "blur":
                    Convolution.gaussien(img, progress, context);
                    break;

                case "averager" :
                    Convolution.moyenneur(img, progress, context);
                    break;

                case "hue" :
                    Filters.hueRs(img, progress, context);
                    break;

                case "gray" :
                    Filters.toGray(img);
                    break;

                case "sepia" :
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
                    Convolution.sobelRS(img, context);
                    break;

                case "laplacien" :
                    Convolution.laplacien(img, context);
                    break;

                case "sketch" :
                    Filters.sketch(img, context);
                    break;

                case "cancel" :
                    break;
            }

            return null;
        }




        @Override
        protected void onPostExecute(Object o) {
            imageView.setImageBitmap(img.getBmp());
            imageView.postInvalidate();
            working = false;
        }
    }
}
