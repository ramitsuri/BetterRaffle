package com.betterraffle.main.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingData {

    private List<TrainingItem> mItems;
    private List<String> mImagePaths;
    private List<Integer> mLabels;

    public List<TrainingItem> getItems() {
        return mItems;
    }

    public void setItems(List<TrainingItem> items) {
        mItems = items;
    }

    public List<String> getImagePaths() {
        return mImagePaths;
    }

    public List<Integer> getLabels() {
        return mLabels;
    }

    public void prepareData() {
        mImagePaths = new ArrayList<>();
        mLabels = new ArrayList<>();
        Collections.sort(mItems);
        for (TrainingItem item : mItems) {
            mImagePaths.add(item.getImageName());
            mLabels.add(item.getDirectory());
        }
    }
}
