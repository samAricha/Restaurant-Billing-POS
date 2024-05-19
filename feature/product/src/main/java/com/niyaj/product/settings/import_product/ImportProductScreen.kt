package com.niyaj.product.settings.import_product

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.niyaj.common.tags.ProductTestTags
import com.niyaj.common.tags.ProductTestTags.IMPORT_PRODUCTS_NOTE_TEXT
import com.niyaj.common.tags.ProductTestTags.IMPORT_PRODUCTS_OPN_FILE
import com.niyaj.common.utils.Constants
import com.niyaj.designsystem.icon.PoposIcons
import com.niyaj.designsystem.theme.SpaceSmall
import com.niyaj.designsystem.theme.SpaceSmallMax
import com.niyaj.domain.utils.ImportExport
import com.niyaj.model.Product
import com.niyaj.product.components.ProductCard
import com.niyaj.product.settings.ProductSettingsEvent
import com.niyaj.product.settings.ProductSettingsViewModel
import com.niyaj.ui.components.EmptyImportScreen
import com.niyaj.ui.components.InfoText
import com.niyaj.ui.components.ScrollToTop
import com.niyaj.ui.components.StandardButton
import com.niyaj.ui.components.StandardScaffoldRouteNew
import com.niyaj.ui.utils.TrackScreenViewEvent
import com.niyaj.ui.utils.TrackScrollJank
import com.niyaj.ui.utils.UiEvent
import com.niyaj.ui.utils.isScrollingUp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Destination
@Composable
fun ImportProductScreen(
    navController: DestinationsNavigator,
    viewModel: ProductSettingsViewModel = hiltViewModel(),
    resultBackNavigator: ResultBackNavigator<String>,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val importedProducts = viewModel.importedProducts.collectAsStateWithLifecycle().value

    val selectedItems = viewModel.selectedItems.toList()

    var importJob: Job? = null

    val importLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            it.data?.data?.let {
                importJob?.cancel()

                importJob = scope.launch {
                    val data = ImportExport.readDataAsync<Product>(context, it)

                    viewModel.onEvent(ProductSettingsEvent.OnImportProductsFromFile(data))
                }
            }
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

    BackHandler {
        if (selectedItems.isNotEmpty()) {
            viewModel.deselectItems()
        } else {
            navController.navigateUp()
        }
    }

    TrackScreenViewEvent(screenName = "Product Import Screen")

    StandardScaffoldRouteNew(
        title = if (selectedItems.isEmpty()) ProductTestTags.IMPORT_PRODUCTS_TITLE else "${selectedItems.size} Selected",
        showBackButton = selectedItems.isEmpty(),
        navigationIcon = {
            IconButton(
                onClick = viewModel::deselectItems,
            ) {
                Icon(
                    imageVector = PoposIcons.Close,
                    contentDescription = Constants.CLEAR_ICON,
                )
            }
        },
        showBottomBar = importedProducts.isNotEmpty() && lazyListState.isScrollingUp(),
        navActions = {
            AnimatedVisibility(
                visible = importedProducts.isNotEmpty(),
            ) {
                IconButton(
                    onClick = viewModel::selectAllItems,
                ) {
                    Icon(
                        imageVector = PoposIcons.Checklist,
                        contentDescription = Constants.SELECT_ALL_ICON,
                    )
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceSmallMax),
                verticalArrangement = Arrangement.spacedBy(SpaceSmall),
            ) {
                InfoText(text = "${if (selectedItems.isEmpty()) "All" else "${selectedItems.size}"} products will be imported.")

                StandardButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(ProductTestTags.IMPORT_PRODUCTS_BTN_TEXT),
                    enabled = true,
                    text = ProductTestTags.IMPORT_PRODUCTS_BTN_TEXT,
                    icon = PoposIcons.Download,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = {
                        scope.launch {
                            viewModel.onEvent(ProductSettingsEvent.ImportProductsToDatabase)
                        }
                    },
                )
            }
        },
        fabPosition = FabPosition.End,
        floatingActionButton = {
            ScrollToTop(
                visible = !lazyListState.isScrollingUp(),
                onClick = {
                    scope.launch {
                        lazyListState.animateScrollToItem(index = 0)
                    }
                },
            )
        },
        onBackClick = navController::navigateUp,
    ) {
        Crossfade(
            targetState = importedProducts.isEmpty(),
            label = "Imported Products",
        ) { productsAvailable ->
            if (productsAvailable) {
                EmptyImportScreen(
                    text = IMPORT_PRODUCTS_NOTE_TEXT,
                    buttonText = IMPORT_PRODUCTS_OPN_FILE,
                    icon = PoposIcons.FileOpen,
                    onClick = {
                        scope.launch {
                            val result = ImportExport.openFile(context)
                            importLauncher.launch(result)
                        }
                    },
                )
            } else {
                TrackScrollJank(
                    scrollableState = lazyListState,
                    stateName = "Imported Products::List",
                )

                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    contentPadding = PaddingValues(SpaceSmall),
                ) {
                    itemsIndexed(
                        items = importedProducts,
                        key = { index, item ->
                            item.productName.plus(index).plus(item.productId)
                        },
                        contentType = { _, item -> item },
                    ) { _, item ->
                        ProductCard(
                            item = item,
                            doesSelected = selectedItems::contains,
                            onClick = viewModel::selectItem,
                            onLongClick = viewModel::selectItem,
                            border = BorderStroke(0.dp, Color.Transparent),
                            showArrow = false,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    }
                }
            }
        }
    }
}