package com.jamal.composeprefssample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jamal.composeprefssample.ui.theme.ComposePrefsSampleTheme

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePrefsSampleTheme {
                SettingsScreen()
            }
        }
    }
}
