package io.paysky.ui.fragment.paymentsuccess;

import android.content.Context;
import android.support.annotation.StringRes;

import io.paysky.ui.mvp.BaseView;

interface PaymentApprovedView extends BaseView {
    void setSendEmailSuccess(String emailTo, int operationType);

    Context getContext();

    void showErrorToast(@StringRes int error);
}
