const os = require('os');
const { register } = require('../middleware/metrics');

const startTime = Date.now();

function getHealth(req, res) {
  const memoryUsage = process.memoryUsage();
  return res.status(200).json({
    status: 'healthy',
    service: 'taskflow-api',
    version: process.env.APP_VERSION || '1.0.0',
    environment: process.env.NODE_ENV || 'development',
    uptimeSeconds: Math.floor((Date.now() - startTime) / 1000),
    timestamp: new Date().toISOString(),
    system: {
      hostname: os.hostname(),
      platform: os.platform(),
      arch: os.arch(),
      loadAverage: os.loadavg(),
      freeMemoryMB: Math.round(os.freemem() / (1024 * 1024)),
      totalMemoryMB: Math.round(os.totalmem() / (1024 * 1024))
    },
    process: {
      pid: process.pid,
      nodeVersion: process.version,
      heapUsedMB: Math.round(memoryUsage.heapUsed / (1024 * 1024)),
      heapTotalMB: Math.round(memoryUsage.heapTotal / (1024 * 1024))
    }
  });
}

async function getMetrics(req, res) {
  try {
    res.set('Content-Type', register.contentType);
    res.end(await register.metrics());
  } catch (err) {
    res.status(500).end(err);
  }
}

// Chaos testing / incident simulation for Monitoring & Alerting stage
function simulateChaos(req, res) {
  const { type = '500' } = req.query;

  if (type === '500') {
    return res.status(500).json({
      status: 'error',
      message: 'SIMULATED_INCIDENT: Unhandled internal error triggered for alerting test',
      timestamp: new Date().toISOString()
    });
  }

  if (type === 'latency') {
    return setTimeout(() => {
      res.status(200).json({
        status: 'delayed',
        message: 'SIMULATED_INCIDENT: Simulated latency spike (3000ms delay)',
        delayMs: 3000
      });
    }, 3000);
  }

  return res.status(400).json({
    status: 'bad_request',
    error: 'Supported chaos types: 500, latency'
  });
}

module.exports = {
  getHealth,
  getMetrics,
  simulateChaos
};
