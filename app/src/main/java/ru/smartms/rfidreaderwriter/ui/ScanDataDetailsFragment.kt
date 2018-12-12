package ru.smartms.rfidreaderwriter.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.smartms.rfidreaderwriter.R

class ScanDataDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ScanDataDetailsFragment()
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
        // TODO: Use the ViewModel
    }

}
