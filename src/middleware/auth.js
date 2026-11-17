const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET || 'devops-secret-sit753-2026';

function verifyToken(req, res, next) {
  const authHeader = req.headers['authorization'];
  if (!authHeader) {
    return res.status(401).json({
      success: false,
      error: 'Authentication failed: No authorization token provided'
    });
  }

  const parts = authHeader.split(' ');
  if (parts.length !== 2 || parts[0] !== 'Bearer') {
    return res.status(401).json({
      success: false,
      error: 'Authentication failed: Invalid authorization format. Expected Bearer <token>'
    });
  }

  const token = parts[1];
  try {
    const decoded = jwt.verify(token, JWT_SECRET);
    req.user = decoded;
    next();
  } catch (err) {
    return res.status(403).json({
      success: false,
      error: 'Authentication failed: Invalid or expired token',
      details: err.message
    });
  }
}

module.exports = {
  verifyToken,
  JWT_SECRET
};
