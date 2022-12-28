package com.brownbear85.agricultism.common.block.entities;

import com.brownbear85.agricultism.Agricultism;
import com.brownbear85.agricultism.common.block.BlockRegistry;
import com.brownbear85.agricultism.common.block.entities.DrumBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Agricultism.MODID);


    public static final RegistryObject<BlockEntityType<DrumBlockEntity>> DRUM_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("drum",
            () -> BlockEntityType.Builder.of(DrumBlockEntity::new,
                            BlockRegistry.OAK_DRUM.get())
                    .build(null));
}
