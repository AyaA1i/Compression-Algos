public class Main {
    public static void main(String[] args) {
        //GUI.createAndShowGUI();
        VectorQuantization vq = new VectorQuantization();
        vq.compress("inputC.txt", "outputC.txt");
        vq.decompress("inputC.txt", "outputC.txt");
    }
}