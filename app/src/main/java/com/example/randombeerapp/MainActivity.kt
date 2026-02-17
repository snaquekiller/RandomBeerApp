package com.example.randombeerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randombeerapp.ui.theme.RandomBeerAppTheme
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Link
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import kotlin.random.Random


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomBeerAppTheme {
                BeerBackground {
                    BeerApp(beerList)
                }
            }
        }
    }
}

@Composable
fun BeerBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE082)) // couleur bière dorée
    ) {
        // Dessin des bulles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val bubbleColor = Color.White.copy(alpha = 0.40f)  // Plus d'opacité pour rendre les bulles plus visibles

            // Générer des bulles aléatoires partout sur l'écran
            repeat(100) {
                val x = Random.nextFloat() * size.width
                val y = Random.nextFloat() * size.height
                val radius = Random.nextFloat() * 50f + 10f // Rayon entre 10 et 60 pour plus de variété

                drawCircle(bubbleColor, radius = radius, center = Offset(x, y))
            }
        }

        // Contenu de l'application par-dessus le fond
        content()
    }
}

@Composable
fun BeerTextDisplay(selectedBeer: Beer?) {
    Text(
        text = selectedBeer?.name ?: "Appuie pour une bière !",
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF86720F), // Un doré/orangée rappelant la bière
        style = TextStyle(
            fontFamily = FontFamily.Default, // Tu peux changer cette ligne pour une police personnalisée si tu veux
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp
        ),
        modifier = Modifier
            .fillMaxWidth() // Centre le texte
            .padding(16.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun BeerApp(beerList: List<Beer>) {
    var selectedBeer by remember { mutableStateOf<Beer?>(null) }
    var showInfo by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BeerTextDisplay(selectedBeer)

        Spacer(modifier = Modifier.height(32.dp))

        // Bouton Choisir une bière avec couleur et icône de bière
        Button(
            onClick = {
                selectedBeer = beerList.random()
                showInfo = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))  // Couleur orange pour rappeler la bière
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,  // Icône de refresh (par défaut dans Material Icons)
                contentDescription = "Nouvelle bière",
                modifier = Modifier.size(24.dp)  // Ajuste la taille de l'icône
            )
            Spacer(Modifier.width(8.dp))  // Espacement entre l'icône et le texte
            Text("Nouvelle bière")
        }

        if (selectedBeer != null) {
            Spacer(modifier = Modifier.height(16.dp))

            // Bouton Infos
            Button(
                onClick = { showInfo = !showInfo },
                colors = ButtonDefaults.buttonColors(containerColor =  Color(0xFF487AD7))
            ) {
                Icon(Icons.Filled.Info, contentDescription = "Infos bière")
                Spacer(Modifier.width(8.dp))
                Text(if (showInfo) "Cacher les infos" else "Infos")
            }
        }

        if (showInfo && selectedBeer != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Description :",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = selectedBeer!!.description)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Type :",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = selectedBeer!!.type)

                    Text(
                        text = "Alcool :",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = "${selectedBeer!!.alcohol} %")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { uriHandler.openUri(selectedBeer!!.website) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Couleur verte (exemple)
                        modifier = Modifier.padding(8.dp), // Ajouter un padding autour du bouton
                        shape = RoundedCornerShape(12.dp) // Rendre le bouton avec des bords arrondis
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Link,  // Icône de globe
                            contentDescription = "Visiter le site",
                            modifier = Modifier.padding(end = 8.dp)  // Ajouter de l'espace entre l'icône et le texte
                        )
                        Text(
                            text = "Visiter le site",
                            style = MaterialTheme.typography.bodyLarge,  // Utiliser un style de texte plus grand
                            color = Color.White // Mettre le texte en blanc
                        )
                    }
                }
            }
        }
    }
}
