package tech.joymo.dahuatest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.company.NetSDK.CB_fSearchDevicesCB;
import com.company.NetSDK.DEVICE_NET_INFO_EX;
import com.company.NetSDK.INetSDK;
import com.company.NetSDK.NET_IN_MODIFY_IP;
import com.company.NetSDK.NET_OUT_MODIFY_IP;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetSDKLib.getInstance().init();

        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceSearchModule mDeviceSearchModule = new DeviceSearchModule(MainActivity.this);
                mDeviceSearchModule.startSearchDevices(callback);
            }
        });
    }

    private CB_fSearchDevicesCB callback = new CB_fSearchDevicesCB() {

        @Override
        public void invoke(DEVICE_NET_INFO_EX device_net_info_ex) {
            String temp = new String(device_net_info_ex.szIP).trim() +
                    " : " + new String(device_net_info_ex.szSerialNo).trim() +
                    "mac" + " : " + new String(device_net_info_ex.szMac).trim();
            Log.i(TAG, temp);
            if (new String(device_net_info_ex.szMac).trim().equals("bc:32:5f:7e:ae:40")) {
                ModifyDevice(device_net_info_ex);
            }
        }
    };

    public void ModifyDevice(DEVICE_NET_INFO_EX info) {
        NET_IN_MODIFY_IP net_in_modify_ip = new NET_IN_MODIFY_IP();
        net_in_modify_ip.stuDevNetInfo.stuDevInfo = info;
        net_in_modify_ip.stuDevNetInfo.stuDevInfo.bNewUserName = true;
        net_in_modify_ip.stuDevNetInfo.stuDevInfo.bNewWordLen = true;

        ToolKits.StringToByteArray("admin", net_in_modify_ip.stuDevNetInfo.stuDevInfo.szNewUserName);
        ToolKits.StringToByteArray("NB@iot123", net_in_modify_ip.stuDevNetInfo.stuDevInfo.szNewPassWord);
        net_in_modify_ip.stuDevNetInfo.stuDevInfo.bDhcpEn = false;

        ToolKits.StringToByteArray("192.168.1.109", net_in_modify_ip.stuDevNetInfo.stuDevInfo.szIP);
        ToolKits.StringToByteArray("255.255.255.0", net_in_modify_ip.stuDevNetInfo.stuDevInfo.szSubmask);
        ToolKits.StringToByteArray("192.168.1.1", net_in_modify_ip.stuDevNetInfo.stuDevInfo.szGateway);
        NET_OUT_MODIFY_IP modifyIPOut = new NET_OUT_MODIFY_IP();
        if (INetSDK.ModifyDeviceEx(net_in_modify_ip, modifyIPOut, 10000)) {
            Log.i(TAG, "ModifyDevice Succeed!");
        } else {
            Log.i(TAG, "ModifyDevice Failed!" + getLastError());
        }
    }

    public static String getLastError() {
        return "FinalVar.java 对应的错误码 ：[0x80000000|" + (INetSDK.GetLastError() & 0x7fffffff) + "]" + "\n" +
                "十六进制错误码 ：[" + String.format("%x", INetSDK.GetLastError()) + "]";
    }
}