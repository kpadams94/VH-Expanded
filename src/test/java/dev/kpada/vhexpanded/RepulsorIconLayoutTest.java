package dev.kpada.vhexpanded;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

/** Native specialization rendering subtracts eight, then blits at the texture's own size. */
class RepulsorIconLayoutTest {
    @Test void iconFitsThePinnedSpecializationAnchor() throws Exception {
        try (var vault = new JarFile(System.getProperty("vaultJar"))) {
            var node = new ClassNode();
            try (var stream = vault.getInputStream(vault.getJarEntry(
                    "iskallia/vault/client/gui/screen/player/legacy/widget/AbilityWidgetSelectable.class"))) {
                new ClassReader(stream).accept(node, 0);
            }
            var render = node.methods.stream().filter(m -> m.desc.equals("(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V")).findFirst().orElseThrow();
            int fixedOffsets = 0;
            boolean nativeBlit = false;
            for (var instruction : render.instructions) {
                if (instruction instanceof LdcInsnNode constant && Double.valueOf(-8).equals(constant.cst)) fixedOffsets++;
                if (instruction instanceof MethodInsnNode call && call.owner.equals("iskallia/vault/client/atlas/TextureAtlasRegion")
                        && call.name.equals("blit")) nativeBlit = true;
            }
            assertTrue(fixedOffsets >= 2 && nativeBlit, "Pinned menu uses the fixed eight-pixel icon anchor");
            try (var stream = vault.getInputStream(vault.getJarEntry("assets/the_vault/textures/gui/abilities/mana_barrier.png"))) {
                var nativeIcon = ImageIO.read(stream);
                var ours = ImageIO.read(Path.of("src/main/resources/assets/the_vault/textures/gui/abilities/vhexpanded_repulsor.png").toFile());
                assertEquals(16, nativeIcon.getWidth());
                assertEquals(nativeIcon.getWidth(), ours.getWidth(), "Icon width must fit the fixed native anchor; 32 shifts the center eight pixels right");
                assertEquals(nativeIcon.getHeight(), ours.getHeight(), "Icon height must fit the fixed native anchor; 32 shifts the center eight pixels down");
                assertEquals(0, ours.getRGB(0, 0) >>> 24, "Transparent icon padding");
            }
        }
    }
}
