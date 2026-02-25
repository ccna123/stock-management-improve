package com.example.sol_denka_stockmanagement.screen.inbound.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure

@Composable
fun ItemSearchBar(
    keyword: String,
    onKeywordChange: (String) -> Unit,
    results: List<ItemTypeMasterModel>,
    onSelectItem: (String, Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = keyword,
            onValueChange = {
                onKeywordChange(it)
                expanded = true
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.item_hint),
                    color = Color.Gray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = brightAzure
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded)
                        Icons.Default.ArrowDropUp
                    else
                        Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = brightAzure,
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            expanded = !expanded
                        }
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = brightAzure,
                unfocusedBorderColor = brightAzure,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        if (expanded && results.isNotEmpty()) {

            Spacer(modifier = Modifier.height(6.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 220.dp)
                ) {
                    items(results) { item ->

                        Text(
                            text = item.itemTypeName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectItem(
                                        item.itemTypeName,
                                        item.itemTypeId
                                    )
                                    expanded = false
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}