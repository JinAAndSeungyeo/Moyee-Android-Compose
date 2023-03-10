package com.moyee.android.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moyee.android.ui.theme.MoyeeTheme
import com.moyee.android.util.executeOrNull
import com.moyee.android.R

data class IconState(
    val vector: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit,
)

data class TextFieldState(
    val value: String,
    val onTextChange: (String) -> Unit,
)

@Composable
fun MoyeeTextField(
    textFieldState: TextFieldState,
    placeholder: String? = null,
    label: String? = null,
    errorMessage: String? = null,
    leadingIcon: IconState? = null,
    trailingIcon: IconState? = null,
    visualTransFormation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    enabled: Boolean = true,
) {
    val isError by rememberSaveable { mutableStateOf(errorMessage.isNullOrBlank().not()) }

    Column {
        TextField(
            value = textFieldState.value,
            onValueChange = textFieldState.onTextChange,
            placeholder = placeholder?.executeOrNull {
                Text(
                    text = it,
                    style = MaterialTheme.typography.button
                )
            },
            label = label?.executeOrNull { Text(text = it) },
            isError = isError,
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
                errorIndicatorColor = Color.Transparent,
                backgroundColor = Color(0xFF332D41),
                textColor = Color.White
            )
        )
        if (isError) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun PreviewMoyeeTextField() {
    MoyeeTheme {
        MoyeeTextField(
            textFieldState = TextFieldState(value = "ㅌㅅㅌ", onTextChange = {}),
            placeholder = "안녕하셈욤?????????"
        )
    }
}

@Composable
fun PasswordTextField(
    passwordState: TextFieldState,
    errorMessage: String? = null,
) {
    var hideVisible by remember { mutableStateOf(true) }

    val (trailingIconVector, trailingIconContentDescription) =
        if (hideVisible) {
            Pair(Icons.Outlined.Visibility, stringResource(R.string.hide_password))
        } else {
            Pair(Icons.Outlined.VisibilityOff, stringResource(R.string.show_password))
        }

    MoyeeTextField(
        textFieldState = passwordState,
        placeholder = stringResource(R.string.password),
        trailingIcon = IconState(
            vector = trailingIconVector,
            contentDescription = trailingIconContentDescription,
            onClick = { hideVisible = hideVisible.not() }),
        visualTransFormation = if (hideVisible) PasswordVisualTransformation() else VisualTransformation.None,
        errorMessage = errorMessage
    )
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun PreviewMoyeeErrorTextField() {
    MoyeeTheme {
        MoyeeTextField(
            textFieldState = TextFieldState(value = "ㅌㅅㅌ", onTextChange = {}),
            placeholder = "안녕하셈욤?????????",
            errorMessage = "에러에러에러"
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun PreviewPasswordTextField() {
    MoyeeTheme {
        PasswordTextField(
            passwordState = TextFieldState(value = "ㅇㅅㅍ", onTextChange = {})
        )
    }
}