package com.maoshen.util.algorithm;

import java.util.Random;

public class BaseRandom extends Random {
	
	private static final long serialVersionUID = 1L;
	
	private boolean constructed = false;

    public BaseRandom() {
        this.constructed = true;
    }

    @Override
	public synchronized void setSeed(long seed) {
        if (this.constructed) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
	public synchronized double nextGaussian() {
        throw new UnsupportedOperationException();
    }

    @Override
	public void nextBytes(byte[] byteArray) {
        throw new UnsupportedOperationException();
    }

    @Override
	public int nextInt() {
        return nextInt(Integer.MAX_VALUE);
    }

    @Override
	public int nextInt(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Upper bound for nextInt must be positive");
        }

        return (int) (Math.random() * n);
    }

    @Override
	public long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    public static long nextLong(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Upper bound for nextInt must be positive");
        }

        return (long) (Math.random() * n);
    }

    @Override
	public boolean nextBoolean() {
        return Math.random() > 0.5;
    }

    @Override
	public float nextFloat() {
        return (float) Math.random();
    }

    @Override
	public double nextDouble() {
        return Math.random();
    }
}
