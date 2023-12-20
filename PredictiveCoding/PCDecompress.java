import java.util.Vector;

public class PCDecompress {
    Vector<Vector<Integer>> quantizedDiff = new Vector<>();
    Vector<Vector<Integer>> decoded = new Vector<>();
    public Vector<Vector<Integer>> decompress(String inputFilePath) {
        quantizedDiff.clear();
        decoded.clear();
        String s = new Read_Write().readFromBinFile(inputFilePath);
        int size = s.charAt(0);
        for (int i = 0;i < size;i++){
            quantizedDiff.add(new Vector<>());
        }
        int ind = 0;
        for (int i = 1; i < s.length(); i += size){
            for (int j = i; j < i + size;j++){
                quantizedDiff.get(ind).add((int) s.charAt(j));
            }
            ind++;
        }
        for (int x = 0; x < quantizedDiff.size(); x++) {
            decoded.add(new Vector<>());
            for (int y = 0; y < quantizedDiff.get(x).size(); y++) {
                if (x == 0 || y == 0) {
                    decoded.get(x).add(quantizedDiff.get(x).get(y));
                }
            }
        }
        for (int x = 1; x < quantizedDiff.size(); x++) {
            for (int y = 1; y < quantizedDiff.get(0).size(); y++) {
                generateDequantizedDifference(x, y);
                generateDecoded(x, y);
            }
        }
        return decoded;
    }
    private void generateDequantizedDifference(int x, int y) {
        int num = quantizedDiff.get(x).get(y);
        int current = 0;
        for (int i = -255; i <= 255; i += 8) {
            if (current == num) {
                int r = i+7;
                decoded.get(x).add( ((i + r) / 2));
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

