package io.oitech.med_application.fragments.login

import io.oitech.med_application.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NumberVerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NumberVerificationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(io.oitech.med_application.R.layout.fragment_number_verification, container, false)
    }

    lateinit var editText1:EditText
    lateinit var editText2:EditText
    lateinit var editText3:EditText
    lateinit var editText4:EditText
    lateinit var editText5:EditText
    lateinit var editText6:EditText



    //val editTexts =mutale


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//
//        editText1 = view.findViewById(R.id.otpEdit1) as EditText
//        editText2 = view.findViewById(R.id.otpEdit2) as EditText
//        editText3 = view.findViewById(R.id.otpEdit3) as EditText
//        editText4 = view.findViewById(R.id.otpEdit4) as EditText
//        editTexts = arrayOf<EditText>(editText1, editText2, editText3, editText4)
//
//        editText1.addTextChangedListener(PinTextWatcher(0))
//        editText2.addTextChangedListener(PinTextWatcher(1))
//        editText3.addTextChangedListener(PinTextWatcher(2))
//        editText4.addTextChangedListener(PinTextWatcher(3))
//
//        editText1.setOnKeyListener(PinOnKeyListener(0))
//        editText2.setOnKeyListener(PinOnKeyListener(1))
//        editText3.setOnKeyListener(PinOnKeyListener(2))
//        editText4.setOnKeyListener(PinOnKeyListener(3))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NumberVerificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NumberVerificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}