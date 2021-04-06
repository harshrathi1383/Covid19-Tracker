package com.example.covid19tracker

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_list.view.*

class CustomAdapter(private val list: List<StatewiseItem>) : BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_list,parent,false)
        val item = list[position]
        view.confirmedTv.text = colorMyText("${item.confirmed}\n ^ ${item.deltaconfirmed?:0}",item.confirmed?.length?:0,
            Color.parseColor("#D32F2F"))
        view.activeTv.text = colorMyText("${item.active}\n ^ ${item.deltaactive?:0}",item.confirmed?.length?:0,
            Color.parseColor("#1976D2"))
        view.deceasedTv.text = colorMyText("${item.deaths}\n ^ ${item.deltadeaths?:0}",item.confirmed?.length?:0,
            Color.parseColor("#FBC02D"))
        view.recoveredTv.text = colorMyText("${item.recovered}\n ^ ${item.deltarecovered?:0}",item.confirmed?.length?:0,
            Color.parseColor("#388E3C"))
        view.stateTv.text = item.state


        return view
    }
    private fun colorMyText(inputText:String,startIndex:Int,textColor:Int): Spannable {
        val outPutColoredText: Spannable = SpannableString(inputText)
        outPutColoredText.setSpan(
            ForegroundColorSpan(textColor), startIndex, inputText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return outPutColoredText
    }
    override fun getItem(position: Int) = list[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = list.size

}