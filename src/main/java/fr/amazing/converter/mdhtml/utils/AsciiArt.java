package fr.amazing.converter.mdhtml.utils;

import javax.swing.*;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class AsciiArt {
    private static AsciiArt instance;
    private AsciiArt() {
    }

    public static AsciiArt getInstance() {
        if (instance == null) {
            instance = new AsciiArt();
        }
        return instance;
    }

    public String drawString(String text, String artChar, Settings settings, boolean reverse) {
        BufferedImage image = getImageIntegerMode(settings.width, settings.height);

        Graphics2D graphics2D = getGraphics2D(image.getGraphics(), settings);
        graphics2D.drawString(text, 6, ((int) (settings.height * 0.67)));
        StringBuilder res = new StringBuilder();
        for (int y = 0; y < settings.height; y++) {
            StringBuilder stringBuilder = new StringBuilder();
            res.append("\n");
            for (int x = 0; x < settings.width; x++) {
                stringBuilder.append(charArt(image.getRGB(x, y) == -16777216, reverse, artChar));
            }

            if (stringBuilder.toString()
                    .trim()
                    .isEmpty()) {
                continue;
            }

            res.append(stringBuilder);
        }
        return res.toString();
    }

    private BufferedImage getImageIntegerMode(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    private Graphics2D getGraphics2D(Graphics graphics, Settings settings) {
        graphics.setFont(settings.font);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        return graphics2D;
    }

    public String defaultDraw(String toDraw, boolean reverse) {
        Settings settings = instance.new Settings(new Font("SansSerif", Font.BOLD, 15), toDraw.length() * 15, 15);
        return this.drawString(toDraw, "*", settings, reverse);
    }

    public class Settings {
        public Font font;
        public int width;
        public int height;

        public Settings(Font font, int width, int height) {
            this.font = font;
            this.width = width;
            this.height = height;
        }

        public Settings(int width, int height) {
            this.font = new JLabel().getFont();
            this.width = width;
            this.height = height;
        }
    }
    private String charArt(boolean isX, boolean reverse, String artChar) {
        if (!reverse) {
            return isX ? " " : artChar;
        } else {
            return isX ? artChar : " ";
        }
    }
}