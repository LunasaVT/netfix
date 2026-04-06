package dev.lunasa.netfix.mixin.minecraft;

import dev.lunasa.netfix.netty.EventLoopGroups;
import io.netty.bootstrap.NetfixBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.network.*;
import net.minecraft.util.Lazy;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.net.InetAddress;
import java.net.SocketAddress;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
    /**
     * @reason Use modern Netty
     * @author Lunasa
     */
    @Overwrite
    public static ClientConnection connect(InetAddress address, int port, boolean shouldUseNativeTransport) {
        shouldUseNativeTransport = useNativeNettyTransport(shouldUseNativeTransport);

        final ClientConnection clientConnection = new ClientConnection(NetworkSide.CLIENTBOUND);
        Class<? extends SocketChannel> channelClass;
        Lazy<? extends EventLoopGroup> eventLoopGroup;

        if (useNativeNettyTransport(Epoll.isAvailable()) && shouldUseNativeTransport) {
            channelClass = EpollSocketChannel.class;
            eventLoopGroup = EventLoopGroups.EPOLL_EVENT_LOOP_GROUP;
        } else {
            channelClass = NioSocketChannel.class;
            eventLoopGroup = EventLoopGroups.NIO_EVENT_LOOP_GROUP;
        }

        NetfixBootstrap bootstrap = new NetfixBootstrap();
        bootstrap.group(eventLoopGroup.get());
        bootstrap.handler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel channel) {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                } catch (ChannelException _) {
                }

                channel.pipeline()
                        .addLast("timeout", new ReadTimeoutHandler(30))
                        .addLast("splitter", new SplitterHandler())
                        .addLast("decoder", new net.minecraft.network.DecoderHandler(NetworkSide.CLIENTBOUND))
                        .addLast("prepender", new SizePrepender())
                        .addLast("encoder", new PacketEncoder(NetworkSide.SERVERBOUND))
                        .addLast("packet_handler", clientConnection);
            }
        });

        bootstrap.channel(channelClass);
        bootstrap.connect(address, port).syncUninterruptibly();
        return clientConnection;
    }

    /**
     * @reason Use modern Netty
     * @author Lunasa
     */
    @Overwrite
    public static ClientConnection connectLocal(SocketAddress address) {
        final ClientConnection clientConnection = new ClientConnection(NetworkSide.CLIENTBOUND);

        NetfixBootstrap bootstrap = new NetfixBootstrap();
        bootstrap.group(EventLoopGroups.LOCAL_EVENT_LOOP_GROUP.get());
        bootstrap.handler(new ChannelInitializer<>() {
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast("packet_handler", clientConnection);
            }
        });

        bootstrap.channel(LocalChannel.class);
        bootstrap.connect(address).syncUninterruptibly();

        return clientConnection;
    }

    @Unique
    private static boolean useNativeNettyTransport(boolean original) {
        return !Util.getOperatingSystem().equals(Util.OperatingSystem.LINUX) && original;
    }
}