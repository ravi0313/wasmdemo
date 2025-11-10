package wasm.project.demo.wasmdemo.data

data class DashboardData(
    val user: User,
    val stats: Stats,
    val recentActivities: List<Activity>,
    val analytics: Analytics,
    val reports: List<Report>,
    val tasks: List<Task>
)

data class User(
    val name: String,
    val email: String,
    val avatarUrl: String? = null
)

data class Stats(
    val totalUsers: Int,
    val revenue: Double,
    val projects: Int,
    val notifications: Int
)

data class Activity(
    val id: String,
    val title: String,
    val timestamp: String,
    val type: ActivityType
)

enum class ActivityType {
    USER_REGISTRATION,
    PROJECT_COMPLETED,
    PAYMENT_RECEIVED,
    DEPLOYMENT,
    OTHER
}

data class Analytics(
    val userGrowth: Metric,
    val revenueTrends: Metric,
    val customerSatisfaction: Metric
)

data class Metric(
    val label: String,
    val value: String,
    val progress: Float,
    val trend: String
)

data class Report(
    val id: String,
    val title: String,
    val date: String,
    val status: ReportStatus
)

enum class ReportStatus {
    COMPLETED,
    IN_PROGRESS,
    PENDING
}

data class Task(
    val id: String,
    val title: String,
    val isCompleted: Boolean,
    val priority: Priority
)

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}

