package com.rongji.dfish.base.crypt;

/**
 * Crockford's Base32
 * <p>(其实个人认为，留U去Z更好，因为Z和2比较容易混但U和V不容易混，见车牌号，但规范就是规范...)</p>
 * <table style="background-color: #f9f9f9; border: 1px #aaa solid; border-collapse: collapse; color: black;" style="width:80ex; text-align: center; margin: 0 auto 0 auto;">
<caption>Crockford's Base32 alphabet</caption>
<tr>
<th width="16%">Value</th>
<th width="16%">Encode Digit</th>
<th width="16%">Decode Digit</th>
<td></td>
<th width="16%">Value</th>
<th width="16%">Encode Digit</th>
<th width="16%">Decode Digit</th>
</tr>
<tr>
<td>0</td>
<td>0</td>
<td>0 o O</td>
<td></td>
<td>16</td>
<td>G</td>
<td>g G</td>
</tr>
<tr>
<td>1</td>
<td>1</td>
<td>1 i I l L</td>
<td></td>
<td>17</td>
<td>H</td>
<td>h H</td>
</tr>
<tr>
<td>2</td>
<td>2</td>
<td>2</td>
<td></td>
<td>18</td>
<td>J</td>
<td>j J</td>
</tr>
<tr>
<td>3</td>
<td>3</td>
<td>3</td>
<td></td>
<td>19</td>
<td>K</td>
<td>k K</td>
</tr>
<tr>
<td>4</td>
<td>4</td>
<td>4</td>
<td></td>
<td>20</td>
<td>M</td>
<td>m M</td>
</tr>
<tr>
<td>5</td>
<td>5</td>
<td>5</td>
<td></td>
<td>21</td>
<td>N</td>
<td>n N</td>
</tr>
<tr>
<td>6</td>
<td>6</td>
<td>6</td>
<td></td>
<td>22</td>
<td>P</td>
<td>p P</td>
</tr>
<tr>
<td>7</td>
<td>7</td>
<td>7</td>
<td></td>
<td>23</td>
<td>Q</td>
<td>q Q</td>
</tr>
<tr>
<td>8</td>
<td>8</td>
<td>8</td>
<td></td>
<td>24</td>
<td>R</td>
<td>r R</td>
</tr>
<tr>
<td>9</td>
<td>9</td>
<td>9</td>
<td></td>
<td>25</td>
<td>S</td>
<td>s S</td>
</tr>
<tr>
<td>10</td>
<td>A</td>
<td>a A</td>
<td></td>
<td>26</td>
<td>T</td>
<td>t T</td>
</tr>
<tr>
<td>11</td>
<td>B</td>
<td>b B</td>
<td></td>
<td>27</td>
<td>V</td>
<td>v V</td>
</tr>
<tr>
<td>12</td>
<td>C</td>
<td>c C</td>
<td></td>
<td>28</td>
<td>W</td>
<td>w W</td>
</tr>
<tr>
<td>13</td>
<td>D</td>
<td>d D</td>
<td></td>
<td>29</td>
<td>X</td>
<td>x X</td>
</tr>
<tr>
<td>14</td>
<td>E</td>
<td>e E</td>
<td></td>
<td>30</td>
<td>Y</td>
<td>y Y</td>
</tr>
<tr>
<td>15</td>
<td>F</td>
<td>f F</td>
<td></td>
<td>31</td>
<td>Z</td>
<td>z Z</td>
</tr>
</table>
 * @author LinLW
 * @deprecated 现在推荐流模式
 * @see AbstractCryptor.Base32OutputStream
 * @see AbstractCryptor.Base32InputStream
 */
@Deprecated
public class Base32 {
	/* Crockford's Base32 */
	
    private static final byte[] ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q',
            'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
    };
    
    private static final byte[] DECODE_TABLE={
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
    /**
     * 把原文转化成编码过的密文
     * @param data byte[]
     * @return byte[]
     */
    public static byte[] encode(byte[] data) {
        
    	byte[] chars = new byte[((data.length * 8) / 5) + ((data.length % 5) != 0 ? 1 : 0)];
        
        for (int i = 0, j = 0, index = 0; i < chars.length; i++) {
            if (index > 3) {
                int b = data[j] & (0xFF >> index);
                index = (index + 5) % 8;
                b <<= index;
                if (j < data.length - 1) {
                    b |= (data[j + 1] & 0xFF) >> (8 - index);
                }
                chars[i] = ALPHABET[b];
                j++;
            } else {
                chars[i] = ALPHABET[((data[j] >> (8 - (index + 5))) & 0x1F)];
                index = (index + 5) % 8;
                if (index == 0) {
                    j++;
                }
            }
        }
        
        return chars;
    }

    /**
     * 把密文解析成原文文
     * @param src byte[]
     * @return byte[]
     */
    public static byte[] decode(byte[] src)  {
        
//        char[] stringData = s.toCharArray();
        byte[] data = new byte[(src.length * 5) / 8];
        
        for (int i = 0, j = 0, index = 0; i < src.length; i++) {
            int val;
            
            try {
                val = DECODE_TABLE[src[i]];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException("Illegal character");
            }
            
            if (val == 0xFF) {
                throw new RuntimeException("Illegal character");
            }
            
            if (index <= 3) {
                index = (index + 5) % 8;
                if (index == 0) {
                    data[j++] |= val;
                }
                else {
                    data[j] |= val << (8 - index);
                }
            } else {
                index = (index + 5) % 8;
                data[j++] |= (val >> index);
                if (j < data.length) {
                    data[j] |= val << (8 - index);
                }
            }
        }
        
        return data;
    }
}
