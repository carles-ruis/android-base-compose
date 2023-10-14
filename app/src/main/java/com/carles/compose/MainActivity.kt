package com.carles.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.carles.compose.ui.composables.MainApp
import com.carles.compose.ui.theme.HyruleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HyruleTheme {
                MainApp()
            }
        }
    }
}