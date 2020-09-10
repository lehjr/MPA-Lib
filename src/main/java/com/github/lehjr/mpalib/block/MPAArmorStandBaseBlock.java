package com.github.lehjr.mpalib.block;

import com.github.lehjr.mpalib.client.sound.SoundDictionary;
import com.github.lehjr.mpalib.container.MPALibContainerProvier;
import com.github.lehjr.mpalib.entity.MPAArmorStandEntity;
import com.github.lehjr.mpalib.tileentity.MPAArmorStandBaseTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Base of the armor workstation.
 */
public class MPAArmorStandBaseBlock extends Block implements IWaterLoggable {
    private static final ITextComponent title = new TranslationTextComponent("container.crafting", new Object[0]);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(
            0.0D, // West
            0.0D, // down?
            0.0D, // north
            16.0D, // east
            1.0D, // up?
            16.0D); // South

    public MPAArmorStandBaseBlock() {
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(0.5F, 4.0F)
                .sound(SoundType.ANVIL)
                .harvestLevel(0)
//                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool());
        setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.WATERLOGGED, false));
    }


//    @Override
//    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//        player.playSound(SoundDictionary.SOUND_EVENT_GUI_SELECT, 1.0F, 1.0F);
//
//        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
//
//        // todo : open gui code
//    }



    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        player.playSound(SoundDictionary.SOUND_EVENT_GUI_SELECT, 1.0F, 1.0F);

//        if(!worldIn.isRemote) {
//            NetworkHooks.openGui((ServerPlayerEntity) player,
//                    new TinkerContainerProvider(0), (buffer) -> buffer.writeInt(0));
//        }

        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            player.openContainer(state.getContainer(worldIn, pos));
//            player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return ActionResultType.SUCCESS;
        }

//        if (worldIn.isRemote()) {
////        Musique.playClientSound(, 1);
//            Minecraft.getInstance().enqueue(() -> Minecraft.getInstance().displayGuiScreen(new TestGui(new TranslationTextComponent("gui.tinkertable"))));
////}
//        }
//        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Nullable
    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return new MPALibContainerProvier(0);


//        return new SimpleNamedContainerProvider((windowID, playerInventory, playerEntity) -> {
//            return new WorkbenchContainer(windowID, playerInventory, IWorldPosCallable.of(worldIn, pos));
//        }, title);
    }



    // temporary fix for armor stand spawned below the block
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof MPAArmorStandEntity && pos.getY() > (int)entityIn.getPositionVec().y) {
            entityIn.setPositionAndUpdate(pos.getX() + 0.5, entityIn.getPositionVec().y + 1, pos.getZ() + 0.5);
        }
        super.onEntityCollision(state, worldIn, pos, entityIn);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 1;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BASE_SHAPE;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MPAArmorStandBaseTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState()
                .with(WATERLOGGED, Boolean.valueOf(ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8));
    }

    @Override
    public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state) {
        return Fluids.EMPTY;
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return IWaterLoggable.super.receiveFluid(worldIn, pos, state, fluidStateIn);
    }
}