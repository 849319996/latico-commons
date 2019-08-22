package com.latico.commons.common.util.collections;

import java.util.BitSet;

/**
 * <PRE>
 * 位集，每个元素是一个二进制位
 * 里面用了一个long数组存储数据：{@link BitSet#words}
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-20 14:14
 * @Version: 1.0
 */
@SuppressWarnings("JavadocReference")
public class BitSetUtils {

    /**
     * 创建一个位集
     * @param initialBitSize 初始的bit大小
     * @return
     */
    public static BitSet createBitSet(int initialBitSize) {
        BitSet bitSet = new BitSet(initialBitSize);
        return bitSet;
    }

    /**
     * 创建一个位集，默认是1一个long元素，里面64位
     * @return
     */
    public static BitSet createBitSet() {
        BitSet bitSet = new BitSet();
        return bitSet;
    }
}
