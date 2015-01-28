package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;

import me.hkgumbs.just_write.C;
import me.hkgumbs.just_write.R;

public class Capture implements MyMenuAction {

    private final static int NOTIFICATION_ID = 88;

    @Override
    public void execute(final Activity activity, final ViewPager pager) {

        // find view
        int p = pager.getCurrentItem();
        String position = p == 0 ? "" : Integer.toString(p);
        View current = pager.findViewWithTag("TAG" + position);
        TextView content = (TextView) current.findViewById(R.id.text);

        // CREATE BITMAP FROM SCREEN CAPTURE
        content.setCursorVisible(false);
        pager.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(pager.getDrawingCache());
        pager.setDrawingCacheEnabled(false);
        content.setCursorVisible(true);

        new AsyncTask<Bitmap, Void, String>() {
            String name;
            String root;
            File file;

            @Override
            protected String doInBackground(Bitmap... b) {

                // image naming scheme
                name = new Timestamp(new java.util.Date().getTime()).toString()
                        + ".jpg";
                root = Environment.getExternalStorageDirectory().toString()
                        + "/Just Write/";
                File dir = new File(root);
                dir.mkdirs();
                file = new File(dir, name);
                if (file.exists())
                    file.delete();

                // save file to device
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    b[0].compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    return sw.toString();
                }

                return C.SUCCESS_MESSAGE;
            }

            @Override
            protected void onPostExecute(String result) {

                // error case
                if (result != C.SUCCESS_MESSAGE) {
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.alert_title)
                            .setMessage(R.string.alert_desc).create().show();
                    // TODO use error string to send email
                    return;
                }

                // refresh gallery
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                activity.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                // create notification
                Uri uri = Uri.parse("file://" + root + name);

                Intent viewIntent = new Intent();
                viewIntent.setAction(Intent.ACTION_VIEW);
                viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                viewIntent.setDataAndType(uri, "image/*");
                PendingIntent viewPending = PendingIntent.getActivity(activity,
                        0, viewIntent, PendingIntent.FLAG_ONE_SHOT);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/jpeg");
                Intent i = Intent.createChooser(shareIntent, "Choose action");
                PendingIntent sharePending = PendingIntent.getActivity(
                        activity, 0, i, PendingIntent.FLAG_ONE_SHOT);

                String title = activity.getString(R.string.notification_title);
                String desc = activity.getString(R.string.notification_desc);
                Bitmap icon = BitmapFactory.decodeResource(
                        activity.getResources(),
                        android.R.drawable.ic_menu_gallery);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        activity)
                        .setContentTitle(title)
                        .setTicker(title)
                        .setContentText(desc)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setAutoCancel(true)
                        .setLargeIcon(icon)
                        .setContentIntent(viewPending)
                        .addAction(android.R.drawable.ic_menu_share, "Share",
                                sharePending);

                NotificationManager nm = (NotificationManager) activity
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(NOTIFICATION_ID); // remove old notification
                nm.notify(NOTIFICATION_ID, builder.build());

            }

        }.execute(bitmap);
    }

}
