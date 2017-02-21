package ru.android.childdiary.presentation.profile.edit;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

import icepick.Icepick;
import icepick.State;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseDialogFragment;
import ru.android.childdiary.utils.RequestPermissionInfo;

import static android.app.Activity.RESULT_OK;

public class ImagePickerDialogFragment extends BaseDialogFragment implements AdapterView.OnItemClickListener {
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;

    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_CAPTURE_IMAGE = 2;
    private static final int REQUEST_CROP_IMAGE = 3;

    @State
    Uri capturedImageFileUri;

    private Listener listener;

    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ListView listView = new ListView(getActivity());
        ArrayAdapter adapter = new ImagePickerArrayAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        Icepick.restoreInstanceState(this, savedInstanceState);

        dialog.setContentView(listView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                captureImage();
                break;
            case 1:
                pickImage();
                break;
            case 2:
                dismiss();
                if (listener != null) {
                    listener.onSetImage(null);
                }
                break;
        }
    }

    private void pickImage() {
        RequestPermissionInfo permissionInfo = RequestPermissionInfo.builder()
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .requestCode(REQUEST_STORAGE_READ_ACCESS_PERMISSION)
                .titleResourceId(R.string.request_read_external_storage_permission_title)
                .textResourceId(R.string.request_read_external_storage_permission_text)
                .build();
        requestPermission(permissionInfo);
    }

    @Override
    protected void permissionGranted(RequestPermissionInfo permissionInfo) {
        if (permissionInfo.getRequestCode() == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), REQUEST_PICK_IMAGE);
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File capturedImageFile = ImagePickerHelper.createCapturedImageFile(getActivity());
            if (capturedImageFile == null) {
                showToast(getString(R.string.failed_to_create_file_for_camera));
            } else {
                capturedImageFileUri = FileProvider.getUriForFile(getActivity(),
                        getString(R.string.file_provider_authorities),
                        capturedImageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageFileUri);

                ImagePickerHelper.grantPermissionToApps(getActivity(), intent, capturedImageFileUri);

                startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
            }
        } else {
            showToast(getString(R.string.camera_not_available));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                final Uri selectedImageUri = data.getData();
                if (selectedImageUri == null) {
                    showToast(getString(R.string.failed_to_select_image));
                    return;
                }

                startCropActivity(selectedImageUri);
            }
        } else if (requestCode == REQUEST_CAPTURE_IMAGE) {
            ImagePickerHelper.revokePermissions(getActivity(), capturedImageFileUri);
            if (resultCode == RESULT_OK) {
                startCropActivity(capturedImageFileUri);
            }
        } else if (requestCode == REQUEST_CROP_IMAGE) {
            if (resultCode == RESULT_OK) {
                handleCropResult(data);
            } else if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            }
        }
    }

    private void startCropActivity(@NonNull Uri sourceUri) {
        File destinationFile = ImagePickerHelper.getCroppedImageFile(getActivity());

        if (destinationFile == null) {
            showToast(getString(R.string.failed_to_create_file_for_crop));
            return;
        }

        UCrop uCrop = UCrop.of(sourceUri, Uri.fromFile(destinationFile));

        uCrop = uCrop.withAspectRatio(1, 1);

        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);
        options.setCircleDimmedLayer(true);
        options.setCropGridColumnCount(0);
        options.setCropGridRowCount(0);
        options.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryBoy));
        options.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDarkBoy));

        uCrop.withOptions(options);

        uCrop.start(getActivity(), this, REQUEST_CROP_IMAGE);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            dismiss();
            File resultFile = ImagePickerHelper.createUniqueImageFile(getActivity(), resultUri);
            if (resultFile == null) {
                showToast(getString(R.string.failed_to_save_result_file));
            } else {
                if (listener != null) {
                    listener.onSetImage(resultFile);
                }
            }
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
        void onSetImage(@Nullable File resultFile);
    }
}
