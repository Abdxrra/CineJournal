package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class ItemRechercheHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layout: ConstraintLayout
    val image: ImageView
    val nom: TextView
    val annee: TextView

    init {

        layout = itemView as ConstraintLayout
        image = itemView.findViewById(R.id.resultatImage)
        nom = itemView.findViewById(R.id.resultatFilm)
        annee = itemView.findViewById(R.id.resultatAnnee)
    }

    fun bind(item: Film) {
        if (item.imageUri != "") {
            image.setImageURI(item.imageUri.toUri())
        } else {
            image.setImageResource(R.drawable.defaultimage)
        }
        nom.text = item.titre
        annee.text = item.annee.toString()
        layout.setOnClickListener{

        }

    }
}