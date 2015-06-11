var request = require('../libs/request');
var codes = require('../app/codes');
var domainUser = require('../domain/user');
var domainGroup = require('../domain/group');
var initGroups = require('../config/data/groups.json');
var shortid = require('shortid');
var Q = require('q');

var fail = request.fail;

exports.requiresGroup = function(req, res, next) {
    var groupId = req.params.groupId;
    if (groupId) {
        var group = domainUser.groupToUser(groupId);
        req.user = group;
        next();
    } else {
        var ret = fail(codes.invalidGroupId);
        res.send(ret);
    }
};

exports.getAll = function(req, res) {
    domainGroup.getAll(request.fromMongo(res));
};

exports.init = function(req, res) {
    domainGroup.getAll(function(err, groups) {
        if (err) {
            res.send(fail(codes.internalError));
        } else if (groups.length > 0){
            res.send(fail(codes.alreadyInitializaed));
        } else {
            addGroups(initGroups, request.fromMongo(res));
        }
    });
};

exports.initClassLeaders = function(req, res) {
    domainUser.getAllClassLeaders(function(err, leaders) {
        if (err) {
            res.send(fail(codes.internalError));
        } else if (leaders.length > 0){
            res.send(fail(codes.alreadyInitializaed));
        } else {
            addClassLeaders(request.fromMongo(res));
        }
    });
};

function addClassLeaders(next) {
    domainGroup.getAll(function(err, groups){
        if (err || groups.length == 0) {
            next(err || true);
        } else {
            addClassLeadersByGroups(groups, next);
        }
    });
}

function addClassLeadersByGroups(groups, next) {
    groups.forEach(function(group){
        var name = group.faculty + " " + group.speciality + "-" + group.course + group.group;
        var userObj = {
            name: name,
            username: name,
            password: shortid.generate(),
            type: domainUser.U_TYPE_CLASS_LEADER,
            groupId: group._id
        };

        console.log(userObj.username + ":" + userObj.password);
        domainUser.save(userObj, function(){});
    });
    next();
}

function createGroup(group) {
    return Q.Promise(function(resolve, reject) {
        domainGroup.save(group, function(err, res){
            if (err) {
                reject(err);
            } else {
                resolve(res);
            }
        });
    });
}

function addGroups(groups, cb) {
    var work = groups.map(createGroup);
    Q.all(work).then(function(res){
        cb(null, res);
    }).fail(function(err){
        cb(err);
    });
}
