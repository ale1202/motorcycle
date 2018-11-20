package motorcycle.motor.com.br.motor.ui.novo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import motorcycle.motor.com.br.motor.MainActivity

import motorcycle.motor.com.br.motor.R
import motorcycle.motor.com.br.motor.api.MotoAPI
import motorcycle.motor.com.br.motor.api.RetrofitClient
import motorcycle.motor.com.br.motor.model.Moto
import motorcycle.motor.com.br.motor.ui.lista.ListaMotosFragment
import kotlinx.android.synthetic.main.fragment_nova_moto.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NovaMotoFragment : Fragment() {

    var moto: Moto = Moto("", "", "", 0, "", "", 0, "")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_nova_moto, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!moto.id.isNullOrEmpty()){
            inputMarca.editText?.setText(moto.marca)
            inputModelo.editText?.setText(moto.modelo)
            inputAno.editText?.setText(Integer.toString(moto.ano))
            inputPlaca.editText?.setText(moto.placa)
            inputUrlImagem.editText?.setText(moto.urlimagem)
            inputPreco.editText?.setText(Integer.toString(moto.preco))
            inputDescricao.editText?.setText(moto.descricao)
            btnDeletar.visibility = View.VISIBLE
        } else {
            btnDeletar.visibility = View.GONE
        }

        btSalvar.setOnClickListener {
            val api = RetrofitClient
                    .getInstance("https://motorcycleapi.herokuapp.com")
                    .create(MotoAPI::class.java)

            if (!validarCadastro()) {
                Toast.makeText(context, "Preencher todos os campos", Toast.LENGTH_LONG).show()
            } else if(inputAno.editText?.length()!! == 4 && inputPreco.editText?.length()!! > 3 && inputPreco.editText?.length()!! < 7){

                if(moto.id.isNullOrEmpty()) {
                    val moto = Moto(null,
                            inputMarca.editText?.text.toString().toLowerCase(),
                            inputModelo.editText?.text.toString(),
                            inputAno.editText?.text.toString().toInt(),
                            inputPlaca.editText?.text.toString(),
                            inputUrlImagem.editText?.text.toString(),
                            inputPreco.editText?.text.toString().toInt(),
                            inputDescricao.editText?.text.toString())

                    api.salvar(moto).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                            if (response?.isSuccessful == true) {
                                Toast.makeText(context,
                                        "Moto cadastrada com Sucesso",
                                        Toast.LENGTH_SHORT).show()
                                limparCampos()
                                val activity = context as MainActivity
                                activity.changeFragment(ListaMotosFragment())

                            } else {
                                Toast.makeText(context,
                                        "Não foi possivel cadastrar a moto",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            Log.e("MOTO", t?.message)
                        }
                    })
                } else {
                    val moto = Moto(moto.id,
                            inputMarca.editText?.text.toString().toLowerCase(),
                            inputModelo.editText?.text.toString(),
                            inputAno.editText?.text.toString().toInt(),
                            inputPlaca.editText?.text.toString(),
                            inputUrlImagem.editText?.text.toString(),
                            inputPreco.editText?.text.toString().toInt(),
                            inputDescricao.editText?.text.toString())

                    api.update(moto).enqueue(object : Callback<Void>{
                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                            if (response?.isSuccessful == true) {
                                Toast.makeText(context, "Moto alterada com sucesso!", Toast.LENGTH_SHORT).show()
                                limparCampos()
                                val activity = context as MainActivity
                                activity.changeFragment(ListaMotosFragment())
                            } else {
                                Toast.makeText(context, "Não foi possivel alterar a Moto!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            Log.e("MOTO", t?.message)                        }
                    })
                }
            }else{
                Toast.makeText(context,
                        "Ano deve ter 4 caracteres e Preço deve ter entre 4 e 6 caracteres",
                        Toast.LENGTH_LONG).show()
            }
        }

        btnDeletar.setOnClickListener {
            val api = RetrofitClient.getInstance("https://motorcycleapi.herokuapp.com").create(MotoAPI::class.java)

            if(!moto.id.isNullOrEmpty()) {
                api.delete(moto.id!!).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response?.isSuccessful == true) {
                            Toast.makeText(context, "Moto deletada com Sucesso!", Toast.LENGTH_SHORT).show()
                            limparCampos()
                            val activity = context as MainActivity
                            activity.changeFragment(ListaMotosFragment())
                        } else {
                            Toast.makeText(context, "Moto não pode ser deletada!!!", Toast.LENGTH_SHORT).show()
                        }                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e("MOTO", t?.message)                    }
                })
            }
        }
    }

    fun validarCadastro(): Boolean {
        var valor = true

        if(inputMarca.editText?.text.toString() == null || inputMarca.editText?.text.toString().equals("")) {
            valor = false
        }

        if(inputModelo.editText?.text.toString() == null || inputModelo.editText?.text.toString().equals("")){
            valor = false
        }

        if(inputAno.editText?.text.toString() == null || inputAno.editText?.text.toString().equals("")){
            valor = false
        }

        if(inputPreco.editText?.text.toString() == null || inputPreco.editText?.text.toString().equals("")){
            valor = false
        }

        return valor
    }

    private fun limparCampos() {
        moto.id = null
        inputMarca.editText?.setText("")
        inputModelo.editText?.setText("")
        inputAno.editText?.setText("")
        inputPlaca.editText?.setText("")
        inputUrlImagem.editText?.setText("")
        inputPreco.editText?.setText("")
        inputDescricao.editText?.setText("")
    }
}
