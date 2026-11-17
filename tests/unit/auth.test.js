const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const { verifyToken, JWT_SECRET } = require('../../src/middleware/auth');

describe('Unit Tests: Authentication & Security', () => {
  test('bcrypt should hash and compare passwords correctly', () => {
    const rawPassword = 'SecretDevOpsPassword2026!';
    const hash = bcrypt.hashSync(rawPassword, 8);
    expect(hash).not.toBe(rawPassword);
    expect(bcrypt.compareSync(rawPassword, hash)).toBe(true);
    expect(bcrypt.compareSync('WrongPassword', hash)).toBe(false);
  });

  test('jwt should sign and verify valid payloads', () => {
    const payload = { id: 'test-123', username: 'devops_tester', role: 'admin' };
    const token = jwt.sign(payload, JWT_SECRET, { expiresIn: '1h' });
    const decoded = jwt.verify(token, JWT_SECRET);
    expect(decoded.id).toBe('test-123');
    expect(decoded.username).toBe('devops_tester');
  });

  test('verifyToken middleware rejects request with missing header', () => {
    const req = { headers: {} };
    const res = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn()
    };
    const next = jest.fn();

    verifyToken(req, res, next);
    expect(res.status).toHaveBeenCalledWith(401);
    expect(res.json).toHaveBeenCalledWith(expect.objectContaining({
      success: false,
      error: expect.stringContaining('No authorization token')
    }));
    expect(next).not.toHaveBeenCalled();
  });

  test('verifyToken middleware rejects invalid token string', () => {
    const req = { headers: { authorization: 'Bearer invalid.token.value' } };
    const res = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn()
    };
    const next = jest.fn();

    verifyToken(req, res, next);
    expect(res.status).toHaveBeenCalledWith(403);
    expect(next).not.toHaveBeenCalled();
  });

  test('verifyToken middleware successfully attaches user for valid token', () => {
    const payload = { id: 'u1', username: 'alex', role: 'developer' };
    const token = jwt.sign(payload, JWT_SECRET);
    const req = { headers: { authorization: `Bearer ${token}` } };
    const res = {};
    const next = jest.fn();

    verifyToken(req, res, next);
    expect(req.user.username).toBe('alex');
    expect(next).toHaveBeenCalled();
  });
});
