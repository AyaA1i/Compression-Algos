import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class LZ77 {
    String file;
    Scanner sc = new Scanner(System.in);
    String data = "";

    private static class Tag {
        public int pos;
        public int length;
        public Character nextc;

        Tag(int p, int l, Character c) {
            pos = p;
            length = l;
            nextc = c;
        }
    }

    public void readFiles() throws IOException {
        int choice;
        System.out.println("if you want to compress choose 1 otherwise 2:");
        choice = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the file name:");
        file = sc.nextLine();
        getData();
        if (choice == 1) {
            compress(data);
        } else {
            decompress();
        }
    }

    public void getData() {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void compress(String s) {
        ArrayList<Tag> tags = new ArrayList<Tag>();
        String tmp = "";
        int prevInd = -1;

        for (int i = 0; i < s.length(); ++i) {
            tmp += s.charAt(i);
            int pos = s.substring(0, i - tmp.length() + 1).lastIndexOf(tmp);
            if (pos == -1) {
                tags.add(new Tag((prevInd == -1 ? 0 : i - tmp.length() + 1 - prevInd),
                        tmp.length() - 1, s.charAt(i)));
                tmp = "";
            } else {
                prevInd = pos;
            }
        }

        if (!tmp.isEmpty()) {
            tags.add(new Tag(s.length() - tmp.length() - prevInd,
                    tmp.length(), null));
        }

        writeinfile(tags);
    }

    public void writeinfile(ArrayList<Tag> ans) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Tag an : ans) {
                bufferedWriter.write("<");
                bufferedWriter.write(String.valueOf(an.pos));
                bufferedWriter.write(",");
                bufferedWriter.write(String.valueOf(an.length));
                bufferedWriter.write(",");
                bufferedWriter.write(String.valueOf(an.nextc));
                bufferedWriter.write(">");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }
    public void decompress() throws IOException {
        StringBuilder uncomp = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            StringBuilder pos = new StringBuilder();
            StringBuilder length = new StringBuilder();
            StringBuilder c = new StringBuilder();
            if (data.charAt(i) == '<') continue;
            while (data.charAt(i) != ',') {
                pos.append(data.charAt(i));
                i++;
            }
            i++;
            while (data.charAt(i) != ',') {
                length.append(data.charAt(i));
                i++;
            }
            i++;
            String last = "";
            while (data.charAt(i) != '>') {
                last += (data.charAt(i));
                i++;
            }
            if(!last.equals("null"))c.append(last);
            int sz = uncomp.length();
            sz -= parseInt(pos.toString());
            for (int j = sz; j < (sz + parseInt(length.toString())); j++) {
                uncomp.append(uncomp.charAt(j));
            }
            if(!c.isEmpty())uncomp.append(c);
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(uncomp.toString());
        bufferedWriter.close();
    }
}
