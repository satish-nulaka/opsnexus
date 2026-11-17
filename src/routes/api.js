const express = require('express');
const router = express.Router();

const authController = require('../controllers/authController');
const taskController = require('../controllers/taskController');
const systemController = require('../controllers/systemController');
const { verifyToken } = require('../middleware/auth');

// Public system endpoints
router.get('/health', systemController.getHealth);
router.get('/metrics', systemController.getMetrics);
router.get('/chaos/error', systemController.simulateChaos);

// Public auth endpoints
router.post('/auth/register', authController.register);
router.post('/auth/login', authController.login);

// Protected task endpoints
router.get('/tasks', verifyToken, taskController.getAllTasks);
router.get('/tasks/:id', verifyToken, taskController.getTaskById);
router.post('/tasks', verifyToken, taskController.createTask);
router.put('/tasks/:id', verifyToken, taskController.updateTask);
router.delete('/tasks/:id', verifyToken, taskController.deleteTask);

module.exports = router;
