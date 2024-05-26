package com.example.quizhub.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizhub.R

class ContactActivity : AppCompatActivity() {
    private lateinit var btnCall: Button
    private lateinit var btnMsg: Button
    private lateinit var btnMail: Button
    private lateinit var imgwhatsapp: ImageView
    private lateinit var imgLink: ImageView
    private lateinit var imgX: ImageView
    private lateinit var imgGithub: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imgLink = findViewById(R.id.imgLink)
        imgwhatsapp = findViewById(R.id.imgWhatsapp)
        imgX = findViewById(R.id.imgX)
        imgGithub = findViewById(R.id.imgGithub)
        btnCall = findViewById(R.id.btnCall)
        btnMsg = findViewById(R.id.btnMsg)
        btnMail = findViewById(R.id.btnMail)

        btnCall.setOnClickListener {
            val idial = Intent(Intent.ACTION_DIAL)
            idial.data = Uri.parse("tel: +917048216866")
            startActivity(idial)
            Toast.makeText(this,"Thanks for Contacting Us !!",Toast.LENGTH_SHORT).show()
        }

        btnMsg.setOnClickListener {
            val imsg = Intent(Intent.ACTION_SENDTO)
            imsg.data = Uri.parse("smsto:" + Uri.encode("+917048216866"))
            imsg.putExtra("sms_body", "This App is Awesome!")
            startActivity(imsg)
            Toast.makeText(this,"Thanks for Contacting Us !!",Toast.LENGTH_SHORT).show()
        }

        btnMail.setOnClickListener {
            val imail = Intent(Intent.ACTION_SEND)
            imail.type = "message/rfc822"
            imail.putExtra(Intent.EXTRA_EMAIL, arrayOf("ankursenpai@gmail.com"))
            imail.putExtra(Intent.EXTRA_SUBJECT, "Queries")
            imail.putExtra(Intent.EXTRA_TEXT, "This App is Awesome!")
            startActivity(Intent.createChooser(imail, "Email via"))
            Toast.makeText(this,"Thanks for Contacting Us !!",Toast.LENGTH_SHORT).show()
        }

        imgwhatsapp.setOnClickListener {
            val sNumber = "+917048216866"
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$sNumber")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            Toast.makeText(this,"Thanks for Contacting Us !!",Toast.LENGTH_SHORT).show()
        }

        imgLink.setOnClickListener {
            val appLink = "https://www.linkedin.com/in/ankur-kushwaha-818791248?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"
            val packageName = "com.linkedin.android"
            val webLink = "https://www.linkedin.com/in/ankur-kushwaha-818791248?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"
            openLink(appLink, packageName, webLink)
            Toast.makeText(this,"Thanks for Contacting Us !!",Toast.LENGTH_SHORT).show()
        }

        imgX.setOnClickListener {
            val appLink = "https://x.com/AnkurKushwaha23?t=K6GrpfTsLnfWb4JQ51pPhQ&s=03"
            val packageName = "com.twitter.android."
            val webLink = "https://x.com/AnkurKushwaha23?t=K6GrpfTsLnfWb4JQ51pPhQ&s=03"
            openLink(appLink, packageName, webLink)
            Toast.makeText(this,"Thanks for Contacting Us !!",Toast.LENGTH_SHORT).show()
        }

        imgGithub.setOnClickListener {
            val appLink = "https://github.com/AnkurKushwaha23"
            val packageName = "com.github.mobile."
            val webLink = "https://github.com/AnkurKushwaha23"
            openLink(appLink, packageName, webLink)
            Toast.makeText(this,"Thanks for Contacting Us !!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun openLink(applink: String, packageName: String, webLink: String) {
        try {
            val uri = Uri.parse(applink)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.setPackage(packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (activityNotFoundException: ActivityNotFoundException) {
            val uri = Uri.parse(webLink)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}