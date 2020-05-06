
package com.m.sofiane.go4lunch.models.pojoAutoComplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AutoComplete {

    @SerializedName("predictions")
    @Expose
    private ArrayList<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(ArrayList<Prediction> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
