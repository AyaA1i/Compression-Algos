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
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = -1 << 24;
                //1010 |
                value = (int) (0xff000000 | (Math.round(pixels.get(y).get(x)) << 16) | (Math.round(pixels.get(y).get(x)) << 8) | Math.round((pixels.get(y).get(x))));
                image.setRGB(x, y, value);
            }
        }
        return image;
    }
}
