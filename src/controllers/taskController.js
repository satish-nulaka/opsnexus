const { taskOperationsTotal } = require('../middleware/metrics');

let tasks = [
  {
    id: 'task-1',
    title: 'Configure Jenkins CI/CD Agent',
    description: 'Set up Docker-in-Docker agent with automated pipelines',
    status: 'completed',
    priority: 'high',
    assignedTo: 'user-1',
    createdAt: new Date().toISOString()
  },
  {
    id: 'task-2',
    title: 'Integrate SonarQube Quality Gate',
    description: 'Set quality threshold gates for code smells and maintainability',
    status: 'in-progress',
    priority: 'high',
    assignedTo: 'user-1',
    createdAt: new Date().toISOString()
  }
];

function getAllTasks(req, res) {
  const { status, priority } = req.query;
  let results = [...tasks];

  if (status) {
    results = results.filter(t => t.status.toLowerCase() === status.toLowerCase());
  }
  if (priority) {
    results = results.filter(t => t.priority.toLowerCase() === priority.toLowerCase());
  }

  taskOperationsTotal.labels('list', 'success').inc();
  return res.status(200).json({
    success: true,
    count: results.length,
    data: results
  });
}

function getTaskById(req, res) {
  const task = tasks.find(t => t.id === req.params.id);
  if (!task) {
    taskOperationsTotal.labels('get', 'not_found').inc();
    return res.status(404).json({
      success: false,
      error: `Task with id '${req.params.id}' not found`
    });
  }

  taskOperationsTotal.labels('get', 'success').inc();
  return res.status(200).json({
    success: true,
    data: task
  });
}

function createTask(req, res) {
  const { title, description, priority = 'medium' } = req.body;

  if (!title || !description) {
    taskOperationsTotal.labels('create', 'validation_error').inc();
    return res.status(400).json({
      success: false,
      error: 'Task title and description are required'
    });
  }

  const newTask = {
    id: `task-${Date.now()}`,
    title,
    description,
    status: 'pending',
    priority,
    assignedTo: req.user.id,
    createdAt: new Date().toISOString()
  };

  tasks.push(newTask);
  taskOperationsTotal.labels('create', 'success').inc();

  return res.status(201).json({
    success: true,
    message: 'Task created successfully',
    data: newTask
  });
}

function updateTask(req, res) {
  const taskIndex = tasks.findIndex(t => t.id === req.params.id);
  if (taskIndex === -1) {
    taskOperationsTotal.labels('update', 'not_found').inc();
    return res.status(404).json({
      success: false,
      error: `Task with id '${req.params.id}' not found`
    });
  }

  const { title, description, status, priority } = req.body;
  tasks[taskIndex] = {
    ...tasks[taskIndex],
    ...(title && { title }),
    ...(description && { description }),
    ...(status && { status }),
    ...(priority && { priority }),
    updatedAt: new Date().toISOString()
  };

  taskOperationsTotal.labels('update', 'success').inc();
  return res.status(200).json({
    success: true,
    message: 'Task updated successfully',
    data: tasks[taskIndex]
  });
}

function deleteTask(req, res) {
  const taskIndex = tasks.findIndex(t => t.id === req.params.id);
  if (taskIndex === -1) {
    taskOperationsTotal.labels('delete', 'not_found').inc();
    return res.status(404).json({
      success: false,
      error: `Task with id '${req.params.id}' not found`
    });
  }

  const removed = tasks.splice(taskIndex, 1)[0];
  taskOperationsTotal.labels('delete', 'success').inc();

  return res.status(200).json({
    success: true,
    message: 'Task deleted successfully',
    data: removed
  });
}

// Reset tasks helper for testing
function _resetTasks() {
  tasks = [
    {
      id: 'task-1',
      title: 'Configure Jenkins CI/CD Agent',
      description: 'Set up Docker-in-Docker agent with automated pipelines',
      status: 'completed',
      priority: 'high',
      assignedTo: 'user-1',
      createdAt: new Date().toISOString()
    }
  ];
}

module.exports = {
  getAllTasks,
  getTaskById,
  createTask,
  updateTask,
  deleteTask,
  _resetTasks
};
