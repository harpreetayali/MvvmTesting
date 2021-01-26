package com.harpreetayali.mvvmexample.mainActivity

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class MainActivityViewModel(val context: Context) :ViewModel()
{
    var name = ObservableField("")
    init {
        name.set("Harpreet Ayali")
    }

    fun onClickBtn(view: View) {
        Toast.makeText(context, "Button Click", Toast.LENGTH_LONG).show()
    }
}