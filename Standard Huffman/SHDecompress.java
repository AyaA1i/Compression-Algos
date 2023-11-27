import java.util.HashMap;
import java.util.Map;

public class SHDecompress {
    SHDecompress(String inputFilePath, String outputFilePath){
        String txtInput = new Writer().readFromBinFile(inputFilePath);

        int i = 0, padding = 0, rows;
        Map<String, Character> codes = new HashMap<>();
        StringBuilder compressedStream = new StringBuilder();
        while (i < txtInput.length()) {
            if(i == 0){
                padding = txtInput.charAt(i++);
            }
            else if(i == 1){
                rows = txtInput.charAt(i++);
                for (int j = 0; j < rows; j++) {
                    char character = txtInput.charAt(i++);
                    String code = String.format("%8s", Integer.toBinaryString(txtInput.charAt(i++) & 0xFF)).replace(' ', '0');
                    int len = txtInput.charAt(i++);
                    codes.put(code.substring(code.length() - len), character);
                }
            }
            else{
                String code = String.format("%8s", Integer.toBinaryString(txtInput.charAt(i++) & 0xFF)).replace(' ', '0');
                compressedStream.append(code);
            }
        }
        if(padding != 0){
            compressedStream = new StringBuilder(compressedStream.substring(padding));
        }
        String result = decompress(String.valueOf(compressedStream), codes);
        new Writer().writeToBinFile(outputFilePath, result);
    }
    public String decompress(String compressedStream, Map<String, Character> codes){
        int i = 0;
        StringBuilder result = new StringBuilder();
        while(i != compressedStream.length()){
            String curr = String.valueOf(compressedStream.charAt(i++));
            while(!codes.containsKey(curr)){
                if(i == compressedStream.length())break;
                curr += compressedStream.charAt(i++);
            }
            result.append(codes.get(curr));
        }
        return String.valueOf(result);
    }
}
