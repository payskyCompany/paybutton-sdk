package io.paysky.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.paybutton.R;

import io.paysky.util.ViewUtil;

/**
 * Created by Paysky-202 on 5/14/2018.
 */

public class InfoDialog extends Dialog implements View.OnClickListener {

    //GUI.
    private TextView dialogTitleTextView;
    private TextView dialogContentTextView;
    private Button agreeButton, cancelButton;
    private DialogAgreeButtonClick agreeButtonClick;
    private DialogCancelButtonClick cancelButtonClick;

    public InfoDialog(@NonNull Context context) {
        super(context);
        initView();
    }


    private void initView() {
        View dialogView = ViewUtil.inflateView(getContext(), R.layout.dialog_info);
        dialogTitleTextView = dialogView.findViewById(R.id.dialog_title_textView);
        dialogContentTextView = dialogView.findViewById(R.id.dialog_content_textView);
        agreeButton = dialogView.findViewById(R.id.agree_button);
        cancelButton = dialogView.findViewById(R.id.cancel_action);
        agreeButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
    }


    public InfoDialog setDialogTitle(@StringRes int title) {
        dialogTitleTextView.setText(title);
        return this;
    }

    public InfoDialog setDialogText(@StringRes int content) {
        dialogContentTextView.setText(content);
        return this;
    }

    public InfoDialog setDialogText(String content) {
        dialogContentTextView.setText(content);
        return this;
    }

    public InfoDialog setCancel(boolean isCancel) {
        setCancelable(isCancel);
        return this;
    }

    public void showDialog() {
        show();
    }


    public InfoDialog showAgreeButton(@StringRes int buttonText, DialogAgreeButtonClick agreeButtonClick) {
        agreeButton.setText(buttonText);
        agreeButton.setVisibility(View.VISIBLE);
        this.agreeButtonClick = agreeButtonClick;
        return this;
    }


    public InfoDialog showAgreeButton(DialogAgreeButtonClick agreeButtonClick) {
        agreeButton.setText(R.string.ok);
        agreeButton.setVisibility(View.VISIBLE);
        this.agreeButtonClick = agreeButtonClick;
        return this;
    }

    public InfoDialog showCancelButton(@StringRes int buttonText, DialogCancelButtonClick cancelButtonClick) {
        cancelButton.setText(buttonText);
        cancelButton.setVisibility(View.VISIBLE);
        this.cancelButtonClick = cancelButtonClick;
        return this;
    }

    public InfoDialog showCancelButton(DialogCancelButtonClick cancelButtonClick) {
        cancelButton.setText(R.string.cancel);
        cancelButton.setVisibility(View.VISIBLE);
        this.cancelButtonClick = cancelButtonClick;
        return this;
    }


    @Override
    public void onClick(View view) {
        if (view.equals(agreeButton)) {
            if (agreeButtonClick != null) {
                agreeButtonClick.onAgreeDialogButtonClick(this);
            } else {
                dismiss();
            }
        } else if (view.equals(cancelButton)) {
            if (cancelButtonClick != null) {
                cancelButtonClick.onCancelDialogButtonClick(this);
            } else {
                dismiss();
            }
        }
    }
}
