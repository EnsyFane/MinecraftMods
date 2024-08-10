package net.ensyfane.choppingblock.datagen;

import net.ensyfane.choppingblock.ChoppingBlockMod;
import net.ensyfane.choppingblock.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ChoppingBlockMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        simpleBlockWithItem(
            ModBlocks.ChoppingBlock.get(),
            new ModelFile.ExistingModelFile(
                ResourceLocation.tryParse(ChoppingBlockMod.MOD_ID+":block/" + ModBlocks.ChoppingBlock.get().asItem()),
                this.models().existingFileHelper
            )
        );
    }
}
