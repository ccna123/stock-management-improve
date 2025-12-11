package com.example.sol_denka_stockmanagement.screen.inbound.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sol_denka_stockmanagement.R
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSearchBar(
    keyword: String,
    onKeywordChange: (String) -> Unit,
    results: List<ItemTypeMasterModel>,
    onSelectItem: (String, Int) -> Unit,
) {
    var active by remember { mutableStateOf(false) }
    val colors1 = SearchBarDefaults.colors(
        containerColor = Color.Transparent,
        dividerColor = Color.LightGray,
        inputFieldColors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            cursorColor = Color.Black
        )
    )
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = keyword,
                onQueryChange = {
                    onKeywordChange(it)
                    active = true
                },
                onSearch = { active = true },
                expanded = active,
                onExpandedChange = { active = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.item_hint),
                        color = Color.Gray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (active) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { active = !active }
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                },
                colors = colors1.inputFieldColors,
            )
        },
        expanded = active,
        onExpandedChange = { active = it },
        modifier = Modifier
            .border(1.dp, color = brightAzure, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth(),
        shape = SearchBarDefaults.inputFieldShape,
        colors = colors1,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        windowInsets = SearchBarDefaults.windowInsets,
        content =
            {
                if (active && results.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(results) { item ->
                            Text(
                                text = item.itemTypeName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectItem(item.itemTypeName, item.itemTypeId)
                                        active = false
                                    }
                                    .padding(12.dp),
                                color = Color.Black
                            )
                        }
                    }
                }
            }
    )
}


