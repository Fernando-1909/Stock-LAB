package com.example.sistema_de_laboratorio.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sistema_de_laboratorio.data.model.Equipamento
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.data.model.Ocorrencia

@Database(entities = [Material::class, Equipamento::class, Ocorrencia::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun materialDao(): MaterialDao
    abstract fun equipamentoDao(): EquipamentoDao
    abstract fun ocorrenciaDao(): OcorrenciaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // O nome da tabela é "equipamentos" conforme definido na entidade @Entity(tableName = "equipamentos")
                db.execSQL("UPDATE equipamentos SET status = 'DISPONIVEL' WHERE status NOT IN ('DISPONIVEL', 'INDISPONIVEL', 'MANUTENCAO')")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "laboratorio_database"
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration() // Resetar se a migração falhar (mais seguro para desenvolvimento)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
