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
import io.paysky.ui.base.BaseFragment

class ContactlessFragment : BaseFragment() {

    private var mNfcAdapter: NfcAdapter? = null
    private var mTurnNfcDialog: AlertDialog? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get data from bundle.

         resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
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

        openNfcIFNeedCheck()

    }

    private fun openNfcIFNeedCheck() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())

        if (mNfcAdapter != null && !mNfcAdapter!!.isEnabled) {

            showTurnOnNfcDialog()
        } else if (mNfcAdapter != null) {
            try {

               // mCardNfcUtils.enableDispatch()
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
                .setPositiveButton(pos, object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        run {
                            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                            resultLauncher.launch(intent)
                        }
                    }
                })
                .setNegativeButton(neg) { dialogInterface: DialogInterface?, i: Int ->


                    mTurnNfcDialog!!.dismiss()


                }
                .create()
        }
    }
}

