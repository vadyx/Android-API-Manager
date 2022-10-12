package hr.algebra.android_api_manager

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.algebra.android_api_manager.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButton()
    }

    private fun initButton() {
        binding.button.setOnClickListener {
            if(formValid()){
                sendEmail()
            } else {
                showModal()
            }
        }
    }

    private fun showModal() {
        AlertDialog.Builder(context).apply {
            setTitle(R.string.emptyInput)
            setMessage(context.getString(R.string.emptyAlert))
            setIcon(R.drawable.about)
            setCancelable(true)
            setPositiveButton(context.getString(R.string.close), null)
            show()
        }
    }

    private fun sendEmail() {
        val emailsend = R.string.email
        val emailsubject = binding.editText2.getText().toString()
        val emailbody = binding.editText3.getText().toString()

        val intent = Intent(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailsend))
        intent.putExtra(Intent.EXTRA_SUBJECT, emailsubject)
        intent.putExtra(Intent.EXTRA_TEXT, emailbody)

        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Choose an Email client :"))

    }

    private fun formValid(): Boolean {
        var ok = true
        arrayOf(binding.editText2, binding.editText3).forEach {
            if (it.text.isNullOrEmpty()) {
                ok = false
            }
        }
        return ok
    }
}