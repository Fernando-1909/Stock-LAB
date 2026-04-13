package com.example.sistema_de_laboratorio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.sistema_de_laboratorio.data.model.Material
import com.example.sistema_de_laboratorio.domain.service.LaboratorioService
import com.example.sistema_de_laboratorio.ui.EquipamentosActivity
import com.example.sistema_de_laboratorio.ui.OcorrenciasActivity
import com.example.sistema_de_laboratorio.ui.RelatorioActivity
import com.example.sistema_de_laboratorio.ui.materiais.MateriaisActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val service = LaboratorioService(this)
        val btnThemeToggle = findViewById<ImageButton>(R.id.btnThemeToggle)

        // Sincronizar ícone do tema (o modo em si já é definido no LaboratorioApp)
        val isDarkMode = sharedPref.getBoolean("dark_mode", true)
        updateThemeIcon(btnThemeToggle, isDarkMode)

        // Botão de alternar tema
        btnThemeToggle.setOnClickListener {
            val currentMode = sharedPref.getBoolean("dark_mode", true)
            val newMode = !currentMode
            
            with(sharedPref.edit()) {
                putBoolean("dark_mode", newMode)
                apply()
            }

            if (newMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Carregar dados iniciais se estiver vazio
        lifecycleScope.launch {
            try {
                if (service.relatorioEstoque().isEmpty()) {
                    service.cadastrarMaterial(
                        Material(
                            nome = "Álcool 70%",
                            quantidade = 15,
                            descricao = "Uso geral",
                            localizacao = "Armário A",
                            dataAquisicao = "2024",
                            fornecedor = "Fornecedor X"
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        findViewById<Button>(R.id.btnMateriais).setOnClickListener {
            startActivity(Intent(this, MateriaisActivity::class.java))
        }

        findViewById<Button>(R.id.btnEquipamentos).setOnClickListener {
            startActivity(Intent(this, EquipamentosActivity::class.java))
        }

        findViewById<Button>(R.id.btnRelatorios).setOnClickListener {
            startActivity(Intent(this, RelatorioActivity::class.java))
        }

        findViewById<Button>(R.id.btnOcorrencias).setOnClickListener {
            startActivity(Intent(this, OcorrenciasActivity::class.java))
        }
    }

    private fun updateThemeIcon(button: ImageButton, isDarkMode: Boolean) {
        if (isDarkMode) {
            button.setImageResource(R.drawable.ic_sun)
        } else {
            button.setImageResource(R.drawable.ic_moon)
        }
    }
}
