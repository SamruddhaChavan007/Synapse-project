package com.example.synapse.feature.home

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.synapse.ui.theme.Aqua_Island
import com.example.synapse.ui.theme.Black
import com.example.synapse.ui.theme.Green_BG
import com.example.synapse.ui.theme.PureWhite
import com.example.synapse.ui.theme.Roboto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val channels = viewModel.channels.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val addChannel = remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PureWhite)
                    .clickable {
                        addChannel.value = true
                    }
            ) {
                Text(
                    text = "Add",
                    modifier = Modifier.padding(16.dp),
                    color = Black
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Green_BG)
        ) {
            LazyColumn {
                item {
                    Text(
                        text = "Channels",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        color = Aqua_Island,
                        fontFamily = Roboto
                    )
                }
                item {
                    TextField(
                        value = "",
                        onValueChange = {},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        },
                        placeholder = { Text(text = "Search") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Green_BG)
                            .clip(RoundedCornerShape(10.dp)),
                        textStyle = TextStyle(
                            color = Black,
                            fontFamily = Roboto,
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = PureWhite,
                            unfocusedContainerColor = PureWhite,
                        )
                    )
                }
                items(channels.value) { channel ->
                    Column {
                        ChannelItem(channel.name) {
                            navController.navigate("chat/${channel.id}")
                        }
//                        Text(
//                            text = channel.name, color = Black,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp)
//                                .clip(RoundedCornerShape(16.dp))
//                                .background(PureWhite)
//                                .clickable {
//                                    navController.navigate("chat/${channel.id}")
//                                }
//                                .padding(16.dp)
//                        )
                    }
                }
            }
        }
    }
    if (addChannel.value) {
        ModalBottomSheet(onDismissRequest = { addChannel.value = false }, sheetState = sheetState) {
            AddChannelDialog {
                viewModel.addChannel(it)
                addChannel.value = false
            }
        }
    }
}

@Composable
fun ChannelItem(channelName: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PureWhite)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Green_BG)
        ) {
            Text(
                text = channelName[0].toString().uppercase(),
                color = Aqua_Island,
                style = TextStyle(fontSize = 35.sp, fontFamily = Roboto),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Text(
            text = channelName,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val channelName = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Channel")
        Spacer(modifier = Modifier.padding(8.dp))
        TextField(
            value = channelName.value, onValueChange = {
                channelName.value = it
            },
            label = { Text(text = "Channel Name") }, singleLine = true
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = { onAddChannel(channelName.value) },
            modifier = Modifier.fillMaxWidth(),
            enabled = channelName.value.isNotEmpty()
        ) {
            Text(text = "Add")
        }
    }
}

@Preview
@Composable
fun PreviewItem() {
    ChannelItem(channelName = "Test Channel", {})
}