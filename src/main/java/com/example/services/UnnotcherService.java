package com.example.services;

import com.example.misc.EasingFunction;
import com.example.misc.Vector4f;
import com.wizzardo.http.framework.di.Service;
import com.wizzardo.tools.image.ImageTools;
import com.wizzardo.tools.image.JpegDecoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UnnotcherService implements Service {

    public byte[] process(File file, String filename, int width, int height) throws IOException {
        System.out.println("processing " + filename);
        BufferedImage image;
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            JpegDecoder decoder = new JpegDecoder();
            try {
                image = decoder.read(file);
            } catch (Exception e) {
                e.printStackTrace();
                image = ImageTools.read(file);
            }
        } else {
            image = ImageTools.read(file);
        }

        if (image == null)
            throw new IllegalStateException();

//        Dimension screenSize = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes())
//                .map(displayMode -> new Dimension(displayMode.getWidth(), displayMode.getHeight()))
//                .max(Comparator.comparingInt(value -> value.width))
//                .get();

//        Dimension screenSize = new Dimension(3456, 2234);
        Dimension screenSize = new Dimension(width, height);

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        double screenRatio = screenSize.getWidth() / screenSize.getHeight();
        double imageRatio = image.getWidth() * 1.0 / image.getHeight();
        double scale;
        if (imageRatio > screenRatio) {
            scale = screenSize.getHeight() / image.getHeight();
        } else {
            scale = screenSize.getWidth() / image.getWidth();
        }
        image = ImageTools.resize(image, scale);
        image = ImageTools.crop(image, 0, 0, screenSize.width, screenSize.height);

        Graphics2D g = image.createGraphics();

        int barHeight = 75;
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, image.getWidth(), height);
//        GradientPaint gradient = new GradientPaint(3024/2-200, 0, Color.black, 3024/2, height, Color.BLACK);
//        g.setPaint(gradient);

        gradient(image, barHeight, screenSize.width);

        g.dispose();
//        if (!new File(toDir).exists())
//            new File(toDir).mkdirs();
//        ImageTools.saveJPG(image, new File(toDir, file.getName()), 90);
        return ImageTools.saveJPGtoBytes(image, 90);
    }

    private void gradient(BufferedImage image, int height, int screenWidth) {
        int gradientWidth = screenWidth / 2;
        EasingFunction easing = EasingFunction.EASE_IN_OUT_QUINT;
        Vector4f to = new Vector4f(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 255);
        Vector4f temp1 = new Vector4f();
        Vector4f temp2 = new Vector4f();

        for (int y = 0; y < height; y++) {
            for (int i = 0; i < gradientWidth; i++) {
                int x = screenWidth / 2 - gradientWidth + i;
                image.setRGB(x, y, getColor(image, x, y, to, easing.ease(i * 1f / gradientWidth), temp1, temp2));
            }
        }
        for (int y = 0; y < height; y++) {
            for (int i = 0; i < gradientWidth; i++) {
                int x = screenWidth / 2 + gradientWidth - i - 1;
                image.setRGB(x, y, getColor(image, x, y, to, easing.ease(i * 1f / gradientWidth), temp1, temp2));
            }
        }
    }

    private int getColor(BufferedImage image, int x, int y, Vector4f to, float percent, Vector4f temp, Vector4f temp2) {
        int rgb = image.getRGB(x, y);
        temp2.set(ImageTools.red(rgb), ImageTools.green(rgb), ImageTools.blue(rgb), 255);

        temp.slerp(temp2, to, percent).normalizeLocal();
        int argb = ((((int) (temp.w * 255 + 0.5f)) & 0xFF) << 24) |
                ((((int) (temp.x * 255 + 0.5f)) & 0xFF) << 16) |
                ((((int) (temp.y * 255 + 0.5f)) & 0xFF) << 8) |
                ((((int) (temp.z * 255 + 0.5f)) & 0xFF));
        return argb;
    }
}
