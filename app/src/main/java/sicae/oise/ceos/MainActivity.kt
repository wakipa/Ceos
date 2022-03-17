package sicae.oise.ceos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** Appelée lorsque l'utilisateur tape le bouton "Accéder au formulaire" */
    open fun btnFormulaire(view: View) {
        val intent = Intent(this, Formulaire::class.java)
        startActivity(intent)
    }
}