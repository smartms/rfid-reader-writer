package ru.smartms.rfidreaderwriter.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        setupRV()
        switch_start_scan.setOnCheckedChangeListener { _, isChecked ->
            rfidScannerLifecycle.startInventory(isChecked)
        }
        switch_power.setOnCheckedChangeListener { _, isChecked ->
            rfidScannerLifecycle.onOffRFID(isChecked)
        }

    }

    private fun setupRV() {
        val adapter = EpcAdapter { scanData ->
            val bundle = Bundle()
            bundle.putString("epc", scanData.barcode)
            findNavController().navigate(R.id.action_mainFragment_to_scanDataDetailsFragment, bundle)
        }
        viewModel.getAllScanData().observe(this, Observer { scanDataList ->
            adapter.submitList(scanDataList)
        })
        rv_epc.adapter = adapter
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        rv_epc.layoutManager = LinearLayoutManager(activity?.applicationContext)
        rv_epc.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(
            rv_epc.context,
            layoutManager.orientation
        )
        rv_epc.addItemDecoration(dividerItemDecoration)
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(rv_epc)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
        }
    }
}
