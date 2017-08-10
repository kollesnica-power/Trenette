package com.example.kollesnica_power.trenette.exception;

/**
 * Created by kollesnica1337 on 09.08.2017.
 */

public class WrongImageReference extends RuntimeException {

    public WrongImageReference(){
        super("Can't load image with id - 0");
    }

}
