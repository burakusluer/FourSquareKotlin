package com.burakusluer.foursquarekotlin.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.burakusluer.foursquarekotlin.R
import com.burakusluer.foursquarekotlin.view.ShowLocationActivity
import java.util.ArrayList

class RecyclerAdapterLocations(var dataArray: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerAdapterLocations.RecyclerViewHolderLocations>() {
    private lateinit var context:Context
    inner class RecyclerViewHolderLocations(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView:TextView=itemView.findViewById(R.id.textViewRecyclerLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolderLocations {
        context=parent.context;
        return RecyclerViewHolderLocations(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_locations_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolderLocations, position: Int) {
        holder.textView.text=dataArray[position]
        holder.textView.setOnClickListener {
           val intent= Intent(context as Activity,ShowLocationActivity::class.java)
            intent.putExtra("locationName",holder.textView.text.toString())
            (context as Activity).startActivity(intent)
        }
    }
}