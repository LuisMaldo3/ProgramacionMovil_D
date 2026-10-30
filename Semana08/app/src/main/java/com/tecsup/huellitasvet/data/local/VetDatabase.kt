package com.tecsup.huellitasvet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Cita

import com.tecsup.huellitasvet.data.local.dao.ServicioDao
import com.tecsup.huellitasvet.data.local.dao.UsuarioDao
import com.tecsup.huellitasvet.data.local.dao.MascotaDao
import com.tecsup.huellitasvet.data.local.dao.CitaDao

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE usuarios ADD COLUMN telefono TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE usuarios ADD COLUMN distrito TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE servicios ADD COLUMN tipo TEXT NOT NULL DEFAULT 'Consulta General'")
    }
}

@Database(
    entities = [
        Servicio::class,
        Usuario::class,
        Mascota::class,
        Cita::class
    ],
    version = 3,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {

    abstract fun servicioDao(): ServicioDao

    abstract fun usuarioDao(): UsuarioDao

    abstract fun mascotaDao(): MascotaDao

    abstract fun citaDao(): CitaDao

    companion object {

        @Volatile
        private var INSTANCE: VetDatabase? = null

        fun obtener(context: Context): VetDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    VetDatabase::class.java,
                    "huellitasvet.db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().also { INSTANCE = it }
            }
    }
}
