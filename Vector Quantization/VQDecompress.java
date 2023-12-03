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
        System.out.println(CompressedImage);
        System.out.println(codeBook);
        System.out.println(Labels);
    }
}
