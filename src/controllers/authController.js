const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const { JWT_SECRET } = require('../middleware/auth');

// In-memory user store for demonstration & testing
const users = [
  {
    id: 'user-1',
    username: 'devops_lead',
    email: 'devops@example.com',
    passwordHash: bcrypt.hashSync('DevOpsPass123!', 8),
    role: 'admin'
  }
];

function register(req, res) {
  const { username, email, password } = req.body;

  if (!username || !email || !password) {
    return res.status(400).json({
      success: false,
      error: 'Missing required fields: username, email, and password are required'
    });
  }

  const existing = users.find(u => u.email === email || u.username === username);
  if (existing) {
    return res.status(409).json({
      success: false,
      error: 'User with this email or username already exists'
    });
  }

  const newUser = {
    id: `user-${users.length + 1}`,
    username,
    email,
    passwordHash: bcrypt.hashSync(password, 8),
    role: 'developer'
  };

  users.push(newUser);

  const token = jwt.sign(
    { id: newUser.id, username: newUser.username, role: newUser.role },
    JWT_SECRET,
    { expiresIn: '2h' }
  );

  return res.status(201).json({
    success: true,
    message: 'User registered successfully',
    data: {
      user: { id: newUser.id, username: newUser.username, email: newUser.email, role: newUser.role },
      token
    }
  });
}

function login(req, res) {
  const { username, password } = req.body;

  if (!username || !password) {
    return res.status(400).json({
      success: false,
      error: 'Missing username or password'
    });
  }

  const user = users.find(u => u.username === username || u.email === username);
  if (!user || !bcrypt.compareSync(password, user.passwordHash)) {
    return res.status(401).json({
      success: false,
      error: 'Invalid credentials'
    });
  }

  const token = jwt.sign(
    { id: user.id, username: user.username, role: user.role },
    JWT_SECRET,
    { expiresIn: '2h' }
  );

  return res.status(200).json({
    success: true,
    message: 'Authentication successful',
    data: {
      user: { id: user.id, username: user.username, email: user.email, role: user.role },
      token
    }
  });
}

module.exports = {
  register,
  login,
  users
};
