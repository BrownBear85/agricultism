package com.brownbear85.agricultism.common.block.entities;

import com.brownbear85.agricultism.Util;
import com.brownbear85.agricultism.common.block.BlockRegistry;
import com.brownbear85.agricultism.common.block.custom.VegetableOilCauldronBlock;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class VegetableOilCauldronBlockEntity extends BlockEntity {

    private int item = 0; // 0 = no item, 1 = cut hide, 2 = preserved hide
    private int progress = 0;
    private int rotation = 0;
    private boolean spinning = false;
    private boolean onCooldown = false;
    private int cooldown = 0;

    public VegetableOilCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.VEGETABLE_OIL_CAULDRON_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof VegetableOilCauldronBlockEntity entity) {
            if (entity.onCooldown) {
                entity.cooldown--;
                entity.rotation += 9;
                if (entity.cooldown <= 0) {
                    entity.onCooldown = false;
                    entity.spinning = false;
                    entity.rotation = 0;
                }
            }
            if (entity.spinning) {
                if (entity.rotation < 351) {
                    entity.rotation += 9;
                } else {
                    entity.rotation = 0;
                    entity.spinning = false;
                    entity.progress++;
                }
            }
            if (entity.progress >= 4) {
                entity.progress = 0;
                entity.cooldown = 40;
                entity.onCooldown = true;
                entity.setItem(2);
                level.playSound(null, pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    private void clearProgress() {
        progress = 0;
        rotation = 0;
        cooldown = 0;
        spinning = false;
        onCooldown = false;
    }

    public int getCauldronLevel() {
        if (level.getBlockState(worldPosition).is(BlockRegistry.VEGETABLE_OIL_CAULDRON.get())) {
            return level.getBlockState(worldPosition).getValue(VegetableOilCauldronBlock.LEVEL);
        } else {
            return 0;
        }
    }

    public double getItemHeight(float partialTick) {
        return 0.5F + 0.1875 * getCauldronLevel() // height of oil in cauldron
                + (onCooldown ? -(cooldown - partialTick) * 0.009F : // float back up to surface while on cooldown
                -(progress * 360 + getRotation(partialTick)) / 4000F); // sink down while spinning
    }

    public float getRotation(float partialTick) {
        if (spinning || onCooldown) {
            return (rotation + partialTick * 9) % 360;
        } else {
            return rotation % 360;
        }
    }

    public boolean isSpinning() {
        return spinning;
    }

    public boolean canStir() {
        return !spinning && !onCooldown && item == 1;
    }

    public boolean canTakeItem() {
        return !spinning && !onCooldown;
    }

    public void stir() {
        spinning = true;
    }

    /* item handling */

    public ItemStack getRenderStack() {
        return switch (item) {
            case 1 -> Util.item(ItemRegistry.CUT_ANIMAL_HIDE.get());
            case 2 -> Util.item(ItemRegistry.PRESERVED_ANIMAL_HIDE.get());
            default -> ItemStack.EMPTY;
        };
    }

    public boolean shouldRender() {
        return item == 1 || item == 2;
    }

    public void setItem(int item) {
        this.item = item;
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
        markUpdated();
        if (item != 2) {
            clearProgress();
        }
    }

    public void drop() {
        ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, getRenderStack());
        level.addFreshEntity(itemEntity);
    }

    /* nbt */

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        item = pTag.getInt("ItemState");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("ItemState", item);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("ItemState", item);
        return tag;
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
