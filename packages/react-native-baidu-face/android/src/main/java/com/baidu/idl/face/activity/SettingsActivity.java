package com.baidu.idl.face.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private TextView mLivenessIndex;

    private CheckBox mCbRandom;
    private CheckBox mCb1;
    private CheckBox mCb2;
    private CheckBox mCb3;
    private CheckBox mCb4;
    private CheckBox mCb5;
    private CheckBox mCb6;
    private CheckBox mCb7;

    private List<LivenessTypeEnum> livenessList = new ArrayList<>();
    private StringBuilder livenessName = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mLivenessIndex = (TextView) this.findViewById(R.id.settings_liveness_index);
        mCbRandom = (CheckBox) this.findViewById(R.id.settings_liveness_random);
        mCb1 = (CheckBox) this.findViewById(R.id.settings_liveness_cb1);
        mCb2 = (CheckBox) this.findViewById(R.id.settings_liveness_cb2);
        mCb3 = (CheckBox) this.findViewById(R.id.settings_liveness_cb3);
        mCb4 = (CheckBox) this.findViewById(R.id.settings_liveness_cb4);
        mCb5 = (CheckBox) this.findViewById(R.id.settings_liveness_cb5);
        mCb6 = (CheckBox) this.findViewById(R.id.settings_liveness_cb6);
        mCb7 = (CheckBox) this.findViewById(R.id.settings_liveness_cb7);

        mCbRandom.setChecked(false);

        mCb1.setTag(LivenessTypeEnum.Eye);
        mCb2.setTag(LivenessTypeEnum.Mouth);
        mCb3.setTag(LivenessTypeEnum.HeadUp);
        mCb4.setTag(LivenessTypeEnum.HeadDown);
        mCb5.setTag(LivenessTypeEnum.HeadLeft);
        mCb6.setTag(LivenessTypeEnum.HeadRight);
        mCb7.setTag(LivenessTypeEnum.HeadLeftOrRight);

        mCb1.setOnCheckedChangeListener(this);
        mCb2.setOnCheckedChangeListener(this);
        mCb3.setOnCheckedChangeListener(this);
        mCb4.setOnCheckedChangeListener(this);
        mCb5.setOnCheckedChangeListener(this);
        mCb6.setOnCheckedChangeListener(this);
        mCb7.setOnCheckedChangeListener(this);

        List<LivenessTypeEnum> list = FaceSDKManager.getInstance().getFaceConfig().getLivenessTypeList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == LivenessTypeEnum.Eye) {
                    mCb1.setChecked(true);
                    livenessName.append(LivenessTypeEnum.Eye.name());
                    if (!livenessList.contains(LivenessTypeEnum.Eye)) {
                        livenessList.add(LivenessTypeEnum.Eye);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.Mouth) {
                    mCb2.setChecked(true);
                    livenessName.append(LivenessTypeEnum.Mouth.name());
                    if (!livenessList.contains(LivenessTypeEnum.Mouth)) {
                        livenessList.add(LivenessTypeEnum.Mouth);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadUp) {
                    mCb3.setChecked(true);
                    livenessName.append(LivenessTypeEnum.HeadUp.name());
                    if (!livenessList.contains(LivenessTypeEnum.HeadUp)) {
                        livenessList.add(LivenessTypeEnum.HeadUp);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadDown) {
                    mCb4.setChecked(true);
                    livenessName.append(LivenessTypeEnum.HeadDown.name());
                    if (!livenessList.contains(LivenessTypeEnum.HeadDown)) {
                        livenessList.add(LivenessTypeEnum.HeadDown);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadLeft) {
                    mCb5.setChecked(true);
                    livenessName.append(LivenessTypeEnum.HeadLeft.name());
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeft)) {
                        livenessList.add(LivenessTypeEnum.HeadLeft);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadRight) {
                    mCb6.setChecked(true);
                    livenessName.append(LivenessTypeEnum.HeadRight.name());
                    if (!livenessList.contains(LivenessTypeEnum.HeadRight)) {
                        livenessList.add(LivenessTypeEnum.HeadRight);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadLeftOrRight) {
                    mCb7.setChecked(true);
                    livenessName.append(LivenessTypeEnum.HeadLeftOrRight.name());
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeftOrRight)) {
                        livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
                    }
                }
            }
            setIndexText();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        LivenessTypeEnum type = (LivenessTypeEnum) buttonView.getTag();
        if (isChecked) {
            if (!livenessList.contains(type))
                livenessList.add(type);
        } else {
            livenessList.remove(type);
        }
        setIndexText();
    }

    private void setIndexText() {
        livenessName.setLength(0);
        for (LivenessTypeEnum type : livenessList) {
            livenessName.append(type.name() + " ");
        }
        mLivenessIndex.setText(livenessName.toString());
    }

    @Override
    public void finish() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        config.setLivenessTypeList(this.livenessList);
        config.setLivenessRandom(mCbRandom.isChecked());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("success", true);
        setResult(Activity.RESULT_OK, resultIntent);

        super.finish();
    }
}
