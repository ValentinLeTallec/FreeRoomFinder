package com.n7.freeroomfinder

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.view_result_card.view.*

class ResultCard @JvmOverloads constructor(
    context: Context,
    title: String,
    content: String,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CardView(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_result_card, this, true)

        titleView.text = title
        contentView.text = content
    }
}