package com.latico.commons.spring.util;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class SoapUtilsTest {

    /**
     * 
     */
    @Test
    public void testHttp(){
        String xmlData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.tmforum.org/mtop/fmw/xsd/hdr/v1\" xmlns:v11=\"http://www.tmforum.org/mtop/mri/xsd/tpr/v1\" xmlns:v12=\"http://www.tmforum.org/mtop/fmw/xsd/nam/v1\" xmlns:v13=\"http://www.tmforum.org/mtop/nrb/xsd/lay/v1\">\n" +
                "   <soapenv:Header>\n" +
                "      <v1:header>\n" +
                "         <v1:activityName>getContainedPotentialConnectionTerminationPoints</v1:activityName>\n" +
                "         <v1:msgName>getContainedPotentialConnectionTerminationPointsRequest</v1:msgName>\n" +
                "         <v1:msgType>REQUEST</v1:msgType>\n" +
                "         <v1:senderURI>/MTOSI/InventoryOS</v1:senderURI>\n" +
                "         <v1:destinationURI>/MTOSI/EmsOS</v1:destinationURI>\n" +
                "         <v1:security>admin:admin123</v1:security>\n" +
                "         <v1:communicationPattern>MultipleBatchResponse</v1:communicationPattern>\n" +
                "         <v1:communicationStyle>RPC</v1:communicationStyle>\n" +
                "         <v1:requestedBatchSize>1000</v1:requestedBatchSize>\n" +
                "         <v1:batchSequenceNumber>1</v1:batchSequenceNumber>\n" +
                "      </v1:header>\n" +
                "   </soapenv:Header>\n" +
                "   <soapenv:Body>\n" +
                "      <v11:getContainedPotentialConnectionTerminationPointsRequest>\n" +
                "         <v11:tpRef>\n" +
                "            <v12:rdn>\n" +
                "               <v12:type>MD</v12:type>\n" +
                "               <v12:value>1</v12:value>\n" +
                "            </v12:rdn>\n" +
                "            <v12:rdn>\n" +
                "               <v12:type>ME</v12:type>\n" +
                "               <v12:value>fe705e3a-fd5e-4c95-b193-16fe8594dfad</v12:value>\n" +
                "            </v12:rdn>\n" +
                "            <v12:rdn>\n" +
                "               <v12:type>PTP</v12:type>\n" +
                "               <v12:value>/rack=1/shelf=0/slot=14/port=1</v12:value>\n" +
                "            </v12:rdn>\n" +
                "         </v11:tpRef>\n" +
                "      </v11:getContainedPotentialConnectionTerminationPointsRequest>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        ResponseEntity<String> responseEntity = SoapUtils.call("http://61.183.207.99:8086/mtosi/TerminationPointRetrieval", "getContainedPotentialConnectionTerminationPoints", xmlData);
        System.out.println(responseEntity.getStatusCode());
        if (SoapUtils.isOkStatus(responseEntity)) {
            System.out.println("状态OK");
        }
        System.out.println(responseEntity.getBody());
    }

    /**
     *
     */
    @Test
    public void testHttps(){
        String xmlData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.tmforum.org/mtop/fmw/xsd/hdr/v1\" xmlns:v11=\"http://www.tmforum.org/mtop/mri/xsd/mer/v1\" xmlns:v12=\"http://www.tmforum.org/mtop/fmw/xsd/nam/v1\">\n" +
                "   <soapenv:Header>\n" +
                "      <v1:header>\n" +
                "         <v1:activityName>getAllManagedElements</v1:activityName>\n" +
                "         <v1:msgName>getAllManagedElementsRequest</v1:msgName>\n" +
                "         <v1:msgType>REQUEST</v1:msgType>\n" +
                "         <v1:senderURI>/MTOSI/InventoryOS</v1:senderURI>\n" +
                "         <v1:destinationURI>/MTOSI/EmsOS</v1:destinationURI>\n" +
                "         <v1:communicationPattern>MultipleBatchResponse</v1:communicationPattern>\n" +
                "         <v1:requestedBatchSize>10</v1:requestedBatchSize>\n" +
                "         <v1:communicationStyle>RPC</v1:communicationStyle>\n" +
                "         <v1:security>admin:Nbi@13579</v1:security>\n" +
                "      </v1:header>\n" +
                "   </soapenv:Header>\n" +
                "   <soapenv:Body>\n" +
                "      <v11:getAllManagedElementsRequest>\n" +
                "         <v11:mdOrMlsnRef>\n" +
                "            <v12:rdn>\n" +
                "               <v12:type>MD</v12:type>\n" +
                "               <v12:value>ZTE/UME(BN)</v12:value>\n" +
                "            </v12:rdn>\n" +
                "         </v11:mdOrMlsnRef>\n" +
                "      </v11:getAllManagedElementsRequest>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        ResponseEntity<String> responseEntity = SoapUtils.call("https://210.21.240.198:28001/mtosi2/services/ManagedElementRetrieval", "getAllManagedElements", xmlData);
        System.out.println(responseEntity.getStatusCode());
        if (SoapUtils.isOkStatus(responseEntity)) {
            System.out.println("状态OK");
        }
        System.out.println(responseEntity.getBody());
    }
}