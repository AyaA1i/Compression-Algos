import java.util.Vector;

public class PCCompress {
    Vector<Vector<Integer>> predicted = new Vector<>();
    Vector<Vector<Integer>> diff = new Vector<>();
    Vector<Vector<Integer>> quantizedDiff = new Vector<>();
    Vector<Vector<Integer>> decoded = new Vector<>();
    Vector<Vector<Integer>> original;
    public void compress(Vector<Vector<Integer>> original ,String outputFilePath) {
        this.original = original;
        quantizedDiff.clear();
        for (int x = 0; x < original.size(); x++) {
            decoded.add(new Vector<>());
            diff.add(new Vector<>());
            quantizedDiff.add(new Vector<>());
            predicted.add(new Vector<>());
            for (int y = 0; y < original.get(x).size(); y++) {
                if (x == 0 || y == 0) {
                    decoded.get(x).add(original.get(x).get(y));
                    diff.get(x).add(original.get(x).get(y));
                    quantizedDiff.get(x).add(original.get(x).get(y));
                    predicted.get(x).add(original.get(x).get(y));
                }
            }
        }
        for (int x = 1; x < original.size(); x++) {
            for (int y = 1; y < original.get(x).size(); y++) {
                generatePredicted(x, y);
                generateDifference(x, y);
                generateQuantizedDifference(x, y);
                generateDequantizedDifference(x, y);
                generateDecoded(x, y);
            }
        }
        StringBuilder result = new StringBuilder();
        for(Vector<Integer> v: quantizedDiff){
            for (Integer i: v){
                result.append(i).append(" ");
            }
            result.append('\n');
        }
        new Read_Write().writeToBinFile(outputFilePath, result.toString());
    }

    private void generatePredicted(int x, int y) {
        if (x - 1 >= 0 && y - 1 >= 0) {
            int A = decoded.get(x).get(y - 1);
            int B = decoded.get(x - 1).get(y - 1);
            int C = decoded.get(x - 1).get(y);
            if (B <= Math.min(A, C)) {
                predicted.get(x).add(Math.max(C, A));
            } else if (B >= Math.max(A, C)) {
                predicted.get(x).add(Math.min(C, A));
            } else {
                predicted.get(x).add(A + C - B);
            }
        } else {
            predicted.get(x).add((decoded.get(x).get(y)));
        }
    }

    private void generateDifference(int x, int y) {
        diff.get(x).add( original.get(x).get(y) - predicted.get(x).get(y));
    }

    private void generateQuantizedDifference(int x, int y) {
        int num = diff.get(x).get(y);
        int current = 0;
        for (int i = -255; i <= 255; i += 8) {
            if (num >= i && num < (i + 8)) {
                quantizedDiff.get(x).add( current);
                break;
            }
            current++;
        }
    }

    private void generateDequantizedDifference(int x, int y) {
        int num = quantizedDiff.get(x).get(y);
        int current = 0;
        for (int i = -255; i <= 255; i += 8) {
            if (current == num) {
                int r = i+7;
                decoded.get(x).add( ((r + i) / 2));
                break;
            }
            current++;
        }
    }

    private void generateDecoded(int x, int y) {
        if (x - 1 >= 0 && y - 1 >= 0) {
            int A = decoded.get(x).get(y - 1);
            int B = decoded.get(x - 1).get(y - 1);
            int C = decoded.get(x - 1).get(y);
            if (B <= Math.min(A, C)) {
                decoded.get(x).set(y, Math.max(C, A) + decoded.get(x).get(y));
            } else if (B >= Math.max(A, C)) {
                decoded.get(x).set(y, Math.min(C, A) + decoded.get(x).get(y));
            } else {
                decoded.get(x).set(y, (A + C - B) + decoded.get(x).get(y));
            }
        }

    }
}