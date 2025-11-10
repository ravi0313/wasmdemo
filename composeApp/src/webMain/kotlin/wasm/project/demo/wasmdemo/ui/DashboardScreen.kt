package wasm.project.demo.wasmdemo.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import wasm.project.demo.wasmdemo.api.DashboardApi
import wasm.project.demo.wasmdemo.data.*
import wasm.project.demo.wasmdemo.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    var menuExpanded by remember { mutableStateOf(false) }
    var dashboardData by remember { mutableStateOf<DashboardData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val api = remember { DashboardApi() }

    // Load data on first composition
    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            errorMessage = null
            api.loadDashboardData().fold(
                onSuccess = { data ->
                    dashboardData = data
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.message ?: "Failed to load data"
                    isLoading = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Dashboard",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        dashboardData?.user?.name?.let { name ->
                            Text(
                                "Welcome, $name",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White
                ),
                actions = {
                    // Notification Badge
                    Box {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                        dashboardData?.stats?.notifications?.let { count ->
                            if (count > 0) {
                                Badge(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp),
                                    containerColor = ErrorRed
                                ) {
                                    Text(count.toString(), color = Color.White)
                                }
                            }
                        }
                    }

                    // Menu Button
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Profile") },
                            onClick = { menuExpanded = false },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = PrimaryBlue) }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { menuExpanded = false },
                            leadingIcon = { Icon(Icons.Default.Settings, null, tint = PrimaryBlue) }
                        )
                        DropdownMenuItem(
                            text = { Text("Help & Support") },
                            onClick = { menuExpanded = false },
                            leadingIcon = { Icon(Icons.Default.Info, null, tint = PrimaryBlue) }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Logout", color = ErrorRed) },
                            onClick = {
                                menuExpanded = false
                                onLogout()
                            },
                            leadingIcon = { Icon(Icons.Default.ExitToApp, null, tint = ErrorRed) }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
        ) {
            when {
                isLoading -> {
                    LoadingScreen()
                }
                errorMessage != null -> {
                    ErrorScreen(errorMessage!!) {
                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            api.loadDashboardData().fold(
                                onSuccess = { data ->
                                    dashboardData = data
                                    isLoading = false
                                },
                                onFailure = { error ->
                                    errorMessage = error.message ?: "Failed to load data"
                                    isLoading = false
                                }
                            )
                        }
                    }
                }
                dashboardData != null -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Modern Tab Row
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = Color.White,
                            contentColor = PrimaryBlue,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = PrimaryBlue,
                                    height = 3.dp
                                )
                            }
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = { Text("Overview", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                                icon = { Icon(Icons.Default.Home, contentDescription = null) }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = { Text("Analytics", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                                icon = { Icon(Icons.Default.DateRange, contentDescription = null) }
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                text = { Text("Reports", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) }
                            )
                            Tab(
                                selected = selectedTab == 3,
                                onClick = { selectedTab = 3 },
                                text = { Text("Tasks", fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
                                icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) }
                            )
                        }

                        // Tab Content with Animation
                        AnimatedContent(
                            targetState = selectedTab,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) togetherWith
                                        fadeOut(animationSpec = tween(300))
                            },
                            label = "tab_content"
                        ) { tab ->
                            when (tab) {
                                0 -> OverviewTab(dashboardData!!)
                                1 -> AnalyticsTab(dashboardData!!)
                                2 -> ReportsTab(dashboardData!!)
                                3 -> TasksTab(dashboardData!!)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = PrimaryBlue,
                strokeWidth = 4.dp
            )
            Text(
                "Loading Dashboard...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(0.8f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = ErrorRed,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    "Oops! Something went wrong",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
fun OverviewTab(data: DashboardData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // User Welcome Card with Gradient
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(PrimaryBlue, SecondaryTeal)
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Column {
                        Text(
                            "Hello, ${data.user.name}!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            data.user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Stats Cards Grid
        Text(
            "Quick Stats",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModernStatCard(
                modifier = Modifier.weight(1f),
                title = "Users",
                value = data.stats.totalUsers.toString(),
                icon = Icons.Default.Person,
                backgroundColor = CardPrimary,
                iconColor = PrimaryBlue
            )
            ModernStatCard(
                modifier = Modifier.weight(1f),
                title = "Revenue",
                value = "$${data.stats.revenue.toInt()}",
                icon = Icons.Default.ShoppingCart,
                backgroundColor = CardSecondary,
                iconColor = SecondaryTeal
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModernStatCard(
                modifier = Modifier.weight(1f),
                title = "Projects",
                value = data.stats.projects.toString(),
                icon = Icons.Default.Build,
                backgroundColor = CardTertiary,
                iconColor = TertiaryPurple
            )
            ModernStatCard(
                modifier = Modifier.weight(1f),
                title = "Alerts",
                value = data.stats.notifications.toString(),
                icon = Icons.Default.Notifications,
                backgroundColor = CardWarning,
                iconColor = WarningOrange
            )
        }

        // Recent Activity Section
        Text(
            "Recent Activity",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                data.recentActivities.take(5).forEach { activity ->
                    ModernActivityItem(activity)
                }
            }
        }
    }
}

@Composable
fun AnalyticsTab(data: DashboardData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Analytics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // User Growth
        ModernMetricCard(
            metric = data.analytics.userGrowth,
            icon = Icons.Default.Person,
            backgroundColor = CardPrimary,
            accentColor = PrimaryBlue
        )

        // Revenue Trends
        ModernMetricCard(
            metric = data.analytics.revenueTrends,
            icon = Icons.Default.ShoppingCart,
            backgroundColor = CardSecondary,
            accentColor = SecondaryTeal
        )

        // Customer Satisfaction
        ModernMetricCard(
            metric = data.analytics.customerSatisfaction,
            icon = Icons.Default.Star,
            backgroundColor = CardWarning,
            accentColor = WarningOrange
        )
    }
}

@Composable
fun ReportsTab(data: DashboardData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Reports",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        data.reports.forEach { report ->
            ModernReportCard(report)
        }
    }
}

@Composable
fun TasksTab(data: DashboardData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "My Tasks",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        data.tasks.forEach { task ->
            ModernTaskItem(task)
        }
    }
}

// Modern Components

@Composable
fun ModernStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color = PrimaryBlue
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ModernActivityItem(activity: Activity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconColor = when (activity.type) {
            ActivityType.USER_REGISTRATION -> PrimaryBlue
            ActivityType.PROJECT_COMPLETED -> SuccessGreen
            ActivityType.PAYMENT_RECEIVED -> SecondaryTeal
            ActivityType.DEPLOYMENT -> TertiaryPurple
            ActivityType.OTHER -> MaterialTheme.colorScheme.onSurfaceVariant
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                activity.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                activity.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ModernMetricCard(
    metric: Metric,
    icon: ImageVector,
    backgroundColor: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        metric.label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        metric.trend,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                metric.value,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            LinearProgressIndicator(
                progress = { metric.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = accentColor,
                trackColor = backgroundColor
            )
        }
    }
}

@Composable
fun ModernReportCard(report: Report) {
    val statusColor = when (report.status) {
        ReportStatus.COMPLETED -> SuccessGreen
        ReportStatus.IN_PROGRESS -> WarningOrange
        ReportStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val statusText = when (report.status) {
        ReportStatus.COMPLETED -> "Completed"
        ReportStatus.IN_PROGRESS -> "In Progress"
        ReportStatus.PENDING -> "Pending"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        report.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        report.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = statusColor.copy(alpha = 0.15f)
            ) {
                Text(
                    statusText,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ModernTaskItem(task: Task) {
    var completed by remember { mutableStateOf(task.isCompleted) }

    val priorityColor = when (task.priority) {
        Priority.HIGH -> ErrorRed
        Priority.MEDIUM -> WarningOrange
        Priority.LOW -> InfoBlue
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = completed,
                onCheckedChange = { completed = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = SuccessGreen,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (completed) MaterialTheme.colorScheme.onSurfaceVariant else Color.Black
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = priorityColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        task.priority.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = priorityColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Icon(
                if (completed) Icons.Default.CheckCircle else Icons.Default.Star,
                contentDescription = null,
                tint = if (completed) SuccessGreen else priorityColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
