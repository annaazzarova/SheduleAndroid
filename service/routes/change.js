var request = require('../libs/request');
var codes = require('../app/codes');
var logger = require('../libs/logger');
var domainChange = require('../domain/change');
var domainLesson = require('../domain/lesson');
var domainUser = require('../domain/user');
var moment = require('moment');
var checker = require('../libs/checker');

var done = request.done;
var fail = request.fail;

exports.getChanges = function(req, res) {
    var user = req.user;
    var dateFrom = req.query.dateFrom && moment(req.query.dateFrom);

    var params = {
        durationFrom: (dateFrom && dateFrom.toDate())
    };

    if (user.type == domainUser.U_TYPE_TEACHER) {
        params.teacherId = user._id;
    } else {
        params.groupId = user.groupId;
    }

    if (!params.groupId && !params.teacherId) {
        return res.send(fail(codes.badRequest));
    }

    domainChange.getChanges(params, request.fromMongo(res), "-__v");
};

exports.deleteChange = function(req, res) {
    var changeId = req.params.changeId;
    domainChange.deleteById(changeId, function(err, data){
        var ret = err ? fail(codes.internalError) : done();
        res.send(ret);
    });
};

exports.createChange = function(req, res) {
    var lessonId = req.params.lessonId;

    if (lessonId) {
        domainLesson.getById(lessonId, function(err, lesson){
            if (err) {
                res.send(fail(codes.internalError));
            } else if (lesson == null) {
                res.send(fail(codes.notFound));
            } else {
                changeLesson(lesson, req, res);
            }
        });
    } else {
        createNewLesson(req, res);
    }
};

exports.updateChange = function(req, res) {
    var changeId = req.params.changeId;

    domainChange.getById(changeId, function(err, change) {
        if (err) {
            res.send(fail(codes.internalError));
        } else if (change == null) {
            res.send(fail(codes.notFound));
        } else {
            updateChangeHelper(change, req, res);
        }
    });
};

function createNewLesson(req, res) {
    var user = req.user;
    var field = checker.getLessonFieldsFromReq(req, checker.NEW_CHANGE_LESSON_CHECKER);
    if (!field) {
        return res.send(fail(codes.badRequest));
    } else if (field.groups[0] != user.groupId){
        logger.info("Allow access. Invalid group id: " + user.groupId);
        return res.send(fail(codes.badRequest));
    } else if (field.durationFrom >= field.durationTo) {
        logger.info("Invalid duration");
        return res.send(fail(codes.badRequest));
    }

    var teacherId = field.teacherId;
    domainUser.getTeacher(teacherId, function(err, teacher){
        if (err) {
            res.send(fail(codes.internalError));
        } else if (!teacher) {
            logger.info("User with " + teacherId + " not cteacher");
            res.send(fail(codes.badRequest));
        } else {
            //todo add check: is free (for group)
            field.teacherName = teacher.name;
            forceCreateNewLesson(field, req, res);
        }
    });
}

function forceCreateNewLesson(lesson, req, res) {
    domainChange.save(lesson, request.fromMongo(res));
}

function changeLesson(lesson, req, res) {
    var field = checker.getLessonFieldsFromReq(req, checker.CHANGE_LESSON_CHECKER);
    if (!field) {
        return res.send(fail(codes.badRequest));
    } else if (field.durationFrom >= field.durationTo) {
        logger.info("Invalid duration");
        return res.send(fail(codes.badRequest));
    }

    field.durationFrom = moment(field.durationFrom).toDate();
    field.durationTo = moment(field.durationTo).toDate();

    domainChange.getChangeByLesson(lesson._id, field.durationFrom, field.durationTo, function(err, change) {
        if (err) {
            logger.error(err);
            res.send(fail(codes.badRequest));
        } else if (change) {
            res.send(fail(codes.changeAlreadyExists))
        } else {
            field.lessonId = lesson._id;
            forceCreateNewLesson(field, req, res);
        }
    });
}

function updateChangeHelper(oldChange, req, res) {
    var body = req.body;
    var fields = checker.getLessonFieldsFromReq(req, checker.UPDATE_CHANGE_CHECKER);
    if (!fields) {
        return res.send(fail(codes.badRequest));
    } else if (oldChange.status == domainChange.S_TYPE_DELETED) {
        return res.send(fail(codes.notFound));
    } else if (fields.status == domainChange.S_TYPE_CANCELED && !oldChange.lessonId) {
        logger.info("Use delete change method  " + oldChange._id);
        return res.send(fail(codes.badRequest));
    }

    domainChange.update(oldChange._id, fields, request.fromMongo(res));
}
