import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Writer {
    public void writeToBinFile(String outputFilePath, String result){
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    public String readFromBinFile(String inputFilePath){
        String txtInput = "";
        try{
            txtInput = new String(Files.readAllBytes(Paths.get(inputFilePath)));
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return txtInput;
    }
}
