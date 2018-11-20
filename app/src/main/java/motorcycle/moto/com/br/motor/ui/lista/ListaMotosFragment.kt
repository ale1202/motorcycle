package motorcycle.motor.com.br.motor.ui.lista

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import motorcycle.motor.com.br.motor.R
import motorcycle.motor.com.br.motor.api.MotoAPI
import motorcycle.motor.com.br.motor.api.RetrofitClient
import motorcycle.motor.com.br.motor.model.Moto
import kotlinx.android.synthetic.main.erro.*
import kotlinx.android.synthetic.main.fragment_lista_motos.*
import kotlinx.android.synthetic.main.fragment_nova_moto.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaMotosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_lista_motos, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPesquisar.setOnClickListener {
            val api = RetrofitClient
                    .getInstance("https://motorcycleapi.herokuapp.com")
                    .create(MotoAPI::class.java)

            api.buscarPorMarca(buscaMarca.editText?.text.toString().toLowerCase()).enqueue(object : Callback<List<Moto>> {
                override fun onResponse(call: Call<List<Moto>>?, response: Response<List<Moto>>?) {
                    if(response?.isSuccessful == true) {
                        if(buscaMarca.editText?.text.toString().isNullOrEmpty()){
                            Toast.makeText(context,
                                    "Preencha uma marca para buscar",
                                    Toast.LENGTH_SHORT).show()
                        }
                        buscaMarca.editText?.setText("")
                        setupLista(response?.body())
                    }
                }

                override fun onFailure(call: Call<List<Moto>>?, t: Throwable?) {
                    Toast.makeText(context,
                            "Não foi possivel carregar as Motos desejadas",
                            Toast.LENGTH_SHORT).show()
                }
            })
        }

        carregarMotos()
    }

    fun carregarMotos() {
        val api = RetrofitClient
                .getInstance("https://motorcycleapi.herokuapp.com")
                .create(MotoAPI::class.java)

        loading.visibility = View.VISIBLE

        api.buscarMotos().enqueue(object: Callback<List<Moto>> {

            override fun onResponse(call: Call<List<Moto>>?, response: Response<List<Moto>>?) {

                containerErro.visibility = View.GONE
                tvMsgErro.text = ""

                if(response?.isSuccessful == true) {
                    setupLista(response?.body())
                } else {
                    containerErro.visibility = View.VISIBLE
                    tvMsgErro.text = response?.errorBody()?.string()
                }

                loading.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Moto>>?, t: Throwable?) {
                containerErro.visibility = View.VISIBLE
                tvMsgErro.text = t?.message
                loading.visibility = View.GONE

            }
        })
    }

    fun setupLista(motos: List<Moto>?) {
        motos.let {
            rvMotos.adapter = ListaMotosAdapter(motos!!, context)
            val layoutManager = LinearLayoutManager(context)
            rvMotos.layoutManager = layoutManager
        }
    }
}
