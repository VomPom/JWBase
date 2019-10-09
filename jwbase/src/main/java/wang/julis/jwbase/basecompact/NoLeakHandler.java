package wang.julis.jwbase.basecompact;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/*******************************************************
 *
 * Created by julis.wang@beibei.com on 2019/10/08 18:41
 *
 * Description :
 * History   :
 *
 *******************************************************/

public class NoLeakHandler extends Handler {
    private WeakReference<MessageHandler> mMsgHandler;

    public NoLeakHandler(MessageHandler msgHandler) {
        super(Looper.getMainLooper());
        mMsgHandler = new WeakReference<MessageHandler>(msgHandler);
    }

    @Override
    public void handleMessage(Message msg) {
        MessageHandler realHandler = mMsgHandler.get();
        if (realHandler != null) {
            realHandler.handleMessage(msg);
        }
    }

    public interface MessageHandler {
        void handleMessage(Message msg);
    }
}
