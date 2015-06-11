var config = require('config-node')({
    dir: 'config',
    ext: null,
    env: process.env.NODE_ENV || 'development'
});

module.exports = config;