package com.tcp.smarttasks.presentation.task_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcp.smarttasks.R
import com.tcp.smarttasks.domain.model.Task
import com.tcp.smarttasks.domain.model.TaskStatus
import com.tcp.smarttasks.presentation.task_detail.ShowTaskStatusImage
import com.tcp.smarttasks.ui.theme.StGray
import com.tcp.smarttasks.utils.DateUtils
import com.tcp.smarttasks.utils.Resource
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun TaskListScreen(
    innerPadding: PaddingValues,
    viewModel: TaskListViewModel = hiltViewModel(),
    onTaskClick: (Task) -> Unit
) {
    val selectedDateTasks = viewModel.tasks.collectAsState().value
    val selectedDate = viewModel.selectedDate.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
    ) {
        TopBarWithPreviousAndNextIcons(
            title = if (DateUtils.isToday(selectedDate)) stringResource(R.string.today) else DateUtils.getDateDisplayFormat(
                selectedDate
            ),
            onPreviousClick = {
                viewModel.getPreviousDayTasks()
            }, onNextClick = {
                viewModel.getNextDayTasks()
            })
        when (selectedDateTasks) {
            is Resource.Loading -> StCircularLoader()

            is Resource.Success -> {
                if (selectedDateTasks.data?.isEmpty() == true)
                    NoTasksForToday()
                else
                    TaskList(tasks = selectedDateTasks.data ?: emptyList(), onTaskClick)
            }

            is Resource.Error -> Text(
                text = "Error: ${selectedDateTasks.message}",
                color = Color.Red
            )
        }
    }
}

@Composable
fun StCircularLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun TopBarWithPreviousAndNextIcons(
    title: String,
    onPreviousClick: (() -> Unit)? = null,
    onNextClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_previous),
            contentDescription = stringResource(R.string.go_to_previous_day_icon), // Provide a description for accessibility
            modifier = Modifier
                .height(40.sdp)
                .width(25.sdp)
                .clickable { onPreviousClick?.invoke() }
                .padding(4.dp)
        )
        Text(
            text = title, color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 15.ssp
        )
        Image(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = stringResource(R.string.go_to_next_day_icon), //a description for accessibility
            modifier = Modifier
                .height(40.sdp)
                .width(25.sdp)
                .clickable { onNextClick?.invoke() }
                .padding(4.dp)
        )
    }
}

@Composable
fun NoTasksForToday() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_screen),
            contentDescription = stringResource(R.string.no_tasks_icon), // a description for accessibility
            modifier = Modifier.fillMaxWidth()
        )
        VerticalSpacer(36.sdp)
        Text(
            stringResource(R.string.no_tasks_for_today), color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.ssp
        )
    }
}


@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.sdp)
            .padding(top = 15.sdp)
    ) {
        items(tasks) { task ->
            TaskItem(task, onTaskClick)
        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(5.sdp)
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(5.sdp), color = Color.White)
            .clickable { onTaskClick.invoke(task) }
            .padding(10.sdp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = task.title ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.ssp,
                modifier = Modifier.weight(1f) // This will make the Text occupy the remaining space
            )
            if (task.status != TaskStatus.UNRESOLVED) {
                ShowTaskStatusImage(
                    status = task.status,
                    size = 14.sdp,
                    drawableId = if (task.status == TaskStatus.RESOLVED)
                        R.drawable.btn_resolved
                    else
                        R.drawable.btn_unresolved
                )
            }
        }
        VerticalSpacer(7.sdp)
        HorizontalDivider(color = StGray)
        VerticalSpacer(10.sdp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.due_date),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.ssp
            )
            Text(
                text = stringResource(R.string.days_left),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.ssp
            )
        }
        VerticalSpacer(7.sdp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (task.dueDate?.isNotEmpty() == true) DateUtils.getDateDisplayFormat(task.dueDate) else "null",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.ssp
            )
            Text(
                text = DateUtils.getDaysLeftUntilDueDate(task.dueDate),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.ssp
            )
        }
    }
}

@Composable
fun VerticalSpacer(height: Dp = 1.sdp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskListScreen() {
    TaskListScreen(innerPadding = PaddingValues(0.dp)) {}
}
