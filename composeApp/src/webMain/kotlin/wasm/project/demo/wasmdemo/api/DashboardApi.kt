package wasm.project.demo.wasmdemo.api

import kotlinx.coroutines.delay
import wasm.project.demo.wasmdemo.data.*

class DashboardApi {

    suspend fun loadDashboardData(): Result<DashboardData> {
        return try {
            // Simulate API call delay
            delay(1000)

            // Mock data - in real app, this would be an HTTP call
            val data = DashboardData(
                user = User(
                    name = "John Doe",
                    email = "john.doe@example.com"
                ),
                stats = Stats(
                    totalUsers = 1234,
                    revenue = 12345.67,
                    projects = 42,
                    notifications = 8
                ),
                recentActivities = listOf(
                    Activity("1", "New user registration completed", "2 hours ago", ActivityType.USER_REGISTRATION),
                    Activity("2", "Project Alpha successfully completed", "5 hours ago", ActivityType.PROJECT_COMPLETED),
                    Activity("3", "Payment of \$5,000 received", "1 day ago", ActivityType.PAYMENT_RECEIVED),
                    Activity("4", "New feature deployed to production", "2 days ago", ActivityType.DEPLOYMENT),
                    Activity("5", "System backup completed", "3 days ago", ActivityType.OTHER)
                ),
                analytics = Analytics(
                    userGrowth = Metric(
                        label = "User Growth",
                        value = "+12.5%",
                        progress = 0.75f,
                        trend = "This month"
                    ),
                    revenueTrends = Metric(
                        label = "Revenue Trends",
                        value = "\$45,678",
                        progress = 0.85f,
                        trend = "Total this quarter"
                    ),
                    customerSatisfaction = Metric(
                        label = "Customer Satisfaction",
                        value = "4.8/5.0",
                        progress = 0.96f,
                        trend = "Average rating"
                    )
                ),
                reports = listOf(
                    Report("1", "Monthly Sales Report", "November 2025", ReportStatus.COMPLETED),
                    Report("2", "User Activity Report", "October 2025", ReportStatus.COMPLETED),
                    Report("3", "Financial Summary", "Q3 2025", ReportStatus.IN_PROGRESS),
                    Report("4", "Performance Metrics", "September 2025", ReportStatus.COMPLETED),
                    Report("5", "Customer Analytics", "August 2025", ReportStatus.PENDING)
                ),
                tasks = listOf(
                    Task("1", "Review project proposal", false, Priority.HIGH),
                    Task("2", "Update user documentation", true, Priority.MEDIUM),
                    Task("3", "Prepare quarterly presentation", false, Priority.HIGH),
                    Task("4", "Code review for new feature", false, Priority.MEDIUM),
                    Task("5", "Team meeting at 3 PM", true, Priority.LOW),
                    Task("6", "Deploy to production", false, Priority.HIGH),
                    Task("7", "Update dependencies", false, Priority.LOW)
                )
            )

            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

