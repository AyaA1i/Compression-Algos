import java.io.IOException;

public class StandardHuffman {
    public void compress(String inputFilePath, String outputFilePath) throws IOException {
        new SHCompress(inputFilePath, outputFilePath);
    }
    public void decompress(String inputFilePath, String outputFilePath){
        new SHDecompress(inputFilePath, outputFilePath);
    }
}
