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

package dev.lunasa.netfix.netty;

import com.google.common.net.InetAddresses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressPatcher {
    private static final Logger LOGGER = LogManager.getLogger("netfix/patcher");

    @SuppressWarnings("UnstableApiUsage")
    public static InetAddress patch(String hostName, InetAddress addr) throws UnknownHostException {
        if (InetAddresses.isInetAddress(hostName)) {
            InetAddress patched = InetAddress.getByAddress(addr.getHostAddress(), addr.getAddress());
            LOGGER.debug("Patching ip-only InetAddresses from {} to {}", addr, patched);
            addr = patched;
        }
        return addr;
    }
}