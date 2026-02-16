package com.example.nexum_cliente.data.client.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nexum_cliente.data.category.local.CategoryDao
import com.example.nexum_cliente.data.category.local.CategoryEntity
import com.example.nexum_cliente.data.client.local.ClientDao
import com.example.nexum_cliente.data.client.local.ClientEntity
import com.example.nexum_cliente.data.client.local.role.RoleDao
import com.example.nexum_cliente.data.client.local.role.RoleEntity
import com.example.nexum_cliente.data.country.local.CountryDao
import com.example.nexum_cliente.data.country.local.CountryEntity
import com.example.nexum_cliente.data.market_location.local.MarketLocationDao
import com.example.nexum_cliente.data.market_location.local.MarketLocationEntity


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/2/2025
 * @version 1.0
 */
@Database(
    entities = [
        ClientEntity::class,
        RoleEntity::class,
        CategoryEntity::class,
        MarketLocationEntity::class,
        CountryEntity::class,
    ],
    version = 1
)
abstract class ClientDb : RoomDatabase() {
    abstract val clientDao: ClientDao
    abstract val roleDao: RoleDao
    abstract val categoryDao: CategoryDao
    abstract val marketLocationDao: MarketLocationDao
    abstract val countryDao: CountryDao
}