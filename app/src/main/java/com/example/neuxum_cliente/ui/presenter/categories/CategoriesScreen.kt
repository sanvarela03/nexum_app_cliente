package com.example.neuxum_cliente.ui.presenter.categories

import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.neuxum_cliente.ui.componets.FilterChipComponent
import com.example.neuxum_cliente.ui.presenter.home.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

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
    homeViewModel: HomeViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {
    val state = homeViewModel.state
    val categoriesState = categoriesViewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = categoriesViewModel.state.isRefreshing
    )
    val categories = categoriesState.categories
//    Column {
//        Text(text = "Categories: ${categoriesState.categories}")
//        Text(text = "Client: ${state.clientEntity}")
//    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            categoriesViewModel.onEvent(CategoriesEvent.Refresh)
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
                FilterChipComponent("Todos")
                FilterChipComponent("24 horas")
                FilterChipComponent("Remodelación")
                FilterChipComponent("Construcción")
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .border(1.dp, color = Color(0xFFD9D9D9), shape = RoundedCornerShape(12.dp))
                    .fillMaxSize(),
            ) {
                items(categories.size) { index ->
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val category = categories[index]
                        val url =
                            if (category.imageUrl != null && !category.imageUrl.contains("NA") && !category.imageUrl.isEmpty()) category.imageUrl else "https://http.cat/images/102.jpg"
                        GlideImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .width(110.dp)
                                .height(108.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(text = category.name, fontWeight = FontWeight.Light)
                    }
                }
            }
        }
    }
}