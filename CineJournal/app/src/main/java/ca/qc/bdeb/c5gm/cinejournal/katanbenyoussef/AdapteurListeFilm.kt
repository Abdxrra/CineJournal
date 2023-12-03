package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AdapteurListeFilm(
    val ctx: Context,
    val activity: MainActivity,
    var data: ArrayList<Film>,
    val onclick:(Film)->Unit
) : RecyclerView.Adapter<ItemFilmHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilmHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.liste_item, parent, false)
        return ItemFilmHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemFilmHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.layout.setOnClickListener {onclick(item)}
    }

    fun addAllFilms(films: List<Film>) {
        this.data = films as ArrayList<Film>
        notifyDataSetChanged()
    }

    fun addFilm(film: Film) {
        this.data.add(film)
        notifyDataSetChanged()

    }

    fun updateFilm(film: Film){
        for (f in data) {
            if(f.uid == film.uid){
                val index = this.data.indexOf(f)
                this.data[index] = film
                break
            }
        }

        notifyDataSetChanged()
    }

    fun test(){
        notifyDataSetChanged()
    }

    fun deleteAllFilms(){
        this.data.clear()
        notifyDataSetChanged()
    }
}