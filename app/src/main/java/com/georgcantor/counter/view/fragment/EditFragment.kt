package com.georgcantor.counter.view.fragment

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.georgcantor.counter.R
import com.georgcantor.counter.viewmodel.EditViewModel
import kotlinx.android.synthetic.main.fragment_edit.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.core.parameter.parametersOf

class EditFragment : Fragment() {

    private lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getSharedViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_edit, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hours = hoursEditText.text

        applyButton.isEnabled = false

        hoursEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                applyButton.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        applyButton.setOnClickListener {
            viewModel.mutableId.observe(viewLifecycleOwner, Observer {
                viewModel.updateDayHours(it, hours.toString().toFloat())
            })
            Handler().postDelayed({
                requireActivity().onBackPressed()
            }, 500)
        }
    }

}