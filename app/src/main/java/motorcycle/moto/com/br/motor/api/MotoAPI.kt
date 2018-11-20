package motorcycle.motor.com.br.motor.api

import motorcycle.motor.com.br.motor.model.Moto
import motorcycle.motor.com.br.motor.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface MotoAPI {

    @GET("/moto")
    fun buscarMotos(): Call<List<Moto>>

    @GET("/moto/{marca}")
    fun buscarPorMarca(@Path("marca") marca: String): Call<List<Moto>>

    @POST("/moto")
    fun salvar(@Body moto: Moto): Call<Void>

    @PUT("/moto")
    fun update(@Body moto: Moto): Call<Void>

    @DELETE("/moto/{id}/deletar")
    fun delete(@Path("id") id: String): Call<Void>
}