package net.ensyfane.choppingblock.block.custom;

import net.ensyfane.choppingblock.entity.ChoppingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ChoppingBlock extends Block implements EntityBlock {
    public static final VoxelShape SHAPE = makeShape();
    private static final Map<Item, Block> logsToPlanksMap = new HashMap<>();

    public ChoppingBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
    {
        return SHAPE;
    }

    private static VoxelShape makeShape()
    {
        var shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.125, 0.0625, 0.9375, 0.1875, 0.9375), BooleanOp.OR);

        return shape;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide())
        {
            return InteractionResult.SUCCESS;
        }

        var be = pLevel.getBlockEntity(pPos);
        if (!(be instanceof ChoppingBlockEntity blockEntity))
        {
            return InteractionResult.SUCCESS;
        }
        var usedItem = pPlayer.getItemInHand(pHand);

        if (usedItem.is(ItemTags.LOGS))
        {
            var inventory = blockEntity.getInventory();
            if (!inventory.getStackInSlot(0).isEmpty())
            {
                return InteractionResult.SUCCESS;
            }

            var toInsert = usedItem.copy();
            toInsert.setCount(1);
            var leftover = inventory.insertItem(0, toInsert, false);

            ItemStack remainder = usedItem.copy();
            remainder.setCount(remainder.getCount() - 1);
            remainder.grow(leftover.getCount());
            pPlayer.setItemInHand(pHand, remainder);

            return InteractionResult.CONSUME_PARTIAL;
        }

        if (usedItem.is(ItemTags.AXES))
        {
            var inventory = blockEntity.getInventory();
            if (inventory.getStackInSlot(0).isEmpty())
            {
                return InteractionResult.SUCCESS;
            }

            var planksBlock = GetPlanksForLog(blockEntity.getItem());
            var extracted = inventory.extractItem(0, inventory.getSlotLimit(0), false);
            extracted.setCount(0);

            Block.dropResources(planksBlock.defaultBlockState(), pLevel, pPos.above());
            if (usedItem.isDamageableItem())
            {
                usedItem.setDamageValue(usedItem.getDamageValue() + 1);
            }
            return InteractionResult.CONSUME_PARTIAL;
        }

        return InteractionResult.SUCCESS;
    }

    private static Block GetPlanksForLog(Item logItem)
    {
        var existingPlanks = logsToPlanksMap.get(logItem);
        if (existingPlanks != null)
        {
            return existingPlanks;
        }

        var location = ForgeRegistries.ITEMS.getKey(logItem);
        var planksLocation = location.withPath(location.getPath()
                .replace("stripped_", "")
                .replace("_log", "_planks"));
        var planksBlock = Block.byItem(ForgeRegistries.ITEMS.getValue(planksLocation));
        if (planksBlock == Blocks.AIR)
        {
            planksBlock = Blocks.OAK_PLANKS;
        }

        logsToPlanksMap.putIfAbsent(logItem, planksBlock);

        return planksBlock;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new ChoppingBlockEntity(pPos, pState);
    }
}
