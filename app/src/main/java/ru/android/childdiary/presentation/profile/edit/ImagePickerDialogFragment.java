package ru.android.childdiary.presentation.profile.edit;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.BaseDialogFragment;
import ru.android.childdiary.utils.RequestPermissionInfo;

import static android.app.Activity.RESULT_OK;

// TODO: доработать
public class ImagePickerDialogFragment extends BaseDialogFragment implements AdapterView.OnItemClickListener {
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_SELECT_PICTURE = 1;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;// Manifest.permission.READ_EXTERNAL_STORAGE;
    private Listener listener;

    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ListView listView = new ListView(getActivity());
        String[] items = getResources().getStringArray(R.array.image_picker_actions);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.image_picker_item, android.R.id.text1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        dialog.setContentView(listView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showToast(String.valueOf(position));
        if (position == 1) pickFromGallery();
    }

    private void pickFromGallery() {
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
            // FIXME
            startActivityForResult(Intent.createChooser(intent, "select picture"), REQUEST_SELECT_PICTURE);
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)));
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
        uCrop.start(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    // FIXME
                    showToast("can not retrieve selected image");
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
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
            if (listener != null) {
                dismiss();
                listener.handleImage();
            }
        } else {
            // FIXME
            showToast("can not retrieve cropped image");
        }
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            logger.error("handleCropError: ", cropError);
            showToast(cropError.getMessage());
        } else {
            // FIXME
            showToast("unexpected error");
        }
    }

    public interface Listener {
        void handleImage();

        void deleteImage();
    }
}
