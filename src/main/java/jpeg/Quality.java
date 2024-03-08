package jpeg;
import Jama.Matrix;


public class Quality {
    // Výpočet MSE. Nutný převod pomocí Matrix.getArray() nebo převod z int[][] na double[][].
    public static double countMSE(double[][] original, double[][] modified) {
        int width = original.length;
        int height = original[0].length;
        double mse = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                mse += Math.pow(original[i][j] - modified[i][j], 2);
            }
        }
        return mse / (width * height);
    }

    // Výpočet MAE. Nutný převod pomocí Matrix.getArray() nebo převod z int[][] na double[][].
    public static double countMAE(double[][] original, double[][] modified) {
        int width = original.length;
        int height = original[0].length;
        double mae = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                mae += Math.abs(original[i][j] - modified[i][j]);
            }
        }
        return mae / (width * height);
    }

    // Výpočet SAE. Nutný převod pomocí Matrix.getArray() nebo převod z int[][] na double[][].
    public static double countSAE(double[][] original, double[][] modified) {
        int width = original.length;
        int height = original[0].length;
        double sae = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                sae += Math.abs(original[i][j] - modified[i][j]);
            }
        }
        return sae;
    }

    // Výpočet PSNR z MSE
    public static double countPSNR(double MSE) {
        return 10 * Math.log10(Math.pow(255, 2) / MSE);
    }

    // ýpočet PSNR z MSE pro RGB obrázek (průměr z barev a použít např. countPSNR)
    public static double countPSNRforRGB(double mseRed, double mseGreen, double mseBlue) {
        double avgPsn = (mseBlue + mseRed + mseGreen) / 3;
        return 10 * Math.log10(Math.pow(255, 2) / avgPsn);
    }

    // Výpočet SSIM, ponechte zde throw, pokud to nebudete implementovat
    public static double countSSIM(Matrix original, Matrix modified) {
        throw new RuntimeException("Not implemented yet.");
    }

    // Výpočet MSSIM, ponechte zde throw, pokud to nebudete implementovat
    public static double countMSSIM(Matrix original, Matrix modified) {
        throw new RuntimeException("Not implemented yet.");
    }
}
