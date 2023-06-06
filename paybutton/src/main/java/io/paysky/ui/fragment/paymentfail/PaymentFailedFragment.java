package io.paysky.ui.fragment.paymentfail;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paybutton.R;

import io.paysky.ui.activity.payment.PaymentActivity;
import io.paysky.ui.base.BaseFragment;
import io.paysky.util.AppConstant;
import io.paysky.util.AppUtils;


public class PaymentFailedFragment extends BaseFragment implements View.OnClickListener {
    //Objects,
    private PaymentActivity activity;
    //Variables.
    private String declineCause;

    public PaymentFailedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PaymentActivity) getActivity();
        extractBundle();
    }

    private void extractBundle() {
        assert getArguments() != null;
        declineCause = getArguments().getString(AppConstant.BundleKeys.DECLINE_CAUSE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_failed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setHeaderIcon(R.drawable.ic_close);
        activity.setHeaderIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        initView(view);

    }

    private void initView(View view) {
        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(this);
        Button tryAgainButton = view.findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(this);
        TextView transactionDeclinedTextView = view.findViewById(R.id.transaction_declined_textView);
        TextView declineCauseTextView = view.findViewById(R.id.declined_cause_textView);
        AppUtils.showHtmlText(transactionDeclinedTextView, R.string.transaction_declined);
        declineCauseTextView.setText(declineCause);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.close_button) {
            activity.onBackPressed();
        } else if (viewId == R.id.try_again_button) {
            activity.showManualPayment();
        }
    }
}
