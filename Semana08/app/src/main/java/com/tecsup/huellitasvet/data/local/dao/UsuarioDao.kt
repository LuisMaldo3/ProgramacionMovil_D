
package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tecsup.huellitasvet.data.local.entity.Usuario

@Dao
interface UsuarioDao {

    @Query("""
        SELECT * FROM usuarios
        WHERE correo = :correo AND clave = :clave
        LIMIT 1
    """)
    suspend fun login(
        correo: String,
        clave: String
    ): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Insert
    suspend fun insertar(usuario: Usuario): Long

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>)

    @Query("UPDATE usuarios SET clave = :nuevaClave WHERE correo = :correo")
    suspend fun actualizarClave(correo: String, nuevaClave: String): Int

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    @Query("SELECT * FROM usuarios WHERE rol = 'CLIENTE' ORDER BY nombre")
    fun observarClientes(): kotlinx.coroutines.flow.Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE rol = 'VETERINARIO' OR rol = 'ADMIN' ORDER BY nombre")
    fun observarPersonal(): kotlinx.coroutines.flow.Flow<List<Usuario>>

    @Query("SELECT COUNT(*) FROM usuarios WHERE rol = 'CLIENTE'")
    suspend fun contarClientes(): Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE rol = 'VETERINARIO' OR rol = 'ADMIN'")
    suspend fun contarPersonal(): Int
}
