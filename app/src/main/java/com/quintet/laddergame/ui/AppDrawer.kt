package com.quintet.laddergame.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = modifier
    ) {
        LadderGameLogo(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
        )

        NavigationDrawerItem(
            // TODO string resource 설정
            label = { Text(text = "게임 재설정") },
            icon = { Icon(imageVector = Icons.Default.Refresh, contentDescription = null) },
            selected = false,
            onClick = { closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
fun LadderGameLogo(
    modifier: Modifier = Modifier
) {
    // TODO string resource 설정
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "ladder_game_logo_image",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Ladder Game",
            style = TextStyle(
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
            ),
            color = Color.Blue,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}