package ru.smartms.rfidreaderwriter.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.smartms.rfidreaderwriter.R
import kotlinx.android.synthetic.main.main_fragment.*
import ru.smartms.rfidreaderwriter.lifecycle.BluetoothScannerLifecycle
import ru.smartms.rfidreaderwriter.lifecycle.RFIDScannerLifecycle
import ru.smartms.rfidreaderwriter.ui.adapter.EpcAdapter


class MainFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: MainViewModel
    private val rfidScannerLifecycle by lazy {
        RFIDScannerLifecycle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(BluetoothScannerLifecycle())
        lifecycle.addObserver(rfidScannerLifecycle)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val adapter = EpcAdapter { scanData ->
            val bundle = Bundle()
            bundle.putString("epc", scanData.barcode)
            findNavController().navigate(R.id.action_mainFragment_to_scanDataDetailsFragment)
        }
        viewModel.getAllScanData().observe(this, Observer { scanDataList -> adapter.submitList(scanDataList) })
        rv_epc.adapter = adapter
        switch_power.setOnCheckedChangeListener { _, isChecked ->
            rfidScannerLifecycle.onOffRFID(isChecked)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start_scan -> {
                rfidScannerLifecycle.startInventory()
                if (rfidScannerLifecycle.runFlag) {
                    btn_start_scan.setText(R.string.start_scan)
                } else {
                    btn_start_scan.setText(R.string.stop_scan)
                }
            }
        }
    }
}
