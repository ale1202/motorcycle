package motorcycle.motor.com.br.motor.ui.lista

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import motorcycle.motor.com.br.motor.MainActivity
import motorcycle.motor.com.br.motor.R
import motorcycle.motor.com.br.motor.model.Moto
import motorcycle.motor.com.br.motor.ui.novo.NovaMotoFragment
import kotlinx.android.synthetic.main.item_moto.view.*

class ListaMotosAdapter(private val motos: List<Moto>, private val context: Context) : RecyclerView.Adapter<ListaMotosAdapter.MotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MotosViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_moto, parent, false)
        return MotosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return motos.size
    }

    override fun onBindViewHolder(holder: MotosViewHolder?, position: Int) {
        val moto = motos[position]
        holder?.let {
            it.bindView(moto, context)
        }
    }

    class MotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(moto: Moto, context: Context) {

            val activity = context as MainActivity

            itemView.setOnClickListener{
                val edicao = NovaMotoFragment()
                edicao.moto = moto
                activity.changeFragment(edicao)
            }

            itemView.tvPreco.text = "R$ " + moto.preco.toString()
            itemView.tvModelo.text = moto.modelo
            if (moto.urlimagem.isNullOrEmpty()) {
                itemView.ivFotoMoto.setBackgroundResource(R.drawable.nofound_moto)
            } else {
                Picasso.get()
                        .load(moto.urlimagem)
                        .placeholder(R.drawable.spinner_loading)
                        .error(R.drawable.nofound_moto)
                        .into(itemView.ivFotoMoto)
            }
        }
    }
}