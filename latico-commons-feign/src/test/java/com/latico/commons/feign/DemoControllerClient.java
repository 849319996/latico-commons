package com.latico.commons.feign;



import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-03-05 16:13
 * @Version: 1.0
 */
@Path("/demo")
public interface DemoControllerClient {

    @POST
    @Path("/serverTime")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    DemoTimeParam serverTime();


    @POST
    @Path("/serverTimeStr")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    String serverTimeStr();

    /**
     * 测试 RestRequestDTO
     * @return 字符串类型数据
     */
    @POST
    @Path("testRequestParam")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String testRequestParam(@QueryParam("name") String name);

    /**
     * 测试 PathVariable
     * @return 字符串类型数据
     */
    @POST
    @Path("testPathVariable/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String testPathVariable(@PathParam("name") String name) ;

    /**
     * 测试 PathVariable
     * @return 字符串类型数据
     */
    @POST
    @Path("/testMultiPathVariable/{name}/{value}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String testMultiPathVariable(@PathParam("name") String name, @PathParam("value") String value) ;
}
