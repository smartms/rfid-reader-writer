package ru.smartms.rfidreaderwriter.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.magicrf.uhfreaderlib.reader.Tools
import kotlinx.android.synthetic.main.scan_data_details_fragment.*
import ru.smartms.rfidreaderwriter.MainActivity
import ru.smartms.rfidreaderwriter.R
import ru.smartms.rfidreaderwriter.lifecycle.BluetoothScannerLifecycle

class ScanDataDetailsFragment : Fragment(), View.OnClickListener {

    companion object {
        const val PASS_NULL = "00000000"
    }

    private lateinit var viewModel: ScanDataDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scan_data_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(BluetoothScannerLifecycle())
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this).get(ScanDataDetailsViewModel::class.java)
        arguments?.getString("epc", null)?.let {
            viewModel.setEPC(it)
            tiet_epc_hex.setText(it)
            tiet_read_epc.setText(String(Tools.HexString2Bytes(it)))
        }
        viewModel.epcHex.observe(this, Observer { epcHex ->
            tiet_epc_hex.setText(epcHex)
        })
        viewModel.epc.observe(this, Observer { epc ->
            tiet_read_epc.setText(epc)
        })
        viewModel.getErrorMessage()?.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                if (!errorMessage.isEmpty()) {
                    Toast.makeText(context, errorMessage[0].message, Toast.LENGTH_SHORT).show()
                    viewModel.deleteErrorMessage()
                }
            }
        })
        viewModel.getScanDataBarcode()?.observe(this, Observer { scanData ->
            if (scanData != null) {
                tiet_read_epc.setText(scanData.barcode)
                viewModel.deleteScanDataBarcode()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_read.setOnClickListener(this)
        btn_write.setOnClickListener(this)
        tiet_password.setText(PASS_NULL)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_read -> {
                viewModel.readEpc(tiet_password.text.toString())
            }
            R.id.btn_write -> {
                viewModel.writeEpc(tiet_read_epc.text.toString(), tiet_password.text.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return true
    }
}
