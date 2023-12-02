package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.app.Activity
import android.app.IntentService
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.transition.Transition
import android.util.Log
import android.widget.RemoteViews
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.AppWidgetTarget

/**
 * Implementation of App Widget functionality.
 */

class WidgetFilm : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

        fetchAndDisplayMovies()
    }
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            "Avant" -> {
                if(imageTracker - 1< 0 ){
                    imageTracker = images.lastIndex
                }else{
                    imageTracker--
                }

                updateWidget(context)
            }
            "Apres" -> {
                if(images.lastIndex < imageTracker + 1){
                    imageTracker = 0
                }else{
                    imageTracker++
                }
                updateWidget(context)
            }
        }
    }

    private fun updateWidget(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)

        val widget = ComponentName(context, WidgetFilm::class.java)

        updateAppWidget(context, appWidgetManager, appWidgetManager.getAppWidgetIds(widget)[0])
    }

    companion object {
        private var imageTracker = 0
        lateinit var filmResults: List<FilmResults>;
        lateinit var images: List<String>;// = listOf(R.drawable.app_widget_inner_view_background, R.drawable.app_widget_background)


        private fun fetchAndDisplayMovies() {
            GlobalScope.launch {
                val response = ApiClient.apiService.getNowPlayingMovies()

                if (response.isSuccessful) {
                    filmResults = response.body()?.results!!
                    images = filmResults.map { it.poster_path }

                } else {
                    // Handle API error
                }
            }
        }

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val view = RemoteViews(context.packageName, R.layout.widget_film)

            // cover du film
            //view.setImageViewResource(R.id.imageView, images[imageTracker])
            // LIBRAIRIE GLIDE POUR LES IMAGES, LE CACHING, ETC. (l'autre façon ne marchait pas)

            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w500/pD6sL4vntUOXHmuvJPPZAgvyfd9.jpg")
                .submit(512, 512)
                .get()

            view.setImageViewBitmap(R.id.imageView, bitmap)

            /*
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/pD6sL4vntUOXHmuvJPPZAgvyfd9.jpg")
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(awt)

            view.setImageViewUri(R.id.imageView, Uri.parse("https://image.tmdb.org/t/p/w500/pD6sL4vntUOXHmuvJPPZAgvyfd9.jpg"))

             */

            // click sur avant
            val intentPrecedent = Intent(context, WidgetFilm::class.java)
            intentPrecedent.action = "Avant"

            val intentPrecedentPending =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intentPrecedent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            view.setOnClickPendingIntent(R.id.buttonAvant, intentPrecedentPending)

            // click sur apres
            val intentSuivant = Intent(context, WidgetFilm::class.java)
            intentSuivant.action = "Apres"

            val intentSuivantPending =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intentSuivant,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            view.setOnClickPendingIntent(R.id.buttonApres, intentSuivantPending)

            // click sur ajouter
            val intentMainActivity = Intent(context, MainActivity::class.java)

            val intentMainPending =
                PendingIntent.getActivity(
                    context,
                    0,
                    intentMainActivity,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

            view.setOnClickPendingIntent(R.id.buttonAjouter, intentMainPending)

            //met a jour le view
            appWidgetManager.updateAppWidget(appWidgetId, view)

        }
    }
}