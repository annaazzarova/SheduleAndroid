var request = require('../libs/request');

var done = request.done;

exports.ping = function(req, res) {
    var time = new Date().getTime();
    var ret = done(time);
    res.send(ret);
};