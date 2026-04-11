package com.example.sistema_de_laboratorio.data.local

import androidx.room.*
import com.example.sistema_de_laboratorio.data.model.Equipamento

@Dao
interface EquipamentoDao {
    @Query("SELECT * FROM equipamentos")
    suspend fun getAll(): List<Equipamento>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(equipamento: Equipamento)

    @Update
    suspend fun update(equipamento: Equipamento)

    @Delete
    suspend fun delete(equipamento: Equipamento)

    @Query("SELECT * FROM equipamentos WHERE id = :id")
    suspend fun getById(id: Int): Equipamento?
}
