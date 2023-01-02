package com.brownbear85.agricultism.common.block.custom;

import com.brownbear85.agricultism.Util;
import com.brownbear85.agricultism.common.block.entities.SprinklerBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SprinklerBlock extends BaseEntityBlock {

    public static final BooleanProperty VALID = BooleanProperty.create("valid");

    private static final VoxelShape BOX = Shapes.box(0.125, 0, 0.125, 0.875, 0.625, 0.875);
    private final int range;

    public SprinklerBlock(int range, Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(VALID, false));
        this.range = range;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BOX;
    }

    /* block entity */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SprinklerBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    /* blockstate */

    private static boolean isValid(LevelAccessor level, BlockPos pos) {
        return level.getFluidState(pos.below()).isSourceOfType(Fluids.WATER);
    }

    private static void updateValid(LevelAccessor pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof SprinklerBlockEntity sprinklerBlockEntity) {
            sprinklerBlockEntity.setValid(isValid(pLevel, pPos));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(VALID);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(VALID, isValid(pContext.getLevel(), pContext.getClickedPos()));
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        updateValid(pLevel, pPos);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        updateValid(pLevel, pCurrentPos);
        return pState.setValue(VALID, isValid(pLevel, pCurrentPos));
    }

    /* other */

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(VALID)) {
            double x = pPos.getX() + 0.5;
            double y = pPos.getY();
            double z = pPos.getZ() + 0.5;

            if (pRandom.nextDouble() < 0.8D) {
                pLevel.playLocalSound(x, y, z, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.1F, 1.4F, false);
            }

            for (Direction dir : Util.CARDINAL_DIRECTIONS) {
                for (int i = 0; i < 3; i++) {
                    pLevel.addParticle(ParticleTypes.SPLASH, x + dir.getStepX() * 0.38, y + 0.5, z + dir.getStepZ() * 0.38, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        int square = 1 + range * 2;
        pTooltip.add(
                Component.translatable("block.agricultism.sprinkler.range",
                Component.literal(square + "x" + square).withStyle(ChatFormatting.GRAY)
                ).withStyle(ChatFormatting.DARK_GRAY));
    }

    public int getRange() {
        return range;
    }
}
