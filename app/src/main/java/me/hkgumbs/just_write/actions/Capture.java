package me.hkgumbs.just_write.actions;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;

import me.drakeet.materialdialog.MaterialDialog;
import me.hkgumbs.just_write.C;
import me.hkgumbs.just_write.R;

public class Capture extends MyMenuAction {

    private final static int NOTIFICATION_ID = 88;

    @Override
    public void execute(final Activity activity, final ViewPager pager) {

        // find textview
        String position = C.spKey(pager.getCurrentItem());
        View current = pager.findViewWithTag(position);
        TextView text = (TextView) current.findViewById(R.id.text);

        // hide keyboard
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);

        // create bitmap from screen capture
        text.setCursorVisible(false);
        pager.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(pager.getDrawingCache());
        pager.setDrawingCacheEnabled(false);
        text.setCursorVisible(true);

        new CaptureAsyncTask(activity, bitmap).execute();
    }

    private class CaptureAsyncTask extends AsyncTask<Void, Void, Exception> {

        final Activity activity;
        final Bitmap bitmap;

        String name;
        String root;
        File file;

        CaptureAsyncTask(Activity activity, Bitmap bitmap) {
            this.activity = activity;
            this.bitmap = bitmap;
        }

        @Override
        protected Exception doInBackground(Void... b) {
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                return e;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Exception e) {
            if (e != null) {
                // error case
                MaterialDialog dialog = new MaterialDialog(activity);
                View.OnClickListener listener = new DialogButtonListener(activity, dialog, e);
                dialog.setTitle(R.string.alert_title);
                dialog.setMessage(R.string.alert_desc);
                dialog.setPositiveButton(R.string.alert_pos, listener);
                dialog.setNegativeButton(R.string.alert_neg, listener);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

            } else {
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
                Intent i = Intent.createChooser(shareIntent, "Share");
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
        }

    }

    private class DialogButtonListener implements View.OnClickListener {
        Activity activity;
        MaterialDialog dialog;
        Exception e;

        DialogButtonListener(Activity activity, MaterialDialog dialog, Exception e) {
            this.activity = activity;
            this.dialog = dialog;
            this.e = e;
        }

        @Override
        public void onClick(View v) {
            String label = ((Button) v).getText().toString();
            String positive = activity.getString(R.string.alert_pos);
            if (label.equals(positive)) {
                String recipient = activity.getString(R.string.email);
                String subject = activity.getString(R.string.subject);
                String type = activity.getString(R.string.type);

                // get stacktrace as string
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String body = sw.toString();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType(type);
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                i.putExtra(Intent.EXTRA_TEXT, body);

                try {
                    activity.startActivity(Intent.createChooser(i, "Send email"));
                } catch (android.content.ActivityNotFoundException ex) {
                }
            }

            dialog.dismiss();
        }
    }

}
