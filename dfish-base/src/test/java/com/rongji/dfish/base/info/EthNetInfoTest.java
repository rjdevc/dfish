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

		String i=e.linuxParseMacAddress(s);
		System.out.println(i);

		String s2="ens32     Link encap:以太网  硬件地址 00:0c:29:ba:c1:34  \n" +
				"          inet 地址:192.168.14.196  广播:192.168.14.255  掩码:255.255.255.0\n" +
				"          inet6 地址: fe80::956d:19fc:53b1:459a/64 Scope:Link\n" +
				"          UP BROADCAST RUNNING MULTICAST  MTU:1500  跃点数:1\n" +
				"          接收数据包:14530852 错误:0 丢弃:5297 过载:0 帧数:0\n" +
				"          发送数据包:9865946 错误:0 丢弃:0 过载:0 载波:0\n" +
				"          碰撞:0 发送队列长度:1000 \n" +
				"          接收字节:3759974559 (3.7 GB)  发送字节:1776874970 (1.7 GB)\n" +
				"\n" +
				"lo        Link encap:本地环回  \n" +
				"          inet 地址:127.0.0.1  掩码:255.0.0.0\n" +
				"          inet6 地址: ::1/128 Scope:Host\n" +
				"          UP LOOPBACK RUNNING  MTU:65536  跃点数:1\n" +
				"          接收数据包:11812096 错误:0 丢弃:0 过载:0 帧数:0\n" +
				"          发送数据包:11812096 错误:0 丢弃:0 过载:0 载波:0\n" +
				"          碰撞:0 发送队列长度:1 \n" +
				"          接收字节:2061040836 (2.0 GB)  发送字节:2061040836 (2.0 GB)";
		i=e.linuxParseMacAddress(s2);
		System.out.println(i);

	}
}
