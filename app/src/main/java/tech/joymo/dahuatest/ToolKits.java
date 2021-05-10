package tech.joymo.dahuatest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.company.NetSDK.INetSDK;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by 29779 on 2017/4/10.
 */
public class ToolKits {
    // 将byte字节解析后，从byte数组的第7位开始保存
    public static byte[] getByteArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    // 将byte字节解析后，从byte数组的第0位开始保存
    public static byte[] getByteArrayEx(byte b) {
        byte[] array = new byte[8];
        for (int i = 0; i < 8; i++) {
            array[i] = (byte) ((b & (1 << i)) > 0 ? 1 : 0);
        }

        return array;
    }

    // 数组拷贝
    public static void StringToByteArray(String src, byte[] dst) {
        for (int i = 0; i < dst.length; i++) {
            dst[i] = 0;
        }

        // 字符串src的长度 小于 dst的长度，拷贝长度按 字符串src的长度
        // 字符串src的长度 不小于 dst的长度，拷贝长度按 dst的长度 - 1
        if (src.getBytes().length < dst.length) {
            System.arraycopy(src.getBytes(), 0, dst, 0, src.getBytes().length);
        } else {
            System.arraycopy(src.getBytes(), 0, dst, 0, dst.length - 1);
        }
    }

    ///from char[] to byte[]
    ///char数组转为byte数组
    public static byte[] getBytes(char[] chars) {
        Charset charset = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = charset.encode(cb);
        return bb.array();
    }

    // 获取错误码, 错误码参考  FinalVar.java
    public static String getLastError() {
        return "FinalVar.java 对应的错误码 ：[0x80000000|" + (INetSDK.GetLastError() & 0x7fffffff) + "]" + "\n" +
                "十六进制错误码 ：[" + String.format("%x", INetSDK.GetLastError()) + "]";
    }

    /**
     * 进度框
     *
     * @param context
     * @param message        提示信息
     * @param bln            是否可以取消
     * @param cancelListener 取消监听
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String message, boolean bln, DialogInterface.OnCancelListener cancelListener) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, null, message, false, bln, cancelListener);
        return mProgressDialog;
    }
}