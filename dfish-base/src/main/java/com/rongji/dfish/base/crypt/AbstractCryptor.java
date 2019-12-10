package com.rongji.dfish.base.crypt;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractCryptor {
    public static class AbstractCryptBuilder<T extends AbstractCryptBuilder> {
        public static final int PRESENT_RAW = 0;
        public static final int PRESENT_HEX = 1;
        public static final int PRESENT_BASE64 = 2;
        public static final int PRESENT_BASE32 = 3;
        /**
         * 注意和 RTF4648 的并不完全一致，该表示方式没有最后的PAD(=)
         * 加密的话和旧版本有所不同，后面不再带点号(.) 所以新版本加密的在旧版本中将无法解密
         * 但旧版本加密的新版本中可以解密。
         */
        public static final int PRESENT_BASE64_URLSAFE = 4;

        /**
         * 字符集用GBK
         */
        public static final String ENCODING_GBK = "GBK";
        /**
         * 字符集用UTF-8
         */
        public static final String ENCODING_UTF8 = "UTF-8";


        protected String algorithm;
        protected String encoding;
        protected int present;
        protected boolean gzip = false;

        public String getAlgorithm() {
            return algorithm;
        }

        public String getEncoding() {
            return encoding;
        }

        public int getPresent() {
            return present;
        }

        public T encoding(String encoding) {
            this.encoding = encoding;
            return (T) this;
        }

        public T present(int present) {
            this.present = present;
            return (T) this;
        }
    }

    /**
     * 每次读取读取8个字节的数据，并转化成5个字节，直到最后一组可能不是5的倍数。
     */
    public static class Base32InputStream extends AbstractBlockInputStream {

        public Base32InputStream(InputStream in) {
            super(in);
            EN_SIZE = 8;
            DATA_SIZE = 5;
            block = new byte[EN_SIZE];
        }

        private static final byte[] DECODE_TABLE = {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                //前0-31都是控制字符
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 2, 3, 4, 5, 6, 7,
                8, 9, 0, 0, 0, 0, 0, 0,
                //数字32-63 中包含数字
                0, 10, 11, 12, 13, 14, 15, 16,//A-G
                17, 1, 18, 19, 1, 20, 21, 0,//H-O
                22, 23, 24, 25, 26, 0, 27, 28,//P-W
                29, 30, 31, 0, 0, 0, 0, 0,//XYZ
                //64-95 是大写字母
                0, 10, 11, 12, 13, 14, 15, 16,//a-g
                17, 1, 18, 19, 1, 20, 21, 0,//h-o
                22, 23, 24, 25, 26, 0, 27, 28,//p-w
                29, 30, 31, 0, 0, 0, 0, 0,//xyz
                //96-127 是小写字母
        };

        @Override
        protected int doFillBlock(byte[] out, int outPos, byte[] in, int inPos) {
            out[outPos] = (byte) (((DECODE_TABLE[in[inPos]] & 0x1F) << 3) | ((DECODE_TABLE[in[inPos + 1]] & 0x1C) >> 2));
            out[outPos + 1] = (byte) (((DECODE_TABLE[in[inPos + 1]] & 0x03) << 6) | ((DECODE_TABLE[in[inPos + 2]] & 0x1F) << 1) | ((DECODE_TABLE[in[inPos + 3]] & 0x10) >> 4));
            out[outPos + 2] = (byte) (((DECODE_TABLE[in[inPos + 3]] & 0x0F) << 4) | ((DECODE_TABLE[in[inPos + 4]] & 0x1E) >> 1));
            out[outPos + 3] = (byte) (((DECODE_TABLE[in[inPos + 4]] & 0x01) << 7) | ((DECODE_TABLE[in[inPos + 5]] & 0x1F) << 2) | ((DECODE_TABLE[in[inPos + 6]] & 0x18) >> 3));
            out[outPos + 4] = (byte) (((DECODE_TABLE[in[inPos + 6]] & 0x07) << 5) | ((DECODE_TABLE[in[inPos + 7]] & 0x1F)));
            return 5;
        }

        @Override
        protected int readTail(byte[] b, int off) {

            int range = blockSize * DATA_SIZE / EN_SIZE;//FIXME
            boolean toLastChuck = false;
            byte[] realb = b;
            if (range > b.length) {
                //一般来说不可能在最后结尾的几个字节足控制字符。
                //碰到了再解决 LinLW 2019-12-09
                toLastChuck = true;
                decryptedBlockSize = range - b.length;
                b = new byte[range];
                decryptedBlock = b;
            }
            Arrays.fill(b, off, off + range, (byte) 0);
            for (int i = 0, j = 0, index = 0; i < blockSize; i++) {
                int val;
                try {
                    val = DECODE_TABLE[block[i]];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new RuntimeException("Illegal character");
                }
                if (index <= 3) {
                    index = (index + 5) % 8;
                    if (index == 0) {
                        b[off + j++] |= val;
                    } else {
                        b[off + j] |= val << (8 - index);
                    }
                } else {
                    index = (index + 5) % 8;
                    b[off + j++] |= (val >> index);
                    if (j < range) {
                        b[off + j] |= val << (8 - index);
                    }
                }
            }
            blockSize = 0;
            if (toLastChuck) {
                System.arraycopy(decryptedBlock, 0, realb, 0, range - decryptedBlockSize);
            }
            return range;
        }
    }

    public static class Base64InputStream extends AbstractBlockInputStream {

        public Base64InputStream(InputStream in) {
            super(in);
            EN_SIZE = 4;
            DATA_SIZE = 3;
            block = new byte[EN_SIZE];
        }

        private static final byte[] DECODE_TABLE = {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                //前0-31都是控制字符
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 62, 0, 62, 0, 63,
                52, 53, 54, 55, 56, 57, 58, 59,
                60, 61, 0, 0, 0, 0, 0, 0,
                //数字32-63 中包含数字
                0, 0, 1, 2, 3, 4, 5, 6, //A-G
                7, 8, 9, 10, 11, 12, 13, 14,//H-O
                15, 16, 17, 18, 19, 20, 21, 22, //P-W
                23, 24, 25, 0, 0, 0, 0, 63,//XYZ
                //64-95 是大写字母
                0, 26, 27, 28, 29, 30, 31, 32, //a-g
                33, 34, 35, 36, 37, 38, 39, 40, //h-o
                41, 42, 43, 44, 45, 46, 47, 48, //p-w
                49, 50, 51, 0, 0, 0, 0, 0,//xyz
                //96-127 是小写字母
        };

        @Override
        protected int doFillBlock(byte[] out, int outPos, byte[] in, int inPos) {
            if (in[inPos + 2] == '=' || in[inPos + 2] == '.') {
                out[outPos] = (byte) (((DECODE_TABLE[in[inPos]] & 0x3F) << 2) | ((DECODE_TABLE[in[inPos + 1]] & 0x30) >> 4));
                return 1;
            } else if (in[inPos + 3] == '=' || in[inPos + 3] == '.') {
                out[outPos] = (byte) (((DECODE_TABLE[in[inPos]] & 0x3F) << 2) | ((DECODE_TABLE[in[inPos + 1]] & 0x30) >> 4));
                out[outPos + 1] = (byte) (((DECODE_TABLE[in[inPos + 1]] & 0x0F) << 4) | ((DECODE_TABLE[in[inPos + 2]] & 0x3C) >> 2));
                return 2;
            } else {
                out[outPos] = (byte) (((DECODE_TABLE[in[inPos]] & 0x3F) << 2) | ((DECODE_TABLE[in[inPos + 1]] & 0x30) >> 4));
                out[outPos + 1] = (byte) (((DECODE_TABLE[in[inPos + 1]] & 0x0F) << 4) | ((DECODE_TABLE[in[inPos + 2]] & 0x3C) >> 2));
                out[outPos + 2] = (byte) (((DECODE_TABLE[in[inPos + 2]] & 0x03) << 6) | ((DECODE_TABLE[in[inPos + 3]] & 0x3F)));
                return 3;
            }
        }

        @Override
        protected int readTail(byte[] b, int off) {
            if (blockSize == 0) {
                return -1;
            }
            if (blockSize == 2) {
                b[0 + off] = (byte) (((DECODE_TABLE[block[0]] & 0x3F) << 2) | ((DECODE_TABLE[block[1]] & 0x30) >> 4));
                blockSize = 0;
                return 1;
            } else if (blockSize == 3) {
                b[0 + off] = (byte) (((DECODE_TABLE[block[0]] & 0x3F) << 2) | ((DECODE_TABLE[block[1]] & 0x30) >> 4));
                b[1 + off] = (byte) (((DECODE_TABLE[block[0 + 1]] & 0x0F) << 4) | ((DECODE_TABLE[block[2]] & 0x3C) >> 2));
                blockSize = 0;
                return 2;
            } else {
                blockSize = 0;
                return 0;
            }
        }
    }

    public static class Base32OutputStream extends AbstractBlockOutputStream {
        public Base32OutputStream(OutputStream out) {
            super(out);
            EN_SIZE = 8;
            DATA_SIZE = 5;
            block = new byte[EN_SIZE];
        }

        private static final byte[] ALPHABET = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q',
                'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
        };

        /**
         * 完整的5字节转8字节。独立出来，是为了性能。 完整的分组无需复杂计算。
         *
         * @param in
         * @param inPos
         * @param out
         * @param outPos
         */
        @Override
        protected void doFillBlock(byte[] in, int inPos, byte[] out, int outPos) {
            out[outPos] = ALPHABET[(in[inPos] & 0xF8) >> 3];
            out[outPos + 1] = ALPHABET[((in[inPos] & 0x07) << 2) | ((in[inPos + 1] & 0xC0) >> 6)];
            out[outPos + 2] = ALPHABET[(in[inPos + 1] & 0x3E) >> 1];
            out[outPos + 3] = ALPHABET[((in[inPos + 1] & 0x1) << 4) | ((in[inPos + 2] & 0xF0) >> 4)];
            out[outPos + 4] = ALPHABET[((in[inPos + 2] & 0x0F) << 1) | ((in[inPos + 3] & 0x80) >> 7)];
            out[outPos + 5] = ALPHABET[(in[inPos + 3] & 0x7C) >> 2];
            out[outPos + 6] = ALPHABET[((in[inPos + 3] & 0x03) << 3) | ((in[inPos + 4] & 0xE0) >> 5)];
            out[outPos + 7] = ALPHABET[in[inPos + 4] & 0x1F];
        }

        @Override
        public void flush() throws IOException {
            if (blockSize > 0) {
                //不管已存在的chunk是否成组，直接产生结果。
                //Crockford's Base32 不产生PAD
                byte[] chars = new byte[((blockSize * 8) / 5) + ((blockSize % 5) != 0 ? 1 : 0)];

                for (int i = 0, j = 0, index = 0; i < chars.length; i++) {
                    if (index > 3) {
                        int b = block[j] & (0xFF >> index);
                        index = (index + 5) % 8;
                        b <<= index;
                        if (j < blockSize - 1) {
                            b |= (block[j + 1] & 0xFF) >> (8 - index);
                        }
                        chars[i] = ALPHABET[b];
                        j++;
                    } else {
                        chars[i] = ALPHABET[((block[j] >> (8 - (index + 5))) & 0x1F)];
                        index = (index + 5) % 8;
                        if (index == 0) {
                            j++;
                        }
                    }
                }
                out.write(chars);
            }
        }
    }

    public static class Base64OutputStream extends AbstractBlockOutputStream {
        public Base64OutputStream(OutputStream out) {
            super(out);
            EN_SIZE = 4;
            DATA_SIZE = 3;
            block = new byte[EN_SIZE];
        }

        private static final byte[] ALPHABET = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
        };
        byte PAD = '=';

        /**
         * 完整的5字节转8字节。独立出来，是为了性能。 完整的分组无需复杂计算。
         *
         * @param in
         * @param inPos
         * @param out
         * @param outPos
         */
        @Override
        protected void doFillBlock(byte[] in, int inPos, byte[] out, int outPos) {
            out[outPos] = ALPHABET[(in[inPos] & 0xFC) >> 2];
            out[outPos + 1] = ALPHABET[(in[inPos] & 0x03) << 4 | ((in[inPos + 1] & 0xF0) >> 4)];
            out[outPos + 2] = ALPHABET[(in[inPos + 1] & 0x0F) << 2 | ((in[inPos + 2] & 0xC0) >> 6)];
            out[outPos + 3] = ALPHABET[(in[inPos + 2] & 0x3F)];
        }

        @Override
        public void flush() throws IOException {
            if (blockSize > 0) {
                //不管已存在的chunk是否成组，直接产生结果。
                byte[] chars = new byte[4];
                if (blockSize == 2) {
                    chars[0] = ALPHABET[(block[0] & 0xFC) >> 2];
                    chars[1] = ALPHABET[(block[0] & 0x03) << 4 | ((block[1] & 0xF0) >> 4)];
                    chars[2] = ALPHABET[(block[1] & 0x0F) << 4];
                    chars[3] = PAD;
                }
                if (blockSize == 1) {
                    chars[0] = ALPHABET[(block[0] & 0xFC) >> 2];
                    chars[1] = ALPHABET[(block[0] & 0x03) << 4];
                    chars[2] = PAD;
                    chars[3] = PAD;
                } else if (blockSize == 3) {//仅仅为了容错;
                    chars[0] = ALPHABET[(block[0] & 0xFC) >> 2];
                    chars[1] = ALPHABET[(block[0] & 0x03) << 4 | ((block[1] & 0xF0) >> 4)];
                    chars[2] = ALPHABET[(block[1] & 0x0F) << 4 | ((block[2] & 0x03) >> 6)];
                    chars[3] = ALPHABET[(block[2] & 0x3F)];
                }
                out.write(chars);
            }
        }
    }

    public static class Base64UrlsafeOutputStream extends AbstractBlockOutputStream {
        public Base64UrlsafeOutputStream(OutputStream out) {
            super(out);
            EN_SIZE = 4;
            DATA_SIZE = 3;
            block = new byte[EN_SIZE];
        }

        private static final byte[] ALPHABET = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
        };

        /**
         * 完整的5字节转8字节。独立出来，是为了性能。 完整的分组无需复杂计算。
         *
         * @param in
         * @param inPos
         * @param out
         * @param outPos
         */
        @Override
        protected void doFillBlock(byte[] in, int inPos, byte[] out, int outPos) {
            out[outPos] = ALPHABET[(in[inPos] & 0xFC) >> 2];
            out[outPos + 1] = ALPHABET[(in[inPos] & 0x03) << 4 | ((in[inPos + 1] & 0xF0) >> 4)];
            out[outPos + 2] = ALPHABET[(in[inPos + 1] & 0x0F) << 2 | ((in[inPos + 2] & 0xC0) >> 6)];
            out[outPos + 3] = ALPHABET[(in[inPos + 2] & 0x3F)];
        }

        @Override
        public void flush() throws IOException {
            if (blockSize > 0) {
                //不管已存在的chunk是否成组，直接产生结果。
                byte[] chars = null;
                if (blockSize == 2) {
                    chars = new byte[3];
                    chars[0] = ALPHABET[(block[0] & 0xFC) >> 2];
                    chars[1] = ALPHABET[(block[0] & 0x03) << 4 | ((block[1] & 0xF0) >> 4)];
                    chars[2] = ALPHABET[(block[1] & 0x0F) << 4];
                }
                if (blockSize == 1) {
                    chars = new byte[2];
                    chars[0] = ALPHABET[(block[0] & 0xFC) >> 2];
                    chars[1] = ALPHABET[(block[0] & 0x03) << 4];
                } else if (blockSize == 3) {//仅仅为了容错;
                    chars[0] = ALPHABET[(block[0] & 0xFC) >> 2];
                    chars[1] = ALPHABET[(block[0] & 0x03) << 4 | ((block[1] & 0xF0) >> 4)];
                    chars[2] = ALPHABET[(block[1] & 0x0F) << 4 | ((block[2] & 0x03) >> 6)];
                    chars[3] = ALPHABET[(block[2] & 0x3F)];
                }
                out.write(chars);
            }
        }
    }

    static abstract class AbstractBlockOutputStream extends FilterOutputStream {
        protected int EN_SIZE = 2;
        protected int DATA_SIZE = 1;
        protected byte[] block;
        protected int blockSize;
        private final byte[] singleByte = new byte[1];

        public AbstractBlockOutputStream(OutputStream out) {
            super(out);
            blockSize = 0;
        }

        @Override
        public void write(int b) throws IOException {
            singleByte[0] = (byte) b;
            this.write(singleByte);
        }

        @Override
        public abstract void flush() throws IOException;

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            byte[] res = new byte[(blockSize + len) * EN_SIZE / DATA_SIZE];
            int filled = 0;
            int consum = 0;
            //如果chunkSize不为0，则需要把上一轮的内容加入到这轮中进行计算。
            if (blockSize > 0) {
                consum = DATA_SIZE - blockSize;
                System.arraycopy(b, 0, block, blockSize, consum);
                doFillBlock(block, 0, res, filled);
                filled += EN_SIZE;
            }
            int i = off + consum;
            for (; i <= len - DATA_SIZE; i += DATA_SIZE) {
                doFillBlock(b, i, res, filled);
                filled += EN_SIZE;
            }
            if (i > len - DATA_SIZE) {
                blockSize = len - i;
                System.arraycopy(b, i, block, 0, blockSize);
            }
            out.write(res, 0, filled);
        }

        protected abstract void doFillBlock(byte[] block, int i, byte[] res, int filled);
    }

    static abstract class AbstractBlockInputStream extends FilterInputStream {
        protected int EN_SIZE = 2;
        protected int DATA_SIZE = 1;
        protected byte[] block;
        protected int blockSize;
        protected int decryptedBlockSize = 0;
        protected byte[] decryptedBlock;

        protected int fillDecrypted(byte[] b, int off, int len) {
            int filled = Math.min(b.length - off, len);
            filled = Math.min(filled - off, decryptedBlockSize);
            if (filled > 0) {
                System.arraycopy(decryptedBlock, decryptedBlock.length - decryptedBlockSize, b, off, filled);
            }
            decryptedBlockSize -= filled;
            return filled;
        }


        //        public int read(byte[] b,int off,int len)throws IOException{
//            int res=read2(b,off,len);
//            System.out.println("b.len="+b.length+" off="+off+" len="+len+" readlen="+res);
//            ArrayList l=new ArrayList();
//            for(int i=off;i<off+res;i++){
//                l.add(Integer.toHexString(b[i]&0xFF));
//            }
//            System.out.println(l);
//            return res;
//        }
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            //尝试从旧的数据中读取数据。
            int readFromLast = fillDecrypted(b, off, len);
            int read = readFromLast;
            if (off >= b.length) {
                return off;
            }
            byte[] res = new byte[(len - readFromLast + DATA_SIZE - 1) / DATA_SIZE * EN_SIZE - blockSize];//确保需要这么多的内容。
            int rread = in.read(res);
            if (rread == -1) {
                if (blockSize == 0) {
                    return -1;
                } else {
                    return readTail(b, off + read);
                }
            }
            int consume = 0;
            //如果chunkSize不为0，则需要把上一轮的内容加入到这轮中进行计算。
            if (blockSize > 0) {
                consume = EN_SIZE - blockSize;
                System.arraycopy(res, 0, block, blockSize, consume);
                int r1 = fillBlock(b, off + read, len, block, 0);
                read += r1;
            }
            int i = consume;
            for (; i <= rread - EN_SIZE; i += EN_SIZE) {
                int r1 = fillBlock(b, off + read, len, res, i);
                read += r1;
            }
            if (i > rread - EN_SIZE) {
                blockSize = rread - i;
                System.arraycopy(res, i, block, 0, blockSize);
            }
            if (read <= len - DATA_SIZE && blockSize > 0) {//如果有足够的空间，尝试，直接把结果压到最后一组中去
                return read + readTail(b, off + read);
            }

            return read;
        }

        protected abstract int readTail(byte[] b, int i);

        private final byte[] SINGLE_BYTE = new byte[1];

        public AbstractBlockInputStream(InputStream in) {
            super(in);
            blockSize = 0;
        }

        @Override
        public int read() throws IOException {
            int read = read(SINGLE_BYTE);
            if (read <= 0) {//等0时是否要阻断
                return -1;
            }
            return 0xFF & SINGLE_BYTE[0];
        }

        protected int fillBlock(byte[] out, int outPos, int len, byte[] in, int inPos) {
            boolean toLastBlock = false;
            byte[] realOut = out;
            int realOutPos = outPos;
            int outCap = Math.min(out.length, len) - outPos;
            if (outCap < DATA_SIZE) {
                toLastBlock = true;
                decryptedBlockSize = DATA_SIZE - out.length + outPos;
                out = new byte[DATA_SIZE];
                outPos = 0;
                decryptedBlock = out;
            }
            int realRead = doFillBlock(out, outPos, in, inPos);
            int readed = Math.min(realRead, outCap);
            decryptedBlockSize = DATA_SIZE - readed;
            if (toLastBlock) {
                System.arraycopy(out, 0, realOut, realOutPos, readed);
            }
            return readed;
        }

        protected abstract int doFillBlock(byte[] out, int outPos, byte[] in, int inPos);
    }

    public static class HexInputStream extends AbstractBlockInputStream {
        private static final byte[] HEX_DE = { // 用于加速解密的cache
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 16
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 32
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, // 48
                0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, //64
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 80
                0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 96

        public HexInputStream(InputStream in) {
            super(in);
            block = new byte[2];
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            //可尝试双倍读取in 的内容，做解压缩
            byte[] buff = new byte[2 * len];
            int rread = in.read(buff);
            if (rread < 0) {
                return rread;
            }
            // 剩余的字符应该加入到洗一次read中去
            int read = 0;
            for (int i = 0; i < rread; i++) {
                byte by = buff[i];
                if (by < '0') { //空格 \r \n \t
                    continue;
                }
                if (blockSize++ == 0) {
                    block[0] = HEX_DE[by];
                } else {
                    byte v = (byte) (block[0] << 4 | HEX_DE[by]);
                    b[off + read++] = v;
                    blockSize = 0;
                }
            }
            return read;
        }

        @Override
        protected int doFillBlock(byte[] out, int outPos, byte[] in, int inPos) {
            return 1;
        }

        @Override
        protected int readTail(byte[] b, int i) {
            return -1;
        }
    }

    public static class HexOutputStream extends AbstractBlockOutputStream {
        public HexOutputStream(OutputStream out) {
            super(out);
        }

        private static final byte[] HEX_EN_BYTE = { // 用于加速加密的cache
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                'F'};

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            byte[] hex = new byte[len * 2];
            for (int i = 0; i < len; i++) {
                int inte = b[i + off] & 0xff;
                hex[i * 2] = HEX_EN_BYTE[inte >> 4];
                hex[i * 2 + 1] = HEX_EN_BYTE[inte & 0x0f];
            }
            out.write(hex);
        }

        @Override
        protected void doFillBlock(byte[] block, int i, byte[] res, int filled) {
        }

        @Override
        public void flush() throws IOException {
        }
    }

    /**
     * 该方法不计入encoding.
     * 1 先过 zip 的流
     * 2 转接 加密流
     * 3 转接 present的流
     *
     * @param is
     * @param os
     */

    protected void encrypt(InputStream is, OutputStream os) {
        OutputStream pos;
        switch (builder.present) {
            case AbstractCryptBuilder.PRESENT_HEX:
                pos = new HexOutputStream(os);
                break;
            case AbstractCryptBuilder.PRESENT_BASE32:
                pos = new Base32OutputStream(os);
                break;
            case AbstractCryptBuilder.PRESENT_BASE64:
                pos = new Base64OutputStream(os);
                break;
            case AbstractCryptBuilder.PRESENT_BASE64_URLSAFE:
                pos = new Base64UrlsafeOutputStream(os);
                break;
            default:
                pos = os;
        }
//        InputStream zis;
//        if(builder.gzip){
//            zos=new ZipOutputStream(is);
//        }else{
//            zos=os;
//        }
        doEncrypt(is, pos);
//        ZipOutputStream(new CipherOutputStream(presentOS ,cipher));

    }

    protected abstract void doEncrypt(InputStream is, OutputStream os);

    protected abstract void doDecrypt(InputStream is, OutputStream os);

    protected void decrypt(InputStream is, OutputStream os) {
        InputStream pis;
        switch (builder.present) {
            case AbstractCryptBuilder.PRESENT_HEX:
                pis = new HexInputStream(is);
                break;
            case AbstractCryptBuilder.PRESENT_BASE32:
                pis = new Base32InputStream(is);
                break;
            case AbstractCryptBuilder.PRESENT_BASE64:
            case AbstractCryptBuilder.PRESENT_BASE64_URLSAFE:
                pis = new Base64InputStream(is);
                break;
            default:
                pis = is;
        }
        doDecrypt(pis, os);
    }

    protected String encrypt(String src) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(src.getBytes(builder.encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.encrypt(bais, baos);
        return new String(baos.toByteArray());
    }

    protected String decrypt(String src) {
        ByteArrayInputStream bais = new ByteArrayInputStream(src.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.decrypt(bais, baos);
        try {
            return new String(baos.toByteArray(), builder.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractCryptor(AbstractCryptBuilder builder) {
        this.builder = builder;
    }

    protected AbstractCryptBuilder builder;


}
