package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ItemFilmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: ConstraintLayout
    val image: ImageView
    val nom: TextView
    val description: TextView
    val rate: RatingBar
    val annee: TextView

    init {
        layout = itemView as ConstraintLayout
        image = itemView.findViewById(R.id.filmImage)
        nom = itemView.findViewById(R.id.filmTitle)
        description = itemView.findViewById(R.id.filmDescription)
        rate = itemView.findViewById(R.id.filmRating)
        annee = itemView.findViewById(R.id.filmAnnee)
    }

    fun bind(item: ItemView) {
        image.setImageResource(item.image)
        nom.text = item.titre
        description.text = item.description
        rate.rating = item.rating.toFloat()
        annee.text = item.annee.toString()
    }
}