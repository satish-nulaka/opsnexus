const express = require('express');
const helmet = require('helmet');
const cors = require('cors');
const { metricsMiddleware } = require('./middleware/metrics');
const apiRoutes = require('./routes/api');

const app = express();

// Security middleware
app.use(helmet());
app.use(cors());

// Body parsing
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Metrics collection
app.use(metricsMiddleware);

// API Routes
app.use('/api', apiRoutes);

// Root greeting / API discovery
app.get('/', (req, res) => {
  res.status(200).json({
    service: 'TaskFlow REST API',
    description: 'DevOps Demonstration Platform for SIT223/SIT753',
    status: 'online',
    documentation: {
      health: '/api/health',
      metrics: '/api/metrics',
      auth: '/api/auth/login',
      tasks: '/api/tasks'
    }
  });
});

// 404 Handler
app.use((req, res) => {
  res.status(404).json({
    success: false,
    error: `Cannot ${req.method} ${req.originalUrl}`
  });
});

// Global Error Handler
// eslint-disable-next-line no-unused-vars
app.use((err, req, res, next) => {
  console.error('Unhandled Server Error:', err);
  res.status(err.status || 500).json({
    success: false,
    error: err.message || 'Internal Server Error'
  });
});

module.exports = app;
