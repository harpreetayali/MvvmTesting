package com.harpreetayali.mvvmexample.mainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.harpreetayali.mvvmexample.R
import com.harpreetayali.mvvmexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainActivityFactory: MainActivityFactory
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        mainActivityFactory = MainActivityFactory(this)

        mainActivityViewModel =
            ViewModelProvider(this, mainActivityFactory).get(MainActivityViewModel::class.java)

        activityMainBinding.mainActivityVM = mainActivityViewModel


    }
}