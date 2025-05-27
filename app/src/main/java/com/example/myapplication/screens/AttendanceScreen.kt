import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodels.EventViewModel
import com.example.myproject.viewmodel.AttendanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    onBackClick: () -> Unit = {},
    eventId: String,
    viewModel: AttendanceViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf("") }
    val students = viewModel.students
    val attendedStudents by viewModel.attendedStudents.collectAsState()
    val event by viewModel.event.collectAsState()
    val checkStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            students.forEach { this[it.email] = true }
        }
    }
    LaunchedEffect(eventId) {
        viewModel.initialize(eventId) // Call on the instance // Initialize with event ID
    }

    val filteredStudents = students.filter {
        it.email.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Attendance") }, // Use hardcoded title
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            OutlinedButton(
                onClick = {
                    viewModel.students.forEach { student ->
                        viewModel.toggleAttendance(student.email, true)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Mark All As Attended")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event?.date?.takeIf { it.isNotEmpty() } ?: "Date not set",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = event?.time?.takeIf { it.isNotEmpty() } ?: "Time not set",
                        fontSize = 12.sp
                    )
                }
                Text("${event?.room}", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search students...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredStudents) { student ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(student.email)
                        Checkbox(
                            checked = attendedStudents.contains(student.email),
                            onCheckedChange = { isChecked ->
                                viewModel.toggleAttendance(student.email, isChecked)
                            }
                        )
                    }
                }
            }
        }
    }
}