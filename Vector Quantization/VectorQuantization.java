import java.awt.*;
import java.util.Random;
import java.util.Vector;

public class VectorQuantization {
//    StringBuilder image;
    String image;
    VectorQuantization(){
        Random random = new Random();
        int size = 6;
        image = "1 2 7 9 4 11\n3 4 6 6 12 12\n4 9 15 14 9 9\n10 10 20 18 8 8\n4 3 17 16 1 4\n4 5 18 18 5 6";
//        image = new StringBuilder();
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                int minElementValue = 0;
//                int maxElementValue = 127;
//                image.append(random.nextInt(maxElementValue - minElementValue + 1) + minElementValue);
//                if(j != size - 1) image.append(" ");
//            }
//            image.append("\n");
//        }
    }
    public void compress(String inputFilePath, String outputFilePath){
        VQCompress vqCompress = new VQCompress();
        //new Read_Write().writeToBinFile(inputFilePath, image.toString());
        new Read_Write().writeToBinFile(inputFilePath, image);
        vqCompress.compress(inputFilePath, outputFilePath);
    }
    public void decompress(String inputFilePath, String outputFilePath){
        VQDecompress vqDecompress = new VQDecompress();
        vqDecompress.decompress(inputFilePath, outputFilePath);
    }
}
