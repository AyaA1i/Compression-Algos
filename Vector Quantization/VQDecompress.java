import java.util.Vector;

public class VQDecompress {
    Vector<Vector<String>> CompressedImage = new Vector<>();
    Vector<Vector<Vector<Double>>> codeBook = new Vector<>();
    Vector<String> Labels = new Vector<>();

    public void decompress(String inputFilePath, String outputFilePath) {
        String s = new Read_Write().readFromBinFile(outputFilePath);
        for (int i = 0; i < s.length(); i++) {
            while (i < 9) {
                int j = 0;
                Vector<String> c = new Vector<>();
                while (j < 3) {
                    String uncom = String.format("%8s", Integer.toBinaryString(s.charAt(i) & 0xFF)).replace(' ', '0');
                    uncom = uncom.substring(6);
                    c.add(uncom);
                    i++;
                    j++;
                }
                CompressedImage.add(c);
            }
            Vector<Vector<Double>> v = new Vector<>();
            Vector<Double> vv = new Vector<>();
            for (int j = i; j < i + 2; j++) {
                vv.add((double) Integer.parseInt(String.format("%8s", Integer.toBinaryString(s.charAt(j) & 0xFF)).replace(' ', '0'), 2));

            }
            v.add(vv);
            i+=2;
            vv = new Vector<>();
            for (int j = i; j < i + 2; j++) {
                vv.add((double) Integer.parseInt(String.format("%8s", Integer.toBinaryString(s.charAt(j) & 0xFF)).replace(' ', '0'), 2));

            }
            i+=2;
            v.add(vv);
            codeBook.add(v);
            String uncom = String.format("%8s", Integer.toBinaryString(s.charAt(i) & 0xFF)).replace(' ', '0');
            uncom = uncom.substring(6);
            Labels.add(uncom);
        }

        Vector<Vector<Vector<Double>>> decompressed = new Vector<>();
        for (int i = 0; i < CompressedImage.size(); i++) {
            for (int j = 0; j < CompressedImage.get(i).size(); j++) {
                int pos = Labels.indexOf(CompressedImage.get(i).get(j));
                decompressed.add(codeBook.get(pos));
            }
        }

        Vector<Vector<Integer>> decompressedImage = new Vector<>();
        decompressedImage.setSize(6);
        for (int i = 0; i < 6; i++) {
            decompressedImage.set(i, new Vector<>());
            decompressedImage.get(i).setSize(6);
        }

        int curIndex = 0;
        for (int i = 0; i < 6; i += 2) {
            for (int j = 0; j < 6; j += 2) {
                decompressedImage.get(i).set(j, (decompressed.get(curIndex).get(0).get(0)).intValue());
                decompressedImage.get(i).set(j + 1, (decompressed.get(curIndex).get(0).get(1)).intValue());
                curIndex++;
            }
        }
        curIndex = 0;
        for (int i = 1; i < 6; i += 2) {
            for (int j = 0; j < 6; j += 2) {
                decompressedImage.get(i).set(j, (decompressed.get(curIndex).get(1).get(0)).intValue());
                decompressedImage.get(i).set(j + 1, (decompressed.get(curIndex).get(1).get(1)).intValue());
                curIndex++;
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                result.append(Integer.toString(decompressedImage.get(i).get(j), 10) + " ");
            }
            result.append("\n");
        }

        Read_Write readWrite = new Read_Write();
        readWrite.writeToBinFile(outputFilePath, result.toString());
    }
}
