const client = require('prom-client');

// Initialize standard Prometheus default metrics (CPU, Memory, Event Loop, GC)
const register = new client.Registry();
client.collectDefaultMetrics({ register, prefix: 'taskflow_' });

// Custom application metrics
const httpRequestDurationMicroseconds = new client.Histogram({
  name: 'taskflow_http_request_duration_seconds',
  help: 'Duration of HTTP requests in seconds',
  labelNames: ['method', 'route', 'status_code'],
  buckets: [0.005, 0.01, 0.025, 0.05, 0.1, 0.25, 0.5, 1, 2.5, 5]
});

const httpRequestsTotal = new client.Counter({
  name: 'taskflow_http_requests_total',
  help: 'Total number of HTTP requests processed',
  labelNames: ['method', 'route', 'status_code']
});

const taskOperationsTotal = new client.Counter({
  name: 'taskflow_task_operations_total',
  help: 'Total number of task operations performed',
  labelNames: ['operation', 'status']
});

register.registerMetric(httpRequestDurationMicroseconds);
register.registerMetric(httpRequestsTotal);
register.registerMetric(taskOperationsTotal);

// Middleware function to intercept requests and record metrics
function metricsMiddleware(req, res, next) {
  const start = process.hrtime();
  res.on('finish', () => {
    const elapsed = process.hrtime(start);
    const durationInSeconds = elapsed[0] + elapsed[1] / 1e9;
    const route = req.route ? req.baseUrl + req.route.path : req.path;
    
    httpRequestDurationMicroseconds
      .labels(req.method, route, res.statusCode)
      .observe(durationInSeconds);
      
    httpRequestsTotal
      .labels(req.method, route, res.statusCode)
      .inc();
  });
  next();
}

module.exports = {
  register,
  metricsMiddleware,
  taskOperationsTotal
};
