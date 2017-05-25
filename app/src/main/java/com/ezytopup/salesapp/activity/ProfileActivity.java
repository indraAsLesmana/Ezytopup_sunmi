package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.WalletbalanceResponse;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;
import com.sw926.imagefileselector.ImageCropper;
import com.sw926.imagefileselector.ImageFileSelector;

import java.io.File;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    private EditText mSaldo, mName, mPhone, mEmail;
    private ImageView mImageprofile;
    private ConstraintLayout container_view;
    private ImageFileSelector mImageFileSelector;
    private ImageCropper mImageCropper;
    private File mCurrentSelectFile;
    public Bitmap bitmap;

    private Button mUpdateButton, mCancelButton;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ProfileActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSaldo = (EditText) findViewById(R.id.ed_profile_saldo);
        mName = (EditText) findViewById(R.id.ed_profile_name);
        mPhone = (EditText) findViewById(R.id.ed_profile_phone);
        mEmail = (EditText) findViewById(R.id.ed_profile_email);
        mImageprofile = (ImageView) findViewById(R.id.im_profile_image);
        container_view = (ConstraintLayout) findViewById(R.id.container_profileactivity);

        mUpdateButton = (Button) findViewById(R.id.btnprofileUpdate);
        mCancelButton = (Button) findViewById(R.id.btnprofileCancel);

        mUpdateButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        String userName = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storefirst_name_key);
        String userPhone = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storephone_number_key);
        String userMail = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storeemail_key);
        String imageUrl = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storeimage_user_key);

        mName.setText(userName);
        mPhone.setText(userPhone);
        mEmail.setText(userMail);

        ImageFileSelector.setDebug(true);

        if (imageUrl != null) {
            Glide.with(ProfileActivity.this)
                    .load(imageUrl).centerCrop()
                    .error(R.drawable.ic_error_loadimage)
                    .into(mImageprofile);
        }

        String checkEmail = PreferenceUtils.
                getSinglePrefrenceString(ProfileActivity.this, R.string.settings_def_storeemail_key);
        if (!checkEmail.startsWith("autoemail") && !checkEmail.endsWith("@mail.com")
                || checkEmail.equals(Constant.PREF_NULL)) {
            getBalance();
        }

        if (Eztytopup.getSunmiDevice()) {
            Helper.setImmersivebyKeyboard(container_view);
        }

        mImageFileSelector = new ImageFileSelector(this);
        mImageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onSuccess(String file) {
                if (!TextUtils.isEmpty(file)) {
                    loadImage(file);
                    mCurrentSelectFile = new File(file);
                } else
                    Toast.makeText(ProfileActivity.this, "select image file error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                Toast.makeText(ProfileActivity.this, "select image file error", Toast.LENGTH_LONG).show();
            }
        });

        mImageCropper = new ImageCropper(this);
        mImageCropper.setCallback(new ImageCropper.ImageCropperCallback() {
            @Override
            public void onCropperCallback(ImageCropper.CropperResult result, File srcFile, File outFile) {
                mCurrentSelectFile = null;
                if (result == ImageCropper.CropperResult.success) {
                    loadImage(outFile.getPath());
                } else if (result == ImageCropper.CropperResult.error_illegal_input_file) {
                    Toast.makeText(ProfileActivity.this, "input file error", Toast.LENGTH_LONG).show();
                } else if (result == ImageCropper.CropperResult.error_illegal_out_file) {
                    Toast.makeText(ProfileActivity.this, "output file error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadImage(final String file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = BitmapFactory.decodeFile(file);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageprofile.setImageBitmap(bitmap);
                        mImageprofile.getLayoutParams().width = mImageprofile.getWidth();
                        mImageprofile.getLayoutParams().height = mImageprofile.getHeight();

                        if (mCurrentSelectFile != null) {
                            mImageCropper.setOutPut(mImageprofile.getWidth(), mImageprofile.getHeight());
                            mImageCropper.setOutPutAspect(1, 1);
                            mImageCropper.cropImage(mCurrentSelectFile);
                        }

                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageFileSelector.onActivityResult(requestCode, resultCode, data);
        mImageCropper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImageFileSelector.onSaveInstanceState(outState);
        mImageCropper.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageFileSelector.onRestoreInstanceState(savedInstanceState);
        mImageCropper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImageFileSelector.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getBalance() {
        String token = PreferenceUtils.getSinglePrefrenceString(ProfileActivity.this,
                R.string.settings_def_storeaccess_token_key);
        Call<WalletbalanceResponse> getBalace = Eztytopup.getsAPIService().getWalletbalance(token);
        getBalace.enqueue(new Callback<WalletbalanceResponse>() {
            @Override
            public void onResponse(Call<WalletbalanceResponse> call,
                                   Response<WalletbalanceResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_CREATED))) {
                    mSaldo.setText(response.body().getBalance().toString());
                } else {
                    Log.i(TAG, "onResponse: " + response.body().status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<WalletbalanceResponse> call, Throwable t) {

            }
        });
    }

    ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.fromCamera:
                initImageFileSelector();
                mImageFileSelector.takePhoto(this);
                break;
            case R.id.fromGallery:
                initImageFileSelector();
                mImageFileSelector.selectImage(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initImageFileSelector() {
        int w = 0;
        int h = 0;
        mImageFileSelector.setOutPutImageSize(w, h);
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnprofileUpdate:
                break;
            case R.id.btnprofileCancel:
                finish();
                break;
        }
    }
}
