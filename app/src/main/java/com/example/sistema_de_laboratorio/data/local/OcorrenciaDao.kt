package com.example.sistema_de_laboratorio.data.local

import androidx.room.*
import com.example.sistema_de_laboratorio.data.model.Ocorrencia

@Dao
interface OcorrenciaDao {
    @Query("SELECT * FROM ocorrencias")
    suspend fun getAll(): List<Ocorrencia>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ocorrencia: Ocorrencia)

    @Delete
    suspend fun delete(ocorrencia: Ocorrencia)

    @Query("SELECT * FROM ocorrencias WHERE itemId = :itemId")
    suspend fun getByItemId(itemId: Int): List<Ocorrencia>
}
