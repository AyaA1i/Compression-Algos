import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

public class ImageVectorConverter {
    public static Vector<Vector<Integer>> imageTo2DVector(String imagePath) {
        try {
            // Read the image
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Get image dimensions
            int width = image.getWidth();
            int height = image.getHeight();

            // Create a 2D vector to store pixel values
            Vector<Vector<Integer>> pixelValues = new Vector<>(height);

            // Iterate through each pixel and store its grayscale value
            for (int y = 0; y < height; y++) {
                Vector<Integer> row = new Vector<>(width);
                for (int x = 0; x < width; x++) {
                    int pixelValue = image.getRGB(x, y);
                    int intensity = (pixelValue >> 16) & 0xFF;

                    row.add(intensity);
                }
                // Add the row to the 2D vector
                pixelValues.add(row);
            }

            return pixelValues;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage convertToImage(Vector<Vector<Double>> pixels) {
        int width = pixels.get(0).size();
        int height = pixels.size();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grayValue = (int) (pixels.get(y).get(x) * 255);
                image.getRaster().setSample(x, y, 0, grayValue);
            }
        }

        return image;
    }
}
