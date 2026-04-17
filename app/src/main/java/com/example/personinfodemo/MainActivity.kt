package com.example.personinfodemo

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.os.Bundle
import com.example.personinfodemo.db.PersonDao
import com.example.personinfodemo.model.Person

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PersonInfoScreen()
            }
        }
    }

    @Composable
    fun PersonInfoScreen() {
        var idText by remember { mutableStateOf("") }
        var nameText by remember { mutableStateOf("") }
        var ageText by remember { mutableStateOf("") }
        var heightText by remember { mutableStateOf("") }

        val context = this
        val dao = PersonDao(context)
        var personList by remember { mutableStateOf(emptyList<Person>()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(top = 50.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "个人信息管理",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2A3B4C),
                modifier = Modifier.padding(bottom = 30.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = idText,
                        onValueChange = { idText = it },
                        label = { Text("ID（数字）") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFF4285F4),
                            focusedLabelColor = Color(0xFF4285F4)
                        )
                    )

                    TextField(
                        value = nameText,
                        onValueChange = { nameText = it },
                        label = { Text("姓名") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFF4285F4),
                            focusedLabelColor = Color(0xFF4285F4)
                        )
                    )

                    TextField(
                        value = ageText,
                        onValueChange = { ageText = it },
                        label = { Text("年龄") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFF4285F4),
                            focusedLabelColor = Color(0xFF4285F4)
                        )
                    )

                    TextField(
                        value = heightText,
                        onValueChange = { heightText = it },
                        label = { Text("身高（米）") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFF4285F4),
                            focusedLabelColor = Color(0xFF4285F4)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val id = idText.toIntOrNull()
                    val age = ageText.toIntOrNull()
                    val h = heightText.toFloatOrNull()
                    if (id == null || nameText.isBlank() || age == null || h == null) {
                        Toast.makeText(context, "请填写完整信息", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val intent = Intent(context, DialogActivity::class.java).apply {
                        putExtra("id", id)
                        putExtra("name", nameText)
                        putExtra("age", age)
                        putExtra("height", h)
                    }
                    startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBE6BE1))
            ) {
                Text("信息处理", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    personList = dao.getAllPersons()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBE6BE1))
            ) {
                Text("信息列出", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "信息列表",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFBE6BE1),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(personList) { person ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        onClick = {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("personId", person.id)
                            startActivity(intent)
                        }
                    ) {
                        Text(
                            text = "ID: ${person.id}  姓名: ${person.name}",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}