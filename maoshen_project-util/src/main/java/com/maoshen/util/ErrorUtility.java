package com.maoshen.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class ErrorUtility {
    /**
     * Validate an argument, throwing ErrorException if the argument Collection
     * has null elements or is null.
     * 
     * @param collection the collection to check
     */
    public static void hasNull(Collection collection) throws IllegalStepException {
        notNull(collection);

        int i = 0;

        for (Iterator iter = collection.iterator(); iter.hasNext(); i++) {
            if (iter.next() == null) {
                throw new IllegalStepException("The validated collection contains null element at index: " + i);
            }
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument Collection
     * has null elements or is null.
     * 
     * @param collection the collection to check
     * @param message the exception message if the collection has null elements
     */
    public static void hasNull(Collection collection, String message) throws IllegalStepException {
        notNull(collection);

        for (Iterator it = collection.iterator(); it.hasNext();) {
            if (it.next() == null) {
                throw new IllegalStepException(message);
            }
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument array has
     * null elements or is null.
     * 
     * @param array the array to check
     */
    public static void hasNull(Object[] array) throws IllegalStepException {
        notNull(array);

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalStepException("The validated array contains null element at index: " + i);
            }
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument Collection
     * has null elements or is null.
     * 
     * @param array the collection to check
     * @param message the exception message if the collection has null elements
     */
    public static void hasNull(Object[] array, String message) throws IllegalStepException {
        notNull(array);

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalStepException(message);
            }
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the test result is true.
     * 
     * @param expression a boolean expression
     */
    public static void isTrue(boolean expression) throws IllegalStepException {
        if (expression) {
            throw new IllegalStepException("The validated expression is true");
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the test result is true.
     * 
     * @param expression a boolean expression
     * @param message the exception message you would like to see if the
     * expression is false
     */
    public static void isTrue(boolean expression, String message) throws IllegalStepException {
        if (expression) {
            throw new IllegalStepException(message);
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument Collection
     * is empty (null or no elements).
     * 
     * @param collection the collection to check is not empty
     */
    public static void notEmpty(Collection collection) throws IllegalStepException {
        if (collection == null || collection.size() == 0) {
            throw new IllegalStepException("The validated collection is empty");
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument Collection
     * is empty (null or no elements).
     * 
     * @param collection the collection to check is not empty
     * @param message the exception message you would like to see if the
     * collection is empty
     */
    public static void notEmpty(Collection collection, String message) throws IllegalStepException {
        if (collection == null || collection.size() == 0) {
            throw new IllegalStepException(message);
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument Map is
     * empty (null or no elements).
     * 
     * @param map the map to check is not empty
     */
    public static void notEmpty(Map map) throws IllegalStepException {
        if (map == null || map.size() == 0) {
            throw new IllegalStepException("The validated map is empty");
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument Map is
     * empty (null or no elements).
     * 
     * @param map the map to check is not empty
     * @param message the exception message you would like to see if the map is
     * empty
     */
    public static void notEmpty(Map map, String message) throws IllegalStepException {
        if (map == null || map.size() == 0) {
            throw new IllegalStepException(message);
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument array is
     * empty (null or no elements).
     * 
     * @param array the array to check is not empty
     */
    public static void notEmpty(Object[] array) throws IllegalStepException {
        if (array == null || array.length == 0) {
            throw new IllegalStepException("The validated array is empty");
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument array is
     * empty (null or no elements).
     * 
     * @param array the array to check is not empty
     * @param message the exception message you would like to see if the array
     * is empty
     */
    public static void notEmpty(Object[] array, String message) throws IllegalStepException {
        if (array == null || array.length == 0) {
            throw new IllegalStepException(message);
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument String is
     * empty (null or zero length).
     * 
     * @param string the string to check is not empty
     */
    public static void notEmpty(String string) throws IllegalStepException {
        if (string == null || string.length() == 0) {
            throw new IllegalStepException("The validated string is empty");
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument String is
     * empty (null or zero length).
     * 
     * @param string the string to check is not empty
     * @param message the exception message you would like to see if the string
     * is empty
     */
    public static void notEmpty(String string, String message) throws IllegalStepException {
        if (string == null || string.length() == 0) {
            throw new IllegalStepException(message);
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument is null.
     * 
     * @param object the object to check is not null
     */
    public static void notNull(Object object) throws IllegalStepException {
        if (object == null) {
            throw new IllegalStepException("The validated object is null");
        }
    }

    /**
     * Validate an argument, throwing ErrorException if the argument is null.
     * 
     * @param object the object to check is not null
     * @param message the exception message you would like to see if the object
     * is null
     */
    public static void notNull(Object object, String message) throws IllegalStepException {
        if (object == null) {
            throw new IllegalStepException(message);
        }
    }
}
