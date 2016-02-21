package com.maoshen.util.character;

public interface Serializable {
    byte[] serialize();
    void unserialize(byte[] ss);
}
