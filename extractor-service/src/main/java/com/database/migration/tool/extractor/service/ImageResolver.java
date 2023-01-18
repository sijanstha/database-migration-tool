package com.database.migration.tool.extractor.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageResolver {
    public BufferedImage resolveLogo(String imageName) {
        String fullImagePath = String.format("/%s/%s", "static", imageName);
        try {
            BufferedImage logo = ImageIO.read(this.getClass().getResource(fullImagePath));
            return logo;
        } catch (IOException e) {
            System.out.println("e = " + e);
            return null;
        }
    }
}
