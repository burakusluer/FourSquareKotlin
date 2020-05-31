package com.burakusluer.foursquarekotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.burakusluer.foursquarekotlin.R
import com.parse.*
import com.squareup.picasso.Picasso

class ShowLocationActivity : AppCompatActivity() {

    //region Init Area
    private lateinit var imageView:ImageView
    private lateinit var textView:TextView
    private fun init() {
        imageView=findViewById(R.id.imageViewShowLocation)
        textView=findViewById(R.id.textViewShowLocation)
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_location)
        init()
        val name=intent.getStringExtra("locationName")
        val query=ParseQuery<ParseObject>("Locations")
        query.whereEqualTo("name",name)
        query.getFirstInBackground{ parseObject: ParseObject?, parseException: ParseException? ->
            val parseFile:ParseFile?=parseObject?.getParseFile("picture")
            Picasso.get().load(parseFile?.url).into(imageView)
            val p= ParseGeoPoint(parseObject?.getParseGeoPoint("location"))
            val fragmentManager=supportFragmentManager;
            val bundle:Bundle= Bundle()
            bundle.putString("locationName",parseObject?.getString("name"))
            bundle.putDoubleArray("locationLatlng", doubleArrayOf(p.latitude,p.longitude))
            val showMaps=ShowMaps();
            showMaps.arguments=bundle;
            fragmentManager.beginTransaction().replace(R.id.fragment,showMaps).commit()

        }
    }
}