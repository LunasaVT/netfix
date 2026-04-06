package dev.lunasa.netfix.netty;

import net.minecraft.util.Lazy;

public class LazyValues {
    @FunctionalInterface
    public interface LazyValueCreator<T> {
        T create();
    }

    public static <T> Lazy<T> of(LazyValueCreator<T> creator) {
        return new Lazy<>() {
            @Override
            public T create() {
                return creator.create();
            }
        };
    }
}
