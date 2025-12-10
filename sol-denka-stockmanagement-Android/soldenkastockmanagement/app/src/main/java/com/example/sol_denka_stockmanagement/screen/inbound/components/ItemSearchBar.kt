package com.example.sol_denka_stockmanagement.screen.inbound.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.sol_denka_stockmanagement.ui.theme.brightAzure
import kotlin.collections.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSearchBar(
    keyword: String,
    onKeywordChange: (String) -> Unit,
    results: List<String?>,
    onSelectItem: (String) -> Unit,
) {
    var active by remember { mutableStateOf(false) }
    SearchBar(
        query = keyword,
        onQueryChange = {
            onKeywordChange(it)
            active = true
        },
        onSearch = { active = true },
        active = if (results.isNotEmpty()) active else false,
        onActiveChange = { active = it },

        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = Color.Transparent,
            dividerColor = Color.LightGray,
            inputFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Color.Black
            )
        ),

        modifier = Modifier
            .border(1.dp, color = brightAzure, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.item_hint),
                color = Color.Gray
            )
        },
    ) {
        // DROPDOWN LIST
        if (active && results.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(results) { item ->
                    Text(
                        text = item ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectItem(item ?: "")
                                active = false
                            }
                            .padding(12.dp),
                        color = Color.Black
                    )
                }
            }
        }
    }
}


