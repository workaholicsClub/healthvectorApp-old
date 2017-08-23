package ru.android.childdiary.presentation.help.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.utils.HtmlUtils;
import ru.android.childdiary.utils.IntentUtils;

public class ContactUsActivity extends BaseMvpActivity implements HtmlUtils.OnLinkClickListener {
    @BindView(R.id.textViewSite)
    TextView textViewSite;

    @BindView(R.id.textViewEmail)
    TextView textViewEmail;

    private String site, email;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        return new Intent(context, ContactUsActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        site = getString(R.string.site);
        String siteStr = getString(R.string.link_format, site, site);
        email = getString(R.string.email);
        String emailStr = getString(R.string.link_format, email, email);
        HtmlUtils.setupClickableLinks(textViewSite, siteStr, this);
        HtmlUtils.setupClickableLinks(textViewEmail, emailStr, this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.contact_us);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    public void onLinkClick(String url) {
        if (url.equals(site)) {
            IntentUtils.startWebBrowser(this, site);
        } else if (url.equals(email)) {
            IntentUtils.startEmailClient(this, email);
        }
    }
}
