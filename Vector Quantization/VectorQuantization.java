import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class VectorQuantization {
    String image = "";

    VectorQuantization() {
    }

    public void compress(String inputFilePath, String outputFilePath) {
        VQCompress vqCompress = new VQCompress();
        new Read_Write().writeToBinFile(inputFilePath, image);
        Vector<Vector<Vector<Integer>>> img = ImageVectorConverter.imageTo2DVector("img2.jpg");
        Vector<Vector<Double>> red = new Vector<>(), green = new Vector<>(), blue = new Vector<>();
        for (int i = 0; i < img.size(); i++) {
            red.add(new Vector<>());
            green.add(new Vector<>());
            blue.add(new Vector<>());
            for (int j = 0;j<img.get(0).size();j++){
                red.get(i).add(Double.valueOf(img.get(i).get(j).get(0)));
                green.get(i).add(Double.valueOf(img.get(i).get(j).get(1)));
                blue.get(i).add(Double.valueOf(img.get(i).get(j).get(2)));
            }
        }
        vqCompress.compress(red, "red.txt");
        vqCompress.compress(green, "green.txt");
        vqCompress.compress(blue, "blue.txt");
    }

    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        VQDecompress vqDecompress = new VQDecompress();
        Vector<Vector<Vector<Double>>>img = new Vector<>();
        Vector<Vector<Double>> red = vqDecompress.decompress("red.txt")
                , green = vqDecompress.decompress("green.txt"),
                blue = vqDecompress.decompress("blue.txt");
        for (int i = 0; i < red.size(); i++) {
            img.add(new Vector<>());
            for (int j = 0;j<red.get(0).size();j++){
                img.get(i).add(new Vector<>());
            }
        }
        for (int i = 0; i < red.size(); i++) {
            for (int j = 0;j<red.get(0).size();j++){
                img.get(i).get(j).add(red.get(i).get(j));
                img.get(i).get(j).add(green.get(i).get(j));
                img.get(i).get(j).add(blue.get(i).get(j));
            }
        }
        File output = new File("output.png");
        ImageIO.write(ImageVectorConverter.convertToImage(img), "png", output);
    }
}
// 2 3 8 9 4 10
// 4 5 7 7 11 11
// 4 10 16 15 8 9
// 11 11 19 18 7 7
// 2 3 16 15 2 3
// 4 5 19 18 4 5
