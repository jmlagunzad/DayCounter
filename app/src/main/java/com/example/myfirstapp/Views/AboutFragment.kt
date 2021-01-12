package com.example.myfirstapp.Views

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import com.example.myfirstapp.R
import kotlinx.android.synthetic.main.fragment_about.constraintLayout
import kotlinx.android.synthetic.main.fragment_about.fb_button
import kotlinx.android.synthetic.main.fragment_about.joker_icon
import kotlinx.android.synthetic.main.fragment_about.ps_button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fb_button.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/jm.lagunzad/")))
        }
        ps_button.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://psntrophyleaders.com/user/view/SlaughterDoi#games")))
        }

        //Initialize Constraint Layouts
        val constraintsSet1 = ConstraintSet()
        val constraintsSet2 = ConstraintSet()

        constraintsSet2.clone(this.context!!, R.layout.fragment_about_large)
        constraintsSet1.clone(constraintLayout)

        var large = false

        joker_icon.setOnClickListener{
            if(!large) {
                TransitionManager.beginDelayedTransition(constraintLayout)
                constraintsSet2.applyTo(constraintLayout)
                large = true
            }else{
                TransitionManager.beginDelayedTransition(constraintLayout)
                constraintsSet1.applyTo(constraintLayout)
                large = false
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}