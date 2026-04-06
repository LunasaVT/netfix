/**
 * GNU Lesser General Public License 3.0
 * Copyright (C) Fallen-Breath
 * <p>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * <p>
 * Backported from https://github.com/Fallen-Breath/fast-ip-ping.
 */

package dev.lunasa.netfix.mixin.minecraft;

import dev.lunasa.netfix.netty.InetAddressPatcher;
import net.minecraft.client.network.MultiplayerServerListPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.InetAddress;
import java.net.UnknownHostException;

// used in mc < 1.17
@Mixin(MultiplayerServerListPinger.class)
public abstract class MixinMultiplayerServerListPinger {
    @Redirect(
            method = "add",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/net/InetAddress;getByName(Ljava/lang/String;)Ljava/net/InetAddress;"
            )
    )
    private InetAddress setHostnameToIpAddressToAvoidReversedDnsLookupOnGetHostname_ping(String address) throws UnknownHostException {
        // vanilla
        InetAddress inetAddress = InetAddress.getByName(address);

        // patch it
        inetAddress = InetAddressPatcher.patch(address, inetAddress);

        return inetAddress;
    }
}