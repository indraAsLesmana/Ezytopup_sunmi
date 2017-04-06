package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.RegisterFragment_Adapter;
import com.ezytopup.salesapp.printhelper.ThreadPoolManager;
import com.ezytopup.salesapp.utility.Constant;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private SliderLayout headerImages;


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerImages = (SliderLayout) findViewById(R.id.slider);
        getImage();

        initTabMenu();
    }

    private void getImage() {

        TextSliderView textSliderView = new TextSliderView(this);
        // initialize a SliderLayout
        textSliderView
                .image(R.drawable.header1)
                .setScaleType(BaseSliderView.ScaleType.Fit);
        headerImages.addSlider(textSliderView);

        headerImages.setPresetTransformer(SliderLayout.Transformer.Accordion);
        headerImages.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        headerImages.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

    }

    private void initTabMenu(){
        ViewPager mMain_Pagger = (ViewPager) findViewById(R.id.main_pagger);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        RegisterFragment_Adapter adapter = new RegisterFragment_Adapter(
                getSupportFragmentManager(), this);

        mMain_Pagger.setAdapter(adapter);
        tabLayout.setupWithViewPager(mMain_Pagger);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setEnabled(true);
//        item.getIcon().setAlpha(0);
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_print:
                ThreadPoolManager.getInstance().executeTask(new Runnable() {
                    @Override
                    public void run() {
                        if( Eztytopup.getmBitmap() == null ){
                        /*Change store logo, here...*/
                            Eztytopup.setmBitmap(BitmapFactory.decodeResource(getResources(),
                                    R.raw.ezy_for_print));
                        }
                        try {
                            String[] text = new String[4];// 4 = set total column
                            for (int i = 0; i < 4; i++) { // 4 = set total looping content buy
                                if (i == 0){
                                /*logo*/
                                    Eztytopup.getWoyouService().setAlignment(1, Eztytopup.getCallback());
                                    Eztytopup.getWoyouService().printBitmap(Eztytopup.getmBitmap(), Eztytopup.getCallback());
                                    Eztytopup.getWoyouService().lineWrap(1, Eztytopup.getCallback());
                                    Eztytopup.getWoyouService().setFontSize(24, Eztytopup.getCallback());
                                /*header*/
                                    text[0] = Constant.ROW_NAME;
                                    text[1] = Constant.ROW_QUANTITY;
                                    text[2] = Constant.ROW_PRICE;
                                    text[3] = Constant.ROW_TOTAL_PRICE;
                                /*print*/
                                    Eztytopup.getWoyouService().printColumnsText(text, Constant.width,
                                            Constant.align_header, Eztytopup.getCallback());
                                }else {
                                    text[0] = "Steam IDR" + i;
                                    text[1] = "1";
                                    text[2] = "72.00";
                                    text[3] = "48.00";
                                    Eztytopup.getWoyouService().printColumnsText(text, Constant.width,
                                            Constant.align, Eztytopup.getCallback());
                                }

                            }
                        /* make space*/
                            Eztytopup.getWoyouService().lineWrap(4, Eztytopup.getCallback());

                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
             break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }
}
