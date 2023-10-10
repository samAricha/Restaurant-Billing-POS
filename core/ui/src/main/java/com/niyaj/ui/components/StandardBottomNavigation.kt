package com.niyaj.ui.components

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Teleport
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import com.niyaj.core.ui.R
import com.niyaj.designsystem.theme.LightColor8
import com.niyaj.designsystem.theme.Purple
import com.niyaj.ui.utils.Screens


@Stable
internal data class NavigationItem(
    val index: Int,
    val name: String,
    val selected: Boolean,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val onClick: () -> Unit,
)

@Composable
private fun BottomNavigationBar(
    navController: NavController,
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Screens.HOME_SCREEN,
            label = {
                val fontWeight = if (currentRoute == Screens.HOME_SCREEN)
                    FontWeight.SemiBold else FontWeight.Normal

                Text(
                    text = "Home",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = fontWeight,
                )
            },
            onClick = {
                navController.navigate(Screens.HOME_SCREEN)
            },
            icon = {
                val icon = if (currentRoute == Screens.HOME_SCREEN) {
                    Icons.Rounded.Home
                } else Icons.Outlined.Home

                Icon(imageVector = icon, contentDescription = "Home")
            }
        )

        NavigationBarItem(
            selected = currentRoute == Screens.CART_SCREEN,
            label = {
                val fontWeight = if (currentRoute == Screens.CART_SCREEN)
                    FontWeight.SemiBold else FontWeight.Normal

                Text(
                    text = "Cart",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = fontWeight,
                )
            },
            onClick = {
                navController.navigate(Screens.CART_SCREEN)
            },
            icon = {
                val icon = if (currentRoute == Screens.CART_SCREEN) {
                    Icons.Rounded.ShoppingCart
                } else Icons.Outlined.ShoppingCart

                Icon(imageVector = icon, contentDescription = "Cart")
            }
        )

        NavigationBarItem(
            selected = currentRoute == Screens.ORDER_SCREEN,
            label = {
                val fontWeight = if (currentRoute == Screens.ORDER_SCREEN)
                    FontWeight.SemiBold else FontWeight.Normal

                Text(
                    text = "Orders",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = fontWeight,
                )
            },
            onClick = {
                navController.navigate(Screens.ORDER_SCREEN)
            },
            icon = {
                val icon = if (currentRoute == Screens.ORDER_SCREEN) {
                    Icons.Rounded.Inventory2
                } else Icons.Outlined.Inventory2

                Icon(imageVector = icon, contentDescription = "Orders")
            }
        )

        NavigationBarItem(
            selected = currentRoute == Screens.REPORT_SCREEN,
            label = {
                val fontWeight = if (currentRoute == Screens.REPORT_SCREEN)
                    FontWeight.SemiBold else FontWeight.Normal

                Text(
                    text = "Reports",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = fontWeight,
                )
            },
            onClick = {
                navController.navigate(Screens.REPORT_SCREEN)
            },
            icon = {
                val icon = if (currentRoute == Screens.ORDER_SCREEN) {
                    Icons.Rounded.Assessment
                } else Icons.Outlined.Assessment

                Icon(imageVector = icon, contentDescription = "Reports")
            }
        )
    }
}


@Composable
internal fun AnimatedBottomNavigationBar(
    navController: NavController,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route.hashCode()

    val navItems = listOf(
        NavigationItem(
            index = Screens.HOME_SCREEN.hashCode(),
            name = "Home",
            selected = currentRoute == Screens.HOME_SCREEN.hashCode(),
            selectedIcon = R.drawable.round_home,
            unselectedIcon = R.drawable.outline_home,
            onClick = {
                navController.navigate(Screens.HOME_SCREEN)
            }
        ),
        NavigationItem(
            index = Screens.CART_SCREEN.hashCode(),
            name = "Cart",
            selected = currentRoute == Screens.CART_SCREEN.hashCode(),
            selectedIcon = R.drawable.round_cart,
            unselectedIcon = R.drawable.outline_cart,
            onClick = {
                navController.navigate(Screens.CART_SCREEN)
            }
        ),
        NavigationItem(
            index = Screens.ORDER_SCREEN.hashCode(),
            name = "Orders",
            selected = currentRoute == Screens.ORDER_SCREEN.hashCode(),
            selectedIcon = R.drawable.round_orders,
            unselectedIcon = R.drawable.outline_orders,
            onClick = {
                navController.navigate(Screens.ORDER_SCREEN)
            }
        ),
        NavigationItem(
            index = Screens.REPORT_SCREEN.hashCode(),
            name = "Reports",
            selected = currentRoute == Screens.REPORT_SCREEN.hashCode(),
            selectedIcon = R.drawable.round_reports,
            unselectedIcon = R.drawable.outline_reports,
            onClick = {
                navController.navigate(Screens.REPORT_SCREEN)
            }
        )
    )

    val index = navItems.indexOf(navItems.find { it.index == currentRoute })

    AnimatedNavigationBar(
        modifier = Modifier
            .windowInsetsPadding(windowInsets)
            .height(80.dp),
        selectedIndex = index,
        cornerRadius = shapeCornerRadius(0.dp),
        barColor = LightColor8,
        ballColor = MaterialTheme.colorScheme.secondary,
        ballAnimation = Teleport(tween(Duration, easing = LinearOutSlowInEasing)),
        indentAnimation = Height(
            indentWidth = 56.dp,
            indentHeight = 15.dp,
            animationSpec = tween(
                DoubleDuration,
                easing = { OvershootInterpolator().getInterpolation(it) }
            )
        )
    ) {
        navItems.forEach {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DropletButton(
                    modifier = Modifier.fillMaxWidth(),
                    isSelected = it.selected,
                    onClick = it.onClick,
                    icon = if (it.selected) it.selectedIcon else it.unselectedIcon,
                    dropletColor = Purple,
                    iconColor = MaterialTheme.colorScheme.tertiary,
                    size = 24.dp,
                    animationSpec = tween(durationMillis = Duration, easing = LinearEasing)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = it.name,
                    color = if (it.selected) Purple else MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (it.selected) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }

}