package com.kaboomroads.lostfeatures.utils;

import com.kaboomroads.lostfeatures.Constants;
import net.minecraft.resources.ResourceLocation;

public enum ClothColor {
    RED(tl("red")),
    WHITE(tl("white")),
    ORANGE(tl("orange")),
    MAGENTA(tl("magenta")),
    LIGHT_BLUE(tl("light_blue")),
    YELLOW(tl("yellow")),
    LIME(tl("lime")),
    PINK(tl("pink")),
    GRAY(tl("gray")),
    LIGHT_GRAY(tl("light_gray")),
    CYAN(tl("cyan")),
    PURPLE(tl("purple")),
    BLUE(tl("blue")),
    BROWN(tl("brown")),
    GREEN(tl("green")),
    BLACK(tl("black"));

    private final ResourceLocation textureLocation;

    ClothColor(ResourceLocation textureLocation) {
        this.textureLocation = textureLocation;
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    private static ResourceLocation tl(String name) {
        return new ResourceLocation(Constants.MOD_ID, "textures/entity/tuff_golem/tuff_golem_" + name + ".png");
    }
}
