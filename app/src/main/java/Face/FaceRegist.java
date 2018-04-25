package Face;

import com.arcsoft.facerecognition.AFR_FSDKFace;

/**
 * Created by BieTong on 2018/4/9.
 */

public class FaceRegist {
    private String name;
    private AFR_FSDKFace face;

    public FaceRegist(String name) {
        this.name = name;
    }

    public FaceRegist(String name, AFR_FSDKFace face) {
        this.name = name;
        this.face = face;
    }

    public AFR_FSDKFace getFace(){
        return face;
    }
}
