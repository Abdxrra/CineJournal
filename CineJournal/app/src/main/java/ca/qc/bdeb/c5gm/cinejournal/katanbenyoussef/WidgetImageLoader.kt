package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.appwidget.AppWidgetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.RemoteViews
import java.io.InputStream
import java.net.URL

class WidgetImageLoader(
    private val views: RemoteViews,
    private val appWidgetManager: AppWidgetManager,
    private val appWidgetId: Int
) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg params: String): Bitmap? {
        val imageUrl = params[0]
        return try {
            // charge l'image depuis une URL
            val input: InputStream = URL(imageUrl).openStream()
            // transforme en bitmap
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        result?.let {
            // ajoute à l'interface
            views.setImageViewBitmap(R.id.imageView, it)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}