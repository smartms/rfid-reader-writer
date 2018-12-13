package ru.smartms.rfidreaderwriter.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.scan_data_details_fragment.*
import ru.smartms.rfidreaderwriter.R

class ScanDataDetailsFragment : Fragment(), View.OnClickListener {

    companion object {
        const val PASS_NULL = "00000000"
    }

    private lateinit var viewModel: ScanDataDetailsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scan_data_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScanDataDetailsViewModel::class.java)
        arguments?.getString("epc", null)?.let {
            viewModel.setEPC(it)
        }
        viewModel.epcHex.observe(this, Observer { epcHex ->
            tiet_epc_hex.setText(epcHex)
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

           }
       }
    }
}
