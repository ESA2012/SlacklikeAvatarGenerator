package esa2012;

import esa2012.generator.AvaGen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    static void savePNGImage(BufferedImage image, String imageFile) throws IOException {
        File outF = new File(imageFile);
        ImageIO.write(image, "PNG", outF);
    }


    public static void main(String[] args) throws IOException {

        AvaGen avaGen = new AvaGen();

        BufferedImage ava1 = avaGen.getAvatar(5, 0);    // best values 3, 4, 5
        BufferedImage ava2 = avaGen.getMirroredAvatar(6);      // best values 4 - 7
        BufferedImage ava3 = avaGen.getRectAvatar(7); // best values 3, 4, 5, 6, 7, 8, 9

        savePNGImage(ava1, "./big1.png");
        savePNGImage(ava2, "./big2.png");
        savePNGImage(ava3, "./big3.png");

        savePNGImage(avaGen.resizeImage(ava1, 128, 128), "./small1.png");
        savePNGImage(avaGen.resizeImage(ava2, 128, 128), "./small2.png");
        savePNGImage(avaGen.resizeImage(ava3, 128, 128), "./small3.png");
    }
}
