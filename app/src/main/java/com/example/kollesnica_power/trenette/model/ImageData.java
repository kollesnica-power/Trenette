package com.example.kollesnica_power.trenette.model;

import com.example.kollesnica_power.trenette.exception.WrongImageReference;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kollesnica1337 on 09.08.2017.
 * Data class for images.
 */

public class ImageData {

    /*
    Resource from application
     */
    private int drawableResource;

    /*
    Image url from server
     */
    @SerializedName("image")
    @Expose
    private String imageUrl;



    public ImageData(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public ImageData(String  imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getImage(){

        if (imageUrl != null && !imageUrl.isEmpty()){
            return imageUrl;
        }

        if (drawableResource == 0){
            throw new WrongImageReference();
        }

        return drawableResource;

    }

    public int getDrawableResource() {
        return drawableResource;
    }

    public void setDrawableResource(int drawableResource) {
        this.drawableResource = drawableResource;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
