package com.latico.commons.disruptor.pc.impl;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.lmax.disruptor.ExceptionHandler;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-05-22 16:56
 * @Version: 1.0
 */
public class ExceptionHandlerDefaultImpl <Arg> implements ExceptionHandler<OneArgEventDefault<Arg>> {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerDefaultImpl.class);

    @Override
    public void handleEventException(Throwable ex, long sequence, OneArgEventDefault<Arg> event) {
        LOG.error(event, ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        LOG.error(ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        LOG.error(ex);
    }
}
