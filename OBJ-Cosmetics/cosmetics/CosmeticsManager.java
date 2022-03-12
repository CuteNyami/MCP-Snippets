package client.cosmetics;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CosmeticsManager {

    public static List<CosmeticsHandler> cosmetics = new ArrayList<>();

    public static void registerCosmetic(CosmeticsHandler... handlers) {
        cosmetics.addAll(Arrays.asList(handlers));
    }

    public static ResourceLocation getWingTexture() {
        return null;
    }
}
