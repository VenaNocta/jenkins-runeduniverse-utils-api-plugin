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

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.runeduniverse.lib.utils.net.IpBinaryUtils;
import net.runeduniverse.lib.utils.net.IpNetworkUtils.TypeFilter;
import net.runeduniverse.lib.utils.net.api.IpAddress;
import net.runeduniverse.lib.utils.net.api.Ip4Address;
import net.runeduniverse.lib.utils.net.api.IpNet4Address;
import net.runeduniverse.lib.utils.net.api.Ip6Address;
import net.runeduniverse.lib.utils.net.api.IpNet6Address;
import net.runeduniverse.lib.utils.net.api.IpNetAddress;

import static net.runeduniverse.lib.utils.net.IpNetworkUtils._interpolateIpRange;
import static net.runeduniverse.lib.utils.net.IpNetworkUtils._extractIpRanges;
import static net.runeduniverse.lib.utils.net.IpNetworkUtils._forEach;

public class IpNetworkUtils {

	private IpNetworkUtils() {
	}

	public static Ip4Address createIp4Address() {
		return new XStreamIp4Address();
	}

	public static Ip4Address createIp4Address(final byte[] address) {
		return XStreamIp4Address.convert(net.runeduniverse.lib.utils.net.IpNetworkUtils.createIp4Address(address));
	}

	public static IpNet4Address createIpNet4Address() {
		return new XStreamIpNet4Address(createIp4Address(), (short) 32);
	}

	public static IpNet4Address createIpNet4Address(final byte[] address, final short mask) {
		return createIpNet4Address(createIp4Address(address), mask);
	}

	public static IpNet4Address createIpNet4Address(final Ip4Address address, final short mask) {
		return XStreamIpNet4Address
				.convert(net.runeduniverse.lib.utils.net.IpNetworkUtils.createIpNet4Address(address, mask));
	}

	public static Ip6Address createIp6Address() {
		return new XStreamIp6Address();
	}

	public static Ip6Address createIp6Address(final byte[] address) {
		return XStreamIp6Address.convert(net.runeduniverse.lib.utils.net.IpNetworkUtils.createIp6Address(address));
	}

	public static IpNet6Address createIpNet6Address() {
		return new XStreamIpNet6Address(createIp6Address(), (short) 128);
	}

	public static IpNet6Address createIpNet6Address(final byte[] address, final short mask) {
		return createIpNet6Address(createIp6Address(address), mask);
	}

	public static IpNet6Address createIpNet6Address(final Ip6Address address, final short mask) {
		return XStreamIpNet6Address
				.convert(net.runeduniverse.lib.utils.net.IpNetworkUtils.createIpNet6Address(address, mask));
	}

	public static IpAddress<?> convert(final IpAddress<?> address) {
		if (address instanceof Ip4Address ip4)
			return XStreamIp4Address.convert(ip4);
		if (address instanceof Ip6Address ip6)
			return XStreamIp6Address.convert(ip6);
		return null;
	}

	public static IpNetAddress<?, ?> convert(final IpNetAddress<?, ?> address) {
		if (address instanceof IpNet4Address subnet4)
			return XStreamIpNet4Address.convert(subnet4);
		if (address instanceof IpNet6Address subnet6)
			return XStreamIpNet6Address.convert(subnet6);
		return null;
	}

	public static IpNetAddress<?, ?> convert2IpNetAddress(final IpAddress<?> ip) {
		if (ip instanceof Ip4Address ip4)
			return new XStreamIpNet4Address(ip4);
		if (ip instanceof Ip6Address ip6)
			return new XStreamIpNet6Address(ip6);
		return null;
	}

	public static Ip4Address parseIp4Address(final String ip) throws UnknownHostException {
		final Ip4Address result = tryParseIp4Address(ip);
		if (result == null)
			throw new UnknownHostException("Ipv4 Address<" + ip + "> can not be parsed!");
		return result;
	}

	public static Ip4Address tryParseIp4Address(final String ip) {
		final byte[] address = IpBinaryUtils.parseIp4Address(ip);
		if (address == null)
			return null;
		return new XStreamIp4Address(address);
	}

	public static IpNet4Address parseIpNet4Address(final String subnet) throws UnknownHostException {
		final IpNet4Address result = tryParseIpNet4Address(subnet);
		if (result == null)
			throw new UnknownHostException("Ipv4 Address (Subnet) <" + subnet + "> can not be parsed!");
		return result;
	}

	public static IpNet4Address tryParseIpNet4Address(final String subnet) {
		return XStreamIpNet4Address
				.convert(net.runeduniverse.lib.utils.net.IpNetworkUtils.tryParseIpNet4Address(subnet));
	}

	public static Ip6Address parseIp6Address(final String ip) throws UnknownHostException {
		final Ip6Address result = tryParseIp6Address(ip);
		if (result == null)
			throw new UnknownHostException("Ipv6 Address<" + ip + "> can not be parsed!");
		return result;
	}

	public static Ip6Address tryParseIp6Address(final String ip) {
		final byte[] address = IpBinaryUtils.parseIp6Address(ip);
		if (address == null)
			return null;
		return new XStreamIp6Address(address);
	}

	public static IpNet6Address parseIpNet6Address(final String subnet) throws UnknownHostException {
		final IpNet6Address result = tryParseIpNet6Address(subnet);
		if (result == null)
			throw new UnknownHostException("Ipv6 Address (Subnet) <" + subnet + "> can not be parsed!");
		return result;
	}

	public static IpNet6Address tryParseIpNet6Address(final String subnet) {
		return XStreamIpNet6Address
				.convert(net.runeduniverse.lib.utils.net.IpNetworkUtils.tryParseIpNet6Address(subnet));
	}

	public static Collection<Ip4Address> interpolateIpRange(final Ip4Address ip1, final Ip4Address ip2) {
		return _interpolateIpRange(ip1, ip2, XStreamIp4Address::new);
	}

	public static Collection<Ip4Address> interpolateIpRange(final IpNet4Address subnet) {
		return interpolateIpRange(subnet.getLowestAddress(), subnet.getHighestAddress());
	}

	public static Collection<Ip6Address> interpolateIpRange(final Ip6Address ip1, final Ip6Address ip2) {
		return _interpolateIpRange(ip1, ip2, XStreamIp6Address::new);
	}

	public static Collection<Ip6Address> interpolateIpRange(final IpNet6Address subnet) {
		return interpolateIpRange(subnet.getLowestAddress(), subnet.getHighestAddress());
	}

	public static Collection<IpNet4Address> extractIp4Subnets(final Collection<Ip4Address> addresses) {
		return net.runeduniverse.lib.utils.net.IpNetworkUtils.<Ip4Address, IpNet4Address>_extractIpSubnets(addresses,
				(short) 32, IpNetworkUtils::createIpNet4Address);
	}

	public static Collection<IpNet6Address> extractIp6Subnets(final Collection<Ip6Address> addresses) {
		return net.runeduniverse.lib.utils.net.IpNetworkUtils.<Ip6Address, IpNet6Address>_extractIpSubnets(addresses,
				(short) 128, IpNetworkUtils::createIpNet6Address);
	}

	public static Collection<IpNetAddress<?, ?>> extractIpSubnets(final Collection<IpAddress<?>> addresses) {
		final Set<Ip4Address> ip4s = new LinkedHashSet<>();
		final Set<Ip6Address> ip6s = new LinkedHashSet<>();

		_forEach(addresses, //
				new TypeFilter<>(Ip4Address.class, ip4s::add), //
				new TypeFilter<>(Ip6Address.class, ip6s::add));

		// extract the subnets
		final Set<IpNetAddress<?, ?>> subnets = new LinkedHashSet<>();
		subnets.addAll(extractIp4Subnets(ip4s));
		try {
			subnets.addAll(extractIp6Subnets(ip6s));
		} catch (UnsupportedOperationException e) {
			// ipv6 currently out of scope!
		}
		return subnets;
	}

	public static Collection<Deque<Ip4Address>> extractIp4Ranges(final Collection<Ip4Address> addresses) {
		return _extractIpRanges(addresses);
	}

	public static Collection<Deque<Ip6Address>> extractIp6Ranges(final Collection<Ip6Address> addresses) {
		return _extractIpRanges(addresses);
	}

	public static Collection<Deque<IpAddress<?>>> extractIpRanges(final Collection<IpAddress<?>> addresses) {
		final Collection<Ip4Address> ipv4Col = new LinkedList<>();
		final Collection<Ip6Address> ipv6Col = new LinkedList<>();

		_forEach(addresses, //
				new TypeFilter<>(Ip4Address.class, ipv4Col::add), //
				new TypeFilter<>(Ip6Address.class, ipv6Col::add));

		final List<Deque<IpAddress<?>>> list = new LinkedList<>();
		for (Deque<Ip4Address> deque : extractIp4Ranges(ipv4Col)) {
			list.add(new LinkedList<IpAddress<?>>(deque));
		}
		for (Deque<Ip6Address> deque : extractIp6Ranges(ipv6Col)) {
			list.add(new LinkedList<IpAddress<?>>(deque));
		}
		return list;
	}
}
