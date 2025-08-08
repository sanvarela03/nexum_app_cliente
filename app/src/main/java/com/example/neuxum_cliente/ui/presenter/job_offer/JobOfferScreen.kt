package com.example.neuxum_cliente.ui.presenter.job_offer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.R
import com.example.neuxum_cliente.ui.componets.MyDialog2
import com.example.neuxum_cliente.ui.componets.MyTextFieldComponent
import com.example.neuxum_cliente.ui.componets.MyTextFieldComponent2
import com.example.neuxum_cliente.ui.presenter.map.MapScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@Composable
fun JobOfferScreen(
    categoryId: Long,
    viewModel: JobOfferViewModel = hiltViewModel()
) {

    val state = viewModel.state

    MyDialog2(
        tittle = "Seleccionar dirección",
        dismissTxt = "Cancelar",
        confirmTxt = "Aceptar",
        content = {
            MapScreen(
                modifier = Modifier
                    .padding(5.dp)
                    .width(406.dp)
                    .height(406.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        },
        show = viewModel.showMapDialog,
        onDismiss = { viewModel.showMapDialog = false },
        onConfirm = {
            viewModel.showMapDialog = false
        },
        modifier = Modifier.wrapContentWidth()
    )
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        Text("Job Offer Screen 😎")
        Text("Category ID: $categoryId")
        Text("Agregar dirección", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        MyTextFieldComponent2(
            labelValue = "Dirección",
            textValue = "",
            leadingIcon = {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.MyLocation, contentDescription = "")
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.showMapDialog = true
                }) {
                    Icon(
                        painter = painterResource(R.drawable.distance), contentDescription = ""
                    )
                }

            },
            onTextSelected = {

            }
        )

        Text("Detalle de la solicitud", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        MyTextFieldComponent(
            labelValue = "Titulo",
            textValue = "",
            trailingIcon = Icons.Default.Edit,
            onTextSelected = {}
        )
    }
}