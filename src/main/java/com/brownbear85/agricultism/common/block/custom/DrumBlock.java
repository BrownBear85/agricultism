package com.brownbear85.agricultism.common.block.custom;

import com.brownbear85.agricultism.common.block.BlockEntityRegistry;
import com.brownbear85.agricultism.common.block.entities.DrumBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class DrumBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape LEG1_X = Shapes.box(0.8125, 0, 0.6875, 0.9375, 0.8125, 0.8125);
    private static final VoxelShape LEG2_X = Shapes.box(0.8125, 0, 0.1875, 0.9375, 0.8125, 0.3125);
    private static final VoxelShape LEG3_X = Shapes.box(0.0625, 0, 0.1875, 0.1875, 0.8125, 0.3125);
    private static final VoxelShape LEG4_X = Shapes.box(0.0625, 0, 0.6875, 0.1875, 0.8125, 0.8125);
    private static final VoxelShape DRUM_X = Shapes.box(0.125, 0.3125, 0.0625, 0.875, 1, 0.9375);
    private static final VoxelShape LEG1_Z = Shapes.box(0.6875, 0, 0.0625, 0.8125, 0.8125, 0.1875);
    private static final VoxelShape LEG2_Z = Shapes.box(0.1875, 0, 0.0625, 0.3125, 0.8125, 0.1875);
    private static final VoxelShape LEG3_Z = Shapes.box(0.1875, 0, 0.8125, 0.3125, 0.8125, 0.9375);
    private static final VoxelShape LEG4_Z = Shapes.box(0.6875, 0, 0.8125, 0.8125, 0.8125, 0.9375);
    private static final VoxelShape DRUM_Z = Shapes.box(0.0625, 0.3125, 0.125, 0.9375, 1, 0.875);
    private static final VoxelShape SHAPE_Z = Shapes.or(DRUM_Z, LEG1_Z, LEG2_Z, LEG3_Z, LEG4_Z);
    private static final VoxelShape SHAPE_X = Shapes.or(DRUM_X, LEG1_X, LEG2_X, LEG3_X, LEG4_X);

    public DrumBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack stack = pPlayer.getItemInHand(pHand);

        if (stack.is(Items.GLASS_BOTTLE)) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof DrumBlockEntity drumBlockEntity) {
                int oil = drumBlockEntity.data.get(1);
                if (oil >= 200) {
                    drumBlockEntity.data.set(1, oil - 200);
                    if (pLevel instanceof ServerLevel serverLevel) {
                        serverLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    }
                    stack.shrink(1);
                    if (!pPlayer.addItem(new ItemStack(Items.ACACIA_BOAT))) {
                        Vec3 pos = pPlayer.getEyePosition();
                        ItemEntity itemEntity = new ItemEntity(pLevel, pos.x(), pos.y(), pos.z(), new ItemStack(Items.ACACIA_BOAT));
                        itemEntity.setDeltaMovement(pPlayer.getLookAngle().multiply(0.2, 0.2, 0.2));
                        pLevel.addFreshEntity(itemEntity);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof DrumBlockEntity drumBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, drumBlockEntity, pPos);
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof DrumBlockEntity drumBlockEntity) {
                Containers.dropContents(pLevel, pPos, drumBlockEntity);
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof DrumBlockEntity drumBlockEntity) {
                drumBlockEntity.setCustomName(pStack.getHoverName());
            }
        }
    }

    /* block entity */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DrumBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, BlockEntityRegistry.DRUM_BLOCK_ENTITY.get(), DrumBlockEntity::serverTick);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    /* blockstates */

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH ? SHAPE_Z : SHAPE_X;
    }
}
