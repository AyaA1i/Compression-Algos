import java.util.Vector;

public class VQCompress {
    public void compress(String inputFilePath, String outputFilePath) {
        String image = new Read_Write().readFromBinFile(inputFilePath);

        //Convert String to Vector of Vectors
        Vector<Vector<Double>> myImage = stringTo2d(image);

        //Divide the image into 2×2 blocks
        Vector<Vector<Vector<Double>>> blocks = divideToBlocks(myImage);

        //Create vector to hold the blocks created till now
        Vector<Vector<Vector<Vector<Double>>>> associated = new Vector<>();
        Vector<Vector<Vector<Vector<Double>>>> prev_associated = new Vector<>();
        Vector<Vector<Vector<Double>>> averages = new Vector<>();
        Vector<Vector<Vector<Double>>> codeBook = new Vector<>();
        associated.add(blocks);

        Vector<Vector<Double>> avg;
        while (codeBook.size() != 4) {
            //Calculate average of blocks
            averages.clear();
            for (Vector<Vector<Vector<Double>>> v_block : associated) {
                avg = getAverage(v_block);
                if (avg != null) averages.add(avg);
            }

            //Split the averages
            codeBook.clear();
            for (Vector<Vector<Double>> v_avg : averages) {
                if (v_avg == null) continue;
                // Create separate vectors for v1 and v2 in each iteration
                Vector<Vector<Double>> v1 = new Vector<>();
                Vector<Vector<Double>> v2 = new Vector<>();
                initialize(v1);
                initialize(v2);

                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        if (v_avg.get(i).get(j) == v_avg.get(i).get(j).intValue()) {
                            v1.get(i).set(j, v_avg.get(i).get(j) - 1);
                            v2.get(i).set(j, v_avg.get(i).get(j) + 1);
                        } else {
                            v1.get(i).set(j, Math.floor(v_avg.get(i).get(j)));
                            v2.get(i).set(j, Math.ceil(v_avg.get(i).get(j)));
                        }
                    }
                }
                // Add v1 and v2 to the codeBook vector
                codeBook.add(v1);
                codeBook.add(v2);
            }

            //try to associate blocks to the codebook
            associated.clear();
            for (int i = 0; i < codeBook.size(); i++) {
                associated.add(new Vector<>());
            }
            for (Vector<Vector<Double>> b : blocks) {
                int ind = match(b, codeBook);
                associated.get(ind).add(b);
            }
        }
        copy(associated, prev_associated);

        //Calculate averages of produced codebook
        averages.clear();
        for (Vector<Vector<Vector<Double>>> v_block : associated) {
            avg = getAverage(v_block);
            if (avg != null) averages.add(avg);
        }

        //Associate blocks to the averages
        associated.clear();
        for (int i = 0; i < averages.size(); i++) {
            associated.add(new Vector<>());
        }
        for (Vector<Vector<Double>> b : blocks) {
            int ind = match(b, averages);
            associated.get(ind).add(b);
        }

        //If associate = prev_associate done
        //Else calculate the average of the new associated blocks
        if (!areEqual(associated, prev_associated)) {
            codeBook.clear();
            for (Vector<Vector<Vector<Double>>> v_block : associated) {
                avg = getAverage(v_block);
                if (avg != null) codeBook.add(avg);
            }
        }

        Vector<String> Labels = constructLabels(codeBook);
        Vector<Vector<String>> CompressedImage =
                constructCompressedImage(blocks, codeBook, Labels);
        String compressedBin = convertToBinary(CompressedImage, codeBook, Labels);
        new Read_Write().writeToBinFile(outputFilePath, compressedBin);
    }

    private Vector<Vector<Vector<Double>>> divideToBlocks(Vector<Vector<Double>> myImage) {
        Vector<Vector<Vector<Double>>> blocks = new Vector<>();
        int rowsCnt = myImage.size();
        int colsCnt = myImage.get(0).size();
        for (int i = 0; i < rowsCnt; i += 2) {
            for (int j = 0; j < colsCnt; j += 2) {
                Vector<Vector<Double>> block = new Vector<>();
                for (int k = 0; k < 2; k++) {
                    Vector<Double> row = new Vector<>();
                    for (int l = 0; l < 2; l++) {
                        row.add(myImage.get(i + k).get(j + l));
                    }
                    block.add(row);
                }
                blocks.add(block);
            }
        }
        return blocks;
    }

    private Vector<Vector<Double>> stringTo2d(String image) {
        Vector<Vector<Double>> myImage = new Vector<>();
        String[] rows = image.split("\n");
        for (String row : rows) {
            String[] numbers = row.split(" ");
            Vector<Double> num = new Vector<>();
            for (String str : numbers) {
                num.add(Double.valueOf(str));
            }
            myImage.add(num);
        }
        return myImage;
    }

    private void initialize(Vector<Vector<Double>> v) {
        for (int i = 0; i < 2; i++) {
            Vector<Double> row = new Vector<>();
            for (int j = 0; j < 2; j++) {
                row.add(0.0);
            }
            v.add(row);
        }
    }

    private Vector<Vector<Double>> getAverage(Vector<Vector<Vector<Double>>> block) {
        if (block == null) return null;
        Vector<Vector<Double>> ret = new Vector<>();
        initialize(ret);

        //Sum values
        for (Vector<Vector<Double>> v : block) {
            ret.get(0).set(0, ret.get(0).get(0) + v.get(0).get(0));
            ret.get(0).set(1, ret.get(0).get(1) + v.get(0).get(1));
            ret.get(1).set(0, ret.get(1).get(0) + v.get(1).get(0));
            ret.get(1).set(1, ret.get(1).get(1) + v.get(1).get(1));
        }

        ret.get(0).set(0, ret.get(0).get(0) / block.size());
        ret.get(0).set(1, ret.get(0).get(1) / block.size());
        ret.get(1).set(0, ret.get(1).get(0) / block.size());
        ret.get(1).set(1, ret.get(1).get(1) / block.size());

        return ret;
    }

    private boolean areEqual(
            Vector<Vector<Vector<Vector<Double>>>> avg1,
            Vector<Vector<Vector<Vector<Double>>>> avg2
    ) {
        if (avg1.size() != avg2.size()) {
            return false;
        }

        double threshold = 0.0001; // Adjust the threshold based on your precision requirements

        for (int i = 0; i < avg1.size(); i++) {
            Vector<Vector<Vector<Double>>> block1 = avg1.get(i);
            Vector<Vector<Vector<Double>>> block2 = avg2.get(i);

            if (block1.size() != block2.size()) {
                return false;
            }

            for (int j = 0; j < block1.size(); j++) {
                Vector<Vector<Double>> row1 = block1.get(j);
                Vector<Vector<Double>> row2 = block2.get(j);

                if (row1.size() != row2.size()) {
                    return false;
                }

                for (int k = 0; k < row1.size(); k++) {
                    Vector<Double> innerRow1 = row1.get(k);
                    Vector<Double> innerRow2 = row2.get(k);

                    if (innerRow1.size() != innerRow2.size()) {
                        return false;
                    }

                    for (int l = 0; l < innerRow1.size(); l++) {
                        if (Math.abs(innerRow1.get(l) - innerRow2.get(l)) > threshold) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private int match(Vector<Vector<Double>> block, Vector<Vector<Vector<Double>>> codeBook) {
        double min = 10000, ret_ind = 0, cnt = 0;
        for (Vector<Vector<Double>> v : codeBook) {
            if (v.get(0).get(0).isNaN()) {
                cnt++;
                continue;
            }
            double total = 0;
            total += Math.abs((block.get(0).get(0) - v.get(0).get(0)));
            total += Math.abs((block.get(0).get(1) - v.get(0).get(1)));
            total += Math.abs((block.get(1).get(0) - v.get(1).get(0)));
            total += Math.abs((block.get(1).get(1) - v.get(1).get(1)));
            if (min > total) {
                min = total;
                ret_ind = cnt;
            }
            cnt++;
        }
        return (int) ret_ind;
    }

    private void copy(Vector<Vector<Vector<Vector<Double>>>> source, Vector<Vector<Vector<Vector<Double>>>> target) {
        if (source == null) return;

        // Clear both target and target2
        target.clear();

        for (Vector<Vector<Vector<Double>>> outerVector : source) {
            Vector<Vector<Vector<Double>>> newOuterVector1 = new Vector<>();
            Vector<Vector<Vector<Double>>> newOuterVector2 = new Vector<>();

            for (Vector<Vector<Double>> innerVector : outerVector) {
                Vector<Vector<Double>> newInnerVector1 = new Vector<>();
                Vector<Vector<Double>> newInnerVector2 = new Vector<>();

                for (Vector<Double> doubleVector : innerVector) {
                    // Copy each inner vector for both target and target2
                    Vector<Double> newDoubleVector1 = new Vector<>(doubleVector);
                    Vector<Double> newDoubleVector2 = new Vector<>(doubleVector);

                    newInnerVector1.add(newDoubleVector1);
                    newInnerVector2.add(newDoubleVector2);
                }

                newOuterVector1.add(newInnerVector1);
                newOuterVector2.add(newInnerVector2);
            }

            target.add(newOuterVector1);
        }
    }

    private Vector<String> constructLabels(Vector<Vector<Vector<Double>>> codebook) {
        Vector<String> labels = new Vector<>();
        int sz = codebook.size();
        int numOfbits = 0;
        while (Math.pow(2, numOfbits) < sz) {
            numOfbits++;
        }
        for (int i = 0; i < sz; i++) {
            String label = Integer.toBinaryString(i);
            int labelsz = label.length();
            String temp = "";
            while (labelsz < numOfbits) {
                temp += "0";
                labelsz++;
            }
            temp += label;
            label = temp;
            labels.add(label);
        }
        return labels;
    }

    private Vector<Vector<String>> constructCompressedImage(Vector<Vector<Vector<Double>>> blocks,
                                                            Vector<Vector<Vector<Double>>> codeBook,
                                                            Vector<String> Labels) {
        Vector<Vector<String>> CompressedImage = new Vector<>();
        Vector<String> temp = new Vector<>();
        for (int i = 0; i < blocks.size(); i++) {
            Vector<Vector<Double>> block = blocks.get(i);
            int indx = match(block, codeBook);
            temp.add(Labels.get(indx));
            if (temp.size() == 3) {
                Vector<String> temp2 = new Vector<>();
                for (String j : temp) temp2.add(j);
                CompressedImage.add(temp2);
                temp.clear();
            }
        }
        return CompressedImage;
    }

    private String convertToBinary(Vector<Vector<String>> CompressedImage,
                                   Vector<Vector<Vector<Double>>> codeBook,
                                   Vector<String> Labels) {
        String compressedStream = "";
        for (int i = 0; i < CompressedImage.size(); i++) {
            for(int j = 0; j < CompressedImage.get(i).size();j++){
                StringBuilder paddedString = new StringBuilder(CompressedImage.get(i).get(j));
                while(paddedString.length() % 8 !=0){
                    paddedString = new StringBuilder("0" + paddedString);
                }
                compressedStream += paddedString;
            }
        }
        for (int i = 0; i < codeBook.size(); i++) {
            for(int j = 0; j < codeBook.get(i).size();j++){
                for(int k = 0;k < codeBook.get(i).get(j).size();k++){
                    StringBuilder paddedString =
                            new StringBuilder(String.format("%8s", Integer.toBinaryString((int) Math.round(codeBook.get(i).get(j).get(k)) & 0xFF)).replace(' ', '0'));

                    while(paddedString.length() % 8 !=0){
                        paddedString = new StringBuilder("0" + paddedString);
                    }
                    compressedStream += paddedString;
                }
            }
            StringBuilder paddedString =
                    new StringBuilder(Labels.get(i));
            while(paddedString.length() % 8 !=0){
                paddedString = new StringBuilder("0" + paddedString);
            }
            compressedStream += paddedString;
        }
        String storedString = "";
        for(int i=0;i<compressedStream.length();i+=8){
            storedString += (char)(Integer.parseInt(compressedStream.substring(i , i+8), 2));
        }
        return storedString;
    }


}
