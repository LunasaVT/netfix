package dev.lunasa.netfix.netty;

import io.netty.channel.IoHandlerFactory;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.epoll.EpollIoHandler;
import io.netty.channel.local.LocalIoHandler;
import io.netty.channel.nio.NioIoHandler;
import net.minecraft.util.Lazy;

public class EventLoopGroups {
    private static final Lazy<IoHandlerFactory> EPOLL_IO_HANDLER = LazyValues.of(EpollIoHandler::newFactory);
    private static final Lazy<IoHandlerFactory> NIO_IO_HANDLER = LazyValues.of(NioIoHandler::newFactory);
    private static final Lazy<IoHandlerFactory> LOCAL_IO_HANDLER = LazyValues.of(LocalIoHandler::newFactory);

    public static final Lazy<MultiThreadIoEventLoopGroup> EPOLL_EVENT_LOOP_GROUP = LazyValues.of(() ->
            new MultiThreadIoEventLoopGroup(
                    EPOLL_IO_HANDLER.get()
            )
    );
    public static final Lazy<MultiThreadIoEventLoopGroup> NIO_EVENT_LOOP_GROUP = LazyValues.of(() ->
            new MultiThreadIoEventLoopGroup(
                    NIO_IO_HANDLER.get()
            )
    );
    public static final Lazy<MultiThreadIoEventLoopGroup> LOCAL_EVENT_LOOP_GROUP = LazyValues.of(() ->
            new MultiThreadIoEventLoopGroup(
                    LOCAL_IO_HANDLER.get()
            )
    );
}
