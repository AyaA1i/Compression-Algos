import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class PredictiveCoding {
    public void compress(String inputFilePath) {
        PCCompress vqCompress = new PCCompress();
        Vector<Vector<Vector<Integer>>> img = ImageVectorConverter.imageTo2DVector(inputFilePath);

        Vector<Vector<Integer>> red = new Vector<>(), green = new Vector<>(), blue = new Vector<>();
        for (int i = 0; i < img.size(); i++) {
            red.add(new Vector<>());
            green.add(new Vector<>());
            blue.add(new Vector<>());
            for (int j = 0;j<img.get(0).size();j++){
                red.get(i).add(img.get(i).get(j).get(0));
                green.get(i).add(img.get(i).get(j).get(1));
                blue.get(i).add(img.get(i).get(j).get(2));
            }
        }
        vqCompress.compress(red, "red.txt");
        vqCompress.compress(green, "green.txt");
        vqCompress.compress(blue, "blue.txt");
    }

    public void decompress(String outputFile) throws IOException {
        PCDecompress vqDecompress = new PCDecompress();
        Vector<Vector<Vector<Integer>>>img = new Vector<>();
        Vector<Vector<Integer>> red = vqDecompress.decompress("red.txt")
                , green = vqDecompress.decompress("green.txt"),
                blue = vqDecompress.decompress("blue.txt");
        for (int i = 0; i < red.size(); i++) {
            img.add(new Vector<>());
            for (int j = 0;j<red.get(0).size();j++){
                img.get(i).add(new Vector<>());
            }
        }
        for (int i = 0; i < red.size(); i++) {
            for (int j = 0;j < red.get(0).size();j++){
                img.get(i).get(j).add(red.get(i).get(j));
                img.get(i).get(j).add(green.get(i).get(j));
                img.get(i).get(j).add(blue.get(i).get(j));
            }
        }
        File output = new File(outputFile);
        ImageIO.write(ImageVectorConverter.convertToImage(img), "jpg", output);
    }
}