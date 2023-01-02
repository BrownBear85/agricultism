package com.brownbear85.agricultism.common.jei;

import com.brownbear85.agricultism.Agricultism;
import mezz.jei.api.IModPlugin;
import net.minecraft.resources.ResourceLocation;

public class AgricultismPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Agricultism.MODID, "plugin");
    }
}
