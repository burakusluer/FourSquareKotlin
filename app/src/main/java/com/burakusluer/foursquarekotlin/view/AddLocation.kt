package com.burakusluer.foursquarekotlin.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.burakusluer.foursquarekotlin.R
import com.burakusluer.foursquarekotlin.functions.DefaultFunctions
import com.google.android.gms.maps.model.LatLng
import com.parse.ParseFile
import com.parse.ParseGeoPoint
import com.parse.ParseObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddLocation : AppCompatActivity() {
    //region Init Area


    private lateinit var imageView: ImageView
    private lateinit var editText: EditText
    private var selectedLocation: LatLng? = null
    private var image: Bitmap? = null
    private var job: Job? = null
    private fun init() {

        imageView = findViewById(R.id.imageViewAddLocation)
        editText = findViewById(R.id.editTextAddLocationName)
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        init()
        imageView.setOnLongClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@AddLocation,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivityForResult(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ), 2
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@AddLocation,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
            return@setOnLongClickListener true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && requestCode == 1 && ContextCompat.checkSelfPermission(
                this@AddLocation,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            imageView.performLongClick()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 2) {
                val imageUri = data.data!!
                try {
                    if (Build.VERSION.SDK_INT >= 28) {
                        image = ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                this@AddLocation.contentResolver,
                                imageUri
                            )
                        )
                        imageView.setImageBitmap(image)
                    } else {
                        image = MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            imageUri
                        )
                        imageView.setImageBitmap(image)
                    }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    Toast.makeText(this@AddLocation, ex.localizedMessage, Toast.LENGTH_LONG).show()
                }
            } else if (requestCode == 15) {
                val a = data.getDoubleArrayExtra("locationData")
                if (a != null) {
                    selectedLocation = LatLng(a[0], a[1])
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this@AddLocation).inflate(R.menu.add_location_activity_options_menu, menu)
        if (selectedLocation?.latitude != 0.0) {
            menu?.findItem(R.id.itemAddLocationPickGeoPoint)?.title = "updateGeoPoint"
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemAddLocationPickGeoPoint -> startActivityForResult(
                Intent(this@AddLocation, SelectLocationActivity::class.java),15)

        }
        return super.onOptionsItemSelected(item)
    }

    fun addLocation(view: View) {
        when (view.id) {
            R.id.buttonAddLocation -> {
                if (selectedLocation != null && selectedLocation?.latitude != 0.0 && image != null) {
                    job = CoroutineScope(Dispatchers.IO).launch {
                        val parseObject = ParseObject("Locations")
                        parseObject.put("name", editText.text.toString())
                        parseObject.put(
                            "location",
                            ParseGeoPoint(selectedLocation!!.latitude, selectedLocation!!.longitude)
                        )
                        val byteArrOutStream = ByteArrayOutputStream()
                        image!!.compress(Bitmap.CompressFormat.PNG, 50, byteArrOutStream)
                        parseObject.put("picture", ParseFile(byteArrOutStream.toByteArray()))
                        parseObject.saveInBackground {
                            if (it != null) {
                                it.printStackTrace()
                                Toast.makeText(
                                    this@AddLocation,
                                    it.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                DefaultFunctions.ActivityTraveler(
                                    this@AddLocation,
                                    LocationsActivity::class.java
                                )
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@AddLocation,
                        "First Check GeoPoint On Options Menu Of This Activity",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        if (job != null && job!!.isCompleted) {
            job!!.cancel()
        }
        super.onDestroy()
    }
}