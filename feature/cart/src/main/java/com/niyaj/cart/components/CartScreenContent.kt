/*
 * Copyright 2024 Sk Niyaj Ali
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.niyaj.cart.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.niyaj.cart.CartEvent
import com.niyaj.cart.CartState
import com.niyaj.core.ui.R
import com.niyaj.model.AddOnItem
import com.niyaj.model.EmployeeNameAndId
import com.niyaj.ui.components.ItemNotAvailable
import com.niyaj.ui.components.LoadingIndicator
import com.niyaj.ui.utils.TrackScreenViewEvent
import com.niyaj.ui.utils.TrackScrollJank
import com.niyaj.ui.utils.isScrollingUp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun CartScreenContent(
    modifier: Modifier = Modifier,
    uiState: CartState,
    snackbarHostState: SnackbarHostState,
    selectedItems: List<Int>,
    addOnItems: List<AddOnItem>,
    showPrintBtn: Boolean = false,
    deliveryPartners: List<EmployeeNameAndId> = emptyList(),
    onClickCreateOrder: () -> Unit,
    onClickEditOrder: (Int) -> Unit,
    onClickOrderDetails: (Int) -> Unit,
    onClickSelectAll: () -> Unit,
    onClickPlaceAllOrder: () -> Unit,
    onEvent: (CartEvent) -> Unit,
    printOrder: (Int) -> Unit = {},
    onClickPrintAllOrder: () -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
) {
    TrackScreenViewEvent(screenName = "DineOut Tab::Cart")

    val totalItems = if (uiState is CartState.Success) uiState.items.size else 0

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = totalItems != 0 && listState.isScrollingUp(),
                label = "BottomBar",
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { fullHeight ->
                        fullHeight / 4
                    },
                ),
                exit = fadeOut() + slideOutVertically(
                    targetOffsetY = { fullHeight ->
                        fullHeight / 4
                    },
                ),
            ) {
                CartFooterPlaceOrder(
                    countTotalItems = totalItems,
                    countSelectedItem = selectedItems.size,
                    showPrintBtn = showPrintBtn,
                    onClickSelectAll = onClickSelectAll,
                    onClickPlaceAllOrder = onClickPlaceAllOrder,
                    onClickPrintAllOrder = onClickPrintAllOrder,
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize(),
    ) {
        when (uiState) {
            is CartState.Loading -> LoadingIndicator()

            is CartState.Empty -> {
                ItemNotAvailable(
                    text = "DineOut orders are not available",
                    buttonText = "Add Item To Cart",
                    image = painterResource(R.drawable.emptycarttwo),
                    onClick = onClickCreateOrder,
                )
            }

            is CartState.Success -> {
                TrackScrollJank(scrollableState = listState, stateName = "DineOut Orders::Cart")

                CartItems(
                    modifier = Modifier,
                    listState = listState,
                    cartState = uiState,
                    selectedCartItems = selectedItems,
                    addOnItems = addOnItems,
                    deliveryPartners = deliveryPartners,
                    showPrintBtn = showPrintBtn,
                    onClickEditOrder = onClickEditOrder,
                    onClickViewOrder = onClickOrderDetails,
                    onClickPrintOrder = printOrder,
                    onEvent = onEvent,
                )
            }
        }
    }
}