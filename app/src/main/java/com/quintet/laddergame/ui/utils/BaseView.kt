package com.quintet.laddergame.ui.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.quintet.laddergame.ui.theme.MontserratRegular
import com.quintet.laddergame.ui.theme.ScreenTitleFontColor

@Composable
fun BaseText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int,
    fontColor: Color = ScreenTitleFontColor,
    fontWeight: Int = 400,
    fontFamily: FontFamily = MontserratRegular,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize.sp,
            color = fontColor,
            fontWeight = FontWeight(fontWeight),
            fontFamily = fontFamily,
            textAlign = textAlign,
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        ),
        modifier = modifier
    )
}