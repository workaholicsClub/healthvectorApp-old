package ru.android.childdiary.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;

import javax.inject.Inject;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.settings.SettingsInteractor;
import ru.android.childdiary.services.core.BaseService;

public class AccountService extends BaseService {
    @Inject
    SettingsInteractor settingsInteractor;

    private final OnAccountsUpdateListener accountsUpdateListener = new OnAccountsUpdateListener() {
        @Override
        public void onAccountsUpdated(Account[] accounts) {
            // called on ui thread
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

    private static Intent getServiceIntent(Context context) {
        return new Intent(context, AccountService.class);
    }

    public static void startService(Context context) {
        Intent intent = getServiceIntent(context);
        context.startService(intent);
    }

    @Nullable
    @Override
    protected IBinder getBinder() {
        return null;
    }

    @Override
    protected void onCreate(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
        accountManager = AccountManager.get(this);
        accountManager.addOnAccountsUpdatedListener(accountsUpdateListener, new Handler(), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accountManager.removeOnAccountsUpdatedListener(accountsUpdateListener);
    }

    @Override
    protected void handleIntent(@Nullable Intent intent) {
    }
}
