var request = require('../libs/request');
var codes = require('../app/codes');
var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;
var domainUser = require('../domain/user');

var done = request.done;
var fail = request.fail;

passport.use(new LocalStrategy(userAuth));

passport.serializeUser(function(user, done) {
    done(null, user);
});

passport.deserializeUser(function(user, done) {
    done(null, user);
});

function userAuth(username, password, done) {
    username = (username || "").toLowerCase();
    domainUser.findByUsername(username, function (err, user) {
        if (err) {
            done(codes.internalError);
        } else if (user) {
            if (domainUser.comparePassword(password, user.password)) {
                done(null, user.toClient());
            } else {
                done(null, null);
            }
        } else {
            done(null, null);
        }
    });
}


exports.me = function(req, res) {
    var cRet;
    if (req.user) {
        var ret = req.user;
        cRet = done(ret);
    } else {
        cRet = fail(codes.userNotFound);
    }
    res.send(cRet);
};

exports.register = function(req, res) {
    var user = {
        username: req.body.username || req.body.name,
        name: req.body.name,
        password: req.body.password,
        type: req.body.type,
        groupId: req.body.groupId
    };

    domainUser.save(user, function(err, user){
        var ret;
        if (err) {
            ret = fail(codes.internalError)
        } else {
            ret = done(user.toClient());
        }
        res.send(ret);
    });
};

exports.login = function(req, res) {
    var getUser = function(err, user){
        var ret = err ? fail(err): done(user);
        res.send(ret);
    };

    passport.authenticate('local', {
        failureRedirect: '/failure_auth',
        failureFlash: true
    })(req, res, getUser);
};

exports.getAllTeachers = function(req, res) {
    domainUser.getAllTeachers(request.fromMongo(res));
};
