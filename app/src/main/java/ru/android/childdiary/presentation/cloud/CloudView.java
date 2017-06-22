package ru.android.childdiary.presentation.cloud;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;

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
    void connectionUnavailable(@NonNull CloudOperation cloudOperation);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showBackupLoading(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void foundBackup();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void failedToCheckBackupAvailability();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showBackupRestoring(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void backupRestored();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void failedToRestoreBackup();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMain();
}
