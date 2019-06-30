package com.latico.commons.designpattern.behavioral.visitor.element;

import com.latico.commons.designpattern.behavioral.visitor.visitor.BillVisitor;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-20 0:33
 * @Version: 1.0
 */
public interface BillElement {

    void accept(BillVisitor visitor);
}
