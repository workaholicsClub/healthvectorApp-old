package ru.android.childdiary.presentation.core.images;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;
import ru.android.childdiary.presentation.core.RequestPermissionInfo;
import ru.android.childdiary.presentation.core.images.adapters.ImagePickerAction;
import ru.android.childdiary.presentation.core.images.adapters.ImagePickerActionAdapter;
import ru.android.childdiary.presentation.core.images.adapters.ImagePickerActionType;
import ru.android.childdiary.presentation.core.images.crop.CropActivity;
import ru.android.childdiary.utils.ui.WidgetsUtils;

import static android.app.Activity.RESULT_OK;

public class ImagePickerDialogFragment extends BaseMvpDialogFragment<ImagePickerDialogArguments>
        implements AdapterView.OnItemClickListener, ImagePickerView {
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;

    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_CAPTURE_IMAGE = 2;
    private static final int REQUEST_CROP_IMAGE = 3;

    @InjectPresenter
    ImagePickerPresenter presenter;

    @State
    Uri capturedImageFileUri;

    @Nullable
    private Listener listener;

    @Override
    @LayoutRes
    protected int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected void setupUi() {
    }

    @NonNull
    @Override
    protected Dialog createDialog(View view) {
        Dialog dialog = new Dialog(getContext(), getTheme());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        List<ImagePickerAction> actions = new ArrayList<>();
        actions.add(ImagePickerAction.builder()
                .type(ImagePickerActionType.CAPTURE)
                .titleResourceId(R.string.action_capture_image)
                .iconResourceId(R.drawable.image_picker_action_capture_image)
                .build());
        actions.add(ImagePickerAction.builder()
                .type(ImagePickerActionType.PICK)
                .titleResourceId(R.string.action_pick_image)
                .iconResourceId(R.drawable.image_picker_action_pick_image)
                .build());
        if (dialogArguments.isShowDeleteItem()) {
            actions.add(ImagePickerAction.builder()
                    .type(ImagePickerActionType.DELETE)
                    .titleResourceId(R.string.action_delete_image)
                    .iconResourceId(R.drawable.image_picker_action_delete_image)
                    .build());
        }

        ListView listView = new ListView(getContext());
        ArrayAdapter adapter = new ImagePickerActionAdapter(getContext(), actions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        dialog.setContentView(listView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImagePickerAction action = ((ImagePickerActionAdapter) parent.getAdapter()).getItem(position);
        switch (action.getType()) {
            case CAPTURE:
                presenter.startCamera();
                break;
            case PICK:
                requestPermissionAndPickImage();
                break;
            case DELETE:
                dismiss();
                if (listener != null) {
                    listener.onSetImage(null);
                }
                break;
        }
    }

    private void requestPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            RequestPermissionInfo permissionInfo = RequestPermissionInfo.builder()
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .requestCode(REQUEST_STORAGE_READ_ACCESS_PERMISSION)
                    .titleResourceId(R.string.request_read_external_storage_permission_title)
                    .textResourceId(R.string.request_read_external_storage_permission_text)
                    .build();
            requestPermission(permissionInfo);
        } else {
            pickImage();
        }
    }

    @Override
    protected void permissionGranted(RequestPermissionInfo permissionInfo) {
        if (permissionInfo.getRequestCode() == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            pickImage();
        }
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), REQUEST_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                final Uri selectedImageFileUri = data.getData();
                if (selectedImageFileUri == null) {
                    showToast(getString(R.string.failed_to_select_image));
                    return;
                }

                presenter.startCrop(selectedImageFileUri);
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
            revokePermissions(capturedImageFileUri);

            if (resultCode == RESULT_OK) {
                presenter.startCrop(capturedImageFileUri);
            }
        } else if (requestCode == REQUEST_CROP_IMAGE) {
            if (resultCode == RESULT_OK) {
                handleCropResult(data);
            } else if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            }
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            presenter.createTemporaryImageFile(resultUri);
        } else {
            logger.error("handleCropResult: resultUri is null");
            showToast(getString(R.string.failed_to_crop_image));
        }
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            logger.error("handleCropError", cropError);
            showToast(getString(R.string.failed_to_crop_image));
        } else {
            logger.error("handleCropError: cropError is null");
            showToast(getString(R.string.failed_to_crop_image));
        }
    }

    @Override
    public void setResultImageFile(@NonNull String relativePath) {
        dismiss();
        if (listener != null) {
            listener.onSetImage(relativePath);
        }
    }

    @Override
    public void failedToCreateResultImageFile() {
        showToast(getString(R.string.failed_to_save_result_file));
    }

    @Override
    public void startCrop(@NonNull Uri sourceUri, @NonNull File destinationFile) {
        UCrop uCrop = UCrop.of(sourceUri, Uri.fromFile(destinationFile));

        uCrop = uCrop.withAspectRatio(1, 1);

        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);
        options.setCropGridColumnCount(0);
        options.setCropGridRowCount(0);
        options.setShowCropFrame(false);

        options.setCircleDimmedLayer(dialogArguments.isShowCircleFrame());
        WidgetsUtils.setupCropActivityToolbar(getContext(), options, dialogArguments.getSex());

        uCrop.withOptions(options);

        Intent intent = uCrop.getIntent(getContext());
        intent.setClass(getContext(), CropActivity.class);
        startActivityForResult(intent, REQUEST_CROP_IMAGE);
    }

    @Override
    public void failedToCreateCropImageFile() {
        showToast(getString(R.string.failed_to_create_file_for_crop));
    }

    @Override
    public void startCamera(@NonNull File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            capturedImageFileUri = FileProvider.getUriForFile(getContext(),
                    getString(R.string.file_provider_authorities), file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageFileUri);

            grantPermissionToApps(intent, capturedImageFileUri);

            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
        } else {
            showToast(getString(R.string.camera_not_available));
        }
    }

    @Override
    public void failedToCreateCameraImageFile() {
        showToast(getString(R.string.failed_to_create_file_for_camera));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface Listener {
        void onSetImage(@Nullable String relativeFileName);
    }
}
