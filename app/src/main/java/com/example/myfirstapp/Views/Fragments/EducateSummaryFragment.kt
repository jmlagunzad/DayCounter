package com.example.myfirstapp.Views.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myfirstapp.Model.Transaction
import com.example.myfirstapp.Presenter.EducatePresenter
import com.example.myfirstapp.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EducateSummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EducateSummaryFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var currView = this.view
    private var presenter : EducatePresenter? = null

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
        return inflater.inflate(R.layout.fragment_educate_summary, container, false)
    }

    fun loadTable(data: MutableList<Transaction>){
        val table = view!!.findViewById<TableLayout>(R.id.educate_table)
        table.removeViews(2, table.getChildCount() - 2)

        var balance : Double = data.sumByDouble { it.amount }

        for(item in data){
            val tableRow = LayoutInflater.from(this.context).inflate(
                R.layout.educate_summary_table_row,
                null,
                false
            )
            tableRow.findViewById<TextView>(R.id.cell_title).text = item.title
            if(item.amount < 0){
                tableRow.findViewById<TextView>(R.id.cell_credit).text = item.amount.toString()
            }
            else{
                tableRow.findViewById<TextView>(R.id.cell_debit).text = item.amount.toString()
            }

            tableRow.findViewById<TextView>(R.id.cell_balance).text = balance.toString()
            balance -= item.amount

            table.addView(tableRow)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currView = view
        presenter = EducatePresenter(currView!!)
        loadTable(presenter!!.getTransactions())
    }

    override fun onResume() {
        super.onResume()
        loadTable(presenter!!.getTransactions())
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EducateSummaryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EducateSummaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}