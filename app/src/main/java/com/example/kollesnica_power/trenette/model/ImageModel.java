package com.example.kollesnica_power.trenette.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kollesnica1337 on 10.08.2017.
 * Model class for Realm DB.
 */

public class ImageModel extends RealmObject {

    @PrimaryKey
    private String id;

    private String imageUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
