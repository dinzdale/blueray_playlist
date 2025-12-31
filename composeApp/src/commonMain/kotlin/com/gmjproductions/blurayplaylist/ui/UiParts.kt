package com.gmjproductions.blurayplaylist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gmjproductions.blurayplaylist.theme.AppTheme
import com.gmjproductions.blurayplaylist.theme.BlueRayBackground
import com.gmjproductions.blurayplaylist.theme.BlueRayPrimary
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ActionButton(
    label: String,
    tooltipText: String,
    enabled: Boolean = true,
    containerColor: Color = BlueRayPrimary,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    TooltipBox(
        modifier = Modifier.wrapContentSize(),
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip(containerColor = Color.DarkGray) {
                Text(tooltipText, color = Color.White)
            }
        },
        state = rememberTooltipState()
    ) {
        Button(
            enabled = enabled,
            onClick = onClick,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = Color.White
            ),
            interactionSource = interactionSource,
            modifier = Modifier.wrapContentWidth().height(40.dp)
        ) {
            Text(
                modifier = Modifier.wrapContentSize().padding(2.dp),
                text = label,
                style = TextStyle(color = Color.White, textAlign = TextAlign.Left)
            )
        }
    }
}

@Composable
@Preview
fun PreviewButton() {
    AppTheme {
        ActionButton("A", "I'm just a little black rain cloud") {
        }
    }
}

// Remove or update other material (M2) imports if you migrate icons/buttons too


@Composable
fun HintBox(text: String) {
    AppTheme {
        Box(
            modifier = Modifier.width(80.dp).wrapContentHeight().background(Color.Black),
            Alignment.Center
        ) {
            Text(text = text, modifier = Modifier.wrapContentSize(), color = Color.White)
        }
    }
}

@Composable
fun ItemUpdate(
    text: String,
    globalConvertSelectedColor: Color = BlueRayPrimary,
    globalSaveSelectedColor: Color = BlueRayPrimary,
    onConvert: (String) -> Unit,
    onUnDo: (String) -> String,
    onSave: (String) -> Unit,
    onGlobalConvert: (String) -> Unit,
    onUserEntryUpdate: ((String) -> Unit)? = null
) {

    var value by remember { mutableStateOf(text) }

    LaunchedEffect(text) {
        value = text
    }
    LaunchedEffect(value) {
        if (value != text) {
            onUserEntryUpdate?.invoke(value)
        }
    }

    Row(
        Modifier.fillMaxWidth().wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value,
            { value = it },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.LightGray,
                textColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth(.60f).wrapContentHeight(),
            singleLine = true
        )
        Row(
            Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                "C", "Convert title, applies regexp to title.", onClick =
                    { onConvert(value) }
            )
            Spacer(Modifier.width(2.dp))
            ActionButton("U", "Undo manual text field changes.", onClick = {
                value = onUnDo(value)
            })
            Spacer(Modifier.width(2.dp))
            ActionButton("S", "Save updates for this entry.", containerColor = globalSaveSelectedColor, onClick = { onSave(value) })
            Spacer(Modifier.width(2.dp))
            ActionButton(
                "G",
                "Global conversion. Apply conversion rule for all entries of this type.",
                containerColor = globalConvertSelectedColor,
                onClick = { onGlobalConvert(value) }
            )
            Spacer(Modifier.width(2.dp))

        }
    }
}

val resolutions = listOf("1280X720", "1920X1280")

@Composable
fun resolutionSelections(onSelection: (String) -> Unit) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(resolutions[0]) }
    Row(
        Modifier.selectableGroup().wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "uncrop resolutions:",
            Modifier.padding(start = 10.dp),
            style = TextStyle(color = Color.White)
        )
        resolutions.forEach { text ->
            Row(
                Modifier.wrapContentSize().height(56.dp).selectable(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                        onSelection(text)
                    },
                    role = Role.RadioButton
                ).padding(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = text,
                    style = TextStyle(color = Color.White)
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewItemUpdate() {
    MaterialTheme {
        Surface() {
            Box(Modifier.fillMaxWidth()) {
                ItemUpdate(
                    "Initial value here",
                    BlueRayPrimary,
                    BlueRayPrimary,
                    { "convert" },
                    { "undow" },
                    {},
                    { "Global" })
            }

        }
    }
}

@Composable
@Preview
fun PreviewResolutionSelection() {
    Surface(Modifier.wrapContentSize()) {
        Box(
            Modifier.wrapContentSize().background(BlueRayBackground),
            contentAlignment = Alignment.TopStart
        ) {
            resolutionSelections {
                print("Selected: $it")
            }
        }
    }
}
