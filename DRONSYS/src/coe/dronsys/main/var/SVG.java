package coe.dronsys.main.var;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Nico & Jesús on 2/11/16.
 */
public class SVG {
    public static enum ImageType {PNG, JPEG}

    /**
     * It will generate a new PNG image (given as a byte array) out of an original one (also given as an array of bytes) and a label which will appear written on it
     *
     * @param inputBytes the starting image
     * @param label      the label to be written on the image
     * @return bytes of the PNG resulting buffered image
     * @throws Exception
     */
    public static byte[] beautifyPicture(byte[] inputBytes, String label) {
        try {
            // The input image
            BufferedImage bin = ImageIO.read(new ByteArrayInputStream(inputBytes));
            String svgString = new String(inputBytes, "UTF-8");
            // The transformed image
            BufferedImage bout = SVG.createOverlay(svgString,ImageType.PNG,600,600);
            // BufferedImage bout = SVG.updateUserImage(bin, toXML(label, bin.getWidth(), bin.getHeight()));
            // Image to array of bytes..
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(bout, "png", baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            baos.close();

            // Return the bytes
            return bytes;

        } catch (Exception e) {
            e.printStackTrace();
            return inputBytes;
        }


    }

    private static String toXML(String label, int width, int height) {

        String result = "<?xml version=\"1.0\" standalone=\"yes\"?>\n" +
                "   <svg width=\"" + width + "\" height=\"" + height + "\"  xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\">\n" +
                "       <text x=\"0\" y=\"10\">" + label + "</text>\n" +
                "   </svg>";

        return result;
    }

    private static BufferedImage updateUserImage(BufferedImage originalImage, String label) throws Exception {

        // Create the overlay
        BufferedImage overlay = createOverlay(label, ImageType.PNG, originalImage.getWidth(), originalImage.getHeight());

        // New image: drawing the overlay on top of the original
        BufferedImage result = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.drawImage(overlay, 0, 0, null);
        g.dispose();

        // Test
        ImageIO.write(result, "png", new File("/tmp/output.png"));

        // Return new image
        return result;
    }


    private static BufferedImage createOverlay(String label, ImageType imageType, int width, int height) throws Exception {

        // Obtain the right transcoder
        Transcoder transcoder;
        if (ImageType.PNG.equals(imageType)) {
            transcoder = new PNGTranscoder();
        } else if (ImageType.JPEG.equals(imageType)) {
            transcoder = new JPEGTranscoder();
            transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
        } else {
            throw new Exception("Image type not specified");
        }

        // Set width and height
        transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float) width);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float) height);

        // Generate the overlay as a BufferedImage
        BufferedImage result;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            TranscoderInput transcoderInput = new TranscoderInput(new StringReader(label));
            TranscoderOutput transcoderOutput = new TranscoderOutput(out);
            transcoder.transcode(transcoderInput, transcoderOutput);
            //ImageIO.write(bimg, "jpg", fos);
            result = ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
            out.flush();
            out.close();
        } catch (TranscoderException e) {
            throw new IOException(e);
        }

        // Return result
        return result;
    }

    public static void main(String[] args) throws Exception {
        String label = "Hello" ;
        //File backgroundImageFile = new File("/root/Downloads/svgExploit.svg") ;
        //BufferedImage bi = ImageIO.read(backgroundImageFile);
        //String svgString = new String(, "UTF-8");


        byte[] encoded = Files.readAllBytes(Paths.get("/root/Downloads/svgExploit.svg"));
        String svgString =  new String(encoded, "UTF-8");


        BufferedImage bo = createOverlay(svgString, ImageType.PNG, 2063, 333);
        ImageIO.write(bo, "png", new File("/tmp/output.png"));
    }
}

