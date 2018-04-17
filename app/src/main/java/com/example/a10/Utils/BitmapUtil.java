package com.example.a10.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by BieTong on 2018/4/12.
 */

public class BitmapUtil {

    /* 压缩bitmap，并将高度裁剪为偶数 */
    public static Bitmap cutBitmap(Context context, Bitmap bitmap) {
        try {
            Bitmap newBitmap = null;
            Matrix matrix = new Matrix();
            matrix.setScale(1f, 1f);
            int width = bitmap.getWidth();
            if (width >= 1000 && width < 2000) {
                matrix.setScale(0.5f, 0.5f);
            } else if (width >= 2000 && width < 4000) {
                matrix.setScale(0.25f, 0.25f);
            } else if (width >= 4000) {
                matrix.setScale(0.1f, 0.1f);
            }
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);

            if (newBitmap.getHeight() % 2 != 0) {
                newBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() - 1, bitmap.getHeight(), true);
            }
            return newBitmap;
        } catch (Exception e) {
            ToastUtil.show(context, e.getMessage());
            return null;
        }
    }

    /* 将NV21格式的bitmap转为NV21格式的字节流 */
    public static byte[] bitmapToNV21Bytes(Bitmap bitmap) {
        byte[] bitmapByte = BitmapUtil.getNV21(bitmap.getWidth(), bitmap.getHeight(), bitmap);
        return bitmapByte;
    }

    /* bitmap转NV21格式 */
    private static byte[] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {
        int[] argb = new int[inputWidth * inputHeight];
        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);
        byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];
        encodeYUV420SP(yuv, argb, inputWidth, inputHeight);
        scaled.recycle();
        return yuv;
    }

    /* 此方法图像过大会ArrayIndexOutOfBoundsException
     * 过小也会ArrayIndexOutOfBoundsException */
    private static void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;
        int yIndex = 0;
        int uvIndex = frameSize;
        int a, R, G, B, Y, U, V;
        int index = 0;
        try {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
                    R = (argb[index] & 0xff0000) >> 16;
                    G = (argb[index] & 0xff00) >> 8;
                    B = (argb[index] & 0xff) >> 0;
                    // well known RGB to YUV algorithm
                    Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                    U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                    V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;
                    // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                    //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                    //    pixel AND every other scanline.
                    yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                    if (j % 2 == 0 && index % 2 == 0) {
                        yuv420sp[uvIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
                        yuv420sp[uvIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
                    }
                    index++;
                }
            }
        }catch (Exception e){
//            DialogActivity.setText("图像识别错误，请重新尝试");
//            ToolUtil.getActivity().startActivity(new Intent(ToolUtil.getActivity(), DialogActivity.class));
        }
    }
}
