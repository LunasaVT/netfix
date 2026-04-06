package dev.lunasa.netfix.mixin.minecraft;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PacketByteBuf.class)
public abstract class MixinPacketByteBuff extends ByteBuf {
    @Shadow
    public abstract int readVarInt();

    /**
     * @reason Remove unsafe {@link ByteBuf#array()}
     * @author Lunasa
     */
    @Overwrite
    public String readString(int maxLength) {
        int length = this.readVarInt();

        if (length > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")");
        } else if (length < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            byte[] bytes = new byte[length];
            this.readBytes(bytes);
            String string = new String(bytes, Charsets.UTF_8);

            if (string.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + length + " > " + maxLength + ")");
            } else {
                return string;
            }
        }
    }
}