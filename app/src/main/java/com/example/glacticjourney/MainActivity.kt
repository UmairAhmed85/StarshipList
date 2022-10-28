package com.example.glacticjourney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.glacticjourney.ui.presentation.GlacticViewModel
import com.example.glacticjourney.ui.presentation.components.ProgressComponent
import com.example.glacticjourney.ui.presentation.components.WordInfoItem
import com.example.glacticjourney.ui.theme.GlacticJourneyTheme

/*
Intergalactic Airways
Problem
The transportation company, Intergalactic Airways, has contracted us to build a new transportation management system for them which will allow them to coordinate transportation of many people between galaxies. The first feature they need is a way to get a list of Starships that can carry the number of passengers needing to be transported. They also need to know which Pilots can fly those Starships. https://swapi.dev/documentation hosts a public API where the data about Starships(https://swapi.dev/api/starships) and Pilots can be retrieved.
Solution
Create an iOS Application using Swift or a framework of your choice that allows the user to enter a number to search for the number of passengers as input, which, once submitted, then presents a list of starships that can support the requested number of passengers. Display the pilots of the starship, along with a few other relevant details (ie. cargo capacity, class, model, etc.). Do not include starships that cannot carry enough passengers.
For example, If the user enters “5” as the requested number of passengers, the page should show:
Millenium Falcon
Pilots: Chewbacca, Han Solo[, etc.]
[a couple other interesting data points]
Slave 1
Pilots: Boba Fett[, etc.]
[a couple other interesting data points]
 [The rest of the starships that support 5 or more passengers]

Guidelines
You may use whatever tools/libraries you’d like. Implement best practices and coding standards as much as possible. Adding styles are optional but encouraged to make the UI presentable.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlacticJourneyTheme {
                val viewModel: GlacticViewModel = hiltViewModel()
                val state = viewModel.glacticState.value
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState
                ) {
                    Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            if (viewModel.showLoading.value)
                                ProgressComponent()
                            TextField(
                                value = viewModel.passengerQuery.value,
                                onValueChange = { viewModel.getStarshipDetails(it) },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Enter number of passengers")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyColumn(

                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(state.wordInfoItem.size, itemContent = { index ->
                                    val wordInfo = state.wordInfoItem[index]
                                    if (index > 0) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                    WordInfoItem(wordInfo = wordInfo)
                                    if (index < state.wordInfoItem.size - 1) {
                                        Divider()
                                    }
                                })
                            }
                        }
                    }
                }
            }

        }
    }

}