package com.chuzi.android.nim.emoji

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Xml
import androidx.collection.LruCache
import com.chuzi.android.shared.global.AppGlobal.context
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.regex.Pattern

object EmojiManager {

    private const val EMOT_DIR = "emoji/"

    private const val CACHE_MAX_SIZE = 1024

    var pattern: Pattern = makePattern()

    private val defaultEntries: MutableList<Entry> = ArrayList()

    private val text2entry: MutableMap<String, Entry> = HashMap()

    private var drawableCache: LruCache<String, Bitmap>? = null

    /**
     * 提前预加载emoji
     *
     * @param context
     */
    fun initLoad(context: Context) {
        for (i in 0 until displayCount) {
            getDisplayDrawable(context, i)
        }
    }

    val displayCount: Int get() = defaultEntries.size

    fun getDisplayDrawable(context: Context, index: Int): Drawable? {
        val text =
            if (index >= 0 && index < defaultEntries.size) defaultEntries[index].text else null
        return if (text == null) null else getDrawable(context, text)
    }

    fun getDisplayText(index: Int): String? {
        return if (index >= 0 && index < defaultEntries.size) defaultEntries[index].text else null
    }

    fun getDrawable(context: Context, text: String): Drawable? {
        val entry = text2entry[text] ?: return null
        var cache = drawableCache!![entry.assetPath]
        if (cache == null) {
            cache = loadAssetBitmap(context, entry.assetPath)
        }
        return BitmapDrawable(context.resources, cache)
    }

    private fun makePattern(): Pattern {
        return Pattern.compile(patternOfDefault())
    }

    private fun patternOfDefault(): String {
        return "\\[[^\\[]{1,10}\\]"
    }

    private fun loadAssetBitmap(context: Context, assetPath: String): Bitmap? {
        var steam: InputStream? = null
        try {
            val resources = context.resources
            val options = BitmapFactory.Options()
            options.inDensity = DisplayMetrics.DENSITY_HIGH
            options.inScreenDensity = resources.displayMetrics.densityDpi
            options.inTargetDensity = resources.displayMetrics.densityDpi
            steam = context.assets.open(assetPath)
            val bitmap = BitmapFactory.decodeStream(steam, Rect(), options)
            if (bitmap != null) {
                drawableCache!!.put(assetPath, bitmap)
            }
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (steam != null) {
                try {
                    steam.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    private class Entry internal constructor(var text: String, var assetPath: String)

    private class EntryLoader : DefaultHandler() {
        private var catalog = ""
        fun load(context: Context, assetPath: String?) {
            var stream: InputStream? = null
            try {
                stream = context.assets.open(assetPath!!)
                Xml.parse(stream, Xml.Encoding.UTF_8, this)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            } finally {
                if (stream != null) {
                    try {
                        stream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        @Throws(SAXException::class)
        override fun startElement(
            uri: String,
            localName: String,
            qName: String,
            attributes: Attributes
        ) {
            if (localName == "Catalog") {
                catalog = attributes.getValue(uri, "Title")
            } else if (localName == "Emoticon") {
                val tag = attributes.getValue(uri, "Tag")
                val fileName = attributes.getValue(uri, "File")
                val entry = Entry(tag, "$EMOT_DIR$catalog/$fileName")
                text2entry[entry.text] = entry
                if (catalog == "default") {
                    defaultEntries.add(entry)
                }
            }
        }
    }

    init {
        EntryLoader().load(context, EMOT_DIR + "emoji.xml")
        drawableCache = object : LruCache<String, Bitmap>(CACHE_MAX_SIZE) {
            override fun entryRemoved(
                evicted: Boolean,
                key: String,
                oldValue: Bitmap,
                newValue: Bitmap?
            ) {
                if (oldValue != newValue) oldValue.recycle()
            }
        }
    }
}