package org.secfirst.advancedsearch

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        intent?.data?.lastPathSegment?.let { id ->
            fetchAndDisplayById(id)
        }
    }

    private fun fetchAndDisplayById(id: String) = AdvancedSearchApp.instance.db?.segmentDao()?.loadById(id)?.
            subscribe ({
                segmentDetailTitle.text = it.title
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    segmentDetailText.text = Html.fromHtml(it.text, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    segmentDetailText.text = Html.fromHtml(it.text)
                }
                segmentDetailCategory.text = it.category.title
                segmentDetailDifficulty.text = it.difficulty.name
            }, {
                Toast.makeText(this@DetailActivity, "The link is invalid", Toast.LENGTH_SHORT).show()
                finish()
            })

}
