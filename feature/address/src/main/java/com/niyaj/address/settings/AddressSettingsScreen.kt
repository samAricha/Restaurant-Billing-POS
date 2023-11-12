package com.niyaj.address.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.niyaj.address.destinations.AddressExportScreenDestination
import com.niyaj.address.destinations.AddressImportScreenDestination
import com.niyaj.common.tags.AddressTestTags.ADDRESS_SETTINGS_TITLE
import com.niyaj.designsystem.theme.SpaceMedium
import com.niyaj.ui.components.SettingsCard
import com.niyaj.ui.components.StandardBottomSheet
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet

@Destination(style = DestinationStyleBottomSheet::class)
@Composable
fun AddressSettingsScreen(
    navController: NavController
) {
    val lazyListState = rememberLazyListState()

    StandardBottomSheet(
        title = ADDRESS_SETTINGS_TITLE,
        onBackClick = {
            navController.navigateUp()
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(SpaceMedium)
        ){
            item("ImportAddress") {
                SettingsCard(
                    title = "Import Address",
                    subtitle = "Click here to import data from file.",
                    icon = Icons.Default.SaveAlt,
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    onClick = {
                        navController.navigate(AddressImportScreenDestination())
                    }
                )
            }

            item("ExportAddress") {
                SettingsCard(
                    title = "Export Address",
                    subtitle = "Click here to export address to file.",
                    icon = Icons.Default.Upload,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = {
                        navController.navigate(AddressExportScreenDestination())
                    }
                )
            }
        }
    }
}