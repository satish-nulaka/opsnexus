const request = require('supertest');
const app = require('../../src/app');
const { _resetTasks } = require('../../src/controllers/taskController');

describe('Integration Tests: TaskFlow End-to-End API', () => {
  let authToken;

  beforeEach(() => {
    _resetTasks();
  });

  describe('1. Health & Observability Endpoints', () => {
    test('GET /api/health should return status healthy and runtime specs', async () => {
      const res = await request(app).get('/api/health');
      expect(res.status).toBe(200);
      expect(res.body.status).toBe('healthy');
      expect(res.body.service).toBe('taskflow-api');
      expect(res.body.system).toBeDefined();
      expect(res.body.process).toBeDefined();
    });

    test('GET /api/metrics should return Prometheus metrics output', async () => {
      const res = await request(app).get('/api/metrics');
      expect(res.status).toBe(200);
      expect(res.text).toContain('taskflow_');
    });

    test('GET /api/chaos/error should simulate 500 error for alert validation', async () => {
      const res = await request(app).get('/api/chaos/error?type=500');
      expect(res.status).toBe(500);
      expect(res.body.message).toContain('SIMULATED_INCIDENT');
    });
  });

  describe('2. Authentication Flows', () => {
    test('POST /api/auth/register should create a new user account', async () => {
      const uniqueName = `user_${Date.now()}`;
      const res = await request(app)
        .post('/api/auth/register')
        .send({
          username: uniqueName,
          email: `${uniqueName}@deakin.edu.au`,
          password: 'Password123!'
        });

      expect(res.status).toBe(201);
      expect(res.body.success).toBe(true);
      expect(res.body.data.token).toBeDefined();
    });

    test('POST /api/auth/login should return JWT token on valid credentials', async () => {
      const res = await request(app)
        .post('/api/auth/login')
        .send({
          username: 'devops_lead',
          password: 'DevOpsPass123!'
        });

      expect(res.status).toBe(200);
      expect(res.body.success).toBe(true);
      expect(res.body.data.token).toBeDefined();
      authToken = res.body.data.token;
    });

    test('POST /api/auth/login should reject invalid credentials', async () => {
      const res = await request(app)
        .post('/api/auth/login')
        .send({
          username: 'devops_lead',
          password: 'WrongPassword'
        });

      expect(res.status).toBe(401);
      expect(res.body.success).toBe(false);
    });
  });

  describe('3. CRUD Task Lifecycle with Token Authorization', () => {
    test('GET /api/tasks without auth header returns 401', async () => {
      const res = await request(app).get('/api/tasks');
      expect(res.status).toBe(401);
    });

    test('Full CRUD cycle with valid bearer token', async () => {
      // 1. List tasks
      const listRes = await request(app)
        .get('/api/tasks')
        .set('Authorization', `Bearer ${authToken}`);
      expect(listRes.status).toBe(200);
      expect(Array.isArray(listRes.body.data)).toBe(true);

      // 2. Create task
      const createRes = await request(app)
        .post('/api/tasks')
        .set('Authorization', `Bearer ${authToken}`)
        .send({
          title: 'Implement Jenkins Pipeline',
          description: 'Automate build, test, sonar, security, deploy, release, monitor',
          priority: 'critical'
        });
      expect(createRes.status).toBe(201);
      const createdTaskId = createRes.body.data.id;

      // 3. Read specific task
      const getRes = await request(app)
        .get(`/api/tasks/${createdTaskId}`)
        .set('Authorization', `Bearer ${authToken}`);
      expect(getRes.status).toBe(200);
      expect(getRes.body.data.title).toBe('Implement Jenkins Pipeline');

      // 4. Update task
      const updateRes = await request(app)
        .put(`/api/tasks/${createdTaskId}`)
        .set('Authorization', `Bearer ${authToken}`)
        .send({
          status: 'completed'
        });
      expect(updateRes.status).toBe(200);
      expect(updateRes.body.data.status).toBe('completed');

      // 5. Delete task
      const deleteRes = await request(app)
        .delete(`/api/tasks/${createdTaskId}`)
        .set('Authorization', `Bearer ${authToken}`);
      expect(deleteRes.status).toBe(200);

      // Verify deletion
      const verifyRes = await request(app)
        .get(`/api/tasks/${createdTaskId}`)
        .set('Authorization', `Bearer ${authToken}`);
      expect(verifyRes.status).toBe(404);
    });
  });
});
