import java.util.Vector;

public class PCDecompress {
    Vector<Vector<Integer>> quantizedDiff = new Vector<>();
    Vector<Vector<Integer>> decoded = new Vector<>();
    public Vector<Vector<Integer>> decompress(String inputFilePath) {
        quantizedDiff.clear();
        decoded.clear();
        String s = new Read_Write().readFromBinFile(inputFilePath);
        String[] lines = s.split("\n");
        for(int i = 0;i < lines.length; i++){
            quantizedDiff.add(new Vector<>());
            String[] nums = lines[i].split(" ");
            for (String n: nums) {
                if(n.charAt(n.length() -1) == '\r'){
                    n = n.substring(0, n.length()-1);
                }
                quantizedDiff.get(i).add(Integer.parseInt(n));
            }
        }
        //System.out.println(quantizedDiff);
        for (int x = 0; x < quantizedDiff.size(); x++) {
            decoded.add(new Vector<>());
            for (int y = 0; y < quantizedDiff.get(x).size(); y++) {
                if (x == 0 || y == 0) {
                    decoded.get(x).add(quantizedDiff.get(x).get(y));
                }
            }
        }
        for (int x = 1; x < 49; x++) {
            for (int y = 1; y < 49; y++) {
                generateDequantizedDifference(x, y);
                generateDecoded(x, y);
            }
        }
        System.out.println(decoded);
        return quantizedDiff;
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

