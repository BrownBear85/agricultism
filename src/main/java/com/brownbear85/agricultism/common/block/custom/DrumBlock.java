package com.brownbear85.agricultism.common.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DrumBlock extends Block {
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



    /* blockstates */

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH ? SHAPE_Z : SHAPE_X;
    }
}
