import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class FontRenderer {

    private final String path;

    private final float size;

    public final int FONT_HEIGHT = 9;

    private final Map<String, Float> cachedStringWidth;

    private float antiAliasingFactor;

    private UnicodeFont unicodeFont;

    private int prevScaleFactor;

    private final ColorEffect colorEffect = new ColorEffect(Color.WHITE);

    private final Pattern pattern = Pattern.compile("§[0123456789abcdefklmnor]");

    public FontRenderer(String path, float size) {
        this.path = path;
        this.size = size;

        this.cachedStringWidth = new HashMap<>();
        this.prevScaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        try {
            this.prevScaleFactor = resolution.getScaleFactor();
            this.unicodeFont = new UnicodeFont(this.getFontFromInput(path).deriveFont(this.size * this.prevScaleFactor / 2.0F));
            this.unicodeFont.addAsciiGlyphs();
            this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
            this.unicodeFont.loadGlyphs();
            this.antiAliasingFactor = resolution.getScaleFactor();
        } catch (SlickException | IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void drawStringShadow(String text, float x, float y, Color color) {
        drawString(text, x + 1.0F, y + 1.0F, Color.BLACK);
        drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, Color color) {
        drawString(text, x - ((int) getWidth(text) >> 1), y, color);
    }

    public void drawCenteredStringShadow(String text, float x, float y, Color color) {
        drawString(text, x + 1.0F, y + 1.0F, Color.BLACK);
        drawString(text, x - ((int) getWidth(text) >> 1), y, color);
    }
    
    public void drawString(String text, float x, float y, Color color) {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        if (text == null) return;
        if (resolution.getScaleFactor() != this.prevScaleFactor) {
            try {
                this.prevScaleFactor = resolution.getScaleFactor();
                this.unicodeFont = new UnicodeFont(this.getFontFromInput(path).deriveFont(this.size * this.prevScaleFactor / 2.0f));
                this.unicodeFont.addAsciiGlyphs();
                this.unicodeFont.getEffects().add(colorEffect);
                this.unicodeFont.loadGlyphs();
            } catch (SlickException | IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }
        }

        this.antiAliasingFactor = resolution.getScaleFactor();

        GL11.glPushMatrix();
        GL11.glScalef(1.0F / this.antiAliasingFactor, 1.0F / this.antiAliasingFactor, 1.0F / this.antiAliasingFactor);

        x *= this.antiAliasingFactor;
        y *= this.antiAliasingFactor;

        float originalX = x;

        float red = color.getRed() / 255.0f;
        float green = color.getGreen() / 255.0f;
        float blue = color.getBlue() / 255.0f;
        float alpha = color.getAlpha() / 255.0f;

        char[] characters = text.toCharArray();
        int index = 0;

        GL11.glColor4f(red, green, blue, alpha);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        String[] parts = pattern.split(text);
        int[] colorCodes = new int[]{0, 170, 43520, 43690, 11141120, 11141290, 16755200, 11184810, 5592405, 5592575, 5635925, 5636095, 16733525, 16733695, 16777045, 16777215};

        Color currentColor = color;

        for (final String s : parts) {
            for (final String s2 : s.split("\n")) {
                for (final String s3 : s2.split("\r")) {
                    this.unicodeFont.drawString(x, y, s3, new org.newdawn.slick.Color(currentColor.getRGB()));
                    x += this.unicodeFont.getWidth(s3);
                    index += s3.length();

                    if (index < characters.length && characters[index] == '\r') {
                        x = originalX;
                        ++index;
                    }
                }
                if (index < characters.length && characters[index] == '\n') {
                    x = originalX;
                    y += this.getHeight(s2) * 2.0f;
                    ++index;
                }
            }
            if (index < characters.length) {
                final char colorCode = characters[index];
                if (colorCode == '§') {
                    final char colorChar = characters[index + 1];
                    final int codeIndex = "0123456789abcdef".indexOf(colorChar);
                    if (codeIndex < 0) {
                        if (colorChar == 'r') {
                            currentColor = color;
                        }
                    } else {
                        currentColor = new Color(colorCodes[codeIndex]);
                    }

                    index += 2;
                }
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();

        GL11.glPopMatrix();
    }

    public float getWidth(final String text) {
        // bad code but idc
        String textWithOutColorCodes = text
                .replace("§0", "")
                .replace("§1", "")
                .replace("§2", "")
                .replace("§3", "")
                .replace("§4", "")
                .replace("§5", "")
                .replace("§6", "")
                .replace("§7", "")
                .replace("§8", "")
                .replace("§9", "")
                .replace("§a", "")
                .replace("§b", "")
                .replace("§c", "")
                .replace("§d", "")
                .replace("§e", "")
                .replace("§f", "")
                .replace("§k", "")
                .replace("§l", "")
                .replace("§m", "")
                .replace("§n", "")
                .replace("§o", "")
                .replace("§r", "");

        if (this.cachedStringWidth.size() > 1000) {
            this.cachedStringWidth.clear();
        }
        return this.cachedStringWidth.computeIfAbsent(text, e -> this.unicodeFont.getWidth(textWithOutColorCodes) / this.antiAliasingFactor);
    }

    public String getPath() {
        return path;
    }

    public float getSize() {
        return size;
    }

    private Font getFontFromInput(String path) throws IOException, FontFormatException {
        return Font.createFont(0, Objects.requireNonNull(FontRenderer.class.getClassLoader().getResourceAsStream(path)));
    }

    public float getHeight(final String s) {
        return this.unicodeFont.getHeight(s) / 2.0f;
    }
}
