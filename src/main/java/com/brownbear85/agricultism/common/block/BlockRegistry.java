package com.brownbear85.agricultism.common.block;

import com.brownbear85.agricultism.Agricultism;
import com.brownbear85.agricultism.common.block.custom.DrumBlock;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Agricultism.MODID);


    public static final RegistryObject<Block> OAK_DRUM = register("oak_drum",
            () -> new DrumBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).dynamicShape().noOcclusion().strength(3.0f, 2.0f)), ItemRegistry.properties());

    public static final RegistryObject<Block> OAK_CARPET = register("oak_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F).sound(SoundType.WOOD)), ItemRegistry.properties());
    public static final RegistryObject<Block> SPRUCE_CARPET = register("spruce_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.copy(OAK_CARPET.get())), ItemRegistry.properties());
    public static final RegistryObject<Block> BIRCH_CARPET = register("birch_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.copy(OAK_CARPET.get())), ItemRegistry.properties());
    public static final RegistryObject<Block> JUNGLE_CARPET = register("jungle_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.copy(OAK_CARPET.get())), ItemRegistry.properties());
    public static final RegistryObject<Block> ACACIA_CARPET = register("acacia_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.copy(OAK_CARPET.get())), ItemRegistry.properties());
    public static final RegistryObject<Block> DARK_OAK_CARPET = register("dark_oak_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.copy(OAK_CARPET.get())), ItemRegistry.properties());
    public static final RegistryObject<Block> MANGROVE_CARPET = register("mangrove_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.copy(OAK_CARPET.get())), ItemRegistry.properties());
    public static final RegistryObject<Block> CRIMSON_CARPET = register("crimson_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F).sound(SoundType.WOOD)), ItemRegistry.properties());
    public static final RegistryObject<Block> WARPED_CARPET = register("warped_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.copy(CRIMSON_CARPET.get())), ItemRegistry.properties());

    public static final RegistryObject<Block> EMPTY_CRATE = register("empty_crate", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(0.7F).sound(SoundType.WOOD).noOcclusion()), ItemRegistry.properties());

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> sup, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, sup);
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}
