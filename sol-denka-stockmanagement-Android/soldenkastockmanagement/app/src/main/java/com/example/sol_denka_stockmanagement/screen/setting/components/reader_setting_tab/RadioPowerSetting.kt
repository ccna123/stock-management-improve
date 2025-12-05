package com.example.sol_denka_stockmanagement.screen.setting.components.reader_setting_tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import com.example.sol_denka_stockmanagement.ui.theme.paleSkyBlue
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RadioPowerSetting(
    radioPower: Int,
    radioPowerMw: Int,
    radioPowerSliderPosition: Float,
    onChangeMinPower: () -> Unit,
    onChangeMaxPower: () -> Unit,
    onChangeSlider: (Int) -> Unit,
) {
    SettingItemTitle(
        text = stringResource(R.string.setting_rfid_power),
        textSize = 20.sp
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = "${radioPower}dbm / ${
            NumberFormat.getNumberInstance(Locale.getDefault())
                .format(radioPowerMw)
        }Mw"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(top = 10.dp, start = 15.dp, end = 15.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Red
            ),
            onClick = { onChangeMinPower() }
        ) {
            Text(text = stringResource(R.string.setting_label_min), color = Color.White)
        }
        Slider(
            value = radioPowerSliderPosition,
            onValueChange = { newValue -> onChangeSlider(newValue.toInt()) },
            onValueChangeFinished = {},
            valueRange = 0f..30f,
            colors = SliderDefaults.colors(
                thumbColor = brightAzure,
                activeTrackColor = brightAzure,
                inactiveTrackColor = paleSkyBlue
            ),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .width(180.dp)
        )
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = brightAzure
            ),
            onClick = { onChangeMaxPower() }
        ) {
            Text(text = stringResource(R.string.setting_label_max), color = Color.White)
        }
    }
}