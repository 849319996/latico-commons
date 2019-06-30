package com.latico.commons.hessian.common.api;

import com.latico.commons.hessian.common.UrlPath;
import com.latico.commons.hessian.common.bean.BeanExample;
/**
 * <PRE>
 * @UrlPath("/hessian") 需要跟web.xml中servlet配置的一致
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-01-09 22:27:43
 * @Version: 1.0
 */
@UrlPath("/hessian")
public interface HelloService {

    String helloWorld(String message);

    BeanExample getMyInfo(BeanExample user);

}