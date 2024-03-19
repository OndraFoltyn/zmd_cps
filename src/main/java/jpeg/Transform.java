package jpeg;

import Jama.Matrix;
import enums.TransformType;

public class Transform {
    // Transformace předané barevné složky
    public static Matrix transform(Matrix input, TransformType type, int blockSize) {
        Matrix output = new Matrix(input.getRowDimension(), input.getColumnDimension());
        Matrix A = getTransformMatrix(type, blockSize);     // transformacni matice
        Matrix aTranspose = A.transpose();                  // transponovana transformacni matice

        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix X = input.getMatrix(i, i + blockSize - 1, j, j + blockSize - 1);
                Matrix mat = A.times(X).times(aTranspose);
                output.setMatrix(i, i + blockSize - 1, j, j + blockSize - 1, mat);

            }
        }
        return output;
    }

    // Inverzní transformace
    public static Matrix inverseTransform(Matrix input, TransformType type, int blockSize) {
        Matrix output = new Matrix(input.getRowDimension(), input.getColumnDimension());
        Matrix A = getTransformMatrix(type, blockSize);     // transformacni matice
        Matrix aTranspose = A.transpose();                  // transponovana transformacni matice

        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix O = input.getMatrix(i, i + blockSize - 1, j, j + blockSize - 1);
                Matrix mat = aTranspose.times(O).times(A);
                output.setMatrix(i, i + blockSize - 1, j, j + blockSize - 1, mat);
            }
        }
        return output;
    }

    // Získání transformační matice, generované podle typu a velikosti bloku
    public static Matrix getTransformMatrix(TransformType type, int blockSize) {
        Matrix transformMatrix = new Matrix(blockSize, blockSize);

        switch (type) {
            case DCT:
                for (int i = 0; i < blockSize; i++) {
                    for (int j = 0; j < blockSize; j++) {
                        double C = Math.cos(((2 * j + 1) * i * Math.PI) / (2 * blockSize));
                        if (i == 0) {
                            C /= Math.sqrt(blockSize);
                        } else {
                            C *= Math.sqrt((double) 2 / blockSize);
                        }
                        transformMatrix.set(i, j, C);
                    }
                }
                break;

            case WHT:
                int N = 1;
                while (N < blockSize) {
                    N *= 2;
                }
                Matrix Hadamard = generateHadamard(N);
                transformMatrix = Hadamard.times(1 / Math.sqrt(blockSize));
                break;
        }
        return transformMatrix;
    }

    // Generate the Hadamard matrix
    public static Matrix generateHadamard(int size) {
        Matrix hadamard = new Matrix(size, size);
        hadamard.set(0, 0, 1);
        for (int k = 1; k < size; k *= 2) {
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    hadamard.set(i + k, j, hadamard.get(i, j));
                    hadamard.set(i, j + k, hadamard.get(i, j));
                    hadamard.set(i + k, j + k, -hadamard.get(i, j));
                }
            }
        }
        return hadamard;
    }
}
