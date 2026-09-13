package dev.kpada.vhexpanded.hearts;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.resource.PathResourcePack;
import java.util.Objects;

/** Required built-in data pack: explicit priority over the combined native mod resources. */
public final class HeartRecipePack {
    public static final String ID = "vhexpanded:heart_recipes";
    private HeartRecipePack() {}
    public static void find(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) return;
        var path = ModList.get().getModFileById("vhexpanded").getFile().findResource("vhexpanded", "heart-recipes");
        event.addRepositorySource((consumer, constructor) -> consumer.accept(Objects.requireNonNull(
                Pack.create(ID, true, () -> new PathResourcePack(ID, path), constructor, Pack.Position.TOP, PackSource.BUILT_IN),
                "Cannot load required approved Heart Canisters recipes")));
    }
}
