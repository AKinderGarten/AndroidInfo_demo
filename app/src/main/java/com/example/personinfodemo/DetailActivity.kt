package com.example.personinfodemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.example.personinfodemo.db.PersonDao

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(androidx.compose.ui.platform.ComposeView(this).apply {
            setContent {
                androidx.compose.material3.MaterialTheme {
                    val personId = intent.getIntExtra("personId", -1)
                    val dao = PersonDao(this@DetailActivity)
                    val person = dao.getPersonById(personId)

                    androidx.compose.foundation.layout.Column(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .padding(40.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                    ) {
                        androidx.compose.material3.Text(
                            text = if (personId == -1 || person == null) {
                                "未找到信息"
                            } else {
                                "ID：${person.id}\n姓名：${person.name}\n年龄：${person.age}\n身高：${person.height} 米"
                            },
                            fontSize = 22.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        })
    }
}