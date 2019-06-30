package com.latico.commons.common.util.other;

import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void negate() {
    }

    @Test
    public void isTrue() {
    }

    @Test
    public void isNotTrue() {
    }

    @Test
    public void isFalse() {
    }

    @Test
    public void isNotFalse() {
    }

    @Test
    public void toBoolean() {
    }

    @Test
    public void toBooleanDefaultIfNull() {
    }

    @Test
    public void toBoolean1() {
    }

    @Test
    public void toBooleanObject() {
    }

    @Test
    public void toBooleanObject1() {
    }

    @Test
    public void toBoolean2() {
    }

    @Test
    public void toBoolean3() {
    }

    @Test
    public void toBooleanObject2() {
    }

    @Test
    public void toBooleanObject3() {
    }

    @Test
    public void toInteger() {
    }

    @Test
    public void xor() {
        System.out.println(BooleanUtils.bothTrueAndFalse(new Boolean(true), true));
        System.out.println(BooleanUtils.bothTrueAndFalse(true, false, true));
        System.out.println(BooleanUtils.bothTrueAndFalse(false, false, false));
    }
}