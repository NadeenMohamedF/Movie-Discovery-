package com.example.mymovieapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Settings_intent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_intent)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // ١. Notification Switch
        val switchNotifications = findViewById<android.widget.Switch>(R.id.switch_notifications)
        switchNotifications.setOnCheckedChangeListener { buttonView, isChecked ->
            val message = if (isChecked) " Notfication are on 🔔" else " Notfication are off 🔕"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // ٢. Clear Favorites

        findViewById<android.view.View>(R.id.btnClearFavorites).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Favourites")
                .setMessage("Are you sure you want to delete all your favourites?")
                .setPositiveButton("Yes") { _, _ ->
                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                    if (userId == null) {
                        Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val favRef = FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("favorites")

                    favRef.get()
                        .addOnSuccessListener { result ->
                            // امسحي كل الوثائق Doc 1 by 1
                            for (doc in result.documents) {
                                doc.reference.delete()
                            }

                            Toast.makeText(this, "All favourites cleared 🎉", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to delete!", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("No", null)
                .show()
        }


        /* findViewById<android.view.View>(R.id.btnClearFavorites).setOnClickListener {
             AlertDialog.Builder(this)
                 .setTitle("Delete Favourites")
                 .setMessage("Are You Sure For Deleting all your favourites?")
                 .setPositiveButton("Yes") { dialog, which ->
                     Toast.makeText(this, " Deleted Successfully ✅ ", Toast.LENGTH_SHORT).show()
                 }
                 .setNegativeButton("No", null)
                 .show()
         }*/

        // ٣. About Us
        findViewById<android.view.View>(R.id.tvAbout).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("About MovieVibe \uD83D\uDCAB")
                .setMessage("️Welcome to MovieVibe \uD83C\uDFAC - Your Ultimate Movie Companion!\n" +
                        "       \uD83C\uDF1F Features:-\n"+
                        "     • Discover trending movies\n"+
                        "     • Save your favorite films\n" +
                        "     • Watch trailers and previews\n"  +
                        "     • Get personalized recommendations\n"  +
                        "     • Read reviews and ratings\n" +
                        "     • 🔔 Get release notifications\n"+
                        "     • Made for movie enthusiasts")
                .setPositiveButton("Ok", null)
                .show()
        }

        // ٤. Logout
        findViewById<android.view.View>(R.id.btnLogout).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Do You Need To LogOut ?")
                .setPositiveButton("Yes") { _, _ ->

                    FirebaseAuth.getInstance().signOut()

                    Toast.makeText(this, "logout successfully ✅", Toast.LENGTH_SHORT).show()

                    // بعد الـ logout ترجّعي المستخدم لصفحة الـ Login/Welcome
                    val intent = Intent(this, MainActivity::class.java)  // مكان Navigation Compose
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    finish() // اقفلي صفحة settings
                }
                .setNegativeButton("No", null)
                .show()
        }

        /* findViewById<android.view.View>(R.id.btnLogout).setOnClickListener {
              AlertDialog.Builder(this)
                  .setTitle("Log out")
                  .setMessage("Do You Need To LogOut ?")
                  .setPositiveButton("Yes") { dialog, which ->
                      Toast.makeText(this, "logout successfully ✅", Toast.LENGTH_SHORT).show()
                      finish() // تغلقي الصفحة
                  }
                  .setNegativeButton("No", null)
                  .show()
          }*/
    }
}