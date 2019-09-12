package com.betterraffle.main.entities;

public class TrainingItem implements Comparable<TrainingItem> {
    private String mImageName;

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String imageName) {
        mImageName = imageName;
    }

    public int getDirectory() {
        String[] thisTokens = mImageName.split("/");
        return Integer.parseInt(thisTokens[0]);
    }

    public int getFile() {
        String[] thisTokens = mImageName.split("/");
        return Integer.parseInt(thisTokens[1]);
    }

    @Override
    public int compareTo(TrainingItem other) {
        int thisDirectory = getDirectory();
        int thisFile = getFile();

        int otherDirectory = other.getDirectory();
        int otherFile = other.getFile();

        if (thisDirectory < otherDirectory) {
            return -1;
        } else if (thisDirectory > otherDirectory) {
            return 1;
        } else {
            return thisFile - otherFile;
        }
    }

    @Override
    public String toString() {
        return "TrainingItem{" +
                "mImageName='" + mImageName + '\'' +
                '}';
    }
}
