package dev.lunasa.netfix.mixin.patchy;

import com.mojang.patchy.BlockingICFB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockingICFB.class)
public class MixinBlockingICFB {
    /**
     * @reason Remove installation of DNS handler, which would otherwise block domain resolution
     * @author Lunasa
     */
    @Overwrite(remap = false)
    public static void install() {
        // No-op
    }
}