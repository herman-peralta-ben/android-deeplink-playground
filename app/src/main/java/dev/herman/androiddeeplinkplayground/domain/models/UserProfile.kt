package dev.herman.androiddeeplinkplayground.domain.models

/** Model for <testing://www.testing.com/abc?name=herman&hobbies=coding,drums,games&age=36> urls*/
data class UserProfile(
    val name: String,
    val hobbies: List<String>,
    val age: Int,
)
