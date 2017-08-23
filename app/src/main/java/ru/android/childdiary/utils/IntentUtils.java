package ru.android.childdiary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpActivity;

public class IntentUtils {
    private static final Logger logger = LoggerFactory.getLogger(IntentUtils.class);

    public static void startIntent(Context context, Intent intent, String message) {
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            context.startActivity(intent);
        }
    }

    public static void startWebBrowser(BaseMvpActivity context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            logger.debug("starting web browser");
            context.startActivity(intent);
        } else {
            logger.error("not found app to open intent: " + intent);
            context.showToast(context.getString(R.string.web_browser_not_available));
        }
    }

    public static void startEmailClient(BaseMvpActivity context, String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            logger.debug("starting email client");
            context.startActivity(intent);
        } else {
            logger.error("not found app to open intent: " + intent);
            context.showToast(context.getString(R.string.email_client_not_available));
        }
    }
}
