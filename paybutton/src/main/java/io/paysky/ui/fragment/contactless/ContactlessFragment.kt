package io.paysky.ui.fragment.contactless

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.paybutton.R
import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils
import io.paysky.ui.base.BaseFragment


class ContactlessFragment : BaseFragment()  {

    private var mNfcAdapter: NfcAdapter? = null
    private var mTurnNfcDialog: AlertDialog? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) {

                  Log.d("mNfcAdapter","aaaa "+it.resultCode)
            if (it.resultCode == Activity.RESULT_OK) {

            } else if (it.resultCode == Activity.RESULT_CANCELED) {
                mNfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())

                Log.d("mNfcAdapter","aaaa "+it.resultCode)

                if (mNfcAdapter != null && !mNfcAdapter!!.isEnabled) {
                    Log.d("mNfcAdapter","aaaa is not enabled")

                    showTurnOnNfcDialog()
                }
                else if(mNfcAdapter == null){
                    Log.d("mNfcAdapter","aaaa is null enabled")

                }
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




}

