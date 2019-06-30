package com.latico.commons.designpattern.creational.factory.abstractfactory.cat.impl.black;

import com.latico.commons.designpattern.creational.factory.abstractfactory.cat.Cat;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-14 16:07
 * @Version: 1.0
 */
public class KaDingBlackCatImpl implements Cat {
    @Override
    public void eat() {
        System.out.println("黑色卡丁猫-吃东西");
    }
}
