package com.example.pruebas.ui.pantallas

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pruebas.datos.listaIdiomas

@Composable
fun Idiomas(
    lista: List<String> = listaIdiomas
)
{
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(lista){ idioma ->
            Card(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(16.dp)) {
                Text(idioma, textAlign = TextAlign.Center)
            }
        }
    }
}