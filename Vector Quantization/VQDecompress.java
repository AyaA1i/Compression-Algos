import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class VQDecompress {
    Vector<Vector<Vector<Double>>> codeBook = new Vector<>();

    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        String s = new Read_Write().readFromBinFile(inputFilePath);
        // format outputC : width height codebook paddingCI CI

        int i = 0, r = 0, c = 0;
        StringBuilder result = new StringBuilder();
        while (i < s.length()) {
            if (i == 0)
                r = s.charAt(i++);
            else if (i == 1)
                c = s.charAt(i++);
            else if (i == 2) {
                for (int j = i; j < 18; j += 4) {
                    Vector<Vector<Double>> b = new Vector<>();
                    b.add(new Vector<>());
                    b.add(new Vector<>());
                    b.get(0).add((double) s.charAt(j));
                    b.get(0).add((double) s.charAt(j + 1));
                    b.get(1).add((double) s.charAt(j + 2));
                    b.get(1).add((double) s.charAt(j + 3));
                    codeBook.add(b);
                }
                i = 18;
            }
            else{
                int p = s.charAt(i++);
                String compressed = "";
                for (int j = i; j < s.length(); j++) {
                    compressed += String.format("%8s", Integer.toBinaryString(s.charAt(j))).replace(' ', '0');
                }
                compressed = compressed.substring(p);
                Vector<Vector<Vector<Vector<Double>>>> image = new Vector<>();
                for (int j = 0; j < r/2; j++) {
                    image.add(new Vector<>(c/2));
                }
                for(Vector<Vector<Vector<Double>>> v: image){
                    for (int j = 0; j < c/2; j++) {
                        int code = Integer.parseInt(compressed.charAt(0) + String.valueOf(compressed.charAt(1)), 2);
                        v.add(codeBook.get(code));
                        compressed = compressed.substring(2);
                    }
                }

                for(Vector<Vector<Vector<Double>>> v: image){
                    String r1 = "", r2 = "";
                    for (Vector<Vector<Double>> b: v) {
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

                Vector<Vector<Double>> decompressed_img = new Vector<>();
                String[] lines = String.valueOf(result).split("\n");
                for (String line: lines) {
                    String[] num = line.split(" ");
                    decompressed_img.add(new Vector<>());
                    for (String n: num) {
                        decompressed_img.get(decompressed_img.size()-1).add(Double.valueOf(n));
                    }
                }
                BufferedImage bufferedImage = ImageVectorConverter.convertToImage(decompressed_img);
                File output = new File("output.png");
                ImageIO.write(bufferedImage, "png", output);
                break;
            }
        }
        new Read_Write().writeToBinFile(outputFilePath, result.toString());
    }
}

