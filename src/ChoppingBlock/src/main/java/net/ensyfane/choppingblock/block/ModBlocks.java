package net.ensyfane.choppingblock.block;

import net.ensyfane.choppingblock.ChoppingBlockMod;
import net.ensyfane.choppingblock.block.custom.ChoppingBlock;
import net.ensyfane.choppingblock.entity.ChoppingBlockEntity;
import net.ensyfane.choppingblock.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ChoppingBlockMod.MOD_ID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChoppingBlockMod.MOD_ID);

    public static final RegistryObject<Block> ChoppingBlock = registerBlock("chopping_block",
            () -> new ChoppingBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB).dynamicShape().noOcclusion()));

    public static final RegistryObject<BlockEntityType<ChoppingBlockEntity>> ChoppingBlockEntity = registerBlockEntity(
            "chopping_block",
            () -> BlockEntityType.Builder.of(ChoppingBlockEntity::new, ChoppingBlock.get()).build(null));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends BlockEntityType<?>>RegistryObject<T> registerBlockEntity(String name, Supplier<T> block)
    {
        return BLOCK_ENTITIES.register(name, block);
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block)
    {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
