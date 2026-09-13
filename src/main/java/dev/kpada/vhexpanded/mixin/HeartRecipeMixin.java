package dev.kpada.vhexpanded.mixin;

import dev.kpada.vhexpanded.hearts.HeartResearch;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

/** Gate the recipe identity, including god_apple's vanilla output, before producing a result. */
@Mixin(ResultContainer.class)
public abstract class HeartRecipeMixin implements RecipeHolder {
    @Override
    public boolean setRecipeUsed(Level level, ServerPlayer player, Recipe<?> recipe) {
        if ("bhc".equals(recipe.getId().getNamespace()) && !HeartResearch.unlocked(player)) return false;
        return RecipeHolder.super.setRecipeUsed(level, player, recipe);
    }
}
