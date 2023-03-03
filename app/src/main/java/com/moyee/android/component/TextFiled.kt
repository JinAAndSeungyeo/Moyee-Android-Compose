package com.moyee.android.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moyee.android.ui.theme.MoyeeTheme

data class IconState(
    val vector: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit,
)

@Composable
fun MoyeeTextField(
    onTextChange: (String) -> Unit,
    text: String,
    placeholder: String? = null,
    label: String? = null,
    leadingIcon: IconState? = null,
    trailingIcon: IconState? = null,
    visualTransFormation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    enabled: Boolean = true,
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = placeholder?.executeOrNull { Text(text = it, style = MaterialTheme.typography.button) },
        label = label?.executeOrNull { Text(text = it) },
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        leadingIcon = leadingIcon?.executeOrNull { state ->
            IconButton(onClick = state.onClick) {
                Icon(imageVector = state.vector, contentDescription = state.contentDescription)
            }
        },
        trailingIcon = trailingIcon?.executeOrNull { state ->
            IconButton(onClick = state.onClick) {
                Icon(imageVector = state.vector, contentDescription = state.contentDescription)
            }
        },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            backgroundColor = Color(0xFF332D41),
            textColor = Color.White
        )
    )
}

/**
 * T 값이 null이 아니면 파라미터로 받은 composable function을 실행하고, null이면 null을 반환함
 * */
@Composable
fun <T> T.executeOrNull(e: @Composable (T) -> Unit): (@Composable () -> Unit)? =
    this?.let { data -> { e.invoke(data) } }

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun PreviewMoyeeTextField() {
    MoyeeTheme {
        MoyeeTextField(onTextChange = {}, text = "werqwer", placeholder = "안녕하셈욤?????????",)
    }
}