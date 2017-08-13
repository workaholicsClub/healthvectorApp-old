package ru.android.childdiary.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import javax.inject.Inject;

import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.settings.SettingsInteractor;

public class AccountService extends Service {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @Inject
    SettingsInteractor settingsInteractor;

    private final OnAccountsUpdateListener accountsUpdateListener = new OnAccountsUpdateListener() {
        @Override
        public void onAccountsUpdated(Account[] accounts) {
            // TODO: called on ui thread
            logger.debug("onAccountsUpdated: " + Arrays.toString(accounts));

            if (accounts == null) {
                settingsInteractor.removeAccount();
                return;
            }

            String accountName = settingsInteractor.getAccountNameOnce().blockingFirst();

            if (TextUtils.isEmpty(accountName)) {
                return;
            }

            boolean accountExists = false;

            for (Account account : accounts) {
                if (accountName.equals(account.name)) {
                    accountExists = true;
                    break;
                }
            }

            if (!accountExists) {
                settingsInteractor.removeAccount();
            }
        }
    };

    private AccountManager accountManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.debug("onStartCommand: " + intent);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        logger.debug("onBind: " + intent);
        return null;
    }

    @Override
    public void onCreate() {
        logger.debug("onCreate");

        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        component.inject(this);

        accountManager = AccountManager.get(this);
        accountManager.addOnAccountsUpdatedListener(accountsUpdateListener, new Handler(), true);
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy");

        accountManager.removeOnAccountsUpdatedListener(accountsUpdateListener);
    }
}
