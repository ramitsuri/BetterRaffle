package com.betterraffle.main;

import com.betterraffle.main.entities.TrainingData;
import com.betterraffle.main.entities.TrainingItem;
import com.betterraffle.main.helper.Constants;
import org.opencv.core.*;
import org.opencv.face.EigenFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Training {
    // Load library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static EigenFaceRecognizer mRecognizer;

    public static void trainRecognizer(TrainingData trainingData, String imageSourcePath) {
        MatOfInt labelsMat = new MatOfInt();
        labelsMat.fromList(trainingData.getLabels());
        mRecognizer = EigenFaceRecognizer.create();
        List<Mat> images = new ArrayList<>();
        for (String imagePath : trainingData.getImagePaths()) {
            Mat image = getImageMat(imageSourcePath + "/" + imagePath + Constants.EXT_JPG);
            images.add(image);
        }
        System.out.println("Started training");
        mRecognizer.train(images, labelsMat);
        System.out.println("Trained");
    }

    // BasedirPath = ./files/upload
    // imageName = 6473483.jpg
    public static void recognize(String imageName, String baseDirPath) {
        CascadeClassifier classifier = new CascadeClassifier(
                Constants.BASE_RESOURCE_PATH + Constants.CLASSIFIER_ALT);
        String inputImagePath = getImagePath(imageName, baseDirPath, false);
        Mat inputImage = getImageMat(inputImagePath);
        List<Mat> faces = findAndCropFace(inputImage, classifier);
        int topLabel = -1;
        double topConfidence = -1;
        for (Mat face : faces) {
            int[] outLabel = new int[1];
            double[] outConf = new double[1];
            mRecognizer.predict(face, outLabel, outConf);
            if (topConfidence == -1) {
                topConfidence = outConf[0];
                topLabel = outLabel[0];
            }

            // Less is more
            if (outConf[0] < topConfidence) {
                topConfidence = outConf[0];
                topLabel = outLabel[0];
            }
        }

        System.out.println("Predicted label is " + topLabel);
    }

    public static List<Mat> findAndCropFace(Mat inputImage, CascadeClassifier classifier) {
        int size = 1000;

        MatOfRect faceDetections = new MatOfRect();
        classifier.detectMultiScale(inputImage, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        List<Mat> faces = new ArrayList<>();

        for (Rect rect : faceDetections.toArray()) {
            // Find rect
            Rect rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);

            // Crop rect
            Mat face = new Mat(inputImage, rectCrop);

            // Resize to reduce size
            Imgproc.resize(face, face, new Size(size, size));

            faces.add(face);
        }
        return faces;
    }

    public static void writeFacesToImgFiles(List<Mat> faces, String imageName, String baseOutputDirPath) {
        int i = 0;
        for (Mat face : faces) {
            // Write new image which just has the face
            String outputImagePath;
            if (i != 0) {
                outputImagePath = baseOutputDirPath + "/" + imageName + "_" + i + Constants.EXT_JPG;
            } else {
                outputImagePath = baseOutputDirPath + "/" + imageName + Constants.EXT_JPG;
            }
            Imgcodecs.imwrite(outputImagePath, face);
            System.out.println("Extracted from " + imageName);
            i++;
        }
    }

    public static TrainingData prepareTrainingData(String sourcePath) {
        TrainingData trainingData = new TrainingData();

        List<String> names = getFileNamesWithDirectoryInDirectory(sourcePath);
        List<TrainingItem> items = new ArrayList<>();
        for (String name : names) {
            TrainingItem item = new TrainingItem();
            item.setImageName(name);
            items.add(item);
        }
        trainingData.setItems(items);
        trainingData.prepareData();
        return trainingData;
    }

    /**
     * returns Matrix from image with absolute address
     * ./resources/input/1001/1.jpg
     */
    public static Mat getImageMat(String imagePath) {
        return Imgcodecs.imread(imagePath, 0);
    }

    public static String getImagePath(String imageName, String baseInputDirPath, boolean addExt) {
        if (addExt)
            return baseInputDirPath + "/" + imageName + Constants.EXT_JPG;
        return baseInputDirPath + "/" + imageName;
    }

    private static void createDirectory(String baseDir, String imagePath) {
        String[] tokens = imagePath.split("/");
        if (tokens.length != 3) {
            return;
        }
        File file = new File(baseDir + "/" + tokens[1]);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Added dir " + tokens[1]);
            } else {
                System.out.println("Failed to add dir");
            }
        }
    }

    public static List<String> getFileNamesInDirectory(String sourceDir) {
        List<String> list = new ArrayList<>();
        try {
            Stream<Path> paths = Files.walk(Paths.get(sourceDir));
            paths = paths.filter(path -> path.toString().endsWith(Constants.EXT_JPG));
            list = paths
                    .map(path -> path.getFileName().toString().replace(Constants.EXT_JPG, ""))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> getFileNamesWithDirectoryInDirectory(String sourceDir) {
        List<String> list = new ArrayList<>();
        try {
            Stream<Path> paths = Files.walk(Paths.get(sourceDir));
            paths = paths.filter(path -> path.toString().endsWith(Constants.EXT_JPG));
            list = paths
                    .map(path -> path.toString().split("/")[3] + "/" + path.toString().split("/")[4])
                    .map(pathString -> pathString.replace(Constants.EXT_JPG, ""))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
