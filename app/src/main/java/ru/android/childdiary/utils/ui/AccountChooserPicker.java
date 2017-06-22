package ru.android.childdiary.utils.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import io.reactivex.Observable;

public class AccountChooserPicker {
    private static final String ACCOUNT_TYPE = "com.google";

    private final Logger logger = LoggerFactory.getLogger(toString());
    private final Context context;
    private final AccountManager accountManager;

    @Inject
    public AccountChooserPicker(Context context) {
        this.context = context;
        this.accountManager = AccountManager.get(context);
    }

    public void show(AppCompatActivity activity, int requestCode, @Nullable String selectedAccountName) {
        // TODO: можно использовать другое API (например, GoogleAccountCredential.newChooseAccountIntent())
        // TODO: ИЛИ создать кастомный диалог, если поведение/дизайн этого не устраивают
        Account selectedAccount = Observable.fromArray(accountManager.getAccounts())
                .filter(account -> account.name.equals(selectedAccountName)).blockingFirst();
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent = AccountManager.newChooseAccountIntent(
                    selectedAccount, null, new String[]{ACCOUNT_TYPE}, null, null, null, null);
        } else {
            //noinspection deprecation
            intent = AccountManager.newChooseAccountIntent(
                    selectedAccount, null, new String[]{ACCOUNT_TYPE}, false, null, null, null, null);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    @Nullable
    private Account getSelectedAccount(@Nullable String selectedAccountName) {
        if (selectedAccountName == null) {
            return null;
        }
        for (Account account : accountManager.getAccounts()) {
            if (selectedAccountName.equals(account.name) && ACCOUNT_TYPE.equals(account.type)) {
                return account;
            }
        }
        return null;
    }
}
