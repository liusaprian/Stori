package app.liusaprian.stori.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import app.liusaprian.stori.R
import app.liusaprian.stori.data.SessionManager
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.network.response.Story

internal class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val stories = ArrayList<Story>()
    private val repo = StoriRepository(SessionManager(context))

    override fun onCreate() {}

    override fun onDataSetChanged() {
        stories.addAll(repo.getStoryList())
    }

    override fun onDestroy() {}

    override fun getCount() = stories.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(
            R.id.story_iv,
            StoryWidget.getBitmapFromURL(stories[position].photoUrl)
        )
        val extras = bundleOf(StoryWidget.NAME to stories[position].name)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.story_iv, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount() = 1

    override fun getItemId(p0: Int) = 0L

    override fun hasStableIds() = false
}