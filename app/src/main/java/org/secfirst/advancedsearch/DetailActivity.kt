package org.secfirst.advancedsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
                val body =
                    "<head><style>body{color:#444444;}img{width:100%}h1{color:#33b5e5; font-weight:normal; text-align:center}h2{color:#9ABE2E; font-weight:normal;}getDifficultyFromId{color:#33b5e5}.button,.button:link{display:block;text-decoration:none;color:white;border:none;width:100%;text-align:center;border-radius:3px;padding-top:10px;padding-bottom:10px;}.green{background:#9ABE2E}.purple{background:#b83656}.yellow{background:#f3bc2b}</style></head><body>${it.text}</body>"
                runOnUiThread {
                    segmentDetailText.loadDataWithBaseURL(
                        "",
                        body,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
                segmentDetailCategory.text = getString(R.string.category_details, it.category.title)
                segmentDetailDifficulty.text = getString(R.string.difficulty_details, it.difficulty.name)
            }, {
                runOnUiThread {
                    Toast.makeText(this@DetailActivity, "The link is invalid", Toast.LENGTH_SHORT).show()
                }
                finish()
            })

}
