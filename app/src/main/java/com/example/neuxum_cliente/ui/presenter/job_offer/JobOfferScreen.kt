package com.example.neuxum_cliente.ui.presenter.job_offer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.neuxum_cliente.R
import com.example.neuxum_cliente.ui.components.FilterChipComponent
import com.example.neuxum_cliente.ui.components.MyDialog2
import com.example.neuxum_cliente.ui.components.MyTextFieldComponent
import com.example.neuxum_cliente.ui.components.MyTextFieldComponent2
import com.example.neuxum_cliente.ui.components.MultiplePhotoPickerComponent
import com.example.neuxum_cliente.ui.components.Stepper
import com.example.neuxum_cliente.ui.presenter.map.MapScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun JobOfferScreen(
    categoryId: Long = -1,
    viewModel: JobOfferViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var selectedServiceType by rememberSaveable { mutableStateOf("Todos") }

    Dialog(viewModel)


    Column(
        modifier = Modifier.padding(start = 2.dp, end = 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
//        Text("Job Offer Screen 😎")
//        Text("Category ID: $categoryId")
        Stepper(
            numberOfSteps = 4,
            currentStep = 2,
            selectedColor = Color(0xFF009963),
            unSelectedColor = Color(0xFFE6E6E6)
        )
        Text("Agregar dirección", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        MyTextFieldComponent2(
            labelValue = "Dirección",
            textValue = "${state.address}",
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
                viewModel.onEvent(JobOfferEvent.AddressChanged(it))
            }
        )

        Text("Detalle de la solicitud", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        MyTextFieldComponent(
            labelValue = "Titulo",
            textValue = state.title,
            trailingIcon = Icons.Default.Edit,
            onTextSelected = {
                viewModel.onEvent(JobOfferEvent.TitleChanged(it))
            }
        )
        MyTextFieldComponent(
            labelValue = "Descripción",
            textValue = state.description,
            trailingIcon = Icons.AutoMirrored.Filled.Assignment,
            onTextSelected = {
                viewModel.onEvent(JobOfferEvent.DescriptionChanged(it))
            }
        )
        Text("Agregar evidencia", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        MultiplePhotoPickerComponent(
            selectedImages = state.images,
            onAddImage = { images ->
                viewModel.onEvent(JobOfferEvent.AddImage(images))
            },
            onRemoveImage = { index ->
                viewModel.onEvent(JobOfferEvent.RemoveImage(index))
            }
        )
        Text("Fecha", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .defaultMinSize(minHeight = 20.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterChipComponent(
                text = "Hoy",
                isSelected = selectedServiceType == "Hoy",
                onClick = {
                    selectedServiceType = "Hoy"
                }
            )
            FilterChipComponent(
                text = "3 dias",
                isSelected = selectedServiceType == "3 dias",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .width(106.dp)
                    .height(46.dp),
                onClick = {
                    selectedServiceType = "3 dias"
                }
            )
            FilterChipComponent(
                text = "1 semana",
                isSelected = selectedServiceType == "1 semana",
                onClick = {
                    selectedServiceType = "1 semana"
                }
            )
        }
        MyTextFieldComponent(
            labelValue = "Fecha",
            textValue = state.requestedDate,
            trailingIcon = Icons.Default.Edit,
            onTextSelected = {})

    }
}

@Composable
private fun Dialog(viewModel: JobOfferViewModel) {
    MyDialog2(
        tittle = "Seleccionar dirección",
        dismissTxt = "Cancelar",
        confirmTxt = "Aceptar",
        content = {
            MapScreen(
                modifier = Modifier
                    .padding(5.dp)
                    .width(306.dp)
                    .height(306.dp)
                    .clip(RoundedCornerShape(12.dp)),
                onMapClick = { latLng, address ->
                    viewModel.onEvent(
                        JobOfferEvent.LatitudeChanged(latLng.latitude)
                    )
                    viewModel.onEvent(
                        JobOfferEvent.LongitudeChanged(latLng.longitude)
                    )
                    viewModel.onEvent(
                        JobOfferEvent.AddressChanged(address)
                    )
                }
            )
        },
        show = viewModel.showMapDialog,
        onDismiss = { viewModel.showMapDialog = false },
        onConfirm = {
            viewModel.showMapDialog = false
        },
        modifier = Modifier.width(360.dp)
    )
}