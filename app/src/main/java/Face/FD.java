package Face;

import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;

import java.util.ArrayList;
import java.util.List;

/* 人脸检测 */
public class FD {

    public List<AFD_FSDKFace> process(byte[] data, int width, int height) {
        AFD_FSDKEngine engine = new AFD_FSDKEngine();
        List<AFD_FSDKFace> result = new ArrayList<AFD_FSDKFace>();// 用来存放检测到的人脸信息列表
        //初始化人脸检测引擎，使用时请替换申请的APPID和SDKKEY
        AFD_FSDKError error = engine.AFD_FSDK_InitialFaceEngine(FaceDB.APP_Id,
                FaceDB.FD_Key,
                AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT,
                16,
                5);
//        Log.e("com.arcsoft", "AFD_FSDK_InitialFaceEngine = " + error.getCode());
        //输入的data数据为NV21格式（如Camera里NV21格式的preview数据），其中height不能为奇数，人脸检测返回结果保存在result。
        error = engine.AFD_FSDK_StillImageFaceDetection(data, width, height, AFD_FSDKEngine.CP_PAF_NV21, result);
//        Log.e("com.arcsoft", "AFD_FSDK_StillImageFaceDetection =" + error.getCode());
//        Log.e("人脸数量", "Face=" + result.size());
        for (AFD_FSDKFace face : result) {
//            Log.e("com.arcsoft", "Face:" + face.toString());
//            Log.e("result",result.toString());
        }
        //销毁人脸检测引擎
        error = engine.AFD_FSDK_UninitialFaceEngine();
//        Log.e("com.arcsoft", "AFD_FSDK_UninitialFaceEngine =" + error.getCode());
        return result;
    }
}
