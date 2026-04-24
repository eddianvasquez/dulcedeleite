package edu.ucne.dulcedeleite.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.dulcedeleite.data.local.dao.DulceDeleiteDao
import edu.ucne.dulcedeleite.data.local.entity.PedidoEntity
import edu.ucne.dulcedeleite.data.local.entity.ProductoEntity

@Database(
    entities = [ProductoEntity::class, PedidoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DulceDeleiteDatabase : RoomDatabase() {
    abstract fun dulceDeleiteDao(): DulceDeleiteDao
}