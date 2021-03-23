package io.paysky.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.paybutton.R;

import io.paysky.ui.activity.payment.PaymentActivity;
import io.paysky.ui.dialog.InfoDialog;
import io.paysky.ui.mvp.BaseView;
import io.paysky.util.AppUtils;
import io.paysky.util.ToastUtils;

/**
 * Created by Paysky-202 on 5/14/2018.
 */

public class BaseFragment extends Fragment implements BaseView {

    public ProgressDialog progressDialog;
    public PaymentActivity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PaymentActivity) getActivity();
    }

    @Override
    public void showProgress() {
        showProgress(getString(R.string.please_wait));
    }

    public void showProgress(@StringRes int message) {
        if (isDetached())return;
        showProgress(getString(message));
    }

    @Override
    public void dismissProgress() {
        if (isDetached())return;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void showToast(int message) {
        ToastUtils.showToast(getActivity(), message);
    }

    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = AppUtils.createProgressDialog(getActivity(), message);
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public String getText(TextView textView) {
        return textView.getText().toString().trim();
    }


    public boolean isInternetAvailable() {
        return AppUtils.isInternetAvailable(activity);
    }

    @Override
    public void showNoInternetDialog() {
        new InfoDialog(activity).setDialogTitle(R.string.error)
                .setDialogText(R.string.check_internet_connection)
                .showAgreeButton(R.string.ok, null).showDialog();
    }

    public boolean isEmpty(String text) {
        return text.isEmpty();
    }
}
