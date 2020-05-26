package com.rongji.dfish.base.info;

import java.io.*;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取网卡MAC地址。注意，调用的是操作系统的方法。所以，有可能被欺骗
 */
@SuppressWarnings("ALL")
public class EthNetInfo {
	/**
	 * 读取出错的时候，记录的MAC地址
	 */
	protected static final String ERR_MAC="00:00:00:00:00:00";
	private static String mac = null;

	/**
	 * 读取网卡MAC地址。因为机器可能有多个物理网卡或虚拟网卡。这里去的是第一个地址。
	 * 因为某些设备，比如说笔记本电脑，在使用电池，并且接了有线的情况下，有可能会自动禁用无线网卡。
	 * 导致这个值可能是变化的。
	 * @return String
	 */
	public static String getMacAddress() {
//		return "00:00:00:00:00:00";
		// String mac = "";
		if (mac == null) {
//			EthNetInfo ins = new EthNetInfo();
//			String os = System.getProperty("os.name");
//			try {
//				if (os.startsWith("Windows")) {
//					mac = ins.windowsParseMacAddress(ins
//							.windowsRunIpConfigCommand());
//				} else if (os.startsWith("Linux") || os.startsWith("Unix")) {
//					mac = ins.linuxParseMacAddress(ins
//							.linuxRunIfConfigCommand());
//				} else if (os.startsWith("Mac OS X")) {
//					mac = ins.osxParseMacAddress(ins.osxRunIfConfigCommand());
//				} else if (os.toLowerCase().startsWith("aix")) {
//					mac = ins.aixParseMacAddress(ins.aixRunIfConfigCommand());
//				}else{
//					mac=ERR_MAC;
//				}
//			} catch (Throwable t) {
//				mac=ERR_MAC;
//				t.printStackTrace();
//			}
//			mac = mac.replaceAll("-", ":").toLowerCase();
			if (macs == null) {
				macs = getAllMacAddress();
			}
			if (macs.isEmpty()) {
				mac = ERR_MAC;
			} else {
				mac = macs.iterator().next();
			}
		}
		return mac;
	}

	private static Set<String> macs = null;

	/**
	 * 读取所有激活状态下的网卡的MAC地址。可能包含物理网卡和虚拟网卡。
	 * 因为某些设备，比如说笔记本电脑，在使用电池，并且接了有线的情况下，有可能会自动禁用无线网卡。
	 * 导致这个值可能是变化的。
	 * @return Mac地址集合
	 */
	public static Set<String> getAllMacAddress() {
//		if(macs==null){
//			macs=new HashSet<String>();
//			macs.add("00:00:00:00:00:00");
//		}
//		return macs;
		if (macs == null) {
			EthNetInfo ins = new EthNetInfo();
			String os = System.getProperty("os.name");
			try {
				if (os.startsWith("Windows")) {
					macs = ins.windowsParseMacAddresses(ins.windowsRunIpConfigCommand());
//				} else if (os.startsWith("Linux") || os.startsWith("Unix")) {
//					macs = ins.linuxParseMacAddresses(ins.linuxRunIfConfigCommand());
				} else if (os.startsWith("Mac OS X")) {
					macs = ins.osxParseMacAddresses(ins.osxRunIfConfigCommand());
				} else if (os.toLowerCase().startsWith("aix")) {
					macs = ins.aixParseMacAddresses(ins.aixRunIfConfigCommand());
				}else{
					macs = ins.linuxParseMacAddresses(ins.linuxRunIfConfigCommand());
				}
			} catch (Throwable t) {
				macs=new HashSet<String>();
				macs.add(ERR_MAC);
				t.printStackTrace();
			}
		}
		return macs;
	}

	/**
	 * osxParseMacAddresses
	 * 
	 * @param ipConfigResponse
	 *            String
	 * @return Set
	 */
	private Set<String> osxParseMacAddresses(String ipConfigResponse)
			throws ParseException {
		String localHost = null;

		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage(), 0);
		}
		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		Set<String> set = new HashSet<String>();
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			// see if line contains MAC address
			int macAddressPosition = line.indexOf("ether");
			if (macAddressPosition != 0) {
				continue;
			}
			String macAddressCandidate = line.substring(macAddressPosition + 6)
					.trim();
			if (osxIsMacAddress(macAddressCandidate)) {
				set.add(macAddressCandidate.replaceAll("-", ":").toLowerCase());
			}
		}
		if (set.size() == 0) {
			ParseException ex = new ParseException(
					"cannot read MAC address for " + localHost + " from ["
							+ ipConfigResponse + "]", 0);
			ex.printStackTrace();
			throw ex;
		}
		return set;
	}

	/**
	 * linuxParseMacAddresses
	 * 支持linux 2.4 和2.6 的不同格式。
	 * 不管是 HWaddr 还是 ether关键字都可以识别。
	 * 但部分操作系统返回的是中文，就可能不可识别了。
	 * 
	 * @param ipConfigResponse
	 *            String
	 * @return Set
	 */
	private Set<String> linuxParseMacAddresses(String ipConfigResponse)
			throws ParseException {
		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;
		Set<String> set = new HashSet<String>();
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			// see if line contains IP address
			if (lastMacAddress != null) {
				set.add(lastMacAddress.replaceAll("-", ":").toLowerCase());
			}

			// see if line contains MAC address
			int pos1= line.indexOf("HWaddr");
			int pos2= line.indexOf("ether");
			int macAddressPosition = Math.max(pos1, pos2);
			if (macAddressPosition < 0) {
				continue;
			}

			String macAddressCandidate = line.substring(macAddressPosition + 6)
					.trim();
			if(macAddressCandidate.indexOf(' ')>0){
				String[] tempStrings=macAddressCandidate.split(" ");
				macAddressCandidate=tempStrings[0];
			}
			if (linuxIsMacAddress(macAddressCandidate)) {
				lastMacAddress = macAddressCandidate;
				continue;
			}
		}
		if (set.size() == 0) {
			ParseException ex = new ParseException(
					"cannot read MAC address  from [" + ipConfigResponse + "]",
					31);
			ex.printStackTrace();
			throw ex;
		}
		return set;
	}

	/**
	 * windowsParseMacAddresses
	 * 
	 * @param string
	 *            String
	 * @return Set
	 */
	private Set<String> windowsParseMacAddresses(String ipConfigResponse)
			throws ParseException {

		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		Set<String> set = new HashSet<String>();
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();

			// see if line contains IP address

			// see if line contains MAC address
			int macAddressPosition = line.indexOf(":");
			if (macAddressPosition < 0) {
				continue;
			}

			String macAddressCandidate = line.substring(macAddressPosition + 1)
					.trim();
			if (windowsIsMacAddress(macAddressCandidate)) {
//				lastMacAddress = macAddressCandidate;
				set.add(macAddressCandidate.replaceAll("-", ":").toLowerCase());
				continue;
			}
		}
		if (set.size() == 0) {
			ParseException ex = new ParseException(
					"cannot read MAC address from [" + ipConfigResponse + "]",
					0);
			ex.printStackTrace();
			throw ex;
		}
		return set;
	}

	/*
	 * Linux stuff
	 */
	private String linuxParseMacAddress(String ipConfigResponse)
			throws ParseException {
		String localHost = null;
		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage(), 0);
		}
		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;

		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			boolean containsLocalHost = line.indexOf(localHost) >= 0;
			// see if line contains IP address
			if (containsLocalHost && lastMacAddress != null) {
				return lastMacAddress;
			}

			// see if line contains MAC address
			int pos1= line.indexOf("HWaddr");
			int pos2= line.indexOf("ether");
			int macAddressPosition = Math.max(pos1, pos2);
			if (macAddressPosition < 0) {
				continue;
			}

			String macAddressCandidate = line.substring(macAddressPosition + 6)
					.trim();
			if(macAddressCandidate.indexOf(' ')>0){
				String[] tempStrings=macAddressCandidate.split(" ");
				macAddressCandidate=tempStrings[0];
			}
			if (linuxIsMacAddress(macAddressCandidate)) {
				lastMacAddress = macAddressCandidate;
				continue;
			}
		}
		
		if (lastMacAddress!=null) {
			return lastMacAddress;
		}

		ParseException ex = new ParseException("cannot read MAC address for "
				+ localHost + " from [" + ipConfigResponse + "]", 0);
		ex.printStackTrace();
		throw ex;
	}
	private static final Pattern LINUX_MAC_PATTERN=Pattern
			.compile("[0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0 -9a-fA-F]{2}[-:][0-9a-fA-F]{2}");
	private boolean linuxIsMacAddress(String macAddressCandidate) {
		Matcher m = LINUX_MAC_PATTERN.matcher(macAddressCandidate);
		return m.matches();
	}

	private String linuxRunIfConfigCommand() throws IOException {
		Runtime r=Runtime.getRuntime();
		Process p = r.exec("ifconfig");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1) {
				break;
			}
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		stdoutStream.close();
		return outputText;
	}

	/*
	 * unix stuff
	 */
	private String aixParseMacAddress(String ipConfigResponse)
			throws ParseException {
		String localHost = null;
		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage(), 0);
		}
		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;

		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			// boolean containsLocalHost = line.indexOf(localHost) >= 0;
			// see if line contains IP address
			if (lastMacAddress != null) {
				return lastMacAddress;
			}

			// see if line contains MAC address
			String[] blockes = line.split("[ ]+");
			if (blockes.length != 9) {
				continue;
			}

			String macAddressCandidate = blockes[3].trim();
			if (aixIsMacAddress(macAddressCandidate)) {
				lastMacAddress = convertUnixMacAddr(macAddressCandidate);
				continue;
			}

		}

		ParseException ex = new ParseException("cannot read MAC address for "
				+ localHost + " from [" + ipConfigResponse + "]", 0);
		ex.printStackTrace();
		throw ex;
	}

	/**
	 * linuxParseMacAddresses
	 * 
	 * @param string
	 *            String
	 * @return Set
	 */
	private Set<String> aixParseMacAddresses(String ipConfigResponse)
			throws ParseException {
		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;
		Set<String> set = new HashSet<String>();
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();

			// see if line contains IP address
			if (lastMacAddress != null) {
				set.add(lastMacAddress);
			}

			// see if line contains MAC address
			String[] blockes = line.split("[ ]+");
			if (blockes.length != 9) {
				continue;
			}

			String macAddressCandidate = blockes[3].trim();
			if (aixIsMacAddress(macAddressCandidate)) {
				lastMacAddress = convertUnixMacAddr(macAddressCandidate);
				continue;
			}
		}
		if (set.size() == 0) {
			ParseException ex = new ParseException(
					"cannot read MAC address  from [" + ipConfigResponse + "]",
					0);
			ex.printStackTrace();
			throw ex;
		}
		return set;
	}

	private String convertUnixMacAddr(String macAddressCandidate) {
		String[] addrs = macAddressCandidate.split("[.]");
		String s = "";
		for (int i = 0; i < addrs.length; i++) {
			String elem = addrs[i];
			s += (elem.length() == 1 ? ("0" + elem) : (elem)) + ":";
		}
		return s.substring(0, 17);
	}

	private static final Pattern AIX_MAC_PATTERN = Pattern
			.compile("[0-9a-fA-F]+[.][0-9a-fA-F]+[.][0-9a-fA-F]+[.][0-9a-fA-F]+[.][0 -9a-fA-F]+[.][0-9a-fA-F]+");
	private boolean aixIsMacAddress(String macAddressCandidate) {
		Matcher m = AIX_MAC_PATTERN.matcher(macAddressCandidate);
		return m.matches();
	}

	private String aixRunIfConfigCommand() throws IOException {
		Process p = Runtime.getRuntime().exec("netstat -i");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1) {
				break;
			}
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		stdoutStream.close();
		return outputText;
	}

	/*
	 * Windows stuff
	 */
	private String windowsParseMacAddress(String ipConfigResponse)
			throws ParseException {
		String localHost = null;
		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage(), 0);
		}

		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		String lastMacAddress = null;

		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();

			// see if line contains IP address
			if (line.indexOf(localHost)>0 && lastMacAddress != null) {
				return lastMacAddress;
			}

			// see if line contains MAC address
			int macAddressPosition = line.indexOf(":");
			if (macAddressPosition <= 0) {
				continue;
			}

			String macAddressCandidate = line.substring(macAddressPosition + 1)
					.trim();
			if (windowsIsMacAddress(macAddressCandidate)) {
				lastMacAddress = macAddressCandidate;
				continue;
			}
		}
		ParseException ex = new ParseException("cannot read MAC address from ["
				+ ipConfigResponse + "]", 0);
		ex.printStackTrace();
		throw ex;
	}

	private static final Pattern WIN_MAC_PATTERN = Pattern
			.compile("[0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0 -9a-fA-F]{2}[-:][0-9a-fA-F]{2}");
	private boolean windowsIsMacAddress(String macAddressCandidate) {
		Matcher m = WIN_MAC_PATTERN.matcher(macAddressCandidate);
		return m.matches();
	}

	private String windowsRunIpConfigCommand() throws IOException {
		Process p = Runtime.getRuntime().exec("ipconfig /all");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1) {
				break;
			}
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		stdoutStream.close();
		return outputText;
	}

	/*
	 * Mac OS X Stuff
	 */
	private String osxParseMacAddress(String ipConfigResponse)
			throws ParseException {
		String localHost = null;

		try {
			localHost = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
			throw new ParseException(ex.getMessage(), 0);
		}
		StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			// see if line contains MAC address
			int macAddressPosition = line.indexOf("ether");
			if (macAddressPosition != 0) {
				continue;
			}
			String macAddressCandidate = line.substring(macAddressPosition + 6)
					.trim();
			if (osxIsMacAddress(macAddressCandidate)) {
				return macAddressCandidate;
			}
		}

		ParseException ex = new ParseException("cannot read MAC address for "
				+ localHost + " from [" + ipConfigResponse + "]", 0);
		ex.printStackTrace();
		throw ex;
	}

	private static final Pattern OSX_MAC_PATTERN = Pattern
			.compile("[0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0 -9a-fA-F]{2}[-:][0-9a-fA-F]{2}");
	private boolean osxIsMacAddress(String macAddressCandidate) {
		Matcher m = OSX_MAC_PATTERN.matcher(macAddressCandidate);
		return m.matches();
	}

	private String osxRunIfConfigCommand() throws IOException {
		Process p = Runtime.getRuntime().exec("ifconfig");
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
		StringBuffer buffer = new StringBuffer();
		while (true) {
			int c = stdoutStream.read();
			if (c == -1) {
				break;
			}
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		stdoutStream.close();
		return outputText;
	}
//	public static void main(String[] args) throws ParseException {
//		String s="ens32: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500\n" +
//				"        inet 192.168.93.76  netmask 255.255.255.192  broadcast 192.168.93.127\n" +
//				"        inet6 fe80::250:56ff:fe9c:3632  prefixlen 64  scopeid 0x20<link>\n" +
////				"        ether 00:50:56:9c:36:32  txqueuelen 1000  (Ethernet)\n" +
//				"        ether 00:50:56:9c:0a:ce  txqueuelen 1000  (Ethernet)\n" +
//				"        RX packets 6856286  bytes 6384398804 (5.9 GiB)\n" +
//				"        RX errors 0  dropped 1321753  overruns 0  frame 0\n" +
//				"        TX packets 3820273  bytes 2095398045 (1.9 GiB)\n" +
//				"        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0\n" +
//				"\n" +
//				"lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536";
//		EthNetInfo en=new EthNetInfo();
//		Set<String> set=en.linuxParseMacAddresses(s);
//		System.out.println(set);
//	}

}
