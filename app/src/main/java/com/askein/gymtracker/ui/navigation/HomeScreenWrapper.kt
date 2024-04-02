package com.askein.gymtracker.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreenWrapper(
    title: String,
    navController: NavHostController,
    homeNavigationOptions: Map<HomeNavigationInformation, Boolean>,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = { },
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = title,
                navController = navController,
                homeScreen = true
            )
        },
        floatingActionButton = {
            floatingActionButton()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                for (route in homeNavigationOptions) {
                    Button(
                        onClick = route.key.navigationFunction,
                        enabled = route.value
                    ) {
                        Text(text = stringResource(id = route.key.title))
                    }
                }
            }
            content()
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
