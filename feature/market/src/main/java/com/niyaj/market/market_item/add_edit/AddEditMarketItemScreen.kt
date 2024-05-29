/*
 *      Copyright 2024 Sk Niyaj Ali
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *              http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package com.niyaj.market.market_item.add_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.niyaj.common.tags.MarketListTestTags.ADD_EDIT_MARKET_ITEM_BUTTON
import com.niyaj.common.tags.MarketListTestTags.CREATE_NEW_ITEM
import com.niyaj.common.tags.MarketListTestTags.MARKET_ITEM_MEASURE_ERROR_TAG
import com.niyaj.common.tags.MarketListTestTags.MARKET_ITEM_MEASURE_FIELD
import com.niyaj.common.tags.MarketListTestTags.MARKET_ITEM_NAME_ERROR_TAG
import com.niyaj.common.tags.MarketListTestTags.MARKET_ITEM_NAME_FIELD
import com.niyaj.common.tags.MarketListTestTags.MARKET_ITEM_PRICE_ERROR_TAG
import com.niyaj.common.tags.MarketListTestTags.MARKET_ITEM_PRICE_FIELD
import com.niyaj.common.tags.MarketListTestTags.MARKET_ITEM_TYPE_ERROR_TAG
import com.niyaj.common.tags.MarketListTestTags.MARKET_ITEM_TYPE_FIELD
import com.niyaj.common.tags.MarketListTestTags.MARKET_LIST_ITEM_DESC
import com.niyaj.common.tags.MarketListTestTags.UPDATE_ITEM
import com.niyaj.designsystem.icon.PoposIcons
import com.niyaj.designsystem.theme.SpaceMedium
import com.niyaj.designsystem.theme.SpaceSmall
import com.niyaj.market.destinations.AddEditMarketTypeScreenDestination
import com.niyaj.market.destinations.AddEditMeasureUnitScreenDestination
import com.niyaj.model.MarketTypeIdAndName
import com.niyaj.ui.components.StandardButton
import com.niyaj.ui.components.StandardOutlinedTextField
import com.niyaj.ui.components.StandardScaffoldRouteNew
import com.niyaj.ui.utils.TrackScreenViewEvent
import com.niyaj.ui.utils.TrackScrollJank
import com.niyaj.ui.utils.UiEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AddEditMarketItemScreen(
    itemId: Int = 0,
    navigator: DestinationsNavigator,
    viewModel: AddEditMarketItemViewModel = hiltViewModel(),
    resultBackNavigator: ResultBackNavigator<String>,
) {
    val lazyListState = rememberLazyListState()
    val typeError = viewModel.typeError.collectAsStateWithLifecycle().value
    val nameError = viewModel.nameError.collectAsStateWithLifecycle().value
    val amountError = viewModel.priceError.collectAsStateWithLifecycle().value
    val unitError = viewModel.unitError.collectAsStateWithLifecycle().value

    val enableBtn = listOf(typeError, nameError, amountError, unitError).all {
        it == null
    }

    val event = viewModel.eventFlow.collectAsStateWithLifecycle(initialValue = null).value

    LaunchedEffect(key1 = event) {
        event?.let { data ->
            when (data) {
                is UiEvent.OnError -> {
                    resultBackNavigator.navigateBack(data.errorMessage)
                }

                is UiEvent.OnSuccess -> {
                    resultBackNavigator.navigateBack(data.successMessage)
                }
            }
        }
    }

    val title = if (itemId == 0) CREATE_NEW_ITEM else UPDATE_ITEM

    var expanded by remember { mutableStateOf(false) }
    var measureExpanded by remember { mutableStateOf(false) }

    val typeNames = viewModel.itemTypes.collectAsStateWithLifecycle().value
    val measureUnits = viewModel.measureUnits.collectAsStateWithLifecycle().value

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    TrackScreenViewEvent(screenName = "$title/itemId=$itemId")

    StandardScaffoldRouteNew(
        title = title,
        showBottomBar = true,
        showBackButton = true,
        bottomBar = {
            StandardButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(ADD_EDIT_MARKET_ITEM_BUTTON)
                    .padding(SpaceMedium),
                text = if (itemId == 0) CREATE_NEW_ITEM else UPDATE_ITEM,
                icon = if (itemId == 0) PoposIcons.Add else PoposIcons.Edit,
                enabled = enableBtn,
                onClick = {
                    viewModel.onEvent(AddEditMarketItemEvent.AddOrUpdateItem)
                },
            )
        },
        onBackClick = navigator::navigateUp,
    ) { paddingValues ->
        TrackScrollJank(
            scrollableState = lazyListState,
            stateName = "AddEditMarketItemScreen::Fields",
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(SpaceMedium),
            verticalArrangement = Arrangement.spacedBy(SpaceSmall),
            state = lazyListState,
        ) {
            item(MARKET_ITEM_TYPE_FIELD) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    StandardOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                //This is used to assign to the DropDown the same width
                                textFieldSize = coordinates.size.toSize()
                            },
                        value = viewModel.state.marketType.typeName,
                        label = MARKET_ITEM_TYPE_FIELD,
                        leadingIcon = PoposIcons.Radar,
                        isError = typeError != null,
                        errorText = typeError,
                        errorTextTag = MARKET_ITEM_TYPE_ERROR_TAG,
                        readOnly = false,
                        onValueChange = {
                            expanded = true
                            viewModel.onEvent(
                                AddEditMarketItemEvent.ItemTypeChanged(
                                    MarketTypeIdAndName(typeId = 0, typeName = it),
                                ),
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                    )

                    DropdownMenu(
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        properties = PopupProperties(
                            focusable = false,
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true,
                            excludeFromSystemGesture = true,
                            clippingEnabled = true,
                        ),
                    ) {
                        typeNames.forEachIndexed { index, marketType ->
                            DropdownMenuItem(
                                modifier = Modifier
                                    .testTag(marketType.typeName)
                                    .fillMaxWidth(),
                                text = { Text(marketType.typeName) },
                                onClick = {
                                    expanded = false
                                    viewModel.onEvent(
                                        AddEditMarketItemEvent.ItemTypeChanged(marketType),
                                    )
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )

                            if (index != typeNames.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 0.8.dp,
                                    color = Color.Gray,
                                )
                            }
                        }

                        if (typeNames.isEmpty()) {
                            DropdownMenuItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                enabled = false,
                                onClick = {},
                                text = {
                                    Text(
                                        text = "Market types are not available, Click below to create new",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterHorizontally),
                                    )
                                },
                            )
                        }

                        HorizontalDivider(modifier = Modifier.fillMaxWidth())

                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                navigator.navigate(
                                    AddEditMarketTypeScreenDestination(
                                        typeName = viewModel.state.marketType.typeName,
                                    ),
                                )
                            },
                            text = {
                                Text(
                                    text = "Create new Type",
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = PoposIcons.Add,
                                    contentDescription = "Create",
                                    tint = MaterialTheme.colorScheme.secondary,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = PoposIcons.ArrowRightAlt,
                                    contentDescription = "trailing",
                                )
                            },
                        )
                    }
                }
            }

            item(MARKET_ITEM_NAME_FIELD) {
                StandardOutlinedTextField(
                    value = viewModel.state.itemName,
                    label = MARKET_ITEM_NAME_FIELD,
                    leadingIcon = PoposIcons.WorkOutline,
                    isError = nameError != null,
                    errorText = nameError,
                    errorTextTag = MARKET_ITEM_NAME_ERROR_TAG,
                    onValueChange = {
                        viewModel.onEvent(AddEditMarketItemEvent.ItemNameChanged(it))
                    },
                )
            }

            item(MARKET_ITEM_MEASURE_FIELD) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    StandardOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                //This is used to assign to the DropDown the same width
                                textFieldSize = coordinates.size.toSize()
                            },
                        value = viewModel.state.itemMeasureUnit.unitName,
                        label = MARKET_ITEM_MEASURE_FIELD,
                        leadingIcon = PoposIcons.MonitorWeight,
                        isError = unitError != null,
                        errorText = unitError,
                        errorTextTag = MARKET_ITEM_MEASURE_ERROR_TAG,
                        readOnly = false,
                        onValueChange = {
                            measureExpanded = true
                            viewModel.onEvent(AddEditMarketItemEvent.ItemMeasureUnitNameChanged(it))
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = measureExpanded)
                        },
                    )


                    DropdownMenu(
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                        expanded = measureExpanded,
                        onDismissRequest = { measureExpanded = false },
                        properties = PopupProperties(
                            focusable = false,
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true,
                            excludeFromSystemGesture = true,
                            clippingEnabled = true,
                        ),
                    ) {
                        measureUnits.forEachIndexed { index, unit ->
                            DropdownMenuItem(
                                modifier = Modifier
                                    .testTag(unit.unitName)
                                    .fillMaxWidth(),
                                text = { Text(unit.unitName) },
                                onClick = {
                                    measureExpanded = false
                                    viewModel.onEvent(
                                        AddEditMarketItemEvent.ItemMeasureUnitChanged(unit),
                                    )
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )

                            if (index != measureUnits.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 0.8.dp,
                                    color = Color.Gray,
                                )
                            }
                        }

                        if (measureUnits.isEmpty()) {
                            DropdownMenuItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                enabled = false,
                                onClick = {},
                                text = {
                                    Text(
                                        text = "Measure units not available, Click below to create new.",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterHorizontally),
                                    )
                                },
                            )
                        }

                        HorizontalDivider(modifier = Modifier.fillMaxWidth())

                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                navigator.navigate(
                                    AddEditMeasureUnitScreenDestination(
                                        unitName = viewModel.state.itemMeasureUnit.unitName
                                    )
                                )
                            },
                            text = {
                                Text(
                                    text = "Create New Unit",
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = PoposIcons.Add,
                                    contentDescription = "Create",
                                    tint = MaterialTheme.colorScheme.secondary,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = PoposIcons.ArrowRightAlt,
                                    contentDescription = "trailing",
                                )
                            },
                        )
                    }

                }
            }

            item(MARKET_ITEM_PRICE_FIELD) {
                StandardOutlinedTextField(
                    value = viewModel.state.itemPrice ?: "",
                    label = MARKET_ITEM_PRICE_FIELD,
                    leadingIcon = PoposIcons.Rupee,
                    isError = amountError != null,
                    errorText = amountError,
                    errorTextTag = MARKET_ITEM_PRICE_ERROR_TAG,
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        viewModel.onEvent(AddEditMarketItemEvent.ItemPriceChanged(it))
                    },
                )
            }

            item(MARKET_LIST_ITEM_DESC) {
                StandardOutlinedTextField(
                    value = viewModel.state.itemDesc ?: "",
                    label = MARKET_LIST_ITEM_DESC,
                    leadingIcon = PoposIcons.Note,
                    onValueChange = {
                        viewModel.onEvent(AddEditMarketItemEvent.ItemDescriptionChanged(it))
                    },
                )
            }
        }
    }
}