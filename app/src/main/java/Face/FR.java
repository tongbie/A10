package Face;

import android.graphics.Rect;
import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;

/* 人脸识别 */
public class FR {
    private AFR_FSDKEngine engine;
    private AFR_FSDKError error;

    public FR() {
        engine = new AFR_FSDKEngine();
        //初始化人脸识别引擎，使用时请替换申请的APPID 和SDKKEY
        error = engine.AFR_FSDK_InitialEngine(FaceDB.APP_Id, FaceDB.FR_Key);
//        Log.d("com.arcsoft", "AFR_FSDK_InitialEngine = " + error.getCode());
    }

    /* 获取人脸特征信息（字节流数据，宽，高，脸框矩阵，角度 */
    public AFR_FSDKFace getFace(byte[] bytes, int width, int height, Rect rect, int drgree) {
        AFR_FSDKFace face = new AFR_FSDKFace();
        engine.AFR_FSDK_ExtractFRFeature(bytes, width, height, AFR_FSDKEngine.CP_PAF_NV21, rect, drgree, face);
        return face;
    }

    /* 获取相似度 */
    public float getSimilarity(AFR_FSDKFace face1, AFR_FSDKFace face2){
        AFR_FSDKMatching score = new AFR_FSDKMatching();
        engine.AFR_FSDK_FacePairMatching(face1, face2, score);
        return score.getScore();
    }


    public void process(byte[] data1, byte[] data2, int width, int height) {
        //用来存放提取到的人脸信息, face_1是注册的人脸，face_2是要识别的人脸
        AFR_FSDKFace face1 = new AFR_FSDKFace();
        AFR_FSDKFace face2 = new AFR_FSDKFace();
//        输入的data数据为NV21格式（如Camera里NV21格式的preview数据）；人脸坐标一般使用人脸检测返回的Rect传入；人脸角度请按照人脸检测引擎返回的值传入。
        error = engine.AFR_FSDK_ExtractFRFeature(data1, width, height, AFR_FSDKEngine.CP_PAF_NV21, new Rect(210, 178, 478, 446), AFR_FSDKEngine.AFR_FOC_0, face1);
//        Log.d("com.arcsoft", "Face=" + face1.getFeatureData()[0] + "," + face1.getFeatureData()[1] + "," + face1.getFeatureData()[2] + "," + error.getCode());
        error = engine.AFR_FSDK_ExtractFRFeature(data2, width, height, AFR_FSDKEngine.CP_PAF_NV21, new Rect(210, 170, 470, 440), AFR_FSDKEngine.AFR_FOC_0, face2);
//        Log.d("com.arcsoft", "Face=" + face2.getFeatureData()[0] + "," + face2.getFeatureData()[1] + "," + face2.getFeatureData()[2] + "," + error.getCode());
        //score用于存放人脸对比的相似度值
        AFR_FSDKMatching score = new AFR_FSDKMatching();
        error = engine.AFR_FSDK_FacePairMatching(face1, face2, score);
//        Log.d("com.arcsoft", "AFR_FSDK_FacePairMatching=" + error.getCode());
        Log.d("com.arcsoft", "Score:" + score.getScore());

        //销毁人脸识别引擎
        error = engine.AFR_FSDK_UninitialEngine();
//        Log.d("com.arcsoft", "AFR_FSDK_UninitialEngine : " + error.getCode());
    }
}
