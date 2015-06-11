var codes = require('../app/codes');
var logger = require('./logger');

function createRequest(code, data, message) {
    message = (0 == code) ? "OK" : (message || "");
    return {
        code: code,
        message: message,
        response: data
    };
}

function doneRequest(data) {
    return createRequest(0, data);
}

function failRequest(data) {
    data = data || {};
    return createRequest(data.code || 1, null, data.message);
}

function fromMongo(res) {
    return function(err, data) {
        if (err) {
            logger.error(err);
        }
        var ret = err ? failRequest(codes.internalError) :doneRequest(data);
        res.send(ret);
    }
}

module.exports = {
    done: doneRequest,
    fail: failRequest,
    fromMongo: fromMongo
};