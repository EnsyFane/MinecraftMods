package net.ensyfane.choppingblock.datagen;

import net.ensyfane.choppingblock.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.ChoppingBlock.get(), 1)
                .requires(ItemTags.LOGS)
                .unlockedBy(ItemTags.LOGS.toString(), has(ItemTags.LOGS))
                .save(pWriter);
    }
}
