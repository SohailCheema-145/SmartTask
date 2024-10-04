package com.tcp.smarttasks.presentation.task_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcp.smarttasks.R
import com.tcp.smarttasks.domain.model.TaskStatus
import com.tcp.smarttasks.presentation.task_list.StCircularLoader
import com.tcp.smarttasks.presentation.task_list.VerticalSpacer
import com.tcp.smarttasks.ui.theme.StBrown
import com.tcp.smarttasks.ui.theme.StGray
import com.tcp.smarttasks.ui.theme.StGreen
import com.tcp.smarttasks.ui.theme.StOrange
import com.tcp.smarttasks.ui.theme.StRed
import com.tcp.smarttasks.utils.DateUtils
import kotlinx.coroutines.launch
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun TaskDetailScreen(
    innerPadding: PaddingValues, taskId: String, onBack: () -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getTaskById(taskId)
    }
    //to show dialog
    var showDialogThatAsksUserForAComment by remember { mutableStateOf(false) }
    //to save user comment
    var userComment by remember { mutableStateOf("") }
    //to save the action to perform like resolve / can't resolve
    var isResolveCommentAction by remember { mutableStateOf(true) }
    //whether user want to add a comment or noe
    var isCommentRequired by remember { mutableStateOf(false) }

    val task = viewModel.selectedTask.collectAsState().value

    // Add vertical scroll state
    val scrollState = rememberScrollState()
    // Track TextField height changes
    var textFieldHeight by remember { mutableIntStateOf(0) }
    val keyboardController =
        LocalSoftwareKeyboardController.current // Keyboard controller for visibility handling
    val coroutineScope = rememberCoroutineScope()

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarWithTitleAndBackIcon(
                title = stringResource(R.string.task_detail),
                onBackClick = onBack
            )
            Column(
                modifier = Modifier
                    .imePadding()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VerticalSpacer(12.sdp)
                if (task != null) {
                    val taskTextsColor = if (task.status == TaskStatus.RESOLVED) StGreen else StRed
                    val taskStatusColor =
                        if (task.status == TaskStatus.RESOLVED) StGreen else if (task.status == TaskStatus.CANT_RESOLVE) StRed else StOrange
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.sdp)
                    ) {
                        val drawablePainter = painterResource(id = R.drawable.task_details)

                        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                            // Create references for the components
                            val (backgroundImageRed, columnRef) = createRefs()

                            // Background Image
                            Image(
                                painter = drawablePainter,
                                contentDescription = null,
                                modifier = Modifier
                                    .constrainAs(backgroundImageRed) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        width = Dimension.fillToConstraints
                                        height = Dimension.fillToConstraints
                                    },
                                contentScale = ContentScale.FillBounds // Crop or scale as necessary
                            )

                            val margin = 10.sdp
                            val marginTop = 30.sdp
                            Column(
                                modifier = Modifier
                                    .constrainAs(columnRef) {
                                        top.linkTo(parent.top, margin = marginTop)
                                        bottom.linkTo(parent.bottom, margin = 0.dp)
                                        start.linkTo(parent.start, margin = margin)
                                        end.linkTo(parent.end, margin = margin)
                                    }
                                    .padding(10.sdp)
                            ) {
                                Text(
                                    text = task.title ?: "N/A",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontSize = 20.ssp,
                                    color = taskTextsColor
                                )
                                VerticalSpacer(10.sdp)
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
                                        text = if (task.dueDate?.isNotEmpty() == true) DateUtils.getDateDisplayFormat(
                                            task.dueDate
                                        ) else "null",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 15.ssp,
                                        color = taskTextsColor
                                    )
                                    Text(
                                        text = DateUtils.getDaysLeftUntilDueDate(task.dueDate),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 15.ssp,
                                        color = taskTextsColor
                                    )
                                }
                                VerticalSpacer(10.sdp)
                                HorizontalDivider(color = StGray)
                                VerticalSpacer(12.sdp)
                                TaskDescriptionText(task.description)
                                VerticalSpacer(12.sdp)
                                HorizontalDivider(color = StGray)
                                VerticalSpacer(10.sdp)
                                Text(
                                    text = when (task.status) {
                                        TaskStatus.RESOLVED -> stringResource(R.string.resolved)
                                        TaskStatus.CANT_RESOLVE -> stringResource(R.string.can_t_resolve)
                                        else -> stringResource(R.string.unresolved)
                                    },
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 15.ssp,
                                    color = taskStatusColor
                                )
                            }
                        }
                    }
                    if (task.status == TaskStatus.UNRESOLVED) {
                        //show action buttons only if unresolved
                        TaskActionButtons(onResolveClick = {
                            if (isCommentRequired) {
                                viewModel.resolveTask(userComment)
                            } else {
                                showDialogThatAsksUserForAComment = true
                                isResolveCommentAction = true
                            }
                        }, onCantResolveClick = {
                            if (isCommentRequired) {
                                viewModel.cantResolveTask(userComment)
                            } else {
                                showDialogThatAsksUserForAComment = true
                                isResolveCommentAction = false
                            }
                        })
                        VerticalSpacer(20.sdp)
                        if (isCommentRequired) {
                            //to add a comment
                            TextField(
                                value = userComment,
                                onValueChange = { userComment = it },
                                label = {
                                    Text(
                                        stringResource(R.string.add_a_comment_here),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = StBrown
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.sdp)
                                    .onGloballyPositioned {
                                        if (keyboardController != null) {
                                            coroutineScope.launch {
                                                scrollState.animateScrollTo(scrollState.maxValue)
                                            }
                                        }
                                    }
                                    .onSizeChanged { size ->
                                        // Scroll to ensure the TextField stays visible when it grows
                                        if (size.height > textFieldHeight) {
                                            coroutineScope.launch {
                                                scrollState.animateScrollTo(scrollState.maxValue)
                                            }
                                        }
                                        textFieldHeight = size.height
                                    },
                                shape = RoundedCornerShape(5.sdp),
                                colors = TextFieldDefaults.colors()
                                    .copy(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        cursorColor = Color.Black
                                    )
                            )
                        }
                    } else {
                        VerticalSpacer(20.sdp)
                        ShowTaskStatusImage(
                            status = task.status,
                            size = if (task.comment.isNotEmpty()) 30.sdp else 60.sdp,
                            drawableId = if (task.status == TaskStatus.RESOLVED)
                                R.drawable.sign_resolved
                            else
                                R.drawable.unresolved_sign
                        )
                        if (task.comment.isNotEmpty()) {
                            VerticalSpacer(10.sdp)
                            ShowAddedCommentView(comment = task.comment, status = task.status)
                        }
                    }
                } else {
                    //show loader
                    StCircularLoader()
                }
            }

        }
        // Dialog for user comment
        if (showDialogThatAsksUserForAComment) {
            AlertDialog(
                onDismissRequest = { showDialogThatAsksUserForAComment = false },
                text = {
                    Text(
                        stringResource(R.string.do_you_want_to_leave_a_comment),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = StBrown,
                            fontSize = 14.ssp
                        ),
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialogThatAsksUserForAComment = false
                        isCommentRequired = true
                    }) {
                        Text(
                            text = stringResource(R.string.yes),
                            color = StGreen,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.ssp
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialogThatAsksUserForAComment = false
                        isCommentRequired = false
                        if (isResolveCommentAction)
                            viewModel.resolveTask(userComment)
                        else
                            viewModel.cantResolveTask(userComment)
                    }) {
                        Text(
                            text = stringResource(R.string.no),
                            color = StRed,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.ssp
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ShowAddedCommentView(comment: String, status: TaskStatus) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.sdp)
            .background(shape = RoundedCornerShape(5.sdp), color = Color.White)
            .padding(10.sdp)
    ) {
        Text(
            text = stringResource(R.string.comment),
            style = MaterialTheme.typography.labelSmall,
            color = StBrown,
            fontSize = 10.ssp
        )
        VerticalSpacer(5.sdp)
        Text(
            text = comment,
            style = MaterialTheme.typography.bodyMedium,
            color = if (status == TaskStatus.RESOLVED) StGreen else StRed
        )
    }
}

@Composable
fun ShowTaskStatusImage(status: TaskStatus, size: Dp = 60.sdp, drawableId: Int) {
    Image(
        painter = painterResource(
            id = drawableId
        ),
        contentDescription = stringResource(
            if (status == TaskStatus.RESOLVED)
                R.string.resolved
            else
                R.string.can_t_resolve
        ),
        modifier = Modifier
            .size(size)
    )
}

@Composable
fun TaskActionButtons(
    onResolveClick: (() -> Unit)? = null,
    onCantResolveClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.sdp),
        horizontalArrangement = Arrangement.spacedBy(10.sdp) // Optional spacing between buttons
    ) {
        Button(
            onClick = { onResolveClick?.invoke() },
            modifier = Modifier
                .weight(1f)
                .height(38.sdp), // Set height as required
            colors = ButtonDefaults.buttonColors(
                containerColor = StGreen, // Custom background color
                contentColor = Color.White // Text color
            ),
            shape = RoundedCornerShape(5.sdp) // Rounded corners with radius
        ) {
            Text(
                text = stringResource(R.string.resolve),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontSize = 14.ssp
            )
        }

        Button(
            onClick = { onCantResolveClick?.invoke() },
            modifier = Modifier
                .weight(1f)
                .height(38.sdp), // Set height as required
            colors = ButtonDefaults.buttonColors(
                containerColor = StRed, // Custom background color
                contentColor = Color.White // Text color
            ),
            shape = RoundedCornerShape(5.sdp) // Rounded corners with radius
        ) {
            Text(
                text = stringResource(R.string.can_t_resolve),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontSize = 14.ssp
            )
        }
    }
}


@Composable
fun TaskDescriptionText(description: String?) {
    // Create an AnnotatedString with styled links
    val annotatedString = buildAnnotatedString {
        description?.let {
            // Split the description into words or use your own logic to extract links
            val words = it.split(" ")
            words.forEach { word ->
                if (isLink(word)) { // Check if the word is a link
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("$word ")
                    }
                } else {
                    append("$word ") // Append non-link text normally
                }
            }
        }
    }

    // Display the Text with styled links
    Text(
        text = annotatedString,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyMedium
    )
}

// Example function to check if a word is a URL
fun isLink(word: String): Boolean {
    // Logic to check if the word is a URL (you can customize this)
    return word.startsWith("http://") || word.startsWith("https://") ||
            word.endsWith(".com") || word.endsWith(".org") ||
            word.endsWith(".net") // Add more extensions if needed
}


@Composable
fun TopBarWithTitleAndBackIcon(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Image(
            painter = painterResource(id = R.drawable.ic_previous),
            contentDescription = stringResource(R.string.go_to_previous_day_icon), // Provide a description for accessibility
            modifier = Modifier
                .height(40.sdp)
                .width(25.sdp)
                .padding(4.dp)
                .clickable { onBackClick() }
        )
        Box(
            modifier = Modifier
                .height(40.sdp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 15.ssp
            )
        }
    }
}