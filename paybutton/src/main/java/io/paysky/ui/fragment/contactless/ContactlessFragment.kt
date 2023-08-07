package io.paysky.ui.fragment.contactless

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.paybutton.R
import io.paysky.creditCardNfcReader.CardNfcAsyncTask
import io.paysky.creditCardNfcReader.utils.CardNfcUtils

import io.paysky.data.model.CardPaymentParameters
import io.paysky.data.model.PaymentData
import io.paysky.ui.activity.payment.PaymentActivity
import io.paysky.ui.base.BaseFragment
import io.paysky.ui.fragment.paymentprocessing.PaymentProcessingFragment
import io.paysky.util.AppConstant
import io.paysky.util.ContaclessInterface


class ContactlessFragment : BaseFragment() , CardNfcAsyncTask.CardNfcInterface,ContaclessInterface {

    private var mNfcAdapter: NfcAdapter? = null
    private var mTurnNfcDialog: AlertDialog? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var cardNfcUtils: CardNfcUtils
    private lateinit var mCardNfcAsyncTask:CardNfcAsyncTask
    private var paymentData: PaymentData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        assert(arguments != null)
        paymentData = requireArguments().getParcelable(AppConstant.BundleKeys.PAYMENT_DATA)

        cardNfcUtils= CardNfcUtils(requireActivity())

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) {

            if (it.resultCode == Activity.RESULT_OK) {

                cardNfcUtils.enableDispatch()

            } else if (it.resultCode == Activity.RESULT_CANCELED) {
                openNfcIFNeedCheck()
            }
        }


    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_contactless, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onResume() {

        super.onResume()
        openNfcIFNeedCheck()

    }

    private fun openNfcIFNeedCheck() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())

        if (mNfcAdapter != null && !mNfcAdapter!!.isEnabled) {

            showTurnOnNfcDialog()
        }

        else if (mNfcAdapter != null) {
            try {
                (getActivity() as PaymentActivity?)!!.initContactlessInterface(this)
                cardNfcUtils.enableDispatch()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showTurnOnNfcDialog() {
        if (mTurnNfcDialog == null) {
            val title = getString(R.string.ad_nfcTurnOn_title)
            val mess = getString(R.string.ad_nfcTurnOn_message)
            val pos = getString(R.string.ad_nfcTurnOn_pos)
            val neg = getString(R.string.ad_nfcTurnOn_neg)
            mTurnNfcDialog = AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(mess)
                .setCancelable(false)
                .setPositiveButton(pos
                ) { _, _ ->
                    val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                    resultLauncher.launch(intent)
                }
                .setNegativeButton(neg) { dialogInterface: DialogInterface?, i: Int ->


                    mTurnNfcDialog!!.dismiss()


                }
               .show()
        }else{
            mTurnNfcDialog!!.show()
        }
    }

    override fun startNfcReadCard() {

    }

    override fun cardIsReadyToRead() {


        val expireDate = mCardNfcAsyncTask.cardExpireDate.replace("/".toRegex(), "")
        val month = expireDate.substring(0, 2)
        val year = expireDate.substring(2)
        //move to processing screen with data
        val cardPaymentParameters = CardPaymentParameters(
            mCardNfcAsyncTask.cardNumber,
            "",
            year + month,
            "",
            false,
            false
        )

        val bundle = Bundle()
        bundle.putParcelable(AppConstant.BundleKeys.PAYMENT_DATA, paymentData)
        bundle.putParcelable(AppConstant.BundleKeys.CARD_DATA, cardPaymentParameters)
        activity.replaceFragmentAndRemoveOldFragment(PaymentProcessingFragment::class.java, bundle)

    }

    override fun doNotMoveCardSoFast() {

    }

    override fun unknownEmvCard() {

    }

    override fun cardWithLockedNfc() {

    }

    override fun finishNfcReadCard() {

    }

    override fun getIntentFromNewIntent(intent: Intent) {

        if (mNfcAdapter != null && mNfcAdapter!!.isEnabled) {
            mCardNfcAsyncTask = CardNfcAsyncTask.Builder(this, intent, true)
                .build()
        }
        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!![key]
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cardNfcUtils.disableDispatch()
    }


}

