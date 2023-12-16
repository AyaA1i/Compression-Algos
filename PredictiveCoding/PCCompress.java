import java.util.Vector;

public class PCCompress {
    public void compress(Vector<Vector<Double>> myImage, String outputFilePath) {
        Vector<Vector<Double>> predicted = new Vector<>();
        Vector<Vector<Double>> diff = new Vector<>();
        predicted = generatePredicted(myImage);
        diff = generateDifference(myImage , predicted);

    }

    private Vector<Vector<Double>> generatePredicted(Vector<Vector<Double>> myImage) {
        Vector<Vector<Double>> predicted = new Vector<>();
        for (int x = 0; x < myImage.size(); x++) {
            predicted.add(new Vector<>());
            for (int y = 0; y < myImage.get(x).size(); y++) {
                if (x - 1 >= 0 && y - 1 >= 0) {
                    predicted.get(x).add((myImage.get(x - 1).get(y) + myImage.get(x).get(y - 1))/2);
                } else {
                    predicted.get(x).add((myImage.get(x).get(y)));
                }
            }
        }
        return predicted;
    }

    private Vector<Vector<Double>> generateDifference(Vector<Vector<Double>> myImage, Vector<Vector<Double>> predicted) {
        Vector<Vector<Double>> diff = new Vector<>();
        for (int x = 0; x < myImage.size(); x++) {
            diff.add(new Vector<>());
            for (int y = 0; y < myImage.get(x).size(); y++) {
                diff.get(x).add((myImage.get(x).get(y) - predicted.get(x).get(y)));
            }
        }
        return diff;
    }

    private void generateQuantizedDifference() {

    }

}