package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.CustomViewPager;
import com.ezytopup.salesapp.adapter.RegisterFragment_Adapter;
import com.ezytopup.salesapp.api.HeaderimageResponse;
import com.ezytopup.salesapp.api.TokencheckResponse;
import com.ezytopup.salesapp.api.TutorialResponse;
import com.ezytopup.salesapp.printhelper.ThreadPoolManager;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView nav_user_name, nav_user_email;
    public static RoundedImageView nav_image_view;

    private static final String TAG = "MainActivity";
    private SliderLayout headerImages;
    private ArrayList<HeaderimageResponse.Result> headerImage;
    private ArrayList<TutorialResponse.Result> tutorialImage;
    private DrawerLayout drawer;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, MainActivity.class);
        caller.startActivity(intent);
        caller.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(false);
        headerImage = new ArrayList<>();
        tutorialImage = new ArrayList<>();

        if (PreferenceUtils.getSinglePrefrenceString(MainActivity.this,
                R.string.settings_def_storeaccess_token_key).equals(Constant.PREF_NULL)
                || PreferenceUtils.getSinglePrefrenceString(MainActivity.this,
                R.string.settings_def_storeemail_key).equals(Constant.PREF_NULL)) {
            Helper.synchronizeFCMRegToken(this, null);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.getHeaderView(0);
        nav_user_name = (TextView) navView.findViewById(R.id.nav_user_name);
        nav_user_email = (TextView) navView.findViewById(R.id.nav_user_email);
        nav_image_view = (RoundedImageView) navView.findViewById(R.id.nav_image_view);

        headerImages = (SliderLayout) findViewById(R.id.slider);
        headerImages.setPresetTransformer(SliderLayout.Transformer.Accordion);
        headerImages.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        headerImages.setDuration(Constant.HEADER_DURATION);
        headerImages.setPresetTransformer(SliderLayout.Transformer.ZoomOut);

        String checkEmail = PreferenceUtils.
                getSinglePrefrenceString(MainActivity.this, R.string.settings_def_storeemail_key);
        if (checkEmail.startsWith("autoemail") && checkEmail.endsWith("@mail.com")
                || checkEmail.equals(Constant.PREF_NULL)) {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_changepassword).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);

            nav_user_name.setText("Welcome, Guest");
            nav_user_email.setText("Your Email Here");
        } else {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_changepassword).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);

            String firstName = PreferenceUtils.getSinglePrefrenceString
                    (this, R.string.settings_def_storefirst_name_key);
            String lastName = PreferenceUtils.getSinglePrefrenceString
                    (this, R.string.settings_def_storelast_name_key);
            String userMail = PreferenceUtils.getSinglePrefrenceString
                    (this, R.string.settings_def_storeemail_key);
            String imageUrl = PreferenceUtils.getSinglePrefrenceString
                    (this, R.string.settings_def_storeimage_user_key);

            nav_user_name.setText(firstName + " " + lastName);
            nav_user_email.setText(userMail);

            if (imageUrl != null) {
                Glide.with(this)
                        .load(imageUrl)
                        .centerCrop()
                        .error(R.drawable.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(nav_image_view);
            }
        }

        if (!PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellerprintlogo_key).equals(Constant.PREF_NULL)) {
            Helper.downloadFile(this, PreferenceUtils.getSinglePrefrenceString(this,
                    R.string.settings_def_sellerprintlogo_key));
        }

        getImageHeader();
        initTabMenu();
        Helper.log(TAG, "setDeviceId: " + PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_storeidevice_key), null);
    }

    private void getImageHeader() {
        Call<HeaderimageResponse> call = Eztytopup.getsAPIService().getImageHeader();
        call.enqueue(new Callback<HeaderimageResponse>() {
            @Override
            public void onResponse(Call<HeaderimageResponse> call,
                                   Response<HeaderimageResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {
                    headerImage.addAll(response.body().result);
                    TextSliderView textSliderView = null;
                    for (int i = 0; i < headerImage.size(); i++) {
                        textSliderView = new TextSliderView(MainActivity.this);
                        textSliderView
                                .image(headerImage.get(i).getImageUrl())
                                .errorDisappear(true)
                                .setScaleType(BaseSliderView.ScaleType.Fit);
                        headerImages.addSlider(textSliderView);
                    }
                    if (textSliderView != null && textSliderView.isErrorLoad()) {
                        headerImages.setVisibility(View.GONE);
                    } else {
                        headerImages.setVisibility(View.VISIBLE);
                    }
                } else {
                    Helper.log(TAG, "onResponse: " + response.body().status.getMessage(), null);
                    headerImages.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<HeaderimageResponse> call, Throwable t) {
                Helper.log(TAG, "onResponse: " + t.getMessage(), null);
                headerImages.setVisibility(View.GONE);
            }
        });
    }


    private void initTabMenu() {
        final CustomViewPager mMain_Pagger = (CustomViewPager) findViewById(R.id.main_pagger);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        RegisterFragment_Adapter adapter = new RegisterFragment_Adapter(
                getSupportFragmentManager(), this);
        mMain_Pagger.setOffscreenPageLimit(4);
        mMain_Pagger.setAdapter(adapter);
        tabLayout.setupWithViewPager(mMain_Pagger);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_category) {
            ListCategoryActivity.start(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_guide:

                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_tutorial);

                final SliderLayout slider_tutorial = (SliderLayout)
                        dialog.findViewById(R.id.slider_tutorial);

                if (tutorialImage.isEmpty()) {
                    Call<TutorialResponse> tutorial = Eztytopup.getsAPIService().getTutorial();
                    tutorial.enqueue(new Callback<TutorialResponse>() {
                        @Override
                        public void onResponse(Call<TutorialResponse> call,
                                               Response<TutorialResponse> response) {
                            if (response.isSuccessful()) {
                                tutorialImage.addAll(response.body().result);

                                initSlider(slider_tutorial, tutorialImage);
                            }
                        }

                        @Override
                        public void onFailure(Call<TutorialResponse> call, Throwable t) {
                            Helper.apiSnacbarError(MainActivity.this, t, drawer);
                            dialog.dismiss();
                        }
                    });
                } else {
                    initSlider(slider_tutorial, tutorialImage);
                }

                slider_tutorial.setDuration(Constant.TUTORIAL_DURATION);
                slider_tutorial.setPresetTransformer(SliderLayout.Transformer.Default);
                TextView tvClose = (TextView) dialog.findViewById(R.id.tvClose);
                tvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.nav_faq:
                FaqActivity.start(MainActivity.this);

                break;
            case R.id.nav_contactus:
                ContactUsActivity.start(MainActivity.this);
                break;
            case R.id.nav_tutorial:
                TutorialActivity.start(MainActivity.this);
                break;
            case R.id.nav_term:
                TermActivity.start(MainActivity.this);

                break;
            case R.id.nav_login:
                Login.start(MainActivity.this);

                break;
            case R.id.nav_logout:
                PreferenceUtils.destroyUserSession(MainActivity.this);
                Login.start(MainActivity.this);
//                setUserlogout();

                break;
            case R.id.nav_changepassword:
                ChangePasswordActivity.start(MainActivity.this);

                break;
            case R.id.nav_profile:
                ProfileActivity.start(MainActivity.this);

                break;
            case R.id.nav_print:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* disable this iOS not implement this API*/
    private void setUserlogout() {
        String token = PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_storeaccess_token_key);
        String deviceid = PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_storeidevice_key);

        Call<TokencheckResponse> setlogout = Eztytopup.getsAPIService().setLogout(token, deviceid, token);
        setlogout.enqueue(new Callback<TokencheckResponse>() {
            @Override
            public void onResponse(Call<TokencheckResponse> call, Response<TokencheckResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_NO_CONTENT))) {
                    PreferenceUtils.destroyUserSession(MainActivity.this);
                    Login.start(MainActivity.this);
                } else {
                    Log.i(TAG, "onResponse: " + response.body().status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<TokencheckResponse> call, Throwable t) {

            }
        });
    }

    // TODO : if this not set, after app resume always set to false. must fix latter
    @Override
    protected void onResume() {
        super.onResume();
        Eztytopup.setIsUserReseller(PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellerid_key).equals(Constant.PREF_NULL)
                ? Boolean.FALSE : Boolean.TRUE);
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    private TextSliderView initSlider(SliderLayout slider,
                                      ArrayList<TutorialResponse.Result> result) {
        TextSliderView textSliderView = null;
        for (int i = 0; i < result.size(); i++) {
            textSliderView = new TextSliderView(MainActivity.this);
            String description = result.get(i).judul;
            String image = result.get(i).image;

            textSliderView
                    .description(description)
                    .image(image)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", description);

            slider.addSlider(textSliderView);

        }
        return textSliderView;
    }
}
