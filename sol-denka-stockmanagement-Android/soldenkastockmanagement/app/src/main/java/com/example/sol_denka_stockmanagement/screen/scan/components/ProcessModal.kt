package com.example.sol_denka_stockmanagement.screen.scan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.constant.ProcessMethod
import com.example.sol_denka_stockmanagement.constant.SelectTitle
import com.example.sol_denka_stockmanagement.model.process.ProcessTypeModel
import com.example.sol_denka_stockmanagement.share.dialog.AppDialog
import com.example.sol_denka_stockmanagement.share.ButtonContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessModal(
    processTypeList: List<ProcessTypeModel>,
    showModalProcessMethod: Boolean,
    selectedCount: Int,
    chosenMethod: String,
    onChooseMethod: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onApplyBulk: () -> Unit
) {
    if (showModalProcessMethod.not()) return
    AppDialog{
        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {
            Text(
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                text = SelectTitle.SelectProcessMethod.displayName
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(R.string.bulk_apply_item_number, selectedCount))
            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(processTypeList) { method ->
                    ProcessGridItem(
                        method = method,
                        isSelected = method.processName == chosenMethod,
                        onClick = { onChooseMethod(method.processName) }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ButtonContainer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    containerColor = Color.Red,
                    buttonText = stringResource(R.string.cancel),
                    onClick = {
                        onDismissRequest()
                    }
                )
                ButtonContainer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    buttonText = stringResource(R.string.bulk_apply),
                    onClick = { onApplyBulk() }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}