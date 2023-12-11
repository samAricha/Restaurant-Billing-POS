package com.niyaj.cart.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.niyaj.designsystem.theme.SpaceMini
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Tabs(tabs: List<CartTabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    // OR ScrollableTabRow()
    PrimaryTabRow(
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        indicator = { tabPositions ->
            if (pagerState.currentPage < tabPositions.size) {
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    width = 80.dp,
                    height = 4.dp,
                    shape = CutCornerShape(topStart = SpaceMini, topEnd = SpaceMini)
                )
            }
        }
    ) {
        // Add tabs for all of our pages
        tabs.forEachIndexed { index, tab ->
            LeadingIconTab(
                icon = { Icon(imageVector = tab.icon, contentDescription = "") },
                text = { Text(tab.title) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                unselectedContentColor = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(tabs: List<CartTabItem>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
    ) { page ->
        tabs[page].screen()
    }
}
