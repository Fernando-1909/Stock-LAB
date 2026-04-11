package com.example.sistema_de_laboratorio.data.local

import androidx.room.*
import com.example.sistema_de_laboratorio.data.model.Material

@Dao
interface MaterialDao {
    @Query("SELECT * FROM materiais")
    suspend fun getAll(): List<Material>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(material: Material)

    @Update
    suspend fun update(material: Material)

    @Delete
    suspend fun delete(material: Material)

    @Query("SELECT * FROM materiais WHERE id = :id")
    suspend fun getById(id: Int): Material?
}
