package com.georgcantor.counter.view.fragment

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.georgcantor.counter.R
import com.georgcantor.counter.model.Day
import com.georgcantor.counter.viewmodel.AddViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class AddFragment : Fragment() {

    private lateinit var viewModel: AddViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveButton.isEnabled = false

        dateEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                saveButton.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        saveButton.setOnClickListener {
            viewModel.addDay(
                Day(
                    id = dateEditText.text.toString(),
                    hours = hoursEditText.text.toString().toFloat()
                )
            )
            Handler().postDelayed({
                requireActivity().onBackPressed()
            }, 500)
        }
    }

}