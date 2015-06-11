var request = require('../libs/request');
var codes = require('../app/codes');
var logger = require('../libs/logger');
var domainLesson = require('../domain/lesson');
var domainUser = require('../domain/user');
var checker = require('../libs/checker');
var lessonInitializer = require('../service/lesson-initializer');

var done = request.done;
var fail = request.fail;

exports.getLessons = function(req, res) {
    var user = req.user;
    var id = user.type == domainUser.U_TYPE_TEACHER ? user._id : user.groupId;
    var getLessons = user.type == domainUser.U_TYPE_TEACHER
        ? domainLesson.getByTeacher
        : domainLesson.getByGroup;

    getLessons(id, request.fromMongo(res), "-__v");
};

exports.createLesson = function(req, res) {
    var user = req.user;
    var field = checker.getLessonFieldsFromReq(req, checker.NEW_LESSON_CHECKER);
    if (!field) {
        return res.send(fail(codes.badRequest));
    }

    var teacherId = field.teacherId;
    domainUser.getTeacher(teacherId, function(err, teacher){
        if (err) {
            res.send(fail(codes.internalError));
        } else if (!teacher) {
            logger.info("User with " + teacherId + " not teacher");
            res.send(fail(codes.badRequest));
        } else {
            field.teacherName = teacher.name;
            forceCreateNewLesson(field, req, res);
        }
    });
};

exports.init = function(req, res) {
    domainLesson.getAll(function(err, lessons) {
        if (err) {
            res.send(fail(codes.internalError));
        } else if (lessons.length > 0){
            res.send(fail(codes.alreadyInitializaed));
        } else {
            lessonInitializer.init(request.fromMongo(res));
        }
    });
};

exports.deleteLesson = function(req, res) {
    var lessonId = req.params.lessonId;
    domainLesson.deleteById(lessonId, function(err, data){
        res.send(err ? fail(codes.internalError) : done());
    });
};

function forceCreateNewLesson(lesson, req, res) {
    domainLesson.save(lesson, request.fromMongo(res));
}
