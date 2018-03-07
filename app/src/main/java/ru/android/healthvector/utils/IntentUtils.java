package ru.android.healthvector.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import ru.android.healthvector.R;

public class IntentUtils {
    private static void startIntent(Context context, Intent intent, String message) {
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            context.startActivity(intent);
        }
    }

    public static void startWebBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startIntent(context, intent, context.getString(R.string.web_browser_not_available));
    }

    public static void startEmailClient(Context context, String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        startIntent(context, intent, context.getString(R.string.email_client_not_available));
    }
}
