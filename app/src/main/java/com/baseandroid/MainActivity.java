package com.baseandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.photocameralib.media.ImageGalleryActivity;
import com.android.photocameralib.media.config.SelectOptions;
import com.baseandroid.base.BaseActivity;
import com.baseandroid.config.Global;
import com.baseandroid.config.WebDataUtils;
import com.baseandroid.jpush.JPushBizutils;
import com.baseandroid.repository.ConfigRepository;
import com.baseandroid.repository.json.CheckUpdate;
import com.baseandroid.repository.json.Data;
import com.baseandroid.repository.json.Result;
import com.baseandroid.repository.json.ServerTime;
import com.baseandroid.repository.json.UserDate;
import com.baseandroid.repository.json.UserTokenInfo;
import com.jayfeng.lesscode.core.DisplayLess;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.test_id1)
    Button test_id1;
    @BindView(R.id.test_id2)
    Button test_id2;
    @BindView(R.id.test_id3)
    Button test_id3;
    @BindView(R.id.test_id4)
    Button test_id4;

    @BindView(R.id.test_id5)
    TextView test_id5;

    @BindView(R.id.test_id6)
    Button test_id6;
    @BindView(R.id.test_id7)
    Button test_id7;
    @BindView(R.id.test_id8)
    Button test_id8;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        test_id1.setOnClickListener(this);
        test_id2.setOnClickListener(this);
        test_id3.setOnClickListener(this);
        test_id4.setOnClickListener(this);
        test_id6.setOnClickListener(this);
        test_id7.setOnClickListener(this);
        test_id8.setOnClickListener(this);
    }

    @Override
    protected void setupData(Bundle savedInstanceState) {
        checkUpdate();
        addVisit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.test_id1:
                getServerTime();
                break;

            case R.id.test_id2:
                loginApp();
                break;

            case R.id.test_id3:
                getUseInfo();
                break;

            case R.id.test_id4:
                getNewGroundedCount();
                break;

            case R.id.test_id6:
                SelectImageCustomActivit.show(MainActivity.this, new SelectOptions.Builder()
                        .setHasCam(true)
                        .setSelectCount(9)
                        .setCallback(new SelectOptions.Callback() {
                            @Override
                            public void doSelected(String[] images) {
                                for (String imgpath : images) {
                                    Log.e("++++++++", "SelectImageActivity imgpath = " + imgpath);
                                }
                            }
                        })
                        .build());
                break;

            case R.id.test_id7:
                ImageGalleryActivity.show(MainActivity.this, new String[]{"http://c.hiphotos.baidu.com/zhidao/pic/item/b64543a98226cffc3cef5decbe014a90f703eaa3.jpg", "http://img0.imgtn.bdimg.com/it/u=3164822311,573063053&fm=26&gp=0.jpg"}, 0, true);
                break;

            case R.id.test_id8:
                SelectImageCustomActivit.show(MainActivity.this, new SelectOptions.Builder()
                        .setHasCam(true)
                        .setSelectCount(1)
                        .setCrop(DisplayLess.$dp2px(256), DisplayLess.$dp2px(256))
                        .setCallback(new SelectOptions.Callback() {
                            @Override
                            public void doSelected(String[] images) {
                                for (String imgpath : images) {
                                    Log.e("++++++++", " imgpath = " + imgpath);
                                }
                            }
                        })
                        .build(), SelectImageCustomActivit.class);
                break;

            default:
                break;
        }
    }

    private void getServerTime() {
        ConfigRepository.getInstance()
                .getServerTime()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<ServerTime>>bindToLifecycle())
                .subscribe(new Observer<Data<ServerTime>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<ServerTime> serverTimeData) {
                        if (WebDataUtils.checkJsonCode(serverTimeData, true)) {
                            test_id5.setText(serverTimeData.getResult().toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loginApp() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userName", "15947860108");
        hashMap.put("password", "12345678");
        hashMap.put("passType", "0");
        hashMap.put("audience", "");

        ConfigRepository.getInstance()
                .loginApp(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<UserTokenInfo>>bindToLifecycle())
                .subscribe(new Observer<Data<UserTokenInfo>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<UserTokenInfo> userTokenInfoData) {
                        if (WebDataUtils.checkJsonCode(userTokenInfoData, true)) {
                            UserTokenInfo userInfo = userTokenInfoData.getResult();
                            userInfo.getUser()
                                    .setMobile("49a52de65c23aa57251dd3701b29e54e");
                            Global.setUserInfo(userInfo);
                            Log.e("+++++++++++", "=====UserTokenInfo token===" + userInfo.getAc_token());
                            Log.e("+++++++++++", "=====UserTokenInfo mobile===" + userInfo
                                    .getUser()
                                    .getMobile());
                            test_id5.setText(userInfo.toString());
                            JPushBizutils.initJPush();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        WebDataUtils.errorThrowable(e, true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getUseInfo() {
        ConfigRepository.getInstance()
                .getUserinfo()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<UserDate>>bindToLifecycle())
                .subscribe(new Observer<Data<UserDate>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<UserDate> userDateData) {
                        if (WebDataUtils.checkJsonCode(userDateData, true)) {
                            UserDate userDate = userDateData.getResult();
                            Log.e("+++++++", "====UserDate mobile====" + userDate.getUser()
                                    .getMobile());
                            test_id5.setText(userDate.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getNewGroundedCount() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String formatdata = simpleDateFormat.format(new Date());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("date", formatdata);

        ConfigRepository.getInstance()
                .getNewGroundedCount(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<Result>>bindToLifecycle())
                .subscribe(new Observer<Data<Result>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<Result> resultData) {
                        if (WebDataUtils.checkJsonCode(resultData, true)) {
                            test_id5.setText(resultData.getResult().toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void checkUpdate() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("classcode", "ANDROID_CRM_VERSION");

        ConfigRepository.getInstance()
                .checkUpdate(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data<CheckUpdate>>bindToLifecycle())
                .subscribe(new Observer<Data<CheckUpdate>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data<CheckUpdate> checkUpdateData) {
                        if (WebDataUtils.checkJsonCode(checkUpdateData, true)) {
                            test_id5.setText(checkUpdateData.getResult().toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void addVisit() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("areaId", "50804");
        hashMap.put("mobile", "13652365236");
        hashMap.put("openorno", "2");
        hashMap.put("type", "2");
        hashMap.put("content", "+2    2+");
        hashMap.put("merchantName", "徐家汇银行");
        hashMap.put("vcpPosition", "中山南二路");
        hashMap.put("visitAddress", "上海市中心");

        ConfigRepository.getInstance()
                .addVisit(hashMap)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(MainActivity.this.<Data>bindToLifecycle())
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Data checkUpdateData) {
                        if (WebDataUtils.checkJsonCode(checkUpdateData, true)) {
                            test_id5.setText(checkUpdateData.getResult().toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
