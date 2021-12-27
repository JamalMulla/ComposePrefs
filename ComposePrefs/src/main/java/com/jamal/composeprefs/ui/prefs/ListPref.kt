package com.jamal.composeprefs.ui.prefs

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jamal.composeprefs.ui.LocalPrefsDataStore
import kotlinx.coroutines.launch
import java.lang.Exception

//TODO useSelectedAsSummary

/**
 * Preference that shows a list of entries in a Dialog where a single entry can be selected at one time.
 *
 * @param key Key used to identify this Pref in the DataStore
 * @param title Main text which describes the Pref. Shown above the summary and in the Dialog.
 * @param modifier Modifier applied to the Text aspect of this Pref
 * @param summary Used to give some more information about what this Pref is for
 * @param defaultValue Default selected key if this Pref hasn't been saved already. Otherwise the value from the dataStore is used.
 * @param onValueChange Will be called with the selected key when an item is selected
 * @param useSelectedAsSummary If true, uses the current selected item as the summary
 * @param dialogBackgroundColor Background color of the Dialog
 * @param textColor Text colour of the [title] and [summary]
 * @param enabled If false, this Pref cannot be clicked and the Dialog cannot be shown.
 * @param entries Map of keys to values for entries that should be shown in the Dialog.
 */
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ListPref(
    key: String,
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    defaultValue: String? = null,
    onValueChange: ((String) -> Unit)? = null,
    useSelectedAsSummary: Boolean = false,
    dialogBackgroundColor: Color = MaterialTheme.colors.surface,
    textColor: Color = MaterialTheme.colors.onBackground,
    enabled: Boolean = true,
    entries: Map<String, String> = mapOf()
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val selectionKey = stringPreferencesKey(key)
    val scope = rememberCoroutineScope()

    // need to observe state with some sort of flow maybe? this also works
    val datastore = LocalPrefsDataStore.current
    val prefs by remember { datastore.data }.collectAsState(initial = null)

    var selected = defaultValue
    prefs?.get(selectionKey)?.also { selected = it } // starting value if it exists in datastore

    fun edit(current: Map.Entry<String, String>) = run {
        scope.launch {
            try {
                datastore.edit { preferences ->
                    preferences[selectionKey] = current.key
                }
                onValueChange?.invoke(current.key)
                showDialog = false
            } catch (e: Exception) {
                Log.e("ListPref", "Could not write pref $key to database. ${e.printStackTrace()}")
            }
        }
    }

    TextPref(
        title = title,
        summary = when {
            useSelectedAsSummary && selected != null -> entries[selected]
            useSelectedAsSummary && selected == null -> "Not Set"
            else -> summary
        },
        modifier = modifier,
        textColor = textColor,
        enabled = true,
        onClick = { if (enabled) showDialog = !showDialog },
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = title) },
            text = {
                Column {
                    entries.forEach { current ->
                        val isSelected = selected == current.key
                        val onSelected = {
                            edit(current)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    onClick = { if (!isSelected) onSelected() }
                                ),
                            verticalAlignment = CenterVertically,
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { if (!isSelected) onSelected() },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary)
                            )
                            Text(
                                text = current.value,
                                style = MaterialTheme.typography.body2,
                                color = textColor
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false },
                ) {
                    Text("Cancel", style = MaterialTheme.typography.body1)
                }

            },
            backgroundColor = dialogBackgroundColor,
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            ),
        )
    }


}