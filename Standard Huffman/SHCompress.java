import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class SHCompress {
    public SHCompress(String inputFilePath, String outputFilePath){
        String txtInput = new Writer().readFromBinFile(inputFilePath);

        Map<Character, String> codes = new HashMap<>();
        compress(buildTree(txtInput), "", codes);
        new Writer().writeToBinFile(outputFilePath, buildResult(codes, txtInput));
    }
    private Node buildTree(String txtInput){
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : txtInput.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (var entry: freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            assert right != null;
            Node internalNode = new Node('-', left.freq + right.freq);
            internalNode.right = right;
            internalNode.left = left;
            pq.add(internalNode);
        }
        return pq.poll();

    }
    private String buildResult(Map<Character, String> codes, String txtInput){
        StringBuilder result = new StringBuilder();
        StringBuilder compressedStream = new StringBuilder();
        for (int i = 0; i < txtInput.length(); i++) {
            compressedStream.append(codes.get(txtInput.charAt(i)));
        }
        int padding = 0;
        while(compressedStream.length() % 8 != 0){
            compressedStream = new StringBuilder("0" + compressedStream);
            padding++;
        }
        int rows = codes.size();
        //Put padding zeroes
        result.append((char) padding);
        //Put number of rows
        result.append((char) rows);
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            //Put character
            result.append(entry.getKey());
            StringBuilder code = new StringBuilder(entry.getValue());
            int codeLen = entry.getValue().length();
            while(code.length() < 8){
                code.insert(0, "0");
            }
            //Put character's code
            result.append((char) (Integer.parseInt(String.valueOf(code), 2)));
            //Put character code's length
            result.append((char) codeLen);
        }
        //Put compressed stream
        String compressed = String.valueOf(compressedStream);

        while(!compressed.isEmpty()){
            String b;
            if(compressed.length() > 8)b = compressed.substring(0, 8);
            else b = compressed;
            compressed = compressed.substring(8);
            result.append((char) (Integer.parseInt(b, 2)));
        }
        return result.toString();
    }
    public void compress(Node root, String str, Map<Character, String> huffmanCode){
        if(root == null)return;
        if (root.right == null && root.left == null && Character.isLetter(root.ch)) {
            huffmanCode.put(root.ch, str);
        }
        compress(root.left, str + '0', huffmanCode);
        compress(root.right, str + '1', huffmanCode);
    }
}
