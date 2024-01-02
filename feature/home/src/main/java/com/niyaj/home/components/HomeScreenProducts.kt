package com.niyaj.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.niyaj.common.tags.HomeScreenTestTags
import com.niyaj.common.tags.ProductTestTags
import com.niyaj.common.utils.toRupee
import com.niyaj.designsystem.theme.SpaceMini
import com.niyaj.designsystem.theme.SpaceSmall
import com.niyaj.home.ProductWithFlowQuantity
import com.niyaj.model.ProductWithQuantity
import com.niyaj.ui.components.CircularBoxWithQty
import com.niyaj.ui.components.ItemNotFound
import com.niyaj.ui.components.TitleWithIcon
import com.niyaj.ui.utils.TrackScrollJank
import com.niyaj.ui.utils.isScrolled
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun HomeScreenProducts(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    products: ImmutableList<ProductWithQuantity>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onCreateProduct: () -> Unit,
) = trace("MainFeedProducts") {
    val scope = rememberCoroutineScope()
    TrackScrollJank(scrollableState = lazyListState, stateName = "products:list")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpaceSmall),
        verticalArrangement = Arrangement.spacedBy(SpaceSmall)
    ) {
        TitleWithIcon(
            text = "Products",
            icon = Icons.Default.Dns,
            showScrollToTop = lazyListState.isScrolled,
            onClickScrollToTop = {
                scope.launch {
                    lazyListState.animateScrollToItem(0)
                }
            }
        )

        LazyColumn(
            state = lazyListState,
        ) {
            items(
                items = products,
                key = {
                    it.productId
                }
            ) { product ->
                HomeScreenProductCard(
                    product = product,
                    onIncrease = onIncrease,
                    onDecrease = onDecrease
                )

                Spacer(modifier = Modifier.height(SpaceSmall))
            }

            item {
                ItemNotFound(
                    btnText = HomeScreenTestTags.CREATE_NEW_PRODUCT,
                    onBtnClick = onCreateProduct
                )

                Spacer(modifier = Modifier.height(SpaceSmall))
            }
        }
    }
}

@Composable
fun HomeScreenFlowProducts(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    products: ImmutableList<ProductWithFlowQuantity>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onCreateProduct: () -> Unit,
) = trace("MainFeedProducts") {
    val scope = rememberCoroutineScope()
    TrackScrollJank(scrollableState = lazyListState, stateName = "products:list")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpaceSmall),
        verticalArrangement = Arrangement.spacedBy(SpaceSmall)
    ) {
        TitleWithIcon(
            text = "Products",
            icon = Icons.Default.Dns,
            showScrollToTop = lazyListState.isScrolled,
            onClickScrollToTop = {
                scope.launch {
                    lazyListState.animateScrollToItem(0)
                }
            }
        )

        LazyColumn(
            state = lazyListState,
        ) {
            items(
                items = products,
                key = {
                    it.productId
                }
            ) { product ->
                HomeScreenProductCard(
                    product = ProductWithQuantity(
                        categoryId = product.categoryId,
                        productId = product.productId,
                        productName = product.productName,
                        productPrice = product.productPrice,
                        quantity = product.quantity.collectAsStateWithLifecycle().value
                    ),
                    onIncrease = onIncrease,
                    onDecrease = onDecrease
                )

                Spacer(modifier = Modifier.height(SpaceSmall))
            }

            item {
                ItemNotFound(
                    btnText = HomeScreenTestTags.CREATE_NEW_PRODUCT,
                    onBtnClick = onCreateProduct
                )

                Spacer(modifier = Modifier.height(SpaceSmall))
            }
        }
    }
}

@Composable
fun HomeScreenProductCard(
    modifier: Modifier = Modifier,
    product: ProductWithQuantity,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
) = trace("MainFeedProductData") {
    ListItem(
        modifier = modifier
            .testTag(ProductTestTags.PRODUCT_TAG.plus(product.productId))
            .fillMaxWidth()
            .clip(RoundedCornerShape(SpaceMini)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ),
        headlineContent = {
            Text(
                text = product.productName,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(text = product.productPrice.toRupee)
        },
        leadingContent = {
            CircularBoxWithQty(
                text = product.productName,
                qty = product.quantity
            )
        },
        trailingContent = {
            ElevatedCard(
                modifier = Modifier
                    .height(40.dp),
                shape = RoundedCornerShape(SpaceMini),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxHeight(),
                        onClick = { onDecrease(product.productId) },
                        enabled = remember(product.quantity) { product.quantity > 0 },
                        shape = RoundedCornerShape(
                            topStart = SpaceMini,
                            topEnd = 0.dp,
                            bottomStart = SpaceMini,
                            bottomEnd = 0.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(SpaceSmall),
                        ) {
                            Spacer(modifier = Modifier.width(SpaceSmall))
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "remove")
                            Spacer(modifier = Modifier.width(SpaceSmall))
                        }
                    }


                    HorizontalDivider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable {
                                onIncrease(product.productId)
                            }
                            .padding(SpaceSmall),
                    ) {
                        Spacer(modifier = Modifier.width(SpaceSmall))
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                        Spacer(modifier = Modifier.width(SpaceSmall))
                    }
                }
            }
        }
    )
}