package Face;

import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.example.a10.BusEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
