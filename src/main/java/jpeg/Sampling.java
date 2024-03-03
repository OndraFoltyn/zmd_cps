package jpeg;

import Jama.Matrix;
import enums.SamplingType;

public class Sampling {
    // Metoda pro zmenšení matice, podle zvoleného vzorkování
    public static Matrix sampleDown(Matrix inputMatrix, SamplingType samplingType) {
        Matrix outputMatrix = null;

        switch (samplingType) {
            case S_4_4_4:
                return inputMatrix;
            case S_4_2_2:
                outputMatrix = downSample(inputMatrix);
                return outputMatrix;
            case S_4_2_0:
                outputMatrix = downSample(inputMatrix);
                outputMatrix = outputMatrix.transpose();
                outputMatrix = downSample(outputMatrix);
                outputMatrix = outputMatrix.transpose();
                return outputMatrix;
            case S_4_1_1:
                outputMatrix = downSample(inputMatrix);
                outputMatrix = downSample(outputMatrix);
                break;
        }
        return outputMatrix;
    }

    // Metoda pro rozšíření matice, podle zvoleného vzorkování
    public static Matrix sampleUp(Matrix inputMatrix, SamplingType samplingType) {
    Matrix outputMatrix = null;
        switch (samplingType) {
            case S_4_4_4:
                return inputMatrix;
            case S_4_2_2:
                outputMatrix = upSample(inputMatrix);
                return outputMatrix;
            case S_4_2_0:
                outputMatrix = upSample(inputMatrix);
                outputMatrix = outputMatrix.transpose();
                outputMatrix = upSample(outputMatrix);
                outputMatrix = outputMatrix.transpose();
                return outputMatrix;
            case S_4_1_1:
                outputMatrix = upSample(inputMatrix);
                outputMatrix = upSample(outputMatrix);
                return outputMatrix;
        }
        return outputMatrix;
    }
    public static Matrix downSample(Matrix mat) {
        Matrix sampledMatrix = new Matrix(mat.getRowDimension(), mat.getColumnDimension()/2);
        int count = 0;
        for (int i = 0; i < mat.getColumnDimension(); i+=2) {
            Matrix subMat = mat.getMatrix(0, mat.getRowDimension() - 1, i, i);
            sampledMatrix.setMatrix(0, mat.getRowDimension() - 1, count, count, subMat);
            count++;
        }
    return sampledMatrix;
    }

    public static Matrix upSample(Matrix mat) {
        Matrix sampledMatrix = new Matrix(mat.getRowDimension(), mat.getColumnDimension()*2);
        for (int i = 0; i < mat.getRowDimension(); i++) {
            for (int j = 0; j < mat.getColumnDimension(); j++) {
                sampledMatrix.set(i, j*2, mat.get(i, j));
                sampledMatrix.set(i, j*2+1, mat.get(i,j));
            }
        }
        return sampledMatrix;
    }
}
