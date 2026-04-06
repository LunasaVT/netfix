/**
 * MIT License
 * <p>
 * Copyright (c) 2023 JustAlittleWolf
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * Backported from https://github.com/JustAlittleWolf/ServerPingerFixer.
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
	public static InetAddress patch(String hostName, InetAddress addr) throws UnknownHostException
	{
		if (InetAddresses.isInetAddress(hostName))
		{
			InetAddress patched = InetAddress.getByAddress(addr.getHostAddress(), addr.getAddress());
			LOGGER.debug("Patching ip-only InetAddresses from {} to {}", addr, patched);
			addr = patched;
		}
		return addr;
	}
}