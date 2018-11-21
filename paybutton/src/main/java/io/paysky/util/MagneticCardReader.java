package io.paysky.util;

import android.device.MagManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MagneticCardReader {

    private final static int MESSAGE_CHECK_FAILE = 2;
    public final static int MESSAGE_READ_MAG = 3;
    public final static String CARD_TRACK_DATA = "card_data";

    private Handler mHandler;
    private MagManager magManager;
    private MagReaderThread magReaderThread;
    private static final int DEFAULT_TAG = 1;

    public MagneticCardReader() {
        magManager = new MagManager();
    }

    // 从字节数组到十六进制字符串转换
    public static String Bytes2HexString(byte[] b) {
        StringBuilder ret = new StringBuilder();

        String hex = "";
        for (byte aB : b) {
            hex = Integer.toHexString(aB & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret.append(hex.toUpperCase());
        }

        return ret.toString();
    }

    public void start(Handler magneticHandler) {
        if (magReaderThread != null) {
            magReaderThread.stopMagReader();
            magReaderThread = null;
        }
        mHandler = magneticHandler;
        magReaderThread = new MagReaderThread("reader--" + DEFAULT_TAG);
        magReaderThread.start();
    }

    public void stop() {
        if (magManager != null) {
            magManager.close();
        }
        if (magReaderThread != null) {
            magReaderThread.stopMagReader();
            magReaderThread = null;
        }
        mHandler = null;
    }

    private class MagReaderThread extends Thread {
        private boolean running = true;

        MagReaderThread(String name) {
            super(name);
            running = true;
        }

        void stopMagReader() {
            running = false;
        }

        public void run() {
            if (magManager != null) {
                magManager.open();
            }
            while (running) {
                int ret = 0;
                if (magManager != null) {
                    ret = magManager.checkCard();
                }
                if (ret != 0) {
                    continue;
                }
                byte[] stripInfo = new byte[2056];
                int allLen = magManager != null ? magManager.getAllStripInfo(stripInfo) : 0;

                TrackData trackData = new TrackData();

                if (allLen > 0) {
                    int len = stripInfo[1];
                    if (len != 0) {
                        trackData.track1 = new String(stripInfo, 2, len);
                        String[] track1Data = trackData.track1.split("^");
                        if (track1Data.length > 1) {
                            trackData.cardHolderName = track1Data[1];
                        }
                    }
                    int len2 = stripInfo[3 + len];
                    if (len2 != 0) {
                        trackData.track2 = new String(stripInfo, 4 + len, len2);
                    } else {
                        continue;
                    }
                    int len3 = stripInfo[5 + len + len2];

                    if (len3 != 0 && len3 < 1024) {
                        String track3 = new String(stripInfo, 6 + len + len2, len3);
                        Log.d("track3", track3);
                    }

                    String[] track2Spilt = trackData.track2.split("=");
                    trackData.cardNumber = track2Spilt[0];
                    trackData.expiryDate = track2Spilt[1].substring(0, 4);
                    //    mHandler.removeMessages(MESSAGE_CHECK_FAILE);
                    Message msg = mHandler.obtainMessage(MESSAGE_READ_MAG);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CARD_TRACK_DATA, trackData);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }

        }
    }
}
