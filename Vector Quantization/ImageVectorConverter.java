import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

public class ImageVectorConverter {
    public static Vector<Vector<Vector<Integer>>>  imageTo2DVector(String imagePath) {
        try {
            // Read the image
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Get image dimensions
            int width = image.getWidth();
            int height = image.getHeight();

            // Create a 2D vector to store pixel values
            Vector<Vector<Vector<Integer>>>imagePixels = new Vector<>();
            for (int i = 0; i < width; i++) {
                imagePixels.add(new Vector<>());
                for (int j = 0;j<height;j++){
                    imagePixels.get(i).add(new Vector<>());
                }
            }
            // Iterate through each pixel and store its grayscale value
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelValue = image.getRGB(x, y);
                    int red = (pixelValue >> 16) & 0xFF;
                    int green = (pixelValue & 0x0000ff00) >> 8;
                    int blue = pixelValue & 0x000000ff;
                    imagePixels.get(y).get(x).add( red);
                    imagePixels.get(y).get(x).add( green);
                    imagePixels.get(y).get(x).add( blue);
                }
            }

            return imagePixels;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage convertToImage(Vector<Vector<Vector<Double>>> pixels) {
        int width = pixels.get(0).size();
        int height = pixels.size();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = -1 << 24;
                value = (int) (0xff000000 | (Math.round(pixels.get(y).get(x).get(0)) << 16) | (Math.round(pixels.get(y).get(x).get(1)) << 8) | Math.round((pixels.get(y).get(x).get(2))));
                image.setRGB(x, y, value);
            }
        }
        return image;
    }
}
