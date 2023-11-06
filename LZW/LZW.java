import java.util.*;
import java.io.*;
public class LZW {
    static String file;
    static Scanner sc = new Scanner(System.in);
    static String Data="";
    public static void readFiles(String s){
        file = s;
        ReadFromFile();
    }
    public static void compress() throws IOException {
        int dicSize = 128;
        List<Integer>CompressedStream = new ArrayList<>();
        Map<String , Integer> dictionary = new HashMap<>();
        dictionary.put(null , 0);
        for(int i = 0; i < 26; i++){
            dictionary.put(String.valueOf((char)('A'+i)), 'A'+i);
            dictionary.put(String.valueOf((char)('a'+i)), 'a'+i);
        }
        for(int i = 0;i<Data.length();i++){
            String Search="";
            Search += Data.charAt(i);
            while(i+1 < Data.length() && dictionary.get(Search)!=null){
                i++;
                Search+=Data.charAt(i);
            }
            String NewValue;
            if(dictionary.get(Search)==null){
                i--;
                NewValue = Search.substring(0,Search.length()-1);
                dictionary.put(Search,dicSize++);
            }else{
                NewValue = Search;
            }
            CompressedStream.add(dictionary.get(NewValue));
        }
        WriteIntoFile(CompressedStream);
    }
    private static void WriteIntoFile(List<Integer> CompressedStream) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        StringBuilder binary = new StringBuilder();
        for(Integer i : CompressedStream){
            String b = Integer.toBinaryString(i);
            StringBuilder Zeros= new StringBuilder();
            while(b.length()+Zeros.length() < 8){
                Zeros.append('0');
            }
            b = Zeros + b;
            binary.append(b);
        }
        for(int i=0;i<binary.length();i+=8){
            String Var = binary.substring(i , i+8);
            int decimalValue = Integer.parseInt(Var, 2);
            char asciiChar = (char) decimalValue;
            bufferedWriter.write(asciiChar);
        }
        bufferedWriter.close();
    }
    private static void ReadFromFile(){
        Data = "";
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Data += line;
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
static void decompress() throws IOException {
        ArrayList<String> dic = new ArrayList<>();
        String s = Data;
        for (int i = 0; i < 128; i++) {
            dic.add(String.valueOf((char) i));
        }
        String ret = String.valueOf(s.charAt(0)), prev = ret;
        for (int i = 1; i < s.length(); i++) {
            String tmp;
            if (dic.size() != (int)s.charAt(i)) {
                ret += dic.get(s.charAt(i));
                tmp = prev + dic.get(s.charAt(i)).charAt(0);
                dic.add(tmp);
                prev = dic.get(s.charAt(i));
            } else {
                tmp = prev + prev.charAt(0);
                ret += tmp;
                dic.add(tmp);
                prev = tmp;
            }
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(ret);
        bufferedWriter.close();
    }
}
