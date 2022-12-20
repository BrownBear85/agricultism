package com.brownbear85.agricultism.common.block.entities;

import com.brownbear85.agricultism.common.block.BlockEntityRegistry;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import com.brownbear85.agricultism.common.menu.DrumBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DrumBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    public static final int STORAGE_SIZE = 6;
    public static final int PROCESS_TIME = 300;
    public static final int MAX_OIL = 1000;
    public static final int OIL_PER_ITEM = 10;

    private NonNullList<ItemStack> items = NonNullList.withSize(STORAGE_SIZE, ItemStack.EMPTY);
    private int progress;
    private int oil;

    public ContainerData data;

    public DrumBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DRUM_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> DrumBlockEntity.this.progress;
                    case 1 -> DrumBlockEntity.this.oil;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> DrumBlockEntity.this.progress = pValue;
                    case 1 -> DrumBlockEntity.this.oil = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DrumBlockEntity entity) {
        if (entity.oil < MAX_OIL) {
            Optional<ItemStack> oilMaker = entity.getFirstOilMaker();
            if (oilMaker.isPresent()) {
                entity.progress++;
                if (entity.progress >= PROCESS_TIME) {
                    oilMaker.get().shrink(1);
                    entity.progress = 0;
                    entity.oil += OIL_PER_ITEM;
                }
            } else {
                entity.progress = 0;
            }
        } else {
            entity.progress = 0;
        }
    }

    private Optional<ItemStack> getFirstOilMaker() {
        for (ItemStack stack : items) {
            if (stack.is(ItemRegistry.Tags.MAKES_VEGETABLE_OIL)) {
                return Optional.of(stack);
            }
        }
        return Optional.empty();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.oil_drum");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new DrumBlockMenu(pContainerId, pInventory, this, this.data);
    }

    /* nbt */

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);
        this.progress = pTag.getShort("Progress");
        this.oil = pTag.getShort("StoredOil");
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, this.items);
        pTag.putShort("Progress", (short) this.progress);
        pTag.putShort("StoredOil", (short) this.oil);
    }

    private final LazyOptional<IItemHandlerModifiable> optional = LazyOptional.of(() -> new ItemStackHandler(items));

    @Override
    public <T> LazyOptional<T> getCapability( Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? optional.cast() : super.getCapability(cap, side);
    }

    /* item handling */

    @Override
    public int getContainerSize() {
        return STORAGE_SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return pSlot >= 0 && pSlot < this.items.size() ? this.items.get(pSlot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(items, pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.items, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if (pSlot >= 0 && pSlot < this.items.size()) {
            this.items.set(pSlot, pStack);
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.level.getBlockEntity(this.worldPosition) == this;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return new int[] {0, 1, 2, 3, 4, 5};
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return true;
    }
}
