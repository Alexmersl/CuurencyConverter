package com.example.myfirstapponkotlin.pages

class TaskPlannerPage(device: UiDevice) : BasePage(device) {
    fun addNewTask(taskText: String) {
        setText("com.example.myfirstapponkotlin:id/new_task_input", taskText)
        clickElement("com.example.myfirstapponkotlin:id/add_task_button")
    }

    fun verifyTaskAdded(taskText: String) {
        waitForText(taskText)
    }

    fun markTaskAsCompleted() {
        clickElement("com.example.myfirstapponkotlin:id/task_checkbox")
    }

    fun deleteTask() {
        clickElement("com.example.myfirstapponkotlin:id/delete_task_button")
    }

    fun verifyTaskDeleted(taskText: String) {
        assert(!isTextDisplayed(taskText))
    }
} 