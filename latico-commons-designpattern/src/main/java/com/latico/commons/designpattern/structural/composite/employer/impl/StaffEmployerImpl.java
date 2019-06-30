package com.latico.commons.designpattern.structural.composite.employer.impl;

import com.latico.commons.designpattern.structural.composite.employer.AbstractEmployer;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-14 23:26
 * @Version: 1.0
 */
public class StaffEmployerImpl extends AbstractEmployer {

    public StaffEmployerImpl() {
        setType("普通职员");
    }
    public StaffEmployerImpl(String name) {
        super(name);
        setType("普通职员");
    }
}
