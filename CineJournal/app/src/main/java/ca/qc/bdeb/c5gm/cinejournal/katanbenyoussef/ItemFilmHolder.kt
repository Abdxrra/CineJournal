package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemFilmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: ConstraintLayout
    val image: ImageView
    val nom: TextView
    val description: TextView
    val rate: RatingBar
    val annee: TextView

    init {

        layout = itemView as ConstraintLayout
        image = itemView.findViewById(R.id.imageFilm)
        nom = itemView.findViewById(R.id.titreFilm)
        description = itemView.findViewById(R.id.filmDescription)
        rate = itemView.findViewById(R.id.editFilmRating)
        annee = itemView.findViewById(R.id.anneeFilm)
    }

    fun bind(item: Film) {
        if (item.imageUri != "") {
            if(item.imageUri.startsWith("https")){
                Glide.with(itemView)
                    .load(item.imageUri)
                    .into(image)
            }
            else{
                image.setImageURI(item.imageUri.toUri())
            }
        } else {
            image.setImageResource(R.drawable.defaultimage)
        }
        nom.text = item.titre
        description.text = item.description
        rate.rating = item.rating.toFloat()
        annee.text = item.annee.toString()
        layout.setOnClickListener{

        }

    }
}