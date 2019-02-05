package org.secfirst.advancedsearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import org.secfirst.advancedsearch.mvp.models.Category
import org.secfirst.advancedsearch.mvp.models.Difficulty
import org.secfirst.advancedsearch.mvp.models.Segment
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

    private fun createRecords() {
        AdvancedSearchApp.instance.db?.segmentDao()?.getAll()?.subscribe {
            if (it.isEmpty()) {
                val segmentsToInsert = arrayOf(
                    Segment(
                        id = "first",
                        title = "First title",
                        text = "<h2 id=\"organisefiles\">Organise files.</h2>\n" +
                                "\n" +
                                "<pre><code>Move all folders containing documents to back up into a single location, such as My Documents.\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h2 id=\"seeifprogramsandappswithstoragedatabaseswillallowyoutochooseanewstoragelocation\">See if programs and apps with storage databases will allow you to choose a new storage location.</h2>\n" +
                                "\n" +
                                "<pre><code>If so,  put them in the same folder.\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h2 id=\"createaregularbackupschedule\">Create a regular backup schedule.</h2>\n" +
                                "\n" +
                                "<pre><code>Set a calendar reminder.\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h2 id=\"establishbackupproceduresforfamilyandcolleagues\">Establish backup procedures for family and colleagues.</h2>\n" +
                                "\n" +
                                "<pre><code>Help them understand the risk of data loss.\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h2 id=\"testtheprocessyouwillusetorecoverdatafromyourbackup\">Test the process you will use to recover data from your backup.</h2>\n" +
                                "\n" +
                                "<pre><code>Recovery matters more than anything!\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h2 id=\"storebackupssecurely\">Store backups securely.</h2>\n" +
                                "\n" +
                                "<pre><code>Backups must be separate from the original information. Don't keep your a USB stick with work backups in your office!\n" +
                                "</code></pre>",
                        difficulty = Difficulty("advanced", "Advanced"),
                        category = Category("personal", "Personal", "")
                    ),
                    Segment(
                        id = "second",
                        title = "Second title",
                        text = "<h1 id=\"accidentaldeletion\">Accidental deletion</h1>\n" +
                                "\n" +
                                "<p>When you delete a file in on your computer, it may disappear, but its contents can be recovered.</p>\n" +
                                "\n" +
                                "<p>(Learn more about <a href=\"umbrella://lesson/safely-deleting\">safely deleting</a>.) </p>\n" +
                                "\n" +
                                "<p>If you accidentally delete information, this security vulnerability can work to your advantage. </p>\n" +
                                "\n" +
                                "<h1 id=\"recoverysoftware\">Recovery software</h1>\n" +
                                "\n" +
                                "<p>Several programs can restore access to recently deleted files, such as Recuva for Windows. </p>\n" +
                                "\n" +
                                "<p>TestDisk and PhotoRec by [CGSecurity] (www.cgsecurity.org/) offer free, open-source data recovery for Linux, Mac, and Windows.</p>\n" +
                                "\n" +
                                "<p>(Learn how to use [Recuva] (umbrella://lesson/recuva) in our tool guide.)</p>\n" +
                                "\n" +
                                "<h1 id=\"recoverytips\">Recovery tips</h1>\n" +
                                "\n" +
                                "<h2 id=\"doaslittleaspossiblewithyourcomputerafteryoudeleteafilebyaccident\">Do as little as possible with your computer after you delete a file by accident.</h2>\n" +
                                "\n" +
                                "<pre><code>The longer you use your computer before attempting to restore the file, the less likely you are to succeed. \n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h2 id=\"installrecoverysoftwarebeforeyouneedit\">Install recovery software <em>before</em> you need it!</h2>\n" +
                                "\n" +
                                "<p>Or use the portable version of a program like Recuva. Don't install it for the first time <em>after</em> you delete an important file by accident.</p>",
                        difficulty = Difficulty("beginner", "Beginner"),
                        category = Category("information", "Information", "")
                    )
                )
                AdvancedSearchApp.instance.db?.segmentDao()?.insertAll(*segmentsToInsert)?.subscribe {
                    Logger.getLogger("createRecords").info("Done")
                }?.dispose() ?: kotlin.run {
                    Logger.getLogger("createRecords").info("There was an error inserting the records")
                }
            }
        } ?: kotlin.run {
            Logger.getLogger("createRecords").info("Not empty?")
        }
    }
}
