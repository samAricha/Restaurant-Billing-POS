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

package com.niyaj.expenses.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.niyaj.common.tags.ExpenseTestTags.EXPENSE_SETTINGS_TITLE
import com.niyaj.designsystem.icon.PoposIcons
import com.niyaj.designsystem.theme.SpaceMedium
import com.niyaj.expenses.destinations.ExpensesExportScreenDestination
import com.niyaj.expenses.destinations.ExpensesImportScreenDestination
import com.niyaj.ui.components.SettingsCard
import com.niyaj.ui.components.StandardBottomSheet
import com.niyaj.ui.utils.TrackScreenViewEvent
import com.niyaj.ui.utils.TrackScrollJank
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet

@Destination(style = DestinationStyleBottomSheet::class)
@Composable
fun ExpensesSettingsScreen(
    navigator: DestinationsNavigator,
) {
    TrackScreenViewEvent(screenName = "Expenses Settings Screen")

    val lazyListState = rememberLazyListState()

    TrackScrollJank(scrollableState = lazyListState, stateName = "Expenses Settings::List")

    StandardBottomSheet(
        title = EXPENSE_SETTINGS_TITLE,
        onBackClick = navigator::navigateUp,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(SpaceMedium),
        ) {
            item("ImportExpenses") {
                SettingsCard(
                    title = "Import Expenses",
                    subtitle = "Click here to import data from file.",
                    icon = PoposIcons.Import,
                    onClick = {
                        navigator.navigate(ExpensesImportScreenDestination())
                    },
                )
            }

            item("ExportExpense") {
                SettingsCard(
                    title = "Export Expenses",
                    subtitle = "Click here to export expenses to file.",
                    icon = PoposIcons.Upload,
                    onClick = {
                        navigator.navigate(ExpensesExportScreenDestination())
                    },
                )
            }
        }
    }
}
