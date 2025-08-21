package com.example.neuxum_cliente.ui.presenter.categories

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.neuxum_cliente.data.category.local.CategoryEntity
import com.example.neuxum_cliente.ui.components.FilterChipComponent
import com.example.neuxum_cliente.ui.navigation.rutes.JobOfferRoutes
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import timber.log.Timber

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/4/2025
 * @version 1.0
 */
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val categories = state.categories

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )

    var selectedServiceType by rememberSaveable { mutableStateOf("Todos") }
    val options = listOf("Todos", "24 horas", "Remodelación", "Construcción")
    val filteredCategories = categoryEntityList(selectedServiceType, categories)

    ErrorMsg(state)

    Timber.tag("CategoriesScreen").d("isRefreshing: ${viewModel.state.isRefreshing}")

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.onEvent(CategoriesEvent.Refresh)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "¿Qué servicio necesitas?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(start = 10.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .defaultMinSize(minHeight = 20.dp)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                options.forEach { option ->
                    FilterChipComponent(
                        text = option,
                        isSelected = selectedServiceType == option,
                        onClick = { selectedServiceType = option },
                        modifier = Modifier.defaultMinSize(minHeight = 24.dp),
                    )
                }
            }
            LazyVerticalGrid(
                columns = if (isPortrait) GridCells.Fixed(3) else GridCells.Fixed(4),
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .border(1.dp, color = Color(0xFFD9D9D9), shape = RoundedCornerShape(12.dp))
                    .fillMaxSize(),
            ) {
                items(filteredCategories.size) { index ->
                    val category = filteredCategories[index]
                    val url =
                        if (category.imageUrl != null && !category.imageUrl.contains("NA") && !category.imageUrl.isEmpty()) category.imageUrl else "https://http.cat/images/102.jpg"
                    CategoryItem(url, category) {
                        navController.navigate(JobOfferRoutes.JobOfferScreen(categoryId = category.id))
                    }
                }
            }
        }
    }
}

@Composable
private fun categoryEntityList(
    selectedServiceType: String,
    categories: List<CategoryEntity>
): List<CategoryEntity> {
    val filteredCategories = if (selectedServiceType == "Todos") {
        categories
    } else {
        categories.filter {
            it.serviceType?.trim()?.equals(selectedServiceType, ignoreCase = true) == true
        }
    }
    return filteredCategories
}

@Composable
private fun ErrorMsg(
    state: CategoriesState
) {
    val context = LocalContext.current
    Log.d("CategoriesScreen", "Recomposing. ErrorMessage: ${state.errorMessage}")
    LaunchedEffect(state.errorMessage) {
        Log.d(
            "CategoriesScreen",
            "LaunchedEffect triggered. ErrorMessage: ${state.errorMessage}"
        )
        if (state.errorMessage.isNotEmpty()) { // Comprueba si hay un mensaje de error
            Log.d("CategoriesScreen", "Showing toast for error: ${state.errorMessage}")
            errorMsgToast(state.errorMessage, context)
            state.errorMessage = ""
        } else {
            Log.d("CategoriesScreen", "LAUNCHED_EFFECT: ErrorMessage is empty. No toast.")
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun CategoryItem(
    url: String,
    category: CategoryEntity,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(10.dp),
    ) {
        GlideImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .width(110.dp)
                .height(108.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = category.name ?: "NA",
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
//            Text(
//                text = category.serviceType ?: "NA",
//                fontWeight = FontWeight.Light,
//                textAlign = TextAlign.Center
//            )
        }
    }
}


private fun errorMsgToast(errorMessage: String, context: Context) {
    Log.d("CategoriesScreen", "SHOW_ERROR_TOAST: Attempting to show toast for: '$errorMessage'")
    val toast =
        Toast.makeText(
            context,
            "Error: ${errorMessage}",
            Toast.LENGTH_SHORT
        )
    toast.setGravity(Gravity.BOTTOM, 0, 300)
    toast.show()
}