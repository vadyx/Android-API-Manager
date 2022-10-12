package hr.algebra.android_api_manager

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import hr.algebra.android_api_manager.databinding.FragmentSoundRecorderBinding
import hr.algebra.android_api_manager.framework.startAnimation
import java.io.IOException


class SoundRecorderFragment: Fragment() {

    private val startTV: TextView? = null
    private val stopTV: TextView? = null
    private val playTV: TextView? = null
    private val stopplayTV: TextView? = null
    private val statusTV: TextView? = null

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mFileName: String? = null

    val REQUEST_AUDIO_PERMISSION_CODE = 1


    private lateinit var binding: FragmentSoundRecorderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSoundRecorderBinding.inflate(inflater, container, false)

        var startTV = binding.btnRecord
        var stopTV = binding.btnStop
        var playTV = binding.btnPlay
        var stopplayTV = binding.btnStopPlay

        startTV.setOnClickListener{ startRecording() }
        stopTV.setOnClickListener { pauseRecording() }
        playTV.setOnClickListener { playAudio() }
        stopplayTV.setOnClickListener { pausePlaying() }

        return binding.root
    }

    private fun pausePlaying() {
        binding.idTVstatus.clearAnimation()
        binding.idTVstatus.text = "Stop playing"
        mPlayer?.release();
        mPlayer = null;
    }

    private fun playAudio() {
        mPlayer = MediaPlayer()
        try {
            binding.idTVstatus.text = "Playing..."
            binding.idTVstatus.startAnimation(R.anim.blink)

            mPlayer?.setDataSource(mFileName)
            mPlayer?.prepare()
            mPlayer?.start()
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed")
        }

    }

    private fun pauseRecording() {
        try {
            mRecorder?.stop()
            mRecorder?.reset()
            mRecorder?.release()
            mRecorder = null
            binding.idTVstatus.clearAnimation()
            binding.idTVstatus.text = "Stop"
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed")
        }



    }

    private fun startRecording() {
        if (checkPermissions()) {

            mFileName = context?.getExternalFilesDir(null)!!.absolutePath + "/m2developer.mp3"

            mRecorder = MediaRecorder()

            mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder?.setOutputFile(mFileName)
            mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                mRecorder?.prepare()
                mRecorder?.start()
                binding.idTVstatus.text = "Recording"
                binding.idTVstatus.startAnimation(R.anim.blink)
                Log.e("TAG", "Prošlo")
            } catch (e: IOException) {
                Log.e("TAG", "prepare() failed or start failed ")
            }
        } else {
            requestPermissions()
        }
    }

    fun checkPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            WRITE_EXTERNAL_STORAGE
        )
        val result1 = ContextCompat.checkSelfPermission(
            requireContext(),
            RECORD_AUDIO
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE),
            REQUEST_AUDIO_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.size > 0) {
                val permissionToRecord = grantResults[0] === PackageManager.PERMISSION_GRANTED
                val permissionToStore = grantResults[1] === PackageManager.PERMISSION_GRANTED
                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(
                        requireContext(),
                        "Permission Granted",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permission Denied",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}