package com.baseandroid;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.android.photocameralib.media.SelectImageActivity;

public class SelectImageCustomActivit extends SelectImageActivity {

    @Override
    public void SetToolBarView(RelativeLayout relativeLayout) {
        ViewGroup viewGroup = (ViewGroup) relativeLayout.getParent();
        viewGroup.setBackgroundColor(Color.RED);
        Button button = (Button) relativeLayout.findViewById(R.id.btn_title_select);
        button.setTextColor(Color.BLUE);
    }

    @Override
    public void SetButtonView(FrameLayout frameLayout) {

    }
}
