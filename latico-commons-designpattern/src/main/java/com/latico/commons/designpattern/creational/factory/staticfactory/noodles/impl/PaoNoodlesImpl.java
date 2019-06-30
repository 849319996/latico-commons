package com.latico.commons.designpattern.creational.factory.staticfactory.noodles.impl;

import com.latico.commons.designpattern.creational.factory.staticfactory.noodles.Noodles;

public class PaoNoodlesImpl implements Noodles {
    @Override
    public String desc() {
        return "泡面";
    }
}