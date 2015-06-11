var initLessons = require('../config/data/lesson.json');
var domainLesson = require('../domain/lesson');
var domainGroup = require('../domain/group');
var domainUser = require('../domain/user');
var shortid = require('shortid');
var logger = require('../libs/logger');
var Q = require('q');


var newLessons = {};
var teachers = {};
var groups = {};

function initTeachersAndGroups(next) {
    mapTeachers(function(err){
        if (err) {
            next(err);
        } else {
            mapGroups(function(err){
                next(err);
            });
        }
    });
}

exports.init = function(next){
    initTeachersAndGroups(function(err){
        if (err) {
            next(err);
        } else {
            initLessons.forEach(createLesson);
            saveLesson(next);
        }
    });
};

function saveLesson(next) {
    var all = [];
    for (var key in newLessons) {
        if (!newLessons.hasOwnProperty(key)) continue;
        var lesson = newLessons[key];
        if (lesson.groups.length > 1) { //todo FIX me
            lesson.type = domainLesson.L_TYPE_LECTURE;
        } else {
            lesson.type = domainLesson.L_TYPE_PRACTICE
        }
        all.push(lesson);
        domainLesson.save(lesson);
    }
    next(null, all);
}

function createLessonObj(lesson, teacherName, teacherId) {
    return {
        title: lesson.title,
        hull: lesson.hull,
        auditory: lesson.auditory,
        dayOfWeek: lesson.dayOfWeek,
        time: lesson.time,
        week: lesson.week,
        type: lesson.type,
        groups: [],
        teacherId: teacherId,
        teacherName: teacherName
    };
}

function createTeacher(teacherName) {
    return Q.Promise(function(resolve, reject) {
        teacherName = teacherName.trim();
        var user = {
            username: teacherName.toLowerCase(),
            name: teacherName,
            type: domainUser.U_TYPE_TEACHER,
            password: shortid.generate()
        };

        domainUser.save(user, function(err, res){
            if (err) {
                reject(err);
            } else {
                console.log(user.username + ":" + user.password);
                resolve(res);
            }
        });
    });
}

function getGroupIdByLesson(lesson) {
    return groups[getGroupKey(lesson)];
}

function getTeacherIdByName(name) {
    return teachers[name];
}

function mapTeachers(next) {
    initTeachers(function(err){
        if (err) {
            return next(err);
        }
        domainUser.getAllTeachers(function(err, teachersFromDb){
            if (!err) {
                teachers = {};
                teachersFromDb.forEach(function(teach){
                    teachers[teach.name] = teach._id;
                });
            }
            next(err);
        });
    });
}

function getAllTeachersFromLesson(lessons) {
    var allTeachers = {};
    lessons.forEach(function(lesson){
        var teacher = lesson.teacher;
        if (teacher) {
            allTeachers[teacher] = teacher;
        }
    });

    var array = [];
    for (var key in allTeachers){
        if (allTeachers.hasOwnProperty(key)) {
            array.push(key);
        }
    }
    return array;
}

function initTeachers(next) {
    var teachers = getAllTeachersFromLesson(initLessons);
    var work = teachers.map(createTeacher);
    Q.all(work).then(function(res){
        next(null, res);
    }).fail(function(err){
        logger.error(err);
        next(err);
    });
}

function mapGroups(next) {
    domainGroup.getAll(function(err, groupsFromDb){
        if (!err) {
            groups = {};
            groupsFromDb.forEach(function(group){
                var key = getGroupKey(group);
                groups[key] = group._id;
            });
        }
        next(err);
    });
}

function getGroupKey(data) {
    return "" + (data.group || "") + (data.educationForm || "") + (data.course || "") +  (data.code || "");
}
function getLessonKey(data) {
    var teacher = data.teacherName || data.teacher || "";
    return (data.title + "") + (data.hull || "") + (data.auditory || "")
        + (data.time || "") + (data.week || "") + (data.time || "") + teacher;
}

function getLessonFromExists(data) {
    var key= getLessonKey(data);
    if (data.title == "ДЕНЬКУРСОВОГОПРОЕКТИРОВАНИЯ" || data.title == "ВОСПИТАТЕЛЬНЫЙ ЧАС" ) {
        return null;
    } else {
        return newLessons[key];
    }
}

function addLesson(lessonObj) {
    var key= getLessonKey(lessonObj);
    newLessons[key] = lessonObj;
}

function createLesson(lesson) {
    var groupId = getGroupIdByLesson(lesson);
    var teacherId = getTeacherIdByName(lesson.teacher);
    if (!groupId || !teacherId) {
        logger.error("Can't create lesson ", lesson, groupId, teacherId);
        return false;
    }

    var lessonObj = getLessonFromExists(lesson);
    if (!lessonObj) {
        lessonObj = createLessonObj(lesson, lesson.teacher, teacherId);
    }

    lessonObj.groups.push(groupId);
    addLesson(lessonObj);
    return true;
}