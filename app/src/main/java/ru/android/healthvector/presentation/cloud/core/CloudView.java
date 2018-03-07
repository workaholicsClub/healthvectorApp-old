package ru.android.healthvector.presentation.cloud.core;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.presentation.core.BaseView;

public interface CloudView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void requestPermission();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void chooseAccount(@Nullable String selectedAccountName);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showPlayServicesErrorDialog(int connectionStatusCode);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void requestAuthorization(Intent intent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void connectionUnavailable();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void securityError();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showCheckBackupAvailabilityLoading(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void checkBackupAvailabilitySucceeded(boolean isBackupAvailable);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void failedToCheckBackupAvailability();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showRestoreLoading(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void restoreSucceeded();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void failedToRestore();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noBackupFound();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showBackupLoading(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void backupSucceeded();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void failedToBackup();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMain();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void restartApp();
}
