package com.moyee.android.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moyee.android.ui.theme.MoyeeTheme

@Composable
fun MoyeeMainButton(
    onClick: () -> Unit,
    text: String,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50)
    ) {
        Text(text = text)
    }
}

@Composable
fun MoyeeSubButton(
    onClick: () -> Unit,
    text: String,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondaryVariant
        ),
        shape = RoundedCornerShape(50)
    ) {
        Text(text = text)
    }
}

@Composable
fun MoyeeTextButton(
    onClick: () -> Unit,
    text: String,
) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            color = MaterialTheme.colors.secondaryVariant
        )
    }
}

@Composable
@Preview
private fun PreviewMoyeeMainButton() {
    MoyeeTheme {
        MoyeeMainButton(onClick = {}, text = "확인")
    }
}

@Composable
@Preview
private fun PreviewMoyeeSubButton() {
    MoyeeTheme {
        MoyeeSubButton(onClick = {}, text = "화깅")
    }
}

@Composable
@Preview
private fun PreviewMoyeeTextButton() {
    MoyeeTheme {
        MoyeeTextButton(onClick = {}, text = "화귄?")
    }
}