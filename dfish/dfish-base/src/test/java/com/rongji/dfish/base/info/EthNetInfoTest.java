package com.rongji.dfish.base.info;

import java.text.ParseException;

import org.junit.Test;

public class EthNetInfoTest {
	@Test
	public void test() throws ParseException{
		String s="eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500\n"
				+ "        inet 172.31.76.253  netmask 255.255.240.0  broadcast 172.31.79.255\n"
				+ "        ether 00:16:3e:04:f3:97  txqueuelen 1000  (Ethernet)\n"
				+ "        RX packets 1562531  bytes 1984062701 (1.8 GiB)\n"
				+ "        RX errors 0  dropped 0  overruns 0  frame 0\n"
				+ "        TX packets 625399  bytes 59033326 (56.2 MiB)\n"
				+ "        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0\n"
				+ "\n"
				+ "lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536\n"
				+ "        inet 127.0.0.1  netmask 255.0.0.0\n"
				+ "        loop  txqueuelen 1  (Local Loopback)\n"
				+ "        RX packets 224  bytes 41054 (40.0 KiB)\n"
				+ "        RX errors 0  dropped 0  overruns 0  frame 0\n"
				+ "        TX packets 224  bytes 41054 (40.0 KiB)\n"
				+ "        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0";
		EthNetInfo e=new EthNetInfo();
//		String i=e.linuxParseMacAddress(s);
//		System.out.println(i);
	}
}
