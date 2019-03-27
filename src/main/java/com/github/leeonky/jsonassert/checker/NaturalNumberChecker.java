package com.github.leeonky.jsonassert.checker;

import java.math.BigInteger;

public class NaturalNumberChecker extends AbstractChecker {

    public NaturalNumberChecker() {
        super(Number.class);
    }

    @Override
    protected boolean isValueMatched(Object actualValue) {
        try {
            return new BigInteger(actualValue.toString()).compareTo(BigInteger.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
