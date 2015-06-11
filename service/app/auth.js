var request = require('../libs/request');
var codes = require('../app/codes');
var passport = require('passport');
var domainUser = require('../domain/user');

var fail = request.fail;
var done = request.done;

exports.requiresLogin = function(req, res, next) {
    if (req.isAuthenticated()) {
        next()
    } else {
        return res.send(fail(codes.notAllowed));
    }
};

exports.login = function(req, res, next) {
    passport.authenticate('local')(req, res, next);
};

exports.requiresTeacher = function(req, res, next) {
    var currentUser = req.user;
    if (currentUser && currentUser.type == domainUser.U_TYPE_TEACHER) {
        return next();
    } else {
        return res.send(fail(codes.notAllowed));
    }
};

exports.logout = function(req, res) {
    req.session.destroy(function (err) {
        var ret = err ? fail(codes.notAllowed) : done();
        res.send(ret);
    });
};

exports.requiresAdmin = function(req, res, next) {
    var currentUser = req.user;
    if (currentUser && currentUser.type == domainUser.U_TYPE_ADMIN) {
        return next();
    } else {
        return res.send(fail(codes.notAllowed));
    }
};

exports.requiresClassLeader = function(req, res, next) {
    var currentUser = req.user;
    if (currentUser && currentUser.type != domainUser.U_TYPE_TEACHER && currentUser.groupId) {
        return next();
    } else {
        return res.send(fail(codes.notAllowed));
    }
};