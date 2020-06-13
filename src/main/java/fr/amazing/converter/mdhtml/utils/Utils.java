package fr.amazing.converter.mdhtml.utils;

import fr.amazing.converter.mdhtml.config.Context;
import fr.amazing.converter.mdhtml.core.tree.TreeNode;
import freemarker.template.utility.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Comparator;

public class Utils {
    private static Context ctx = Context.getInstance();

    private Utils() {

    }

    public static Comparator<TreeNode<File>> tnOfFileComparator() {
        return (o1, o2) -> {
            int separator = StringUtils.countMatches(o1.getContent().getAbsolutePath(), File.separator) - StringUtils.countMatches(o2.getContent().getAbsolutePath(), File.separator);
            if (separator == 0) {
                var comp =  StringUtils.compare(o1.getContent().getName(), o2.getContent().getName());
                return comp;
            }
            return separator;
        };
    }

    public static String buildFileName(File f, String extension) {
        String wd = ctx.getWorkDir();
        String parent = StringUtils.remove(f.getParent(), wd);
        String name = f.getName();
        if (StringUtils.contains(name, ctx.getOptions().getHomePageMd())) {
            return StringUtils.stripAccents(new StringBuilder(ctx.getOptions().getHomePage()).append(".").append(extension).toString());
        } else {
            return StringUtils.stripAccents(new StringBuilder(StringUtils.replace(parent, File.separator, "_"))
                    .append("_").append(FilenameUtils.removeExtension(f.getName()))
                    .append(".").append(extension).toString());
        }
    }

    public String asciiArt(String toDraw, String art) {
        BufferedImage bufferedImage = new BufferedImage(
                60, 40,
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString(toDraw, 12, 24);
        return "";
    }
}
