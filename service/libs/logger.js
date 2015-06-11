var log4js = require('log4js');

log4js.loadAppender('console');
var logger = log4js.getDefaultLogger();

module.exports = logger;
