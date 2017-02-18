package ru.android.childdiary.presentation.profile.edit;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import static android.app.Activity.RESULT_OK;

// TODO: доработать
public class ImagePickerDialogFragment extends BaseDialogFragment implements AdapterView.OnItemClickListener {
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_SELECT_PICTURE = 1;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    // FIXME
                    showToast("can not retrieve selected image");
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showAlertDialog(@Nullable String title, @Nullable String message,
                                 @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                 @NonNull String positiveText,
                                 @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                 @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
            // FIXME
            showAlertDialog("permission title rationale", rationale,
                    (DialogInterface dialog, int which) ->
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{permission}, requestCode), "OK", null, "Cancel");
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        }
    }

    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // FIXME
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    "read storage rationale",
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
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
        uCrop = advancedConfig(uCrop);
        uCrop.start(getActivity());
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
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
        return uCrop.withOptions(options);
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
