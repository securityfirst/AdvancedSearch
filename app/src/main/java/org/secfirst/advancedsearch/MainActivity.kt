package org.secfirst.advancedsearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.secfirst.advancedsearch.mvp.models.Category
import org.secfirst.advancedsearch.mvp.models.Difficulty
import org.secfirst.advancedsearch.mvp.models.Segment
import org.secfirst.advancedsearch.util.Global
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        centerText.text = ""
        createRecords()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
            isSubmitButtonEnabled = true
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    p0?.let {
                        val i = Intent(this@MainActivity, SearchActivity::class.java)
                        i.action = Intent.ACTION_SEARCH
                        i.putExtra(SearchManager.QUERY, it)
                        startActivity(i)
                        return true
                    }
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }

            })
        }
        Logger.getLogger(javaClass.simpleName).info("on create options menu")
        return true
    }

    override fun onSearchRequested(): Boolean {
        Logger.getLogger(javaClass.simpleName).info("search requested")
        return super.onSearchRequested()
    }

    private fun createRecords(): Disposable? {
        return Global.instance.db?.segmentDao()?.getAll()?.subscribe {
            if (it.isEmpty()) {
                val segmentsToInsert = arrayOf(
                    Segment(
                        id = "first",
                        title = "First title",
                        text = "First text",
                        difficulty = Difficulty("advanced", "Advanced"),
                        category = Category("personal", "Personal", "")
                    ),
                    Segment(
                        id = "second",
                        title = "Second title",
                        text = "second text",
                        difficulty = Difficulty("beginner", "Beginner"),
                        category = Category("information", "Information", "")
                    )
                )
                Global.instance.db?.segmentDao()?.insertAll(*segmentsToInsert)?.subscribe {
                    Logger.getLogger("createRecords").info("krneki")
                } ?: kotlin.run {
                    Logger.getLogger("createRecords").info("There was an error inserting the records")
                }
            }
        }
    }
}
