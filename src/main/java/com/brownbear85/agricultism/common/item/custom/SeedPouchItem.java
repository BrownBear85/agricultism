package com.brownbear85.agricultism.common.item.custom;

import com.brownbear85.agricultism.common.item.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SeedPouchItem extends Item {

    private final int storageSize;
    private final int stackSize;
    private static final int FULLNESS_COLOR = 3272990;

    public SeedPouchItem(Item.Properties properties, int storage, int stack) {
        super(properties);
        this.storageSize = storage;
        this.stackSize = stack;
    }

    public static ItemStack filledAdvancedSeedPouch() {
        ItemStack stack = new ItemStack(ItemRegistry.ADVANCED_SEED_POUCH.get());
        ListTag list = new ListTag();
        list.add(SeedPouchItem.of(Items.WHEAT_SEEDS, 512));
        list.add(SeedPouchItem.of(ItemRegistry.POTATO_SEEDS.get(), 512));
        list.add(SeedPouchItem.of(ItemRegistry.CARROT_SEEDS.get(), 512));
        list.add(SeedPouchItem.of(Items.BEETROOT_SEEDS, 512));
        list.add(SeedPouchItem.of(Items.PUMPKIN_SEEDS, 512));
        list.add(SeedPouchItem.of(Items.MELON_SEEDS, 512));
        stack.getOrCreateTag().put("Items", list);
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (getNumItems(stack) == 0) {
            return InteractionResultHolder.fail(stack);
        }
        ListTag list = getOrCreateItems(stack);
        if (list.get(list.size() - 1) instanceof CompoundTag compoundTag) {
            playRemoveSound(player);
            Vec3 pos = player.getEyePosition();
            ItemEntity entity = new ItemEntity(level, pos.x(), pos.y(), pos.z(), new ItemStack(itemFromId(compoundTag.getString("id")), Math.min(64, compoundTag.getInt("Count"))));
            entity.setDeltaMovement(player.getLookAngle().multiply(0.2, 0.2, 0.2));
            entity.setPickUpDelay(10);
            level.addFreshEntity(entity);
            compoundTag.putInt("Count", compoundTag.getInt("Count") - 64);
            if (compoundTag.getInt("Count") <= 0) {
                list.remove(list.size() - 1);
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack slotItem = slot.getItem();
            if (slotItem.isEmpty()) {
                removeStack(stack).ifPresent((item) -> {
                    playRemoveSound(player);
                    addStack(stack, slot.safeInsert(item));
                });
            } else if (canGoInSeedPouch(slotItem)) {
                int added = addStack(stack, slot.safeTake(slotItem.getCount(), getCapacityOfStack(stack, slotItem), player));
                if (added > 0) {
                    playAddSound(player);
                }
            }
            return true;
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack addStack, Slot slot, ClickAction action, Player player, SlotAccess slotAccess) {
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (addStack.isEmpty()) {
                removeStack(stack).ifPresent((item) -> {
                    slotAccess.set(item);
                    playRemoveSound(player);
                });
            } else {
                int added = addStack(stack, addStack);
                if (added > 0) {
                    playAddSound(player);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        ItemUtils.onContainerDestroyed(entity, getContents(entity.getItem()).stream());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) ((getNumItems(stack) / (float) (stackSize * storageSize)) * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FULLNESS_COLOR;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getOrCreateItems(stack).size() > 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ListTag list = getOrCreateItems(stack);
        if (list.size() == 0) {
            tooltip.add(Component.translatable("item.agricultism.seed_pouch.empty").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        } else {
            for (Tag tag : list) {
                if (tag instanceof CompoundTag compoundTag && tagIsItem(compoundTag)) {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(compoundTag.getString("id")));
                    MutableComponent mutablecomponent = item.getDefaultInstance().getHoverName().copy();
                    mutablecomponent.append(" x").append(String.valueOf(compoundTag.getInt("Count"))).withStyle(ChatFormatting.GRAY);
                    tooltip.add(mutablecomponent);
                }
            }
        }
    }

    public static CompoundTag of(Item item, int count) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("Count", count);
        compoundTag.putString("id", String.valueOf(ForgeRegistries.ITEMS.getKey(item)));
        return compoundTag;
    }

    private int addStack(ItemStack stack, ItemStack addItem) {
        if (canGoInSeedPouch(addItem)) {
            ListTag list = getOrCreateItems(stack);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag compoundTag = list.getCompound(i);
                if (tagIsItem(compoundTag)) {
                    if (addItem.is(itemFromId(compoundTag.getString("id")))) {
                        int originalCount = compoundTag.getInt("Count");
                        int addedAmount = Math.min(originalCount + addItem.getCount(), stackSize) - originalCount;
                        addItem.shrink(addedAmount);
                        compoundTag.putInt("Count", originalCount + addedAmount);
                        return addedAmount;
                    }
                }
            }
            if (list.size() < storageSize) {
                int addedAmount = Math.min(addItem.getCount(), stackSize);
                CompoundTag compoundTag = new CompoundTag();
                list.add(compoundTag);
                compoundTag.putString("id", String.valueOf(ForgeRegistries.ITEMS.getKey(addItem.getItem())));
                compoundTag.putInt("Count", addedAmount);
                addItem.shrink(addedAmount);
                return addedAmount;
            }
        }
        return 0;
    }

    private static Optional<ItemStack> removeStack(ItemStack stack) {
        ListTag list = getOrCreateItems(stack);
        CompoundTag compoundTag = list.getCompound(list.size() - 1);
        if (tagIsItem(compoundTag)) {
            int originalCount = compoundTag.getInt("Count");
            int removedAmount = originalCount - Math.max(0, originalCount - 64);
            compoundTag.putInt("Count", originalCount - removedAmount);
            if (compoundTag.getInt("Count") <= 0) {
                list.remove(compoundTag);
            }
            return Optional.of(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(compoundTag.getString("id"))), removedAmount));
        } else {
            return Optional.empty();
        }
    }

    public static NonNullList<ItemStack> getContents(ItemStack stack) {
        ListTag listTag = getOrCreateItems(stack);
        NonNullList<ItemStack> list = NonNullList.createWithCapacity(listTag.size());
        listTag.forEach((tag) -> {
            if (tag instanceof CompoundTag compoundTag && tagIsItem(compoundTag)) {
                list.add(new ItemStack(itemFromId(compoundTag.getString("id")), compoundTag.getInt("Count")));
            }
        });
        return list;
    }

    public static int getNumItems(ItemStack stack) {
        return getContents(stack).stream().mapToInt(ItemStack::getCount).sum();
    }

    private int getCapacityOfStack(ItemStack stack, ItemStack checkStack) {
        if (!canGoInSeedPouch(checkStack)) {
            return 0;
        }
        NonNullList<ItemStack> list = getContents(stack);
        for (ItemStack item : list) {
            if (ItemStack.isSame(item, checkStack)) {
                return stackSize - item.getCount();
            }
        }
        return list.size() < storageSize ? stackSize : 0;
    }

    private static boolean canGoInSeedPouch(ItemStack stack) {
        return stack.is(ItemRegistry.Tags.SEEDS) && !stack.hasTag();
    }

    private static boolean tagIsItem(CompoundTag compoundTag) {
        return compoundTag.contains("Count") && compoundTag.contains("id");
    }

    private static Item itemFromId(String id) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
    }

    private static ListTag getOrCreateItems(ItemStack stack) {
        ListTag list = stack.getOrCreateTag().getList("Items", 10);
        stack.getTag().put("Items", list);
        return list;
    }

    private void playRemoveSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    private void playAddSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }
}
