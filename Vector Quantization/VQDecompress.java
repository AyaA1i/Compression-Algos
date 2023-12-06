import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class VQDecompress {
    Vector<Vector<Vector<Integer>>> codeBook = new Vector<>();
    public Vector<Vector<Double>> decompress(String inputFilePath) throws IOException {
        String s = new Read_Write().readFromBinFile(inputFilePath);
        // format outputC : width height codebook paddingCI CI
        Vector<Vector<Double>> decompressed_img = new Vector<>();
        int i = 0, r = 0, c = 0;
        StringBuilder result = new StringBuilder();
        while (i < s.length()) {
            if (i == 0)
                r = s.charAt(i++);
            else if (i == 1)
                c = s.charAt(i++);
            else if (i == 2) {
                //32 * (4 (size of codebook)) = 128

                for (int j = i; j < 130; j += 4) {
                    Vector<Vector<Integer>> b = new Vector<>();
                    b.add(new Vector<>());
                    b.add(new Vector<>());
                    b.get(0).add((int) s.charAt(j));
                    b.get(0).add((int) s.charAt(j + 1));
                    b.get(1).add((int) s.charAt(j + 2));
                    b.get(1).add((int) s.charAt(j + 3));
                    codeBook.add(b);
                }
                //System.out.println(codeBook);
                i = 130;
            }
            else{
                int p = s.charAt(i++);
                String compressed = "";
                for (int j = i; j < s.length(); j++) {
                    compressed += String.format("%8s", Integer.toBinaryString(s.charAt(j))).replace(' ', '0');
                }
                compressed = compressed.substring(p);
                Vector<Vector<Vector<Vector<Integer>>>> image = new Vector<>();
                for (int j = 0; j < r/2; j++) {
                    image.add(new Vector<>(c/2));
                }
                for(Vector<Vector<Vector<Integer>>> v: image){
                    for (int j = 0; j < c/2; j++) {
                        String cd = "";
                        cd += compressed.charAt(0);
                        cd += compressed.charAt(1);
                        cd += compressed.charAt(2);
                        cd += compressed.charAt(3);
                        cd += compressed.charAt(4);

                        int code = Integer.parseInt(cd, 2);
                        v.add(codeBook.get(code));
                        compressed = compressed.substring(5);
                    }
                }

                for(Vector<Vector<Vector<Integer>>> v: image){
                    String r1 = "", r2 = "";
                    for (Vector<Vector<Integer>> b: v) {
                        r1 += b.get(0).get(0) + " ";
                        r1 += b.get(0).get(1) + " ";
                        r2 += b.get(1).get(0) + " ";
                        r2 += b.get(1).get(1) + " ";
                    }
                    result.append(r1);
                    result.append('\n');
                    result.append(r2);
                    result.append('\n');
                }
                result = new StringBuilder(result.toString().trim());

                String[] lines = String.valueOf(result).split("\n");
                for (String line: lines) {
                    String[] num = line.split(" ");
                    decompressed_img.add(new Vector<>());
                    for (String n: num) {
                        decompressed_img.get(decompressed_img.size()-1).add(Double.valueOf(n));
                    }
                }
                break;
            }
        }
        return decompressed_img;
    }
}

