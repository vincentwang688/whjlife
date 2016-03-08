package com.vincentwang.android.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.vincentwang.android.tools.ResourceTools;

/**
 * 自定义对话框
 *
 * @author vincentwang
 * @version 0.1
 */
public class ProgressDialog extends Dialog {

    /**
     * 消息TextView
     */
    private TextView tvMsg;

    public ProgressDialog(Context context, String strMessage) {
        this(context, ResourceTools.getStyleId("CustomProgressDialog"), strMessage);
    }

    public ProgressDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        this.setContentView(ResourceTools.getLayoutId("view_progress_dialog"));
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        tvMsg = (TextView) this.findViewById(ResourceTools.getIdId("tv_msg"));
        setMessage(strMessage);
    }

    /**
     * 设置进度条消息
     *
     * @param strMessage 消息文本
     */
    public void setMessage(String strMessage) {
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
    }
}