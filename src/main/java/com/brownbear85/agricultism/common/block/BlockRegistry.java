package com.brownbear85.agricultism.common.block;

import com.brownbear85.agricultism.Agricultism;
import com.brownbear85.agricultism.common.block.custom.DrumBlock;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Agricultism.MODID);


    public static final RegistryObject<Block> OAK_DRUM = register("oak_drum",
            () -> new DrumBlock(BlockBehaviour.Properties.of(Material.WOOD).dynamicShape().noOcclusion()), ItemRegistry.properties());



    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> sup, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, sup);
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}
