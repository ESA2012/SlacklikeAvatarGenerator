package esa2012.generator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Random;


public class AvaGen {

    private static final int IMAGE_SIZE = 1024;

    private Color generateColor(boolean useAlpha) {
        int[] values = new int[4];
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            values[i] = (random.nextInt(10) + 6) * 15;
        }
        if (useAlpha) {
            return new Color(values[0], values[1], values[2], 150);
        } else {
            return new Color(values[0], values[1], values[2]);
        }
    }


    private BufferedImage rotateImage(BufferedImage source, double angle) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), IMAGE_SIZE / 2, IMAGE_SIZE / 2);

        BufferedImage dest = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

        BufferedImageOp filtered = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        filtered.filter(source, dest);

        return dest;
    }


    private BufferedImage cropImage(BufferedImage source) {
        int quarter = IMAGE_SIZE / 4;
        int half = IMAGE_SIZE / 2;
        return source.getSubimage(quarter, quarter, half, half);
    }


    public BufferedImage resizeImage(BufferedImage source, int newWidth, int newHeight) {
        AffineTransform transform = new AffineTransform();
        transform.scale((double)newWidth / (double)source.getWidth(), (double)newHeight / (double)source.getHeight());

        BufferedImage dest = new BufferedImage(newWidth, newHeight, source.getType());

        BufferedImageOp filtered = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        filtered.filter(source, dest);

        return dest;
    }


    private Color darkenColor(Color color, int value) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        int newR = r >= value ? r - value : 0;
        int newG = g >= value ? g - value : 0;
        int newB = b >= value ? b - value : 0;

        return new Color(newR, newG, newB, color.getAlpha());
    }


    public BufferedImage getMirroredAvatar(int halfSide) {
        int blocks = halfSide * 2;

        BufferedImage bufferedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int side = IMAGE_SIZE / blocks;

        Color[][] colorMatrix = new Color[blocks][blocks];
        for(int cx = 0; cx < halfSide; cx ++) {
            for(int cy = 0; cy < halfSide; cy ++) {
                Color color = generateColor(false);
                colorMatrix[cx][cy] = color;
                colorMatrix[blocks - 1 - cx][cy] = color;
                colorMatrix[blocks - 1 - cx][blocks - 1 - cy] = color;
                colorMatrix[cx][blocks - 1 - cy] = color;
            }
        }

        for(int qx = 0; qx < 2; qx ++) {
            for(int qy = 0; qy < 2; qy ++) {
                for (int x = 0; x < blocks; x++) {
                    for (int y = 0; y < blocks; y ++) {
                        Rectangle rect = new Rectangle(
                                qx * halfSide + x * side,
                                qy * halfSide + y * side,
                                qx * halfSide + side,
                                qy * halfSide + side);
                        g2d.setColor(colorMatrix[x][y]);
                        g2d.fill(rect);
                    }
                }
            }
        }
        g2d.dispose();
        return cropImage(rotateImage(bufferedImage, 25.0));
    }


    public BufferedImage getRectAvatar(int blocks) {
        BufferedImage bufferedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int side = IMAGE_SIZE / blocks;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);

        for (int x = 0; x < blocks; x += 2) {
            Rectangle rect = new Rectangle(x * side, 0, side, IMAGE_SIZE);
            g2d.setColor(darkenColor(generateColor(true), 50));
            g2d.fill(rect);
        }

        for (int y = 0; y < blocks; y += 2) {
            Rectangle rect = new Rectangle(0, y * side, IMAGE_SIZE, side);
            g2d.setColor(darkenColor(generateColor(true), 50));
            g2d.fill(rect);
        }

        g2d.dispose();
        return cropImage(rotateImage(bufferedImage, 25.0));
    }


    public BufferedImage getAvatar(int blocks, int lineThik) {
        BufferedImage bufferedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int side = IMAGE_SIZE / blocks;

        Color lineColor = generateColor(false);
        g2d.setStroke(new BasicStroke(lineThik * 2));

        for (int x = 0; x < blocks; x++) {
            for (int y = 0; y < blocks; y ++) {
                Rectangle rect = new Rectangle(x * side, y * side, side, side);
                if (lineThik > 0) {
                    g2d.setColor(lineColor);
                    g2d.draw(rect);
                }
                g2d.setColor(generateColor(false));
                g2d.fill(rect);
            }
        }

        g2d.dispose();
        return cropImage(rotateImage(bufferedImage, 25.0));
    }

}
