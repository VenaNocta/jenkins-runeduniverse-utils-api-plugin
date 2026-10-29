/*
 * Copyright © 2026 VenaNocta (venanocta@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jenkins.runeduniverse.utils.net;

import net.runeduniverse.lib.utils.net.DefaultIpNet4Address;
import net.runeduniverse.lib.utils.net.api.Ip4Address;
import net.runeduniverse.lib.utils.net.api.IpNet4Address;

import static net.runeduniverse.lib.utils.net.IpNetworkUtils.tryParseIpNet4Address;

public class XStreamIpNet4Address extends DefaultIpNet4Address {

	private static final long serialVersionUID = 1L;

	public XStreamIpNet4Address(final Ip4Address address) {
		this(address, (short) 32);
	}

	public XStreamIpNet4Address(final Ip4Address address, final short mask) {
		super(address, mask);
	}

	@Override
	protected IpNet4Address _new(final Ip4Address address, final short mask) {
		return new XStreamIpNet4Address(address, mask);
	}

	@Override
	protected Ip4Address _newIp(final byte[] data) {
		return new XStreamIp4Address(data);
	}

	// XStream2 hook -> handles marshal on save()
	private Object writeReplace() {
		return new Snapshot(toCidrNotation());
	}

	public static XStreamIpNet4Address convert(final IpNet4Address address) {
		if (address == null)
			return null;
		if (address instanceof XStreamIpNet4Address addr)
			return addr;
		return new XStreamIpNet4Address(address.getBaseAddress(), address.getMask());
	}

	public static class Snapshot {
		private final String subnet;

		protected Snapshot(final String subnet) {
			this.subnet = subnet;
		}

		// XStream2 hook -> handles unmarshal on load()
		private Object readResolve() {
			final IpNet4Address address = tryParseIpNet4Address(this.subnet);
			if (address == null)
				return null;
			return new XStreamIpNet4Address(address.getBaseAddress(), address.getMask());
		}
	}
}
