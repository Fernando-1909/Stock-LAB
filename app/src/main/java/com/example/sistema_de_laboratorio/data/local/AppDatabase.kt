package com.example.sistema_de_laboratorio.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sistema_de_laboratorio.data.model.Equipamento
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.data.model.Ocorrencia

@Database(entities = [Material::class, Equipamento::class, Ocorrencia::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun materialDao(): MaterialDao
    abstract fun equipamentoDao(): EquipamentoDao
    abstract fun ocorrenciaDao(): OcorrenciaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "laboratorio_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
