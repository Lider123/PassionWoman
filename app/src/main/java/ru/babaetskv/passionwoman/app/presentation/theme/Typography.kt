package ru.babaetskv.passionwoman.app.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.babaetskv.passionwoman.app.R

private val LoraFontFamily = FontFamily(
    Font(R.font.lora_regular, style = FontStyle.Normal, weight = FontWeight.Normal),
    Font(R.font.lora_italic, style = FontStyle.Italic, weight = FontWeight.Normal),
    Font(R.font.lora_bold, style = FontStyle.Normal, weight = FontWeight.Bold),
    Font(R.font.lora_bold_italic, style = FontStyle.Italic, weight = FontWeight.Bold),
    Font(R.font.lora_medium, style = FontStyle.Normal, weight = FontWeight.Medium),
    Font(R.font.lora_medium_italic, style = FontStyle.Italic, weight = FontWeight.Medium),
    Font(R.font.lora_semi_bold, style = FontStyle.Normal, weight = FontWeight.SemiBold),
    Font(R.font.lora_semi_bold_italic, style = FontStyle.Italic, weight = FontWeight.SemiBold),
)

val PassionWomanTypography = Typography(
    h4 = TextStyle(
        fontSize = 28.sp,
        fontFamily = LoraFontFamily,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold
    ),
    h5 = TextStyle(
        fontSize = 22.sp,
        fontFamily = LoraFontFamily,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold
    ),
    h6 = TextStyle(
        fontSize = 18.sp,
        fontFamily = LoraFontFamily,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold
    ),
    subtitle1 = TextStyle(
        fontSize = 16.sp,
        fontFamily = LoraFontFamily,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        fontFamily = LoraFontFamily,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal
    ),
    button = TextStyle(
        fontSize = 18.sp,
        fontFamily = LoraFontFamily,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold
    )
)