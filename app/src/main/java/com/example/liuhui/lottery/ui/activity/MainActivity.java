package com.example.liuhui.lottery.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import com.example.liuhui.lottery.R;
import com.example.liuhui.lottery.adapter.BallAdapter;
import com.example.liuhui.lottery.adapter.CalcHeightGridLayoutManager;
import com.example.liuhui.lottery.bean.Ball;
import com.example.liuhui.lottery.constant.GlobalConstant;
import com.example.liuhui.lottery.event.BaseEvent;
import com.example.liuhui.lottery.event.BigLottoEvent;
import com.example.liuhui.lottery.handler.WeakHandler;
import com.example.liuhui.lottery.presenter.BigLotteryPresenter;
import com.example.liuhui.lottery.presenter.BigLotteryPresenterImpl;
import com.example.liuhui.lottery.ui.BaseActivity;
import com.example.liuhui.lottery.ui.view.RandomView;
import com.example.liuhui.lottery.utils.ExtendUtils;
import com.example.liuhui.lottery.utils.LotteryUtils;
import com.example.liuhui.lottery.utils.SpannableUtils;
import com.example.liuhui.lottery.view.BigLotteryView;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements SensorEventListener, BigLotteryView {
    private static final int RADNOM_RESULT = 1;
    private static final int WHAT_DRAW_VIEW = 2;
    private static final int WHAT_ANIM = 3;
    private static final int WHAT_GET_LOCATION = 4;
    private static final int WHAT_REREGISTER_LISTENER = 5;

    @BindView(R.id.rcv_red)
    RecyclerView mRedBallRcv;
    @BindView(R.id.rcv_blue)
    RecyclerView mBlueBallRcv;


    @BindView(R.id.randomview)
    RandomView mRandomView;

    @BindView(R.id.tv_machine_random)
    TextView mRandomTv;

    @BindView(R.id.tv_result)
    TextView mResultTv;


    private final List<Ball> mRedBallList = new ArrayList<>();
    private final List<Ball> mBlueBallList = new ArrayList<>();

    private BallAdapter mRedBallAdapter;
    private BallAdapter mBlueBallAdapter;

    private Vibrator mVibrator;
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;

    private static final int UPTATE_INTERVAL_TIME = 50;
    private static final int SPEED_SHRESHOLD = 30;//这个值调节灵敏度

    private long mLastUpdateTime;
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    private MyVirbatorHandler mHandler;
    private List<Ball> mRandomList;

    private BigLotteryPresenter mPresenter;

    private final List<int[]> mRedLocations = new ArrayList<>();
    private final List<int[]> mBlueLocations = new ArrayList<>();
    private final List<AnimatorSet> mAnimationList = new ArrayList<>();

    private  class MyVirbatorHandler extends WeakHandler<MainActivity> {
        public MyVirbatorHandler(MainActivity target) {
            super(target);
        }

        @Override
        public void handle(MainActivity target, Message msg) {
            if (msg.what == RADNOM_RESULT) {
                mPresenter.resetSourceDataSet(mRedBallList);
                mPresenter.resetSourceDataSet(mBlueBallList);
                mRedBallAdapter.notifyDataSetChanged();
                mBlueBallAdapter.notifyDataSetChanged();
                for (AnimatorSet animatorSet : mAnimationList) {
                    if (animatorSet != null && animatorSet.isRunning()) {
                        animatorSet.end();
                    }
                }
                mAnimationList.clear();
                mRandomView.removeAllViews();

                mRandomList = new ArrayList<>();
                mPresenter.randomSource(mRandomList);
                mRandomView.setVisibility(View.VISIBLE);

                for (int i = 0, size = mRandomList.size(); i < size; i++) {
                    Ball ball = mRandomList.get(i);
                    ball.isSelect = true;
                    mRandomView.addView(ball);
                }
                mRandomView.startAnim();
                sendEmptyMessageDelayed(WHAT_ANIM, 1500);
            } else if (mRandomView != null && !ExtendUtils.listIsNullOrEmpty(mRandomList) && msg.what == WHAT_DRAW_VIEW && msg.arg1 >= 0 && msg.arg1 < mRandomList.size()) {
                Ball ball = mRandomList.get(msg.arg1);
                ball.isSelect = true;
                mRandomView.addView(ball);
            } else if (msg.what == WHAT_ANIM) {
                if (mRandomList == null || mRandomView == null || mRandomList.size() != mRandomView.getChildCount()) {
                    return;
                }
                for (int i = 0; i < mRandomList.size(); i++) {
                    Ball ball = mRandomList.get(i);
                    if (ball == null) {
                        return;
                    }
                    int index = 0;
                    int[] positions = null;
                    if (i < 5 && i < mRedBallList.size() && i < mRedLocations.size()) {
                        index = Integer.parseInt(ball.name) - 1;
                        mRedBallList.get(index).isSelect = true;
                        positions = mRedLocations.get(index);
                    } else if (i < mBlueBallList.size() && i < mBlueLocations.size()) {
                        index = Integer.parseInt(ball.name) - 1;
                        positions = mBlueLocations.get(index);
                        mBlueBallList.get(index).isSelect = true;
                    }
                    View view = mRandomView.getChildAt(i);
                    int[] screens = new int[2];
                    view.getLocationOnScreen(screens);
                    ObjectAnimator y = ObjectAnimator.ofFloat(view, "y", screens[1], positions[1]);
                    ObjectAnimator x = ObjectAnimator.ofFloat(view, "x", screens[0], positions[0]);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(x, y);
                    animatorSet.setDuration(250);
                    animatorSet.start();
                    mAnimationList.add(animatorSet);
                }
                mHandler.sendEmptyMessageDelayed(WHAT_REREGISTER_LISTENER, 500);
                //本身接收事件
                EventBus.getDefault().post(new BigLottoEvent());

            } else if (msg.what == WHAT_GET_LOCATION) {
                getLocationsOnScreen(mRedBallRcv, mRedLocations);
                getLocationsOnScreen(mBlueBallRcv, mBlueLocations);
            } else if (msg.what == WHAT_REREGISTER_LISTENER) {
                //重新注册传感器
                target.registerSensorListener();
                mRandomView.removeAllViews();
                mRandomView.setVisibility(View.GONE);
                mRedBallAdapter.notifyDataSetChanged();
                mBlueBallAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void getLocationsOnScreen(ViewGroup viewGroup, List<int[]> locationsList) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                ViewGroup viewGroup2 = (ViewGroup) viewGroup.getChildAt(i);
                if (viewGroup2.getChildCount() > 0) {
                    View childAt1 = viewGroup2.getChildAt(0);
                    int[] locations = new int[2];
                    childAt1.getLocationOnScreen(locations);
                    locations[1] -= ExtendUtils.getStatusBarHeight(this);
                    locationsList.add(locations);
                }
            }


        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BigLotteryPresenterImpl(this, this);
    }

    @Override
    protected void initContentView() {
        super.initContentView();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        //获取Vibrator震动服务
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHandler = new MyVirbatorHandler(this);

        mPresenter.generaterSource(mRedBallList, GlobalConstant.BigLottery.RED);
        mPresenter.generaterSource(mBlueBallList, GlobalConstant.BigLottery.BLUE);

        mRedBallAdapter = new BallAdapter(this, mRedBallList, R.drawable.bg_select_circle_white_red, R.color.text_color_red_and_white);
        mBlueBallAdapter = new BallAdapter(this, mBlueBallList, R.drawable.bg_select_circle_white_blue, R.color.text_color_blue_and_white);
        mRedBallRcv.setLayoutManager(new CalcHeightGridLayoutManager(this, 8));
        mBlueBallRcv.setLayoutManager(new CalcHeightGridLayoutManager(this, 8));
        mRedBallRcv.setAdapter(mRedBallAdapter);
        mBlueBallRcv.setAdapter(mBlueBallAdapter);

        mHandler.sendEmptyMessageDelayed(WHAT_GET_LOCATION, 200);

        mResultTv.setText(SpannableUtils.getBuilder("")
                .appendStringId(R.string.unit, 0)
                .setTextSize(14)
                .setForegroundColorId(R.color.white).append(" ")
                .appendStringId(R.string.chinese_yuan, 0)
                .setTextSize(14)
                .setForegroundColorId(R.color.yellow_fdff52)
                .create());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.tv_machine_random, R.id.iv_clean_up})
    public void funcOnclick(View view) {
        switch (view.getId()) {
            case R.id.tv_machine_random:
                randomOneResult();
                break;
            case R.id.iv_clean_up:
                mPresenter.resetSourceDataSet(mRedBallList);
                mPresenter.resetSourceDataSet(mBlueBallList);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onEventMainThread(BaseEvent event) {
        if (event instanceof BigLottoEvent) {
            int red = 0;
            for (Ball ball : mRedBallList) {
                if (ball != null && ball.isSelect) {
                    red++;
                }
            }
            int blue = 0;
            for (Ball ball : mBlueBallList) {
                if (ball != null && ball.isSelect) {
                    blue++;
                }
            }
            long result = LotteryUtils.getBigLottoMaxResult(red, blue);
            mResultTv.setText(SpannableUtils.getBuilder("")
                    .appendStringId(R.string.unit, result)
                    .setTextSize(14)
                    .setForegroundColorId(R.color.white).append("  ")
                    .appendStringId(R.string.chinese_yuan, result * 2)
                    .setTextSize(14)
                    .setForegroundColorId(R.color.yellow_fdff52)
                    .create());
        }
    }

    @Override
    protected void onResume() {

        registerSensorListener();
        super.onResume();
    }

    private void registerSensorListener() {
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    @Override
    protected void onPause() {
        unRegisterSensorListener();

        super.onPause();
    }

    private void unRegisterSensorListener() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - mLastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME) {
            return;
        }
        mLastUpdateTime = currentUpdateTime;
        // 传感器信息改变时执行该方法
        float[] values = event.values;
        float x = values[0]; // x轴方向的重力加速度，向右为正
        float y = values[1]; // y轴方向的重力加速度，向前为正
        float z = values[2]; // z轴方向的重力加速度，向上为正
        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;

        mLastX = x;
        mLastY = y;
        mLastZ = z;
        double speed = (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval) * 100;
        if (speed >= SPEED_SHRESHOLD) {
            unRegisterSensorListener();
            Message msg = new Message();
            msg.what = RADNOM_RESULT;
            mHandler.sendMessageDelayed(msg, 200);
            mVibrator.vibrate(50);
        }
    }

    /**
     * 生成随机一注
     */
    private void randomOneResult() {
        mPresenter.resetSourceDataSet(mRedBallList);
        mPresenter.resetSourceDataSet(mBlueBallList);
        for (AnimatorSet animatorSet : mAnimationList) {
            if (animatorSet != null && animatorSet.isRunning()) {
                animatorSet.end();
            }
        }
        mAnimationList.clear();
        mRandomView.removeAllViews();
        List<Integer> redIndex = new ArrayList<>();
        List<Integer> blueIndex = new ArrayList<>();
        mPresenter.randomSourceIndex(redIndex, GlobalConstant.BigLottery.RED, 5);
        mPresenter.randomSourceIndex(blueIndex, GlobalConstant.BigLottery.BLUE, 2);
        for (Integer integer : redIndex) {
            if (integer > 0 && integer < mRedBallList.size()) {
                mRedBallList.get(integer - 1).isSelect = true;
            }
        }
        for (Integer integer : blueIndex) {
            if (integer > 0 && integer < mBlueBallList.size()) {
                mBlueBallList.get(integer - 1).isSelect = true;
            }
        }

        mRedBallAdapter.notifyDataSetChanged();
        mBlueBallAdapter.notifyDataSetChanged();
        //本身接收事件
        EventBus.getDefault().post(new BigLottoEvent());

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
