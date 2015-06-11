var config = require('../config.js');
var mongoose = require('mongoose');
var logger = require('./logger');

mongoose.connect(config.mongodb.url);

mongoose.connection.on('connected', function () {
    logger.info('Mongoose default connection open to %s', config.mongodb.url);
});

// If the connection throws an error
mongoose.connection.on('error', function (err) {
    logger.error('Mongoose default connection error: %s');
});

// When the connection is disconnected
mongoose.connection.on('disconnected', function () {
    logger.info('Mongoose default connection disconnected');
});

// When the connection is open
mongoose.connection.on('open', function () {
    logger.info('Mongoose default connection is open');
});

// If the Node process ends, close the Mongoose connection
process.on('SIGINT', function () {
    mongoose.connection.close(function () {
        logger.debug('Mongoose default connection disconnected through app termination');
        process.exit(0);
    });
});

module.exports = mongoose;