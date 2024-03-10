package jpeg;

import Jama.Matrix;
import enums.ColorType;
import enums.QualityType;
import enums.SamplingType;
import graphics.Dialogs;

import java.awt.*;
import java.awt.image.BufferedImage;

import static core.Helper.checkValue;
import static enums.ColorType.*;


public class Process {
    private BufferedImage originalImage;
    private int imageHeight;
    private int imageWidth;


    private int [][] originalRed, modifiedRed;
    private int [][] originalGreen, modifiedGreen;
    private int [][] originalBlue, modifiedBlue;

    private Matrix originalY, modifiedY;
    private Matrix originalCb, modifiedCb;
    private Matrix originalCr, modifiedCr;

    public Process(String path) {
        this.originalImage = Dialogs.loadImageFromPath(path);

        imageWidth = originalImage.getWidth();
        imageHeight = originalImage.getHeight();

        originalRed = new int[imageHeight][imageWidth];
        originalGreen = new int[imageHeight][imageWidth];
        originalBlue = new int[imageHeight][imageWidth];

        originalY = new Matrix(imageHeight, imageWidth);
        originalCb = new Matrix(imageHeight, imageWidth);
        originalCr = new Matrix(imageHeight, imageWidth);

        setOriginalRGB();
    }


    private void setOriginalRGB() {
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                Color color = new Color(originalImage.getRGB(w, h));
                originalRed[h][w] = color.getRed();
                originalBlue[h][w] = color.getBlue();
                originalGreen[h][w] = color.getGreen();
            }
        }
    }

    public BufferedImage getImageFromRGB() {
        BufferedImage bfImage = new BufferedImage(
                imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,
                        (new Color(modifiedRed[h][w],
                                   modifiedGreen[h][w],
                                   modifiedBlue[h][w])).getRGB());
            }
        }
        return bfImage;
    }

    public BufferedImage showOneColorImageFromRGB(int [][] color, ColorType type) {
        BufferedImage bfImage = new BufferedImage(
                color.length, color[0].length,
                BufferedImage.TYPE_INT_RGB);

        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                int rgbValue;
                switch (type) {
                    case RED:
                        rgbValue = new Color(color[h][w], 0, 0).getRGB();
                        break;
                    case BLUE:
                        rgbValue = new Color(0, 0, color[h][w]).getRGB();
                        break;
                    case GREEN:
                        rgbValue = new Color(0,color[h][w], 0).getRGB();
                        break;
                    default:
                        rgbValue = new Color(color[h][w], color[h][w], color[h][w]).getRGB();
                    }
                bfImage.setRGB(w, h, rgbValue);
            }
        }
        return bfImage;
    }

    public BufferedImage showOneColorImageFromYCbCr(Matrix color) {
        BufferedImage bfImage = new BufferedImage(
                color.getColumnDimension(), color.getRowDimension(),
                BufferedImage.TYPE_INT_RGB);

        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                int pixel = checkValue(color.get(h, w)); // Používá metodu z třídy Helper, která zajistí, že hodnota bude v rozsahu 0-255.
                bfImage.setRGB(w, h, (new Color(pixel, pixel, pixel)).getRGB());
            }
        }
        return bfImage;
    }

    public void convertToYCbCr(){
        Matrix[] res = ColorTransform.convertOriginalRGBtoYcBcR(originalRed, originalGreen, originalBlue);
        originalY = res[0];
        originalCb = res[1];
        originalCr = res[2];

        modifiedY = originalY.copy();
        modifiedCb = originalCb.copy();
        modifiedCr = originalCr.copy();
    }
    public void convertToRGB(){
        int [][][] res = ColorTransform.convertModifiedYcBcRtoRGB(originalY, originalCb, originalCr);
        modifiedRed = res[0];
        modifiedGreen = res[1];
        modifiedBlue = res[2];
    }

    public BufferedImage showRed() {
        return showOneColorImageFromRGB(originalRed, RED);
    }
    public BufferedImage showGreen() {
        return showOneColorImageFromRGB(originalGreen, GREEN);
    }

    public BufferedImage showBlue() {
        return showOneColorImageFromRGB(originalBlue, BLUE);
    }

    public BufferedImage showModRed() {
        return showOneColorImageFromRGB(modifiedRed, RED);
    }

    public BufferedImage showModGreen() {
        return showOneColorImageFromRGB(modifiedGreen, GREEN);
    }

    public BufferedImage showModBlue() {
        return showOneColorImageFromRGB(modifiedBlue, BLUE);
    }

    public BufferedImage showY() {
        return showOneColorImageFromYCbCr(originalY);
    }

    public BufferedImage showCb() {
        return showOneColorImageFromYCbCr(originalCb);
    }

    public BufferedImage showCr() {
        return showOneColorImageFromYCbCr(originalCr);
    }

    public BufferedImage showModY() {
        return showOneColorImageFromYCbCr(modifiedY);
    }

    public BufferedImage showModCb() {
        return showOneColorImageFromYCbCr(modifiedCb);
    }

    public BufferedImage showModCr() {
        return showOneColorImageFromYCbCr(modifiedCr);
    }

    public void downSample(SamplingType samplingType) {
        modifiedCb = Sampling.sampleDown(modifiedCb, samplingType);
        modifiedCr = Sampling.sampleDown(modifiedCr, samplingType);
    }

    public void upSample(SamplingType samplingType) {
        modifiedCb = Sampling.sampleUp(modifiedCb, samplingType);
        modifiedCr = Sampling.sampleUp(modifiedCr, samplingType);
    }


    public double[] qualityCount(QualityType qualityType) throws Exception {
        double[] values = new double[4];
        double psnrVal = 0;
        double mse = 0;
        double mae = 0;
        double sae = 0;
        switch (qualityType) {
            case RED:
                mse = Quality.countMSE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                mae = Quality.countMSE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                sae = Quality.countSAE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                psnrVal = Quality.countPSNR(mse);
                break;

            case GREEN:
                mse = Quality.countMSE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                mae = Quality.countMSE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                sae = Quality.countSAE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                psnrVal = Quality.countPSNR(mse);
                break;

            case BLUE:
                mse = Quality.countMSE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));
                mae = Quality.countMSE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));
                sae = Quality.countSAE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));
                psnrVal = Quality.countPSNR(mse);
                break;

            case Y:
                mse = Quality.countMSE(originalY.getArray(), modifiedY.getArray());
                mae = Quality.countMAE(originalY.getArray(), modifiedY.getArray());
                sae = Quality.countSAE(originalY.getArray(), modifiedY.getArray());
                psnrVal = Quality.countPSNR(mse);
                break;

            case Cb:
                mse = Quality.countMSE(originalCb.getArray(), modifiedCb.getArray());
                mae = Quality.countMAE(originalCb.getArray(), modifiedCb.getArray());
                sae = Quality.countSAE(originalCb.getArray(), modifiedCb.getArray());
                psnrVal = Quality.countPSNR(mse);
                break;

            case Cr:
                mse = Quality.countMSE(originalCr.getArray(), modifiedCr.getArray());
                mae = Quality.countMAE(originalCr.getArray(), modifiedCr.getArray());
                sae = Quality.countSAE(originalCr.getArray(), modifiedCr.getArray());
                psnrVal = Quality.countPSNR(mse);
                break;

            case RGB:
                double mseRedRGB = Quality.countMSE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                double mseGreenRGB = Quality.countMSE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                double mseBlueRGB = Quality.countMSE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));

                double maeRedRGB = Quality.countMAE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                double maeGreenRGB = Quality.countMAE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                double maeBlueRGB = Quality.countMAE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));

                double saeRedRGB = Quality.countSAE(Quality.convertIntToDouble(originalRed), Quality.convertIntToDouble(modifiedRed));
                double saeGreenRGB = Quality.countSAE(Quality.convertIntToDouble(originalGreen), Quality.convertIntToDouble(modifiedGreen));
                double saeBlueRGB = Quality.countSAE(Quality.convertIntToDouble(originalBlue), Quality.convertIntToDouble(modifiedBlue));

                mse = (mseRedRGB +  mseGreenRGB + mseBlueRGB) / 3;
                mae = (maeRedRGB + maeGreenRGB + maeBlueRGB) / 3;
                sae = (saeRedRGB + saeGreenRGB + saeBlueRGB) / 3;

                psnrVal = Quality.countPSNRforRGB(mseRedRGB, mseGreenRGB, mseBlueRGB);
                break;

            case YcBcR:
                double mseY = Quality.countMSE(originalY.getArray(), modifiedY.getArray());
                double mseCb = Quality.countMSE(originalCb.getArray(), modifiedCb.getArray());
                double mseCr = Quality.countMSE(originalCr.getArray(), modifiedCr.getArray());

                double maeY = Quality.countMAE(originalY.getArray(), modifiedY.getArray());
                double maeCb = Quality.countMAE(originalCb.getArray(), modifiedCb.getArray());
                double maeCr = Quality.countMAE(originalCr.getArray(), modifiedCr.getArray());

                double saeY = Quality.countSAE(originalY.getArray(), modifiedY.getArray());
                double saeCb = Quality.countSAE(originalCb.getArray(), modifiedCb.getArray());
                double saeCr = Quality.countSAE(originalCr.getArray(), modifiedCr.getArray());

                mse = (mseY +  mseCb + mseCr) / 3;
                mae = (maeY + maeCb + maeCr) / 3;
                sae = (saeY + saeCb + saeCr) / 3;
                psnrVal = Quality.countPSNR(mse);
                break;
        }
        values[0] = psnrVal;
        values[1] = mse;
        values[2] = mae;
        values[3] = sae;
        return values;
    }
}


