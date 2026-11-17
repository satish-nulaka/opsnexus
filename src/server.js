require('dotenv').config();
const app = require('./app');

const PORT = process.env.PORT || 3000;
const HOST = '0.0.0.0';

const server = app.listen(PORT, HOST, () => {
  console.log(`[TaskFlow API] Service started successfully`);
  console.log(`[TaskFlow API] Listening on http://${HOST}:${PORT}`);
  console.log(`[TaskFlow API] Environment: ${process.env.NODE_ENV || 'development'}`);
  console.log(`[TaskFlow API] Health probe: http://${HOST}:${PORT}/api/health`);
  console.log(`[TaskFlow API] Metrics endpoint: http://${HOST}:${PORT}/api/metrics`);
});

// Graceful shutdown handling
process.on('SIGTERM', () => {
  console.log('[TaskFlow API] SIGTERM received. Initiating graceful shutdown...');
  server.close(() => {
    console.log('[TaskFlow API] Process terminated gracefully.');
    process.exit(0);
  });
});

process.on('SIGINT', () => {
  console.log('[TaskFlow API] SIGINT received. Shutting down...');
  server.close(() => {
    console.log('[TaskFlow API] Server closed.');
    process.exit(0);
  });
});

module.exports = server;
