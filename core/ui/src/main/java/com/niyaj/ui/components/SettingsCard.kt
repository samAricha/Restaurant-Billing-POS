package com.niyaj.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.niyaj.designsystem.theme.SpaceMini

@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SpaceMini))
            .clickable {
                onClick()
            },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )
        },
        supportingContent = {
            Text(text = subtitle)
        },
        leadingContent = {
            CircularBox(
                icon = icon,
                doesSelected = false,
                showBorder = false,
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = "Arrow right icon"
            )
        },
        tonalElevation = 1.dp,
        shadowElevation = 4.dp,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}