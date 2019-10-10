package com.rongji.dfish.misc;

public class GeohashTest {
	public static void main(String[] args) {
		String hash=Geohash.encode(40.222, 116.248, 5);
		System.out.println(hash);
		double[] decode=Geohash.decode(hash);
		System.out.println("{lat="+decode[0]+",lon="+decode[1]+"}");
		
		hash=Geohash.encode(40.222, 116.248, 8);
		System.out.println(hash);
		decode=Geohash.decode(hash);
		System.out.println("{lat="+decode[0]+",lon="+decode[1]+"}");
		
		hash=Geohash.encode(0, 180, 5);
		System.out.println(hash);
		for(String nb:Geohash.expand(hash)){
			double[] d=Geohash.decode(nb);
			System.out.println(nb+"={lat="+d[0]+",lon="+d[1]+"}");
		}
//		{lat=45.0,lon=-135.0}
//		9rjkf
//		{lat=40.22201478481293,lon=-116.24827980995178}
//		xbpbp
//		[xbpbq, xbpbr, 80002, xbpbn, 80000, rzzzy, rzzzz, 2pbpb]
//		xbpbq={lat=0.0439453125,lon=179.9560546875}
//		xbpbr={lat=0.0439453125,lon=179.9560546875}
//		80002={lat=0.0439453125,lon=-179.9560546875}
//		xbpbn={lat=0.0439453125,lon=179.9560546875}
//		80000={lat=0.0439453125,lon=-179.9560546875}
//		rzzzy={lat=-0.0439453125,lon=179.9560546875}
//		rzzzz={lat=-0.0439453125,lon=179.9560546875}
//		2pbpb={lat=-0.0439453125,lon=-179.9560546875}

	}
}
