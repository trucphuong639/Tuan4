package com.example.tuan4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnBoardingApp()
        }
    }
}

@Composable
fun OnBoardingApp() {
    val navController = rememberNavController()
    val viewModel: TaskViewModel = viewModel() // 🔥 Khởi tạo ViewModel

    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 1
            OnBoardingScreen(navController, index)
        }
        composable("home") { MainScreen(navController, viewModel) } // 🔥 Truyền ViewModel

        composable("taskDetail/{title}/{description}/{status}/{time}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "Unknown"
            val description = backStackEntry.arguments?.getString("description") ?: "No description"
            val status = backStackEntry.arguments?.getString("status") ?: "Unknown"
            val time = backStackEntry.arguments?.getString("time") ?: "Unknown"

            TaskDetailScreen(
                task = Task(title, description, status, time),
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("onboarding/1")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "UTH SmartTasks",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BFFF)
            )
        }
    }
}

@Composable
fun OnBoardingScreen(navController: NavHostController, index: Int) {
    val pages = listOf(
        PageData("Easy Time Management", "With management based on priority...", R.drawable.img_1),
        PageData("Increase Work Effectiveness", "Time management and priority...", R.drawable.img_2),
        PageData("Reminder Notification", "Provides reminders...", R.drawable.img_3)
    )

    val isFirstPage = index == 1
    val isLastPage = index == pages.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.padding(start = 16.dp)) {
                repeat(pages.size) { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == index - 1) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (i == index - 1) Color.Blue else Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            Text(
                text = "Skip",
                fontSize = 14.sp,
                color = Color.Blue,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { navController.navigate("home") }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = pages[index - 1].imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = pages[index - 1].title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = pages[index - 1].description,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.85f)
        )

        Spacer(modifier = Modifier.height(400.dp))

        if (isFirstPage) {
            Button(
                onClick = { navController.navigate("onboarding/2") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF))
            ) {
                Text(text = "Next", color = Color.White, fontSize = 16.sp)
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_4),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { navController.navigate("onboarding/${index - 1}") }
                )

                Button(
                    onClick = {
                        if (index < pages.size) {
                            navController.navigate("onboarding/${index + 1}")
                        } else {
                            navController.navigate("home")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(55.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF))
                ) {
                    Text(text = if (index < pages.size) "Next" else "Get Started", color = Color.White, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MainScreen(navController: NavController, viewModel: TaskViewModel) {
    val taskList by remember { mutableStateOf(viewModel.taskList) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(modifier = Modifier.offset(y = -25.dp))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "SmartTasks",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = Color(0xFF00BFFF)
                        )
                        Text(
                            text = "A simple and efficient to-do app",
                            fontSize = 14.sp,
                            color = Color(0xFF00BFFF)
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.img_5),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(28.dp)
                )
            }

            // Nội dung chính
            if (taskList.isEmpty()) {
                ErrorScreen(navController)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(taskList) { task ->
                        TaskCard(task, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("taskDetail/${task.title}/${task.description}/${task.status}/${task.time}")
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E90FF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = task.description, fontSize = 14.sp, color = Color.White)
        }
    }
}

@Composable
fun ErrorScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hình ảnh trong khung xám
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_13),
                contentDescription = "No Tasks",
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "No Tasks Yet!",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Stay productive—add something to do",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(350.dp))

        BottomNavigationBar(Modifier.offset(y = -10.dp))
    }
}

@Composable
fun TaskDetailScreen(navController: NavController, task: Task, viewModel: TaskViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // Header giữ nguyên
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.img_14),
                contentDescription = "Back",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Task Detail",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BFFF)
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.img_15),
                contentDescription = "Delete",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { /* Xử lý xóa task */ }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = task.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Text(
            text = task.description,
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Giữ nguyên bố cục UI chi tiết task
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD0A7A7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TaskDetailItem(icon = R.drawable.img_16, label = "Category", value = "Work")
                TaskDetailItem(icon = R.drawable.img_17, label = "Status", value = task.status)
                TaskDetailItem(icon = R.drawable.img_18, label = "Priority", value = "High")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subtasks Section
        Text(text = "Subtasks", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))

        val subtasks = listOf(
            "This task is related to Fitness. It needs to be completed",
            "This task is related to Fitness. It needs to be completed",
            "This task is related to Fitness. It needs to be completed"
        )

        subtasks.forEach { subtask ->
            SubtaskItem(text = subtask)
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Attachments Section
        Text(text = "Attachments", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Mở file đính kèm */ },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDCDCDC)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.img_19),
                    contentDescription = "Attachment",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "document_1_0.pdf", fontSize = 16.sp)
            }
        }
    }
}


// Component hiển thị thông tin chi tiết (Category, Status, Priority)
@Composable
fun TaskDetailItem(icon: Int, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(8.dp)) // Tạo khoảng cách giữa icon và chữ
        Column {
            Text(text = label, fontSize = 14.sp, color = Color.Black)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

// Component hiển thị từng subtask
@Composable
fun SubtaskItem(text: String) {
    var isChecked by remember { mutableStateOf(false) } // Lưu trạng thái checkbox

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCDCDC)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it } // Cập nhật trạng thái khi nhấn
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 16.sp)
        }
    }
}

// Data class chứa thông tin từng trang OnBoarding
data class Task(val title: String, val description: String, val status: String, val time: String)

data class PageData(val title: String, val description: String, val imageRes: Int)

fun getTaskList(): List<Task> {
    return listOf(
        Task("Complete Android Project", "Finish the UI, integrate API, and write documentation", "In Progress", "14:00 2500-03-26"),
        Task("Doctor Appointment 2", "This task is related to Work. It needs to be completed", "Pending", "14:00 2500-03-26"),
        Task("Meeting", "This task is related to Fitness. It needs to be completed", "Pending", "14:00 2500-03-26")
    )
}

@Composable
fun BottomNavigationBar(modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = -25.dp), // 🔥 Đẩy toàn bộ thanh navigation lên 10dp
        contentAlignment = Alignment.BottomCenter
    ) {
        // Thanh nền điều hướng
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Home
            Icon(
                painter = painterResource(id = R.drawable.img_8),
                contentDescription = "Home",
                tint = Color(0xFF1E90FF),
                modifier = Modifier.size(30.dp)
            )

            // Icon Calendar
            Icon(
                painter = painterResource(id = R.drawable.img_9),
                contentDescription = "Calendar",
                tint = Color.Gray,
                modifier = Modifier.size(30.dp)
            )

            // Placeholder khoảng trống cho nút "+"
            Spacer(modifier = Modifier.size(56.dp))

            // Icon Document
            Icon(
                painter = painterResource(id = R.drawable.img_10),
                contentDescription = "Document",
                tint = Color.Gray,
                modifier = Modifier.size(30.dp)
            )

            // Icon Settings
            Icon(
                painter = painterResource(id = R.drawable.img_11),
                contentDescription = "Settings",
                tint = Color.Gray,
                modifier = Modifier.size(30.dp)
            )
        }

        // Nút cộng nằm trên thanh điều hướng
        FloatingActionButton(
            onClick = { /* TODO: Chuyển đến màn hình thêm task */ },
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.Center)
                .offset(y = -22.dp),
            containerColor = Color(0xFF1E90FF),
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(id = R.drawable.img_12),
                contentDescription = "Add Task",
                tint = Color.White,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}
