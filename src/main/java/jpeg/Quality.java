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
//        throw new RuntimeException("Not implemented yet.");
        double K1 = 0.01;
        double K2 = 0.03;
        int L = 255;
        double C1 = Math.pow(K1*L, 2);
        double C2 = Math.pow(K2*L, 2);
        double u_x = 0;
        double u_y = 0;
        double sigma_x = 0;
        double sigma_y = 0;
        double sigma_xy = 0;
        double size = original.getColumnDimension() * original.getRowDimension();

        for (int i = 0; i < original.getRowDimension(); i++) {
            for (int j = 0; j < original.getColumnDimension(); j++) {
                u_x += original.get(i, j);
                u_y += modified.get(i, j);
            }
        }

        u_x /= size;
        u_y /= size;

        for (int i = 0; i < original.getRowDimension(); i++) {
            for (int j = 0; j < original.getColumnDimension(); j++) {
                double x = original.get(i, j);
                double y = modified.get(i, j);
                sigma_x += Math.pow(x - u_x, 2);
                sigma_y += Math.pow(y - u_y, 2);
                sigma_xy += (x - u_x)*(y - u_y);
            }
        }

        sigma_x /= size-1;
        sigma_x = Math.pow(sigma_x, 0.5);
        sigma_y /= size-1;
        sigma_y = Math.pow(sigma_y, 0.5);
        sigma_xy /= size-1;

        double numerator = (2 * u_x * u_y + C1) * (2 * sigma_xy + C2);
        double denominator = (Math.pow(u_x,2) + Math.pow(u_y,2) + C1) * (Math.pow(sigma_x,2) + Math.pow(sigma_y,2) + C2);

        return numerator/denominator;
    }

    // Výpočet MSSIM, ponechte zde throw, pokud to nebudete implementovat
    public static double countMSSIM(Matrix original, Matrix modified) {
//        throw new RuntimeException("Not implemented yet.");
        double ssim = 0;
        double M = original.getColumnDimension() * original.getRowDimension();

        for (int i = 0; i < original.getRowDimension(); i++) {
            for (int j = 0; j < original.getColumnDimension(); j++) {
                ssim += countSSIM(original.getMatrix(i,i,j,j), modified.getMatrix(i,i,j,j));
            }
        }
        return ssim / M;
    }

    public static double[][] convertIntToDouble(int[][] intArray) {
        double[][] doubleArray = new double[intArray.length][intArray[0].length];
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[0].length; j++) {
                doubleArray[i][j] = intArray[i][j];
            }
        }
        return doubleArray;
    }
}
