<html>
  <head>
    <title>Todo App</title>
  </head>
  <body>
    <h2>Hello World!</h2>
    <form action="register" method="post">
      <!-- Task Description -->
      <label for="description">Task Description:</label>
      <input
        type="text"
        id="description"
        name="description"
        placeholder="Enter task description"
        required
      />

      <!-- Task Status -->
      <label for="status">Task Status:</label>
      <select id="status" name="status" required>
        <option value="Pending">Pending</option>
        <option value="Completed">Completed</option>
      </select>

      <!-- Due Date -->
      <label for="due_date">Due Date:</label>
      <input type="date" id="due_date" name="due_date" required />

      <!-- Submit Button -->
      <button type="submit">Register Task</button>
    </form>

    <br />
    <a href="displayTasks">View All Tasks</a>
    <form action="deleteTask" method="post">
      <!-- Task ID -->
      <label for="task_id">Task ID:</label>
      <input
        type="number"
        id="task_id"
        name="id"
        placeholder="Enter task ID to delete"
        required
      />

      <!-- Submit Button -->
      <button type="submit">Delete Task</button>
    </form>

    <form action="searchTasks" method="get">
      <!-- Search Description -->
      <label for="search_description">Search Description:</label>
      <input
        type="text"
        id="search_description"
        name="description"
        placeholder="Enter description to search"
        required
      />

      <!-- Submit Button -->
      <button type="submit">Search Tasks</button>
    </form>
  </body>
</html>
