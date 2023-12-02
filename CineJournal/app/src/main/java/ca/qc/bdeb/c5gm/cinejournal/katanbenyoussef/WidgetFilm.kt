package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    Log.d("Main", images.toString())

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

            // Load image depuis une URL
            val imageUrl = "https://image.tmdb.org/t/p/w500/pD6sL4vntUOXHmuvJPPZAgvyfd9.jpg" // Replace with your image URL
            WidgetImageLoader(view, appWidgetManager, appWidgetId).execute(imageUrl)

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