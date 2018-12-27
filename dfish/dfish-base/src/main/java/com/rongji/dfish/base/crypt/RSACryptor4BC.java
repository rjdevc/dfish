package com.rongji.dfish.base.crypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/**
 * RSA 加解密的方法。使用<code>bouncycastle<code>的实现。
 *
 * @author LinLW
 * @version 1.0
 */
public class RSACryptor4BC extends StringCryptor {
	Provider PROVIDER = new org.bouncycastle.jce.provider.BouncyCastleProvider();

	KeyPair keyPair;

	RSACryptor4BC(String encoding, int presentStyle, Object arg) {
		super.encoding = encoding;
		super.presentStyle = presentStyle;

		if (arg instanceof KeyPair) {
			keyPair = (KeyPair) arg;
		} else if (arg != null && arg instanceof BigInteger[]) {
			final BigInteger[] cast = (BigInteger[]) arg;
			RSAPrivateKey prikey = new DFishRSAPrivateKey(cast[2], cast[1]);
			RSAPublicKey pubkey = new DFishRSAPublicKey(cast[2], cast[0]);
			keyPair = new KeyPair(pubkey, prikey);
		} else {
			KeyPairGenerator keyPairGen=null;
			try {
				keyPairGen = KeyPairGenerator.getInstance("RSA", PROVIDER);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			final int KEY_SIZE = 1024;// 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			keyPair = keyPairGen.genKeyPair();
		}
	}

	@Override
	protected byte[] encrypt(byte[] src) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA", PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
			int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
			// 加密块大小为127
			// byte,加密后为128个byte;因此共有2个加密块，第一个127
			// byte第二个为1个byte
			int outputSize = cipher.getOutputSize(src.length);// 获得加密块加密后块大小
			int leavedSize = src.length % blockSize;
			int blocksSize = leavedSize != 0 ? src.length / blockSize + 1
					: src.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (src.length - i * blockSize > 0) {
				if (src.length - i * blockSize > blockSize)
					cipher.doFinal(src, i * blockSize, blockSize, raw, i
							* outputSize);
				else
					cipher.doFinal(src, i * blockSize, src.length - i
							* blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。

				i++;
			}
			return raw;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	protected byte[] decrypt(byte[] src) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA", PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;

			while (src.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(src, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	static class DFishRSAPublicKey implements RSAPublicKey {

		DFishRSAPublicKey(BigInteger modulus, BigInteger publicExponent) {
			this.modulus = modulus;
			this.publicExponent = publicExponent;
		}

		public BigInteger getModulus() {
			return modulus;
		}

		public BigInteger getPublicExponent() {
			return publicExponent;
		}

		public String getAlgorithm() {
			return "RSA";
		}

		public String getFormat() {
			return "X.509";
		}

		public byte[] getEncoded() {
			byte []b = null;
			SubjectPublicKeyInfo subjectpublickeyinfo;
			try {
				subjectpublickeyinfo = new SubjectPublicKeyInfo(
						new AlgorithmIdentifier(
								PKCSObjectIdentifiers.rsaEncryption, new DERNull()),
						(new RSAPublicKeyStructure(getModulus(),
								getPublicExponent())).toASN1Primitive());
				b = subjectpublickeyinfo.getEncoded(ASN1Encoding.DER);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return b;
		}

		public int hashCode() {
			return getModulus().hashCode() ^ getPublicExponent().hashCode();
		}

		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (!(obj instanceof RSAPublicKey)) {
				return false;
			} else {
				RSAPublicKey rsapublickey = (RSAPublicKey) obj;
				return getModulus().equals(rsapublickey.getModulus())
						&& getPublicExponent().equals(
								rsapublickey.getPublicExponent());
			}
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			String s = System.getProperty("line.separator");
			sb.append("RSA Public Key").append(s);
			sb.append("            modulus: ")
					.append(getModulus().toString(16)).append(s);
			sb.append("    public exponent: ").append(
					getPublicExponent().toString(16)).append(s);
			return sb.toString();
		}

		static final long serialVersionUID = 2675817738516720772L;
		private BigInteger modulus;
		private BigInteger publicExponent;
	}

	static class DFishRSAPrivateKey implements RSAPrivateKey {

		DFishRSAPrivateKey(BigInteger modulus, BigInteger privateExponent) {
			this.modulus = modulus;
			this.privateExponent = privateExponent;
		}

		public BigInteger getModulus() {
			return modulus;
		}

		public BigInteger getPrivateExponent() {
			return privateExponent;
		}

		public String getAlgorithm() {
			return "RSA";
		}

		public String getFormat() {
			return "PKCS#8";
		}

		public byte[] getEncoded() {
			byte []b = null;
			PrivateKeyInfo privatekeyinfo;
			try {
				privatekeyinfo = new PrivateKeyInfo(
						new AlgorithmIdentifier(
								PKCSObjectIdentifiers.rsaEncryption, new DERNull()),
						(new RSAPrivateKeyStructure(getModulus(), ZERO,
								getPrivateExponent(), ZERO, ZERO, ZERO, ZERO, ZERO))
								.toASN1Primitive());
				b = privatekeyinfo.getEncoded(ASN1Encoding.DER);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return b;
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof RSAPrivateKey))
				return false;
			if (obj == this) {
				return true;
			} else {
				RSAPrivateKey rsaprivatekey = (RSAPrivateKey) obj;
				return getModulus().equals(rsaprivatekey.getModulus())
						&& getPrivateExponent().equals(
								rsaprivatekey.getPrivateExponent());
			}
		}

		public int hashCode() {
			return getModulus().hashCode() ^ getPrivateExponent().hashCode();
		}

		static final long serialVersionUID = 5110188922551353628L;
		private static BigInteger ZERO = BigInteger.valueOf(0L);
		protected BigInteger modulus;
		protected BigInteger privateExponent;
	}
}
