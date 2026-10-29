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

import net.runeduniverse.lib.utils.net.DefaultIp4Address;
import net.runeduniverse.lib.utils.net.IpBinaryUtils;
import net.runeduniverse.lib.utils.net.api.Ip4Address;

public class XStreamIp4Address extends DefaultIp4Address {

	private static final long serialVersionUID = 1L;

	public XStreamIp4Address() {
		super(new byte[4]);
	}

	public XStreamIp4Address(final byte byte0, final byte byte1, final byte byte2, final byte byte3) {
		// byte0.byte1.byte2.byte3
		// data[0].data[1].data[2].data[3]
		super(new byte[] { byte0, byte1, byte2, byte3 });
	}

	public XStreamIp4Address(final byte[] data) {
		super(data);
	}

	@Override
	protected Ip4Address _new(final byte[] data) {
		return new XStreamIp4Address(data);
	}

	// XStream2 hook -> handles marshal on save()
	private Object writeReplace() {
		return new Snapshot(toCidrNotation());
	}

	public static XStreamIp4Address convert(final Ip4Address address) {
		if (address == null)
			return null;
		if (address instanceof XStreamIp4Address addr)
			return addr;
		return new XStreamIp4Address(address.getBinaryAddress());
	}

	public static class Snapshot {
		private final String ip;

		protected Snapshot(final String ip) {
			this.ip = ip;
		}

		// XStream2 hook -> handles unmarshal on load()
		private Object readResolve() {
			final byte[] address = IpBinaryUtils.parseIp4Address(this.ip);
			if (address == null)
				return null;
			return new XStreamIp4Address(address);
		}
	}
}
