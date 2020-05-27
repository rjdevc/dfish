package com.rongji.dfish.base;

import org.junit.Test;

public class RangeDataFinderTest {

    @Test
    public void test() {
        String[][] ADDRS = new String[][]{
                {"福州市", "27.148.0.0", "27.149.255.255"},
                {"南平市", "27.150.32.0", "27.150.37.255"},
                {"福州市", "27.155.41.0", "27.156.127.255"},
                {"福州市", "36.248.0.0", "36.248.127.255"},
                {"内网地址", "192.168.0.0", "192.168.255.255"},
        };
        RangeDataFinder<String, Long> rdf = new RangeDataFinder<>();
        for (String[] row : ADDRS) {
            rdf.add(row[0], ip2long(row[1]), ip2long(row[2]));
        }
        System.out.println(rdf.find(ip2long("192.168.0.51")));
        System.out.println(rdf.find(ip2long("36.248.0.51")));


    }

    private Long ip2long(String s) {
        String[] p = s.split("[.]");
        return (Long.parseLong(p[0]) << 24) |
                (Long.parseLong(p[1]) << 16) |
                (Long.parseLong(p[2]) << 8) |
                Long.parseLong(p[3]);
    }
}
