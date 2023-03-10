package com.moyee.android.feat.user

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moyee.android.R
import com.moyee.android.component.*
import com.moyee.android.feat.user.register.REGISTER_PAGER_COUNT
import com.moyee.android.ui.theme.MoyeeTheme

/**
 * 로그인/회원가입 화면 구분을 위한 enum class
 *
 * @param nameResource 해당 type의 이름의 resource id
 * @param buttonLabelResources 버튼 아래 label의 Text, Text Button resource id 집합
 * */
enum class UserScreenType(
    @StringRes val nameResource: Int,
    @StringRes val buttonLabelResources: Pair<Int, Int>,
) {
    Register(R.string.register, Pair(R.string.already_member, R.string.login)),
    LogIn(R.string.login, Pair(R.string.not_member, R.string.register))
}

/**
 * 로그인/회원가입 공통 화면
 *
 * @param type 로그인/회원가입 화면 구분
 * @param navigateRegisterOrLogin 하단의 Label Text Button을 클릭했을 때 실행되는 이벤트로, 로그인 또는 회원가입으로 이동함
 * @param onFooterButtonClick 하단의 버튼 클릭 이벤트 (다음 pager로 이동하거나 로그인/회원가입 기능 수행)
 * @param footerButtonText 하단의 버튼 텍스트
 * @param currentTabIndex 현재 페이지 (viewPager 기능이 필요 없으면 값을 보내지 않음)
 * */
@Composable
fun BaseUserScreen(
    type: UserScreenType,
    navigateRegisterOrLogin: () -> Unit,
    onBackPressure: () -> Unit,
    onFooterButtonClick: () -> Unit,
    footerButtonText: String = stringResource(R.string.next),
    currentTabIndex: Int = -1,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(36.dp)
    ) {
        UserHeader(
            titleText = stringResource(type.nameResource),
            onBackPressure = onBackPressure,
            isVisibleIndicator = currentTabIndex != -1,
            currentTabIndex = currentTabIndex
        )
        Spacer(Modifier.height(100.dp))
        content.invoke()
        Spacer(Modifier.weight(1f))
        UserFooterButton(
            onClick = onFooterButtonClick,
            navigateRegisterOrLogin = navigateRegisterOrLogin,
            text = footerButtonText,
            isVisibleLabel = currentTabIndex.isFirstIndex(),
            labelResources = type.buttonLabelResources,
        )
    }
}

/**
 * 로그인/회원가입 공통 화면 Header - 뒤로 가기 버튼, title, Pager Indicator 포함 (자세한 건 피그마 참고)
 *
 * @param titleText 해당 화면의 제목 (로그인/회원가입)
 * @param isVisibleIndicator Pager indicator 표시 여부
 * @param currentTabIndex indicator 사용시 현재 탭
 * */
@Composable
private fun UserHeader(
    onBackPressure: () -> Unit,
    titleText: String,
    isVisibleIndicator: Boolean,
    currentTabIndex: Int,
) {
    Column {
        Row {
            IconButton(onClick = onBackPressure, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
            }
            Spacer(Modifier.weight(1f))
            if (isVisibleIndicator) {
                MoyeeIndicator(count = REGISTER_PAGER_COUNT, selectedIndex = currentTabIndex)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(text = titleText, style = MaterialTheme.typography.h2)
    }
}

/**
 * 로그인/회원가입 공통 화면 Footer 버튼 - Button, Label 포함 (자세한 건 피그마 참고)
 *
 * @param navigateRegisterOrLogin label TextButton을 클릭했을 때 회원가입/로그인으로 이동하는 이벤트
 * @param isVisibleLabel 버튼 아래의 라벨 표시/숨김 여부
 * @param labelResources 버튼 아래의 라벨 resource id
 * */
@Composable
private fun UserFooterButton(
    onClick: () -> Unit,
    navigateRegisterOrLogin: () -> Unit,
    text: String,
    isVisibleLabel: Boolean,
    labelResources: Pair<Int, Int>,
) {
    MoyeeMainButton(onClick = onClick, text = text)

    if (isVisibleLabel) {
        Row {
            val (labelResId, labelTextButtonResId) = labelResources
            Text(text = stringResource(id = labelResId))
            MoyeeTextButton(
                onClick = navigateRegisterOrLogin,
                text = stringResource(id = labelTextButtonResId)
            )
        }
    }
}

private fun Int.isFirstIndex() = this == 0

@Composable
@Preview(showSystemUi = true)
fun PreviewBaseUserScreen() {
    MoyeeTheme {
        BaseUserScreen(
            type = UserScreenType.Register,
            onFooterButtonClick = {},
            navigateRegisterOrLogin = {},
            onBackPressure = {},
            currentTabIndex = 1
        ) {
            MoyeeTextField(textFieldState = TextFieldState("sdasd", {}))
        }
    }
}