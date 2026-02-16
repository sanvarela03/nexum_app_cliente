<<<<<<< Updated upstream
package com.example.nexum_trabajador.ui.components
=======
package com.example.nexum_cliente.ui.components
>>>>>>> Stashed changes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
<<<<<<< Updated upstream
import com.example.nexum_trabajador.R
=======
import com.example.nexum_cliente.R
>>>>>>> Stashed changes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/3/2025
 * @version 1.0
 */
@Composable
fun NexumLogo() {
    Image(
        painter = painterResource(R.drawable.nexum_logo_2),
        modifier = Modifier
            .size(140.dp),
        contentDescription = "Logo",
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}