package jpeg;

import Jama.Matrix;

public class Quantization {

    // Metoda pro kvantizaci
    public static Matrix quantize(Matrix input, int blockSize, double quality, boolean matrixY) {
        Matrix quantizationMatrix = getQuantizationMatrix(blockSize, quality, matrixY);
        Matrix outputMatrix = new Matrix(input.getRowDimension(), input.getColumnDimension());

        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix subMatrix = input.getMatrix(i, i + blockSize - 1, j, j + blockSize - 1);

                for (int subMatRow = 0; subMatRow< subMatrix.getRowDimension(); subMatRow ++) {
                    for (int subMatCol = 0; subMatCol<subMatrix.getColumnDimension(); subMatCol ++) {

                        double divisor = subMatrix.get(subMatRow, subMatCol) /  quantizationMatrix.get(subMatRow, subMatCol);

                        if (divisor >= -0.2 && divisor <= 0.2) {
                            subMatrix.set(subMatRow, subMatCol, (double) Math.round(divisor * 100) / 100);
                        } else {
                            subMatrix.set(subMatRow, subMatCol, (double) Math.round(divisor * 10) / 10);
                        }
                    }
                }
                outputMatrix.setMatrix(i, i + blockSize - 1, j, j + blockSize - 1, subMatrix);
            }
        }
        return outputMatrix;
    }


    // Metoda pro inverzni kvantizaci
    public static Matrix inverseQuantize(Matrix input, int blockSize, double quality, boolean matrixY) {
        Matrix quantizationMatrix = getQuantizationMatrix(blockSize, quality, matrixY);
        Matrix outputMatrix = new Matrix(input.getRowDimension(), input.getColumnDimension());

        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix subMatrix = input.getMatrix(i, i + blockSize - 1, j, j + blockSize - 1);
                outputMatrix.setMatrix(i,i+blockSize-1, j, j+blockSize-1, subMatrix.arrayTimes(quantizationMatrix));
            }
        }
        return outputMatrix;
    }


    // Metoda pro ziskani kvantizacni matice
    public static Matrix getQuantizationMatrix(int blockSize, double quality, boolean matrixY) {
        Matrix outputMatrix;
        double alpha;

        if (matrixY) {
            outputMatrix = matY;
        } else {
            outputMatrix = matC;
        }

        if (quality == 100) {
            return new Matrix(blockSize, blockSize, 1);
        }

        if (blockSize < 8) {
            for (int i = 0; i < blockSize; i += 2) {
                outputMatrix = Sampling.downSample(outputMatrix);
                outputMatrix = outputMatrix.transpose();
            }
        } else if (blockSize > 8) {
            for (int i = 8; i <= blockSize; i += 8) {
                outputMatrix = Sampling.upSample(outputMatrix);
                outputMatrix = outputMatrix.transpose();
            }
        }

        if (quality < 50) {
            alpha = 50.0 / quality;
        } else {
            alpha = 2.0 - ((2.0 * quality) / 100.0);
        }

        return outputMatrix.times(alpha);
    }

    // Kvantizacni matice 8x8 pro jasovou slozku
    private static final double[][] quantizationMatrix8Y = {
            {16, 11, 10, 16, 24, 40, 51, 61},
            {12, 12, 14, 19, 26, 58, 60, 55},
            {14, 13, 16, 24, 40, 57, 69, 56},
            {14, 17, 22, 29, 51, 87, 80, 62},
            {18, 22, 37, 56, 68, 109, 103, 77},
            {24, 35, 55, 64, 81, 104, 113, 92},
            {49, 64, 78, 87, 103, 121, 120, 101},
            {72, 92, 95, 98, 112, 100, 103, 99}};

    // Kvantizacni matice 8x8 pro chromaticke slozky
    private static final double[][] quantizationMatrix8C = {
            {17, 18, 24, 47, 99, 99, 99, 99},
            {18, 21, 26, 66, 99, 99, 99, 99},
            {24, 26, 56, 99, 99, 99, 99, 99},
            {47, 66, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99}};
    public static Matrix matC = Matrix.constructWithCopy(quantizationMatrix8C);
    public static Matrix matY = Matrix.constructWithCopy(quantizationMatrix8Y);


}
