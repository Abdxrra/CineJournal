package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapteurListeFilm(
    val ctx: Context, val activity: MainActivity, var data: ArrayList<ItemView>
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
    }

    fun addAllFilms(films: ArrayList<ItemView>) {
        this.data = films
        notifyDataSetChanged()
    }

    fun addFilm(film: ItemView) {
        this.data.add(film)
        notifyItemInserted(this.data.size - 1)
    }

    fun deleteAllFilms() {
        this.data.clear()
        notifyDataSetChanged()
    }
}