package com.burakusluer.foursquarekotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burakusluer.foursquarekotlin.R
import com.burakusluer.foursquarekotlin.adapter.RecyclerAdapterLocations
import com.burakusluer.foursquarekotlin.functions.DefaultFunctions
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class LocationsActivity : AppCompatActivity() {
    //region Init Area
    fun init() {
        recyclerViewLocations = findViewById(R.id.recyclerViewLocations)
        recyclerViewLocations.layoutManager=LinearLayoutManager(this)
    }

    private lateinit var recyclerViewLocations: RecyclerView
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)
        init()
        CoroutineScope(Dispatchers.IO).launch {
            val query = ParseQuery.getQuery<ParseObject>("Locations")
            query.selectKeys(listOf("name"))
            val names = ArrayList<String>()
            query.findInBackground { objects, e ->
                if (objects.size > 0) {
                    objects.forEach {
                        it.getString("name")?.let { it1 -> names.add(it1) }
                    }
                    val recyclerViewAdapter=RecyclerAdapterLocations(names)
                    recyclerViewLocations.adapter=recyclerViewAdapter
                }
            }



        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.locations_activity_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemAddLocationActivityStarter -> DefaultFunctions.ActivityTraveler(
                this,
                AddLocation::class.java,
                false
            )
        }

        return super.onOptionsItemSelected(item)
    }

}
