const { register, taskOperationsTotal } = require('../../src/middleware/metrics');

describe('Unit Tests: Metrics & Observability', () => {
  test('register contains core application metrics', async () => {
    const metricsString = await register.metrics();
    expect(metricsString).toContain('taskflow_http_requests_total');
    expect(metricsString).toContain('taskflow_task_operations_total');
    expect(metricsString).toContain('taskflow_http_request_duration_seconds');
  });

  test('taskOperationsTotal counter increments accurately', async () => {
    taskOperationsTotal.labels('test_op', 'success').inc();
    const metricsString = await register.metrics();
    expect(metricsString).toContain('taskflow_task_operations_total{operation="test_op",status="success"} 1');
  });
});
