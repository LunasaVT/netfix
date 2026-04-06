package io.netty.bootstrap;

import io.netty.channel.Channel;

public final class NetfixBootstrapConfig extends AbstractBootstrapConfig<NetfixBootstrap, Channel> {
    NetfixBootstrapConfig(NetfixBootstrap bootstrap) {
        super(bootstrap);
    }
}