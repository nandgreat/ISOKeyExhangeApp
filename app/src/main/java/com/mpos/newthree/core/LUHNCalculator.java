package com.mpos.newthree.core;

public interface LUHNCalculator {
    boolean verify(String pan) throws InvalidCardException;
    char calculate(String pan) throws InvalidCardException;
}
