package com.example.personinfodemo

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personinfodemo.db.PersonDao
import com.example.personinfodemo.model.Person

class DialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(android.view.Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name") ?: ""
        val age = intent.getIntExtra("age", 0)
        val height = intent.getFloatExtra("height", 0f)
        val person = Person(id, name, age, height)
        val dao = PersonDao(this)

        setContentView(
            androidx.compose.ui.platform.ComposeView(this).apply {
                setContent {
                    MaterialTheme {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(24.dp),
                                elevation = CardDefaults.cardElevation(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF2F9FF)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(28.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "选择操作",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2A3B4C),
                                        modifier = Modifier.padding(bottom = 24.dp)
                                    )

                                    Button(
                                        onClick = {
                                            val success = dao.insertPerson(person)
                                            Toast.makeText(this@DialogActivity, if (success) "插入成功" else "插入失败（ID重复）", Toast.LENGTH_SHORT).show()
                                            finish()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .padding(bottom = 10.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFBE6BE1)
                                        )
                                    ) {
                                        Text("信息插入", fontSize = 16.sp)
                                    }

                                    Button(
                                        onClick = {
                                            val success = dao.deleteById(id)
                                            Toast.makeText(this@DialogActivity, if (success) "删除成功" else "删除失败", Toast.LENGTH_SHORT).show()
                                            finish()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .padding(bottom = 10.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFBE6BE1)
                                        )
                                    ) {
                                        Text("信息删除", fontSize = 16.sp)
                                    }

                                    Button(
                                        onClick = {
                                            val success = dao.updatePerson(person)
                                            Toast.makeText(this@DialogActivity, if (success) "更新成功" else "更新失败", Toast.LENGTH_SHORT).show()
                                            finish()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFBE6BE1)
                                        )
                                    ) {
                                        Text("信息更新", fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}