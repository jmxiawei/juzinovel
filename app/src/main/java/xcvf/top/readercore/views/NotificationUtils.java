//package xcvf.top.readercore.views;
//
//import android.app.NotificationChannel;
//import android.app.NotificationChannelGroup;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.graphics.Color;
//import android.support.annotation.RequiresApi;
//
//public class NotificationUtils {
//
//
//    // 通知渠道组的id.
//    private static String group_download_id = "group_download_id";
//    //用户可见的通知渠道组名称
//    private static String group_download_name = "My group_download_name01";
//
//    /**
//     * 创建通知渠道
//     * @param channel_id 渠道id
//     * @param channel_name 渠道名称
//     * @param channel_desc 渠道描述
//     * @param importance 渠道优先级
//     * @param group_id 渠道组，若没有渠道组，则传null
//     */
//    @RequiresApi(api = 26)
//    private static void createNotificationChannel(Context context, String channel_id, String channel_name, String channel_desc, int importance, String group_id) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        //配置通知渠道id,渠道名称（用户可以看到），渠道优先级
//        NotificationChannel mChannel = new NotificationChannel(channel_id, channel_name, importance);
//        //配置通知渠道的描述
//        mChannel.setDescription(channel_desc);
//        //配置通知出现时的闪灯（如果 android 设备支持的话）
//        mChannel.enableLights(true);
//        mChannel.setLightColor(Color.RED);
//        //配置通知出现时的震动（如果 android 设备支持的话）
//        mChannel.enableVibration(true);
//        mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
//        //配置渠道组
//        if (group_id != null) {
//            mChannel.setGroup(group_id);//设置渠道组
//        }
//        //在NotificationManager中创建该通知渠道
//        manager.createNotificationChannel(mChannel);
//    }
//
//    @RequiresApi(api = 26)
//    private static void createNotificationChannelGroup(Context context) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannelGroup(new NotificationChannelGroup(group_download_id, group_download_name));
//    }
//
//
//    /**
//     * 带有确定进度条的通知
//     */
//    @RequiresApi(api = 26)
//    private void showProgressNotification02() {
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        //创建并打包PendingIntent
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setAction(TEST_ACTION_03);
//        intent.putExtra(EXTRA_NOTIFICATION_ID_03, NOTIFICATION_ID_03);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        createNotificationChannelGroup();//创建渠道组
//        createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, "渠道描述", NotificationManager.IMPORTANCE_HIGH, group_id);
//
//        mNotificationBuilder03 = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
//                .setContentTitle("带有进度条的通知")
//                .setContentText("正在等待下载")
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.logo)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
//                .setContentIntent(pendingIntent)
//                .setProgress(100, 0, false)
//                .setAutoCancel(true)
//                .addAction(R.mipmap.ic_launcher, "关闭", pendingIntent);
//
//        mNotificationManager.notify(NOTIFICATION_ID_03, mNotificationBuilder03.build());
//    }
//
//}
