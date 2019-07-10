package com.latico.commons.net.cmdclient;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import org.junit.Test;

public class CmdClientConfigTest {

    @Test
    public void getInstance() {
        LogUtils.loadLogBackConfigDefault();
        CmdClientConfig.getInstance();
    }

    @Test
    public void loadConfig() {
        System.out.println(CmdClientConfig.getInstance().initOrRefreshConfig());
    }

    @Test
    public void reloadConfig() {
        CmdClient cmdClient = CmdClientFactory.getCmdClient(CmdClientTypeEnum.SSH);
        cmdClient.close();
//        System.out.println(System.getProperty("os.arch"));
//        System.out.println(SystemUtils.isRunByTomcat());
    }

    @Test
    public void ping() {
        CmdClient cmdClient = CmdClientFactory.getCmdClient(CmdClientTypeEnum.SSH);
        try {
            boolean isLoginSucc = cmdClient.login("172.168.10.7", 22, "laticosoft", "laticoosft");
            if (isLoginSucc) {
                String receiveData = cmdClient.execCmdAndReceiveData("ping www.baidu.com -c 5");
                System.out.println(receiveData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cmdClient.close();
        }

    }
    @Test
    public void ping2() {
        CmdClient cmdClient = CmdClientFactory.getCmdClient(CmdClientTypeEnum.Telnet);
        if (cmdClient.login("10.24.89.13", 23, "admin", "admin")) {
            cmdClient.execCmdAndReceiveData("ping 192.168.100.100");
        }
        cmdClient.close();
    }

    @Test
    public void checkHasFailInfo() {
        String str = "\n" +
                "\n" +
                "Login authentication\n" +
                "\n" +
                "\n" +
                "Username:adminadmin\n" +
                "Password:******\n" +
                "Info: The max number of VTY users is 10, and the number\n" +
                "      of current VTY users on line is 1.\n" +
                "      The current login time is 2018-12-29 18:02:44.\n" +
                "<AR14>sys\n" +
                "Enter system view, return user view with Ctrl+Z.\n" +
                "[AR14]    interface GigabitEthernet0/0/4\n" +
                "[AR14-GigabitEthernet0/0/4]     description TEST20181212144259A\n" +
                "[AR14-GigabitEthernet0/0/4]     undo shutdown\n" +
                "Info: Interface GigabitEthernet0/0/4 is not shutdown.\n" +
                "[AR14-GigabvaliditEthernet0/0/4]    interface GigabitEthernet0/0/4.1111\n" +
                "[AR14-GigabiUnrecognized cmandtEthernet0/0/4.1111]     description TEST20181212144259A\n" +
                "[AR14-GigabitEthernet0/0/4.1111]     vlan-type dot1q 1111\n" +
                "[AR14-GigabitEthernet0/0/4.1111]     mpls static-l2vc destination 10.25.7.40 30000028 transmit-vpn-label 999 receive-vpn-label 999 control-word tagged      mpls static-l2vc destination 10.25.7.40 300 \u001B[1D00028 transmit-vpn-label 999 receive-vpn-label 999 control-word tagged \n" +
                "[AR14-GigabitEthernet0/0/4.1111]     mpls static-l2vc destination 10.25.7.40 40000027 transmit-vpn-label 999 receive-vpn-label 999 control-word tagged  secondary      mpls static-l2vc destination 10.25.7.40 400 \u001B[1D00027 transmit-vpn-label 999 receive-vpn-label 999 control-word tagged  secondar \u001B[1Dy \n" +
                "nother SVC to 10.25.7.40 has used 999 as transmit VPN label.\n" +
                "[AR14-GigabitEthernet0/0/4.1111]    quit\n" +
                "[AR14]quit\n" +
                "<AR14>save\n" +
                "The current configuration will be written to the device.\n" +
                "Are you sure to continue?[Y/N]y\n" +
                "Now saving the current configuration to the slot 17.\n" +
                "Save the configuration successfully.\n" +
                "<AR14>";

        System.out.println(CmdClientConfig.getInstance().checkHasFailInfo(str, "jajg"));

    }

    @Test
    public void getDevPageBreakCmds() {
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("rc", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("huawei", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("hw", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("zte", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("zx", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("FH", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("fEnghu", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("FENGHUO", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("fiberhome", null));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("cisco", null));
    }
    @Test
    public void getEnableModel() {
        System.out.println(CmdClientConfig.getInstance().getEnableModel("rc", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("huawei", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("hw", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("zte", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("zx", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("FH", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("fEnghu", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("FENGHUO", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("fiberhome", null));
        System.out.println(CmdClientConfig.getInstance().getEnableModel("cisco", null));
    }

    @Test
    public void getDevDefaultUserPwd() {
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("rc", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("huawei", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("hw", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("zte", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("zx", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("FH", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("fEnghu", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("FENGHUO", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("fiberhome", null));
        System.out.println(CmdClientConfig.getInstance().getDevDefaultUserPwd("cisco", null));
    }

    @Test
    public void getDevDefaultUserPwd2() {
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("fiberhome", "CiTRANS R8000-5"));
        System.out.println(CmdClientConfig.getInstance().getDevPageBreakCmd("fh", "CiTRANS R8000-10"));
    }
}