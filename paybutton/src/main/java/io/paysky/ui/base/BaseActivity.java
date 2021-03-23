package io.paysky.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.paybutton.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.paysky.ui.dialog.InfoDialog;
import io.paysky.ui.mvp.BaseView;
import io.paysky.util.AppUtils;
import io.paysky.util.ToastUtils;
/**
 * Created by Paysky-202 on 5/13/2018.
 */

public class BaseActivity extends AppCompatActivity implements BaseView {

    private ProgressDialog progressDialog;


    @Override
    protected void attachBaseContext(Context newBase) {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("medium.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void showProgress() {
        showProgress(getString(R.string.please_wait));
    }

    public void showProgress(@StringRes int message) {
        showProgress(getString(message));
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = AppUtils.createProgressDialog(this, message);
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public boolean isInternetAvailable() {
        return AppUtils.isInternetAvailable(this);
    }

    @Override
    public void showToast(@StringRes int text) {
        ToastUtils.showToast(this, text);
    }


    public void showToast(String text) {
        ToastUtils.showToast(this, text);
    }


    protected void replaceFragment(Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addOldToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        try {
            fragment = fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        if (addOldToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void showNoInternetDialog() {
        new InfoDialog(this).setDialogTitle(R.string.error)
                .setDialogText(R.string.check_internet_connection)
                .showAgreeButton(R.string.ok, null).showDialog();
    }



}
