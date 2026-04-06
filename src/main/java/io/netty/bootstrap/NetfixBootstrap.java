package io.netty.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class NetfixBootstrap extends AbstractBootstrap<NetfixBootstrap, Channel> {
    private final NetfixBootstrapConfig config = new NetfixBootstrapConfig(this);

    public NetfixBootstrap() {}

    private NetfixBootstrap(NetfixBootstrap bootstrap) {
        super(bootstrap);
    }

    @Override
    public NetfixBootstrap validate() {
        super.validate();
        return this;
    }

    @Override
    public AbstractBootstrapConfig<NetfixBootstrap, Channel> config() {
        return config;
    }

    public ChannelFuture connect(InetAddress inetHost, int inetPort) {
        return connect(new InetSocketAddress(inetHost, inetPort));
    }

    public ChannelFuture connect(String inetHost, int inetPort) {
        return connect(new InetSocketAddress(inetHost, inetPort));
    }

    public ChannelFuture connect(SocketAddress remoteAddress) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        validate();
        return doResolveAndConnect(remoteAddress, config.localAddress());
    }

    private ChannelFuture doResolveAndConnect(SocketAddress remoteAddress, SocketAddress localAddress) {
        ChannelFuture regFuture = initAndRegister();
        final Channel channel = regFuture.channel();

        if (regFuture.isDone()) {
            if (!regFuture.isSuccess()) {
                return regFuture;
            }
        }
        return doConnect(channel, remoteAddress, localAddress, regFuture.channel().newPromise());
    }

    private ChannelFuture doConnect(final Channel channel,
                                    final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        // This method logic is from Bootstrap.
        channel.eventLoop().execute(() -> {
            if (localAddress != null) {
                try {
                    channel.bind(localAddress);
                } catch (Throwable t) {
                    promise.setFailure(t);
                    return;
                }
            }
            channel.connect(remoteAddress, promise).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        });
        return promise;
    }

    @Override
    void init(Channel channel) {
        channel.pipeline().addLast(config.handler());
    }

    @Override
    public NetfixBootstrap clone() {
        return new NetfixBootstrap(this);
    }
}