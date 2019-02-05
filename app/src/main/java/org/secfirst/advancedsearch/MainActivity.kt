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
        return true
    }

    override fun onSearchRequested(): Boolean {
        return super.onSearchRequested()
    }

    private fun createRecords() {
        AdvancedSearchApp.instance.db?.segmentDao()?.getAll()?.subscribe {
            if (it.isEmpty()) {
                val segmentsToInsert = arrayOf(
                    Segment(
                        id = "first",
                        title = "First title",
                        text = "<h1>Arrivals or Departures</h1>\n" +
                                "\n" +
                                "<h2>Prepare for anything</h2>\n" +
                                "\n" +
                                "<p>Assume that:</p>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li>You will be searched.</li>\n" +
                                "<li>Anything written will be read or copied.</li>\n" +
                                "<li>Your equipment will be confiscated.</li>\n" +
                                "</ul>\n" +
                                "\n" +
                                "<p><em>Note: Authorities may delay you or seize your devices if you refuse to give up a password or unlock your device.</em></p>\n" +
                                "\n" +
                                "<p>(Learn more about this in <a href=\"umbrella://lesson/passwords/2\">passwords expert</a>.)</p>\n" +
                                "\n" +
                                "<h2>Know your cover story</h2>\n" +
                                "\n" +
                                "<p>What you reveal to border authorities depends on context and risk.</p>\n" +
                                "\n" +
                                "<p>(Learn about risk assessment for travel <a href=\"umbrella://lesson/preparation\">preparation</a>.)</p>\n" +
                                "\n" +
                                "<p>A cover story does not have to be the full truth, but it should be:</p>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li>Simple;</li>\n" +
                                "<li>Easily repeatable;</li>\n" +
                                "<li>Verifiable.</li>\n" +
                                "</ul>\n" +
                                "\n" +
                                "<p>It may not be wise, or possible, to lie, but neither should you volunteer information.</p>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li><p>Provide minimal details.</p></li>\n" +
                                "\n" +
                                "<li><p>Be prepared to be questioned about recent travel, visas or passport stamps.</p></li>\n" +
                                "\n" +
                                "<li><p>Border authorities may require certain information, like where you will stay. Decide in advance how much to reveal.</p></li>\n" +
                                "</ul>\n" +
                                "\n" +
                                "<p><em>Note: Providing real information may increase your risk, but providing false information may be a red flag.</em></p>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li>Set boundaries in advance. What sensitive information will you absolutely not reveal?</li>\n" +
                                "</ul>\n" +
                                "\n" +
                                "<h2>Coordinate</h2>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li><p>Agree what you will say with your team and set boundaries in advance.</p></li>\n" +
                                "\n" +
                                "<li><p>Teams may attract attention. Travel and approach immigration separately.</p></li>\n" +
                                "</ul>\n" +
                                "\n" +
                                "<p><em>Note: Authorities may already be aware that a big team is planning a visit or a meeting.</em></p>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li>Set a place to meet after immigration.</li>\n" +
                                "</ul>\n" +
                                "\n" +
                                "<h2>Bring something else to do.</h2>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li>Keep phone and Wi-Fi off, except in an emergency.<br />\n" +
                                "</li>\n" +
                                "</ul>\n" +
                                "\n" +
                                "<p><em>Note: Airport networks are notorious targets for surveillance and malware. Do not connect to airport Wi-Fi.</em></p>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li><p>In case of delay or emergency, set a time to turn on phones and check in.</p></li>\n" +
                                "\n" +
                                "<li><p>Working may attract attention, especially if you are travelling low profile.</p></li>\n" +
                                "\n" +
                                "<li><p>Do not discuss your trip, either with your team or on the phone. You may be overheard.</p></li>\n" +
                                "</ul>\n" +
                                "\n" +
                                "<h2>Remember:</h2>\n" +
                                "\n" +
                                "<ul>\n" +
                                "<li><p>Be polite, calm, confidant, and firm with border authorities.</p></li>\n" +
                                "\n" +
                                "<li><p>Do not get separated from your belongings, especially electronic items. If they are taken away, assume they are compromised.</p></li>\n" +
                                "</ul>",
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
