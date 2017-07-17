package com.android.photocameralib.media;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.android.photocameralib.R;
import com.android.photocameralib.media.adapter.ImageFolderAdapter;
import com.android.photocameralib.media.bean.ImageFolder;

public class ImageFolderPopupWindow extends PopupWindow implements View.OnAttachStateChangeListener {
    private ImageFolderAdapter mAdapter;
    private RecyclerView mFolderView;
    private Callback mCallback;

    public ImageFolderPopupWindow(Context context, Callback callback) {
        super(LayoutInflater.from(context).inflate(R.layout.popup_window_folder, null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mCallback = callback;

        // init
        setAnimationStyle(R.style.popup_anim_style_alpha);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setFocusable(true);

        // content
        View content = getContentView();
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        content.addOnAttachStateChangeListener(this);

        mFolderView = (RecyclerView) content.findViewById(R.id.rv_popup_folder);
        mFolderView.setLayoutManager(new LinearLayoutManager(context));

    }

    public void setAdapter(ImageFolderAdapter adapter) {
        this.mAdapter = adapter;
        mFolderView.setAdapter(adapter);
        mAdapter.setonItemClick(new ImageFolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onFolderItemClick(position);
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        final Callback callback = mCallback;
        if (callback != null) {
            callback.onShow();
        }
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        final Callback callback = mCallback;
        if (callback != null) {
            callback.onDismiss();
        }
    }

    public void onFolderItemClick(int position) {
        final Callback callback = mCallback;
        if (callback != null) {
            callback.onSelect(this, mAdapter.getItem(position));
        }
    }

    public interface Callback {
        void onSelect(ImageFolderPopupWindow popupWindow, ImageFolder model);

        void onDismiss();

        void onShow();
    }
}
