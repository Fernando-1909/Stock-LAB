package com.example.sistema_de_laboratorio.data.local

import androidx.room.TypeConverter
import com.example.sistema_de_laboratorio.data.model.Status

class Converters {
    @TypeConverter
    fun fromStatus(status: Status): String {
        return status.name
    }

    @TypeConverter
    fun toStatus(value: String): Status {
        return try {
            Status.valueOf(value)
        } catch (e: Exception) {
            Status.DISPONIVEL
        }
    }
}
