package com.example.neuxum_cliente.ui.presenter.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.ui.presenter.home.HomeViewModel

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/4/2025
 * @version 1.0
 */
@Composable
fun CategoriesScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state = homeViewModel.state
    Column {
        Text(text = "Categories")
        Text(text = "Client: ${state.clientEntity}")
    }
}