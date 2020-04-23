package com.latico.commons.common.util.collections;


import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Collection;

/**
 * <PRE>
 * 布隆过滤器
 *      也可以使用Google的guava包中的（但是里面使用了@Beta注解，所以还说建议使用本类吧），在测试用例里面有使用示例
 *      一个高效节省内存的去重算法，用于存储数据量大的需要经常判断是否有数据重复
 *      该类支持方法很多，但是常用的就几个方法：创建布隆过滤器、添加元素、判断元素是否存在
 *      该算法允许用户输入允许可以允许判断失误的概率。
 *  不支持读取、删除、更新等操作，只用来存储和判断是否存在。
 *
 * example:
 * 下面例子，大概会有0.3的概率产生误判，所以
 BloomFilter<String> bloomFilter = new BloomFilter<String>(0.3, 10);
 int i = 1;
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 bloomFilter.add("abc" + i++);
 i = 1;
 System.out.println(bloomFilter.contains("abc" + i++));
 System.out.println(bloomFilter.contains("abc" + i++));
 System.out.println(bloomFilter.contains("abc" + i++));
 System.out.println(bloomFilter.contains("abc" + i++));
 System.out.println(bloomFilter.contains("abc" + i++));
 System.out.println("===========================");
 i = 1;
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));
 System.out.println(bloomFilter.contains("bcd" + i++));

 bloomFilter.close();

 * @param <E> 元素类型
 * </PRE>
 * @author: latico
 * @date: 2018/12/16 03:04:59
 * @version: 1.0
 */
public class BloomFilter<E> implements Serializable {

    private static final long serialVersionUID = 1L;
    private BitSet bitset;
    private int bitSetSize;
    /**
     * 预期设计上给每个元素多少个位
     */
    private double bitsPerElement;
    /**
     * expected (maximum) number of elements to be added，预期要增加元素的最大数量
     */
    private int expectedNumberOfFilterElements;
    /**
     * number of elements actually added to the Bloom filter，已经进入set中元素的个数
     */
    private int numberOfAddedElements;
    /**
     * // number of hash functions，哈希函数个数，使用MD5压缩算法得出的哈希值长度为k的整形数组
     */
    private int k;

    /**
     * // encoding used for storing hash values as strings
     */
    static final Charset charset = Charset.forName("UTF-8");
    /**
     * 使用MD5计算hash，MD5 gives good enough accuracy in most circumstances. Change to SHA1 if it's needed
     */
    static final String hashName = "MD5";
    /**
     * // MD5消息摘要对象
     */
    static final MessageDigest digestFunction;

    static {
        // The digest method is reused between instances
        MessageDigest tmp;
        try {
            tmp = MessageDigest.getInstance(hashName);
        } catch (NoSuchAlgorithmException e) {
            tmp = null;
        }
        digestFunction = tmp;
    }

    /**
     * Constructs an empty Bloom filter. The total length of the Bloom filter
     * will be c*n.
     *
     * @param c is the number of bits used per element.
     * @param n is the expected number of elements the filter will contain.
     * @param k is the number of hash functions used.
     */
    public BloomFilter(double c, int n, int k) {
        this.expectedNumberOfFilterElements = n;
        this.k = k;
        this.bitsPerElement = c;
        this.bitSetSize = (int) Math.ceil(c * n);
        numberOfAddedElements = 0;
        this.bitset = new BitSet(bitSetSize);
    }

    /**
     * Constructs an empty Bloom filter. The optimal number of hash functions
     * (k) is estimated from the total size of the Bloom and the number of
     * expected elements.
     *
     * @param bitSetSize              defines how many bits should be used in total for the filter.
     * @param expectedNumberOElements defines the maximum number of elements the filter is expected to contain.
     */
    public BloomFilter(int bitSetSize, int expectedNumberOElements) {
        this(bitSetSize / (double) expectedNumberOElements,
                expectedNumberOElements, (int) Math
                        .round((bitSetSize / (double) expectedNumberOElements)
                                * Math.log(2.0)));
    }

    /**
     * Constructs an empty Bloom filter with a given false positive probability.
     * The number of bits per element and the number of hash functions is
     * estimated to match the false positive probability.
     *
     * @param falsePositiveProbability 允许判断失误的概率，is the desired false positive probability.
     * @param expectedNumberOfElements 预估最大要存储元素的数量，is the expected number of elements in the Bloom filter.
     */
    public BloomFilter(double falsePositiveProbability,
                       int expectedNumberOfElements) {
        this(Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2)))
                        / Math.log(2), // c = k / ln(2)
                expectedNumberOfElements, (int) Math.ceil(-(Math
                        .log(falsePositiveProbability) / Math.log(2)))); // k = ceil(-log_2(false prob.))
    }

    /**
     * Construct a new Bloom filter based on existing Bloom filter data.
     *
     * @param bitSetSize                     defines how many bits should be used for the filter.
     * @param expectedNumberOfFilterElements defines the maximum number of elements the filter is expected to contain.
     * @param actualNumberOfFilterElements   specifies how many elements have been inserted into the <code>filterData</code> BitSet.
     * @param filterData                     a BitSet representing an existing Bloom filter.
     */
    public BloomFilter(int bitSetSize, int expectedNumberOfFilterElements,
                       int actualNumberOfFilterElements, BitSet filterData) {
        this(bitSetSize, expectedNumberOfFilterElements);
        this.bitset = filterData;
        this.numberOfAddedElements = actualNumberOfFilterElements;
    }

    /**
     * Generates a digest based on the contents of a String.
     *
     * @param val     specifies the input data.
     * @param charset specifies the encoding of the input data.
     * @return digest as long.
     */
    public static int createHash(String val, Charset charset) {
        return createHash(val.getBytes(charset));
    }

    /**
     * Generates a digest based on the contents of a String.
     *
     * @param val specifies the input data. The encoding is expected to be UTF-8.
     * @return digest as long.
     */
    public static int createHash(String val) {
        return createHash(val, charset);
    }

    /**
     * Generates a digest based on the contents of an array of bytes.
     *
     * @param data specifies input data.
     * @return digest as long.
     */
    public static int createHash(byte[] data) {
        return createHashes(data, 1)[0];
    }

    /**
     * Generates digests based on the contents of an array of bytes and splits
     * the result into 4-byte int's and store them in an array. The digest
     * function is called until the required number of int's are produced. For
     * each call to digest a salt is prepended to the data. The salt is
     * increased by 1 for each call.
     * 哈希值数组生成器，使用MD5的方法生成。
     *
     * @param data   specifies input data.
     * @param hashes number of hashes/int's to produce.
     * @return array of int-sized hashes
     */
    public static int[] createHashes(byte[] data, int hashes) {
        int[] result = new int[hashes];

        int k = 0;
        byte salt = 0;
        /*
         * 如果下面的MD5值生成的4个哈希值不够哈希函数的个数， 用一次新的salt值初始化下一个MD5继续获取新的哈希值
         */
        while (k < hashes) {
            byte[] digest;
            synchronized (digestFunction) {
                digestFunction.update(salt); // 用一个byte更新摘要，这里用作初始化
                salt++;
                /*
                 * 在这里可以进行最后更新，因为初始化的值不同，相同的data获取不同的MD5值，
                 * 使用MD5算法，digest为长度16的byte数组，就是128位
                 */
                digest = digestFunction.digest(data);
            }
            // 除于4是为了使128位的MD5值变成4段，每段加起来就是32位，也就是可以转化成int值
            for (int i = 0; i < digest.length / 4 && k < hashes; i++) {
                int h = 0; // 每个int值从0开始，以字节(8bit)长度左移，然后与下一个字节，这样循环4次
                // 遍历段，把段的4个byte转换成一个int值
                for (int j = (i * 4); j < (i * 4) + 4; j++) {
                    h <<= 8;
                    h |= ((int) digest[j]) & 0xFF;
                }
                result[k] = h;
                k++;
            }
        }
        return result;
    }

    /**
     * Compares the contents of two instances to see if they are equal.
     * 覆盖方法，为了与BloomFilter的参数结合起来，判断起来更快
     *
     * @param obj is the object to compare to.
     * @return True if the contents of the objects are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BloomFilter<E> other = (BloomFilter<E>) obj;
        if (this.expectedNumberOfFilterElements != other.expectedNumberOfFilterElements) {
            return false;
        }
        if (this.k != other.k) {
            return false;
        }
        if (this.bitSetSize != other.bitSetSize) {
            return false;
        }
        if (this.bitset != other.bitset
                && (this.bitset == null || !this.bitset.equals(other.bitset))) {
            return false;
        }
        return true;
    }

    /**
     * Calculates a hash code for this class.
     * 覆盖方法，为了与BloomFilter的参数结合起来而生成的哈希值与其有点关系
     * 虽然在此覆盖该方法作用不大，一般情况下，equals()和hashCode()方法是一起覆盖的
     *
     * @return hash code representing the contents of an instance of this class.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.bitset != null ? this.bitset.hashCode() : 0);
        hash = 61 * hash + this.expectedNumberOfFilterElements;
        hash = 61 * hash + this.bitSetSize;
        hash = 61 * hash + this.k;
        return hash;
    }

    /**
     * Calculates the expected probability of false positives based on the
     * number of expected filter elements and the size of the Bloom filter. <br />
     * <br />
     * The value returned by this method is the <i>expected</i> rate of false
     * positives, assuming the number of inserted elements equals the number of
     * expected elements. If the number of elements in the Bloom filter is less
     * than the expected value, the true probability of false positives will be lower.
     *
     * @return expected probability of false positives.
     */
    public double expectedFalsePositiveProbability() {
        return getFalsePositiveProbability(expectedNumberOfFilterElements);
    }

    /**
     * Calculate the probability of a false positive given the specified number
     * of inserted elements.
     *
     * @param numberOfElements number of inserted elements.
     * @return probability of a false positive.
     */
    public double getFalsePositiveProbability(double numberOfElements) {
        // (1 - e^(-k * n / m)) ^ k
        return Math.pow(
                (1 - Math.exp(-k * (double) numberOfElements
                        / (double) bitSetSize)), k);

    }

    /**
     * Get the current probability of a false positive. The probability is
     * calculated from the size of the Bloom filter and the current number of
     * elements added to it.
     *
     * @return probability of false positives.
     */
    public double getFalsePositiveProbability() {
        return getFalsePositiveProbability(numberOfAddedElements);
    }

    /**
     * Returns the value chosen for K.<br />
     * <br />
     * K is the optimal number of hash functions based on the size of the Bloom
     * filter and the expected number of inserted elements.
     *
     * @return optimal k.
     */
    public int getK() {
        return k;
    }

    /**
     * Sets all bits to false in the Bloom filter.
     */
    public void clear() {
        bitset.clear();
        numberOfAddedElements = 0;
    }

    /**
     * Adds an object to the Bloom filter. The output from the object's
     * toString() method is used as input to the hash functions.
     *
     * @param element is an element to register in the Bloom filter.
     */
    public void add(E element) {
        add(element.toString().getBytes(charset));
    }

    /**
     * Adds an array of bytes to the Bloom filter.
     *
     * @param bytes array of bytes to add to the Bloom filter.
     */
    public void add(byte[] bytes) {
        int[] hashes = createHashes(bytes, k);
        for (int hash : hashes) {
            bitset.set(Math.abs(hash % bitSetSize), true);
        }
        numberOfAddedElements++;
    }

    /**
     * Adds all elements from a Collection to the Bloom filter.
     *
     * @param c Collection of elements.
     */
    public void addAll(Collection<? extends E> c) {
        for (E element : c) {
            add(element);
        }
    }

    /**
     * Returns true if the element could have been inserted into the Bloom
     * filter. Use getFalsePositiveProbability() to calculate the probability of
     * this being correct.
     *
     * @param element element to check.
     * @return true if the element could have been inserted into the Bloom filter.
     */
    public boolean contains(E element) {
        return contains(element.toString().getBytes(charset));
    }

    /**
     * Returns true if the array of bytes could have been inserted into the
     * Bloom filter. Use getFalsePositiveProbability() to calculate the
     * probability of this being correct.
     *
     * @param bytes array of bytes to check.
     * @return true if the array could have been inserted into the Bloom filter.
     */
    public boolean contains(byte[] bytes) {
        int[] hashes = createHashes(bytes, k);
        for (int hash : hashes) {
            if (!bitset.get(Math.abs(hash % bitSetSize))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if all the elements of a Collection could have been inserted
     * into the Bloom filter. Use getFalsePositiveProbability() to calculate the
     * probability of this being correct.
     *
     * @param c elements to check.
     * @return true if all the elements in c could have been inserted into the  Bloom filter.
     */
    public boolean containsAll(Collection<? extends E> c) {
        for (E element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Read a single bit from the Bloom filter.
     *
     * @param bit the bit to read.
     * @return true if the bit is set, false if it is not.
     */
    public boolean getBit(int bit) {
        return bitset.get(bit);
    }

    /**
     * Set a single bit in the Bloom filter.
     *
     * @param bit   is the bit to set.
     * @param value If true, the bit is set. If false, the bit is cleared.
     */
    public void setBit(int bit, boolean value) {
        bitset.set(bit, value);
    }

    /**
     * Return the bit set used to store the Bloom filter.
     *
     * @return bit set representing the Bloom filter.
     */
    public BitSet getBitSet() {
        return bitset;
    }

    /**
     * Returns the number of bits in the Bloom filter. Use count() to retrieve
     * the number of inserted elements.
     *
     * @return the size of the bitset used by the Bloom filter.
     */
    public int size() {
        return this.bitSetSize;
    }

    /**
     * Returns the number of elements added to the Bloom filter after it was
     * constructed or after clear() was called.
     *
     * @return number of elements added to the Bloom filter.
     */
    public int count() {
        return this.numberOfAddedElements;
    }

    /**
     * Returns the expected number of elements to be inserted into the filter.
     * This value is the same value as the one passed to the constructor.
     *
     * @return expected number of elements.
     */
    public int getExpectedNumberOfElements() {
        return expectedNumberOfFilterElements;
    }

    /**
     * Get expected number of bits per element when the Bloom filter is full.
     * This value is set by the constructor when the Bloom filter is created.
     * See also getBitsPerElement().
     *
     * @return expected number of bits per element.
     */
    public double getExpectedBitsPerElement() {
        return this.bitsPerElement;
    }

    /**
     * Get actual number of bits per element based on the number of elements
     * that have currently been inserted and the length of the Bloom filter. See
     * also getExpectedBitsPerElement().
     *
     * @return number of bits per element.
     */
    public double getBitsPerElement() {
        return this.bitSetSize / (double) numberOfAddedElements;
    }

    /**
     * 关闭
     */
    public void close() {
        this.bitset = null;
    }
}