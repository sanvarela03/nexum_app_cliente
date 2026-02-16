package com.example.nexum_cliente.data.job_offer.local

<<<<<<< Updated upstream
=======
import androidx.room.Entity
import androidx.room.PrimaryKey

>>>>>>> Stashed changes

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 1/14/2026
 * @version 1.0
 */
<<<<<<< Updated upstream
class JobOfferEntity {
}
=======
@Entity(tableName = "job_offers")
data class JobOfferEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val categoryId: Long,
    val requestedDate: String,
)

>>>>>>> Stashed changes
