package top.iscore.freereader.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import bolts.Continuation;
import bolts.Task;
import top.iscore.freereader.R;

/**
 * 分享消息到微信
 *
 * @author xiaw
 * @date 2017/4/5 0005
 */

public class WXIntentUtils {


    /**
     * 微信分享 页面
     * 发送到聊天界面——WXSceneSession
     * 发送到朋友圈——WXSceneTimeline
     * 添加到微信收藏——WXSceneFavorite
     */
    public static final int WXSceneSession = SendMessageToWX.Req.WXSceneSession;
    public static final int WXSceneTimeline = SendMessageToWX.Req.WXSceneTimeline;
    public static final int WXSceneFavorite = SendMessageToWX.Req.WXSceneFavorite;


    public static final int THUMB_SIZE = 100;


    public static final int WXShareTypeText = 1;
    public static final int WXShareTypeImage = 2;
    public static final int WXShareTypeVoice = 3;
    public static final int WXShareTypeWebPage = 4;
    public static final int WXShareTypeMessages = 5;

    @IntDef({WXSceneSession, WXSceneTimeline, WXSceneFavorite})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WXScene {
    }


    /**
     * 分享聊天记录到 微信
     *
     * @param activity
     * @param scene
     * @param type
     * @param params   WZShareTypeText param[0] 为文本内容
     */
    public static void share(final Activity activity, final @WXScene int scene, int type, Object... params) {

        IWXAPI api = WXAPIFactory.createWXAPI(activity, WXEntryActivity.APP_ID);
        if (!api.isWXAppInstalled()) {
            ToastUtils.showShort("没有安装微信，无法启动微信");
            return;
        }

        if (type == WXShareTypeText) {
            shareText(activity, String.valueOf(params[0]), scene);
        } else if (type == WXShareTypeImage) {
            shareImage(activity, String.valueOf(params[0]), scene);
        } else if (type == WXShareTypeVoice) {
            shareMusic(activity, String.valueOf(params[0]), String.valueOf(params[1]), String.valueOf(params[2]), scene);
        } else if (type == WXShareTypeWebPage) {
            shareWebPage(activity, String.valueOf(params[0]), String.valueOf(params[1]), String.valueOf(params[2]), scene);
        }


    }

    /**
     * 分享文本
     *
     * @param activity
     * @param text
     * @param scene
     */
    public static void shareText(Activity activity, String text, @WXScene int scene) {

        IWXAPI api = WXAPIFactory.createWXAPI(activity, WXEntryActivity.APP_ID);
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // msg.title = "Will be ignored";
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = WXIntentUtils.buildTransaction("text"); //
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }


    /**
     * 分享文本 只支持本地路径
     *
     * @param activity
     * @param path
     * @param scene
     */
    public static void shareImage(Activity activity, String path, @WXScene int scene) {

//
//        IWXAPI api = WXAPIFactory.createWXAPI(activity, WXEntryActivity.APP_ID);
//        File file = new File(path);
//        WXImageObject imgObj = new WXImageObject();
//        imgObj.setImagePath(path);
//
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = imgObj;
//
//        //Bitmap bmp = BitmapFactory.decodeFile(path);
//        Bitmap thumbBmp = BitmapU.decodeSampledBitmapFromFile(file, THUMB_SIZE, THUMB_SIZE);
//        //Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//        //bmp.recycle();
//        msg.thumbData = bmpToByteArray(thumbBmp, true);
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("img");
//        req.message = msg;
//        req.scene = scene;
//        api.sendReq(req);
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static void shareWebPage(Activity activity, String url, String title, String description, @WXScene int scene) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, WXEntryActivity.APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    /**
     * 分享音频文件， 只支持网络连接
     *
     * @param activity
     * @param url
     * @param title
     * @param description
     * @param scene
     */
    public static void shareMusic(Activity activity, String url, String title, String description, @WXScene int scene) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, WXEntryActivity.APP_ID);
        WXMusicObject music = new WXMusicObject();
        //music.musicUrl = "http://www.baidu.com";
        music.musicUrl = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
        //music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;
        //Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.c_icon_voice);
        // Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        //bmp.recycle();
        //msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}




