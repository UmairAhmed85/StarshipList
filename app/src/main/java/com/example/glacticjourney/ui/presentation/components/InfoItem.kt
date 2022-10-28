package com.example.glacticjourney.ui.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.glacticjourney.data.dto.Result

@Composable
fun WordInfoItem(
    wordInfo: Result,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Ship name: ${wordInfo.name}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = "Model: ${wordInfo.model}", fontSize = 22.sp, fontWeight = FontWeight.Light)
        Text(text = "Passengers: ${wordInfo.passengers}", fontSize = 20.sp, fontWeight = FontWeight.Light)
        Text(text = "Consumables: ${wordInfo.consumables}", fontSize = 20.sp, fontWeight = FontWeight.Light)
        Text(text = "Cargo Capacity: ${wordInfo.cargo_capacity}", fontSize = 20.sp, fontWeight = FontWeight.Light)
        repeat(wordInfo.pilots.size) {
            Text(text = "Pilot${it + 1}: ${wordInfo.pilots[it]}", fontSize = 20.sp, fontWeight = FontWeight.Light)
        }


        /*Spacer(modifier = modifier.height(16.dp))
       wordInfo.meanings.forEach { meaning ->
       Text(text = meaning.partOfSpeech, fontWeight = FontWeight.Bold, color = Color.Black)
           meaning.definitions.forEachIndexed { index, definition ->
               Text(text = "${index + 1}. ${definition.definition}")
               Spacer(modifier = modifier.height(8.dp))
               definition.example?.let {example->
                   Text(text = "Example: $example")
               }
           }
           Spacer(modifier = Modifier.height(16.dp))
       }*/
    }
}