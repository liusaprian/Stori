package app.liusaprian.stori.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.liusaprian.stori.data.SessionManager
import app.liusaprian.stori.data.StoriRepository
import app.liusaprian.stori.databinding.ActivityAddStoryBinding
import app.liusaprian.stori.ui.camera.CameraActivity
import app.liusaprian.stori.ui.home.HomeActivity
import app.liusaprian.stori.utils.rotateBitmap
import app.liusaprian.stori.utils.uriToFile
import app.liusaprian.stori.viewmodel.StoryViewModel
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {

    private val binding: ActivityAddStoryBinding by viewBinding()
    private var getFile: File? = null

    private val storyViewModel by lazy {
        StoryViewModel(
            StoriRepository.getInstance(
                SessionManager(this)
            )
        )
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        const val PICTURE = "picture"
        const val IS_BACK_CAMERA = "is_back_camera"
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "No permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        with(binding) {
            cameraBtn.setOnClickListener { startCameraX() }
            galleryBtn.setOnClickListener { startGallery() }
            addStoryBtn.setOnClickListener {
                showLoading(true)
                addStory()
            }
        }
        storyViewModel.uploadSuccess.observe(this) {
            showLoading(false)
            if(it == null) Toast.makeText(this, "Error while uploading to server", Toast.LENGTH_LONG).show()
            else {
                if(!it.error) {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                    setResult(HomeActivity.NEW_STORY_RESULT, Intent())
                    finish()
                } else
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun addStory() {
        if (getFile != null && binding.descriptionEt.text.isNotEmpty()) {
            val file = getFile as File
            val desc = binding.descriptionEt.text.toString()
            storyViewModel.addStory(file, desc)
        } else {
            showLoading(false)
            Toast.makeText(this, "Please upload both story's photo and description", Toast.LENGTH_LONG).show()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(PICTURE) as File
            val isBackCamera = it.data?.getBooleanExtra(IS_BACK_CAMERA, true) as Boolean
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            val out = FileOutputStream(myFile)
            result.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
            getFile = myFile

            binding.previewIv.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.previewIv.setImageURI(selectedImg)
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if(show) View.VISIBLE else View.GONE
    }
}