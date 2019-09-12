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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Training {
    // Load library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static int RUN_MODE = Constants.RUN_MODE_EXTRACT;

    private EigenFaceRecognizer mRecognizer;

    private void trainRecognizer(TrainingData trainingData, String imageSourcePath) {
        MatOfInt labelsMat = new MatOfInt();
        labelsMat.fromList(trainingData.getLabels());
        mRecognizer = EigenFaceRecognizer.create();
        List<Mat> images = new ArrayList<>();
        for (String imagePath : trainingData.getImagePaths()) {
            Mat image = getImageMat(imageSourcePath + imagePath + Constants.EXT_JPG);
            images.add(image);
        }
        mRecognizer.train(images, labelsMat);
    }

    private void recognize() {
        String baseImgPath = Constants.BASE_RESOURCE_PATH + Constants.OUTPUT_DIR_PATH;
        String imagePath = baseImgPath + "/1004/4.jpg";
        Mat image = Imgcodecs.imread(imagePath, 0);
        Imgproc.resize(image, image, new Size(500, 500));
        int[] outLabel = new int[1];
        double[] outConf = new double[1];
        System.out.println("Starting Prediction...");
        mRecognizer.predict(image, outLabel, outConf);

        System.out.println("***Predicted label is " + Arrays.toString(outLabel) + ".***");

        System.out.println("***Actual label is " + 1004 + ".***");
        System.out.println("***Confidence value is " + Arrays.toString(outConf) + ".***");
    }

    private List<Mat> findAndCropFace(String imageName, String baseInputDirPath, CascadeClassifier classifier) {
        int size = 500;

        MatOfRect faceDetections = new MatOfRect();
        String inputImagePath = baseInputDirPath + "/" + imageName + Constants.EXT_JPG;
        Mat inputImage = getImageMat(inputImagePath);
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

    private void writeFacesToImgFiles(List<Mat> faces, String imageName, String baseOutputDirPath) {
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

    private TrainingData prepareTrainingData(String sourcePath) {
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
    private Mat getImageMat(String imagePath) {
        return Imgcodecs.imread(imagePath, 0);
    }

    private void createDirectory(String baseDir, String imagePath) {
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

    private List<String> getFileNamesInDirectory(String sourceDir) {
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

    private List<String> getFileNamesWithDirectoryInDirectory(String sourceDir) {
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
