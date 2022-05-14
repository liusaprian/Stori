package app.liusaprian.stori.ui.locaton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.viewbinding.library.activity.viewBinding
import app.liusaprian.stori.R
import app.liusaprian.stori.databinding.ActivityLocationBinding
import app.liusaprian.stori.network.response.Story
import app.liusaprian.stori.ui.detail.DetailActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding: ActivityLocationBinding by viewBinding()
    private lateinit var map: GoogleMap
    private lateinit var data: Story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getParcelableExtra<Story>(DetailActivity.STORY_DETAIL)?.let {
            data = it
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gmap: GoogleMap) {
        map = gmap

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isIndoorLevelPickerEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMapToolbarEnabled = true

        val storyLoc = LatLng(data.lat, data.lon)
        Log.d("asdasd", data.lat.toString() + " " + data.lon.toString())
        map.addMarker(
            MarkerOptions()
                .position(storyLoc)
                .title(getString(R.string.location_content_description, data.name))
        )
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(storyLoc, 15f))
    }
}