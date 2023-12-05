import java.io.IOException;
import java.util.Vector;

public class VectorQuantization {
    String image = "";

    VectorQuantization() {
//       int size = 6;
//       image = "1 2 7 9 4 11\n3 4 6 6 12 12\n4 9 15 14 9 9\n10 10 20 18 8 8\n4 3 17 16 1 4\n4 5 18 18 5 6";
         Vector<Vector<Integer>> img = ImageVectorConverter.imageTo2DVector("img.png");
         for (int i = 0; i < img.size(); i++) {
            for (int j = 0; j < img.get(i).size(); j++) {
                image += img.get(i).get(j);
                if (j != img.get(i).size())
                    image += " ";
            }
            image += "\n";
         }
    }

    public void compress(String inputFilePath, String outputFilePath) {
        VQCompress vqCompress = new VQCompress();
        new Read_Write().writeToBinFile(inputFilePath, image);
        vqCompress.compress(inputFilePath, outputFilePath);
    }

    public void decompress(String inputFilePath, String outputFilePath) throws IOException {
        VQDecompress vqDecompress = new VQDecompress();
        vqDecompress.decompress(inputFilePath, outputFilePath);
    }
}
// 2 3 8 9 4 10
// 4 5 7 7 11 11
// 4 10 16 15 8 9
// 11 11 19 18 7 7
// 2 3 16 15 2 3
// 4 5 19 18 4 5
