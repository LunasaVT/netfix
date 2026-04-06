package dev.lunasa.netfix.mixin.minecraft;

import dev.lunasa.netfix.netty.EventLoopGroups;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalServerChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerNetworkIo;
import net.minecraft.server.network.IntegratedServerHandshakeNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.SocketAddress;
import java.util.List;

@Mixin(ServerNetworkIo.class)
public class MixinServerNetworkIo {
    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    @Final
    private List<ChannelFuture> channels;

    @Shadow
    @Final
    private List<ClientConnection> connections;

    /**
     * @reason Use correct event loop group
     * @author Lunasa
     */
    @Overwrite
    public SocketAddress bindLocal() {
        ChannelFuture channelFuture;

        synchronized (this.channels) {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(LocalServerChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<>() {
                protected void initChannel(Channel channel) {
                    ClientConnection clientConnection = new ClientConnection(NetworkSide.SERVERBOUND);
                    clientConnection.setPacketListener(new IntegratedServerHandshakeNetworkHandler(MixinServerNetworkIo.this.server, clientConnection));
                    MixinServerNetworkIo.this.connections.add(clientConnection);
                    channel.pipeline().addLast("packet_handler", clientConnection);
                }
            });
            serverBootstrap.group(EventLoopGroups.LOCAL_EVENT_LOOP_GROUP.get());
            serverBootstrap.localAddress(LocalAddress.ANY);
            channelFuture = serverBootstrap.bind().syncUninterruptibly();
            this.channels.add(channelFuture);
        }

        return channelFuture.channel().localAddress();
    }
}