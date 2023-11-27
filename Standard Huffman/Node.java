class Node implements Comparable<Node> {
    char ch;
    int freq;
    Node left, right;

    public Node(Character ch, Integer freq) {
        this.ch = ch;
        this.freq = freq;
        this.left = this.right = null;

    }
    @Override
    public int compareTo(Node other) {
        return this.freq - other.freq;
    }
}