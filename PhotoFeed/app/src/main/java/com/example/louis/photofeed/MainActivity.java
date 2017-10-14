package com.example.louis.photofeed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_PHOTO_IMAGE = 2;
    private Button mTakePhotoButton;
    private Button mPickPhotoButton;
    private Button mDownloadImageButton;
    private ImageView mImageResult;
    private String mCurrentPath;
    private TextView mInfo;
    private ListView mImageList;
    private ImageAdapter mAdapter;
    private List<String> mImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfo = (TextView) findViewById(R.id.info);
        mTakePhotoButton = (Button) findViewById(R.id.takePhoto);
        mPickPhotoButton = (Button) findViewById(R.id.pickPhoto);
        mImageResult = (ImageView) findViewById(R.id.imageResult);
        mDownloadImageButton = (Button) findViewById(R.id.downloadImage);
        mImageUrls = MockDataBase.allImages;
        mImageList = (ListView) findViewById(R.id.imageList);
        mAdapter = new ImageAdapter(this, R.layout.image_item , mImageUrls);
        mImageList.setAdapter(mAdapter);

        attachClickHandlers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            setPhotoFromFile();
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_PHOTO_IMAGE) {
            setPhotoFromGallery(data);
            addPhotoToGallery();
        }
    }
    private void attachClickHandlers() {
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePhoto();

            }
        });
        mPickPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhoto();

            }
        });
        mDownloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage();

            }
        });
    }
    private void dispatchTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try{
                photoFile = createImageFile();

            } catch (IOException e) {
                String error = "Error: " + e.getMessage();
                mInfo.setText(error);
            }
            if (photoFile != null){
                Uri filePath = FileProvider.getUriForFile(this, "com.example.louis.photofeed", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent chooser = Intent.createChooser(intent, "Select Photo");
        startActivityForResult(chooser, REQUEST_PHOTO_IMAGE);
    }
    private void downloadImage() {
        String url = MockDataBase.image1;
        (new DownloadImageTask(this, url, mImageResult)).execute();

    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName,".jpg", storageDir);
        mCurrentPath = image.getAbsolutePath();
        return image;
    }
    private void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File galleryFile = new File(mCurrentPath);
        Uri contentUri = Uri.fromFile(galleryFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void setPhotoFromFile() {
//        Bundle extras = data.getExtras();
//        Bitmap imageBitmap = (Bitmap) extras.get("data");
//        Not: this is reading from the file location that we told the camera to save the image to.
        Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPath);
        mImageResult.setImageBitmap(imageBitmap);
    }
    private void setPhotoFromGallery(Intent data) {
        try {
            InputStream stream = this.getContentResolver().openInputStream(data.getData());
            Bitmap galleryPhotos = BitmapFactory.decodeStream(stream);
            mImageResult.setImageBitmap(galleryPhotos);
        } catch (FileNotFoundException e) {
            mInfo.setText("Error: " + e.getMessage());
        }

    }
}
