package com.combatsasality.scol.blocks;

import com.combatsasality.scol.blocks.generic.BaseItemStackBlock;
import com.combatsasality.scol.registries.ScolTiles;
import com.combatsasality.scol.tileentity.AltarTile;
import com.combatsasality.scol.tileentity.PedestalTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class Altar extends BaseItemStackBlock {
    private static final VoxelShape ALTAR_SHAPE = Stream.of(
            Block.box(4, 10, 4, 12, 11.3, 12),
            Block.box(3, 0, 3, 13, 0.30000000000000004, 13),
            Block.box(6, 0, 6, 10, 10, 10)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    public Altar() {
        super(Properties.of(Material.STONE));
    }
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return ALTAR_SHAPE;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof AltarTile) {
            if (player.isCrouching()) {
                AltarTile altarTile = (AltarTile) tileEntity;
                ItemStack stack = altarTile.getItem();
                if (altarTile.getPedestals().isEmpty() || stack.isEmpty() || !stack.isEnchantable() && !stack.isEnchanted() && !stack.getItem().equals(Items.ENCHANTED_BOOK)) {
                    return ActionResultType.FAIL;
                }
                if (altarTile.getEnchantType() == 0 && stack.getItem().getItem().equals(Items.ENCHANTED_BOOK)) {
                    return ActionResultType.FAIL;
                }
                altarTile.doActivate();
                return ActionResultType.SUCCESS;
            }
        }
        super.use(state, world, pos, player, hand, rayTraceResult);
        return ActionResultType.SUCCESS;
    }


    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ScolTiles.ALTAR.create();
    }

}