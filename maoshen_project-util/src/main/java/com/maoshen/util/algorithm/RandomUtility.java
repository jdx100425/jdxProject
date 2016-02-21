package com.maoshen.util.algorithm;

import java.util.LinkedHashSet;
import java.util.Random;



public class RandomUtility {
    private static final Random JVM_RANDOM = new BaseRandom();

    /**
     * Returns the next pseudorandom, uniformly distributed int value from the
     * Math.random() sequence.
     * 
     * @return the random int
     */
    public static int nextInt() {
        return nextInt(JVM_RANDOM);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed int value from the
     * given random sequence.
     * 
     * @param random the Random sequence generator
     * @return the random int
     */
    public static int nextInt(Random random) {
        return random.nextInt();
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between 0
     * (inclusive) and the specified value (exclusive), from the Math.random()
     * sequence.
     * 
     * @param n the specified exclusive max-value
     * @return the random int
     */
    public static int nextInt(int n) {
        return nextInt(JVM_RANDOM, n);
    }

    /**
     * Returns a pseudorandom, uniformly distributed int value between 0
     * (inclusive) and the specified value (exclusive), from the given Random
     * sequence.
     * 
     * @param random the Random sequence generator
     * @param n the specified exclusive max-value
     * @return the random int
     */
    public static int nextInt(Random random, int n) {
        return random.nextInt(n);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed long value from the
     * Math.random() sequence.
     * 
     * @return the random long
     */
    public static long nextLong() {
        return nextLong(JVM_RANDOM);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed long value from the
     * given Random sequence.
     * 
     * @param random the Random sequence generator
     * @return the random long
     */
    public static long nextLong(Random random) {
        return random.nextLong();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed boolean value from
     * the Math.random() sequence.
     * 
     * @return the random boolean
     */
    public static boolean nextBoolean() {
        return nextBoolean(JVM_RANDOM);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed boolean value from
     * the given random sequence.
     * 
     * @param random the Random sequence generator
     * @return the random boolean
     */
    public static boolean nextBoolean(Random random) {
        return random.nextBoolean();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed float value between
     * 0.0 and 1.0 from the Math.random() sequence.
     * 
     * @return the random float
     */
    public static float nextFloat() {
        return nextFloat(JVM_RANDOM);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed float value between
     * 0.0 and 1.0 from the given Random sequence.
     * 
     * @param random the Random sequence generator
     * @return the random float
     */
    public static float nextFloat(Random random) {
        return random.nextFloat();
    }

    /**
     * Returns the next pseudorandom, uniformly distributed float value between
     * 0.0 and 1.0 from the Math.random() sequence.
     * 
     * @return the random double
     */
    public static double nextDouble() {
        return nextDouble(JVM_RANDOM);
    }

    /**
     * Returns the next pseudorandom, uniformly distributed float value between
     * 0.0 and 1.0 from the given Random sequence.
     * 
     * @param random the Random sequence generator
     * @return the random double
     */
    public static double nextDouble(Random random) {
        return random.nextDouble();
    }

    @SuppressWarnings("rawtypes")
	public static LinkedHashSet nextRange(int n) {
        return nextRange(JVM_RANDOM, n);
    }

    @SuppressWarnings("rawtypes")
	public static LinkedHashSet nextRange(Random random, int n) {
        LinkedHashSet<Integer> container = new LinkedHashSet<Integer>();
        while (true) {
            if (container.size() >= n) {
                break;
            }
            container.add(nextInt(n));
        }
        return container;
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     * 
     * Characters will be chosen from the set of characters whose ASCII value is
     * between 32 and 126 (inclusive).
     * 
     * @param count the length of random string to create
     * @return the random string
     */
    public static String nextAscii(int count) {
        return nextChar(count, 32, 127, false, false, null, JVM_RANDOM);
    }

    /**
     * Creates a random string whose length is the number of characters
     * specified.
     * 
     * Characters will be chosen from the set of characters whose ASCII value is
     * between 32 and 126 (inclusive).
     * 
     * @param count the length of random string to create
     * @param random a source of randomness
     * @return the random string
     */
    public static String nextAscii(int count, Random random) {
        return nextChar(count, 32, 127, false, false, null, random);
    }

    /**
     * Creates a random string based on a variety of options, using supplied
     * source of randomness.
     * 
     * @param count the length of random string to create
     * @param letters only allow letters?
     * @param numbers only allow numbers?
     * @return the random string
     */
    public static String nextChar(int count, boolean letters, boolean numbers) {
        return nextChar(count, 0, 0, letters, numbers, null, JVM_RANDOM);
    }

    /**
     * Creates a random string based on a variety of options, using supplied
     * source of randomness.
     * 
     * @param count the length of random string to create
     * @param start the position in set of chars to start at
     * @param end the position in set of chars to end before
     * @param letters only allow letters?
     * @param numbers only allow numbers?
     * @param chars the set of chars to choose randoms from If null, then it
     * will use the set of all chars
     * @param random a source of randomness
     * @return the random string
     */
    public static String nextChar(int count, int start, int end, boolean letters, boolean numbers,
            char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count
                    + " is less than 0");
        }
        if ((start == 0) && (end == 0)) {
            end = 'z' + 1;
            start = ' ';
            if (!letters && !numbers) {
                start = 0;
                end = Integer.MAX_VALUE;
            }
        }
        char[] buffer = new char[count];
        int gap = end - start;
        while (count-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(gap) + start];
            }
            if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch))
                    || (!letters && !numbers)) {
                if (ch >= 56320 && ch <= 57343) {
                    if (count == 0) {
                        count++;
                    } else {
                        buffer[count] = ch;
                        count--;
                        buffer[count] = (char) (55296 + random.nextInt(128));
                    }
                } else if (ch >= 55296 && ch <= 56191) {
                    if (count == 0) {
                        count++;
                    } else {
                        buffer[count] = (char) (56320 + random.nextInt(128));
                        count--;
                        buffer[count] = ch;
                    }
                } else if (ch >= 56192 && ch <= 56319) {
                    count++;
                } else {
                    buffer[count] = ch;
                }
            } else {
                count++;
            }
        }
        return new String(buffer);
    }
}