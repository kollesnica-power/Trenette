package com.example.kollesnica_power.trenette.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.kollesnica_power.trenette.R;

import com.example.kollesnica_power.trenette.model.ImageData;
import com.example.kollesnica_power.trenette.model.ImageModel;
import com.example.kollesnica_power.trenette.request.RequestBuilder;
import com.example.kollesnica_power.trenette.view.kenburnsview.KenBurnsView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private Realm mRealm;

    private ArrayList<ImageData> mImages = new ArrayList<>();
    private ImageSwitcher mIsSwitcher;

    private int mIndex = 0;

    private boolean isRunning = true;
    private int mSwitchDelay;
    private int mFadeOutDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mRealm =  Realm.getDefaultInstance();

        // Setting duration values
        mSwitchDelay = getResources().getInteger(R.integer.switch_delay);
        mFadeOutDuration = getResources().getInteger(R.integer.fade_out_duration);

        makeRequest();

        getFromDataBase();

        setSwitcher();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mRealm.close();

    }

    private void setSwitcher() {

        // Setting switcher
        mIsSwitcher = (ImageSwitcher) findViewById(R.id.is_switcher);
        mIsSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                KenBurnsView imageView = new KenBurnsView(getApplicationContext());
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });

        // Post switcher cycling
        mIsSwitcher.post(cycledSlideShow);

    }



    Runnable cycledSlideShow = new Runnable() {

        @Override
        public void run() {

            if (isRunning) {

                Glide
                        .with(getApplicationContext())
                        .load(mImages.get(mIndex).getImage())
                        .animate(R.anim.fade_in)
                        .into((ImageView) mIsSwitcher.getCurrentView());

                // Shifting images in array
                mIndex++;
                mIndex %= mImages.size();

                // Cycling switcher
                mIsSwitcher.postDelayed(this, mSwitchDelay);
                mIsSwitcher.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // Run animation before switch image
                        mIsSwitcher.getCurrentView().startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out));
                    }

                }, mSwitchDelay - mFadeOutDuration);

            }

        }
    };



    private void makeRequest() {

        // Sending request
        RequestBuilder.imagesRequest(getString(R.string.images_url)).getData().enqueue(new Callback<List<ImageData>>() {

            @Override
            public void onResponse(@NonNull Call<List<ImageData>> call, @NonNull Response<List<ImageData>> response) {

                Log.e("RequestSuccess", response.toString());



                if (response.code() < 400 || response.code() >= 599) {

                    if (response.body() != null) {

                        Toast.makeText(getApplicationContext(), R.string.images_loaded, Toast.LENGTH_SHORT).show();

                        putInDataBase((ArrayList<ImageData>) response.body());

                        getFromDataBase();
                    }

                }else {

                    Toast.makeText(getApplicationContext(), R.string.load_failed, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(@NonNull Call<List<ImageData>> call, @NonNull Throwable t) {

                Log.e("RequestFail", t.getLocalizedMessage());

                Toast.makeText(getApplicationContext(), R.string.load_failed, Toast.LENGTH_SHORT).show();

                getFromDataBase();



            }
        });

    }



    private void putInDataBase(ArrayList<ImageData> images) {

        mRealm.beginTransaction();

        for (ImageData image : images) {

            if (image.getImageUrl() != null && !image.getImageUrl().isEmpty()) {

                // Define is this image exist already in DB
                ImageModel imageUrl = mRealm.where(ImageModel.class).equalTo("imageUrl", image.getImageUrl()).findFirst();

                if (imageUrl == null) {

                    ImageModel imageModel = mRealm.createObject(ImageModel.class, UUID.randomUUID().toString());
                    imageModel.setImageUrl(image.getImageUrl());
                    Log.e("Realm", "ROW ADDED: " + image.getImageUrl());

                }

            }

        }

        mRealm.commitTransaction();



        /*RealmResults<ImageModel> imageModels = mRealm.where(ImageModel.class).findAll();

        for (ImageModel image : imageModels){

            Log.e(image.getId() + ": ", image.getImageUrl());

        }*/

    }



    private void getFromDataBase(){

       RealmResults<ImageModel> imageModels = mRealm.where(ImageModel.class).findAll();

        // Copping from DB
        if (imageModels.size() > 0) {

            mImages.clear();

            for (int i = 0; i < imageModels.size(); i++) {

                mImages.add(new ImageData(imageModels.get(i).getImageUrl()));

            }

        }else {

            setDefaultData();

        }

    }



    private void setDefaultData() {

        // Setting image bundle array
        mImages = new ArrayList<>();
        mImages.add(new ImageData(R.drawable.carissa_gan_76325));
        mImages.add(new ImageData(R.drawable.jakub_kapusnak_296128));
        mImages.add(new ImageData(R.drawable.eaters_collective_132773));
        mImages.add(new ImageData(R.drawable.eaters_collective_132772));

    }



    @Override
    public void finish() {

        isRunning = false;
        super.finish();

    }

}
