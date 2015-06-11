var moment = require('moment');
var logger = require('../libs/logger');
var domainChange = require('../domain/change');
var domainLesson = require('../domain/lesson');

var NEW_LESSON_CHECKER = exports.NEW_LESSON_CHECKER = {
    title: {checker: notEmpty, required: false},
    hull: {checker: isHull, required: false},
    teacherId: {checker: alwaysTrue, required: true},
    groups: {checker: isValidGroupsIds, required: true},
    auditory: {checker: isAuditory, required: false},
    dayOfWeek: {checker: isDayOfWeek, required: true},
    time: {checker: isValidLessonTime, required: true},
    week: {checker: isValidWeekType, required: true},
    type: {checker: isValidType, required: false}
};

var NEW_CHANGE_LESSON_CHECKER = exports.NEW_CHANGE_LESSON_CHECKER = {
    title: {checker: notEmpty, required: false},
    hull: {checker: isHull, required: false},
    teacherId: {checker: alwaysTrue, required: true},
    groups: {checker: isSingleGroup, required: true},
    auditory: {checker: isAuditory, required: false},
    dayOfWeek: {checker: isDayOfWeek, required: true},
    time: {checker: isValidLessonTime, required: true},
    week: {checker: isValidWeekType, required: true},
    type: {checker: isValidType, required: true},
    durationFrom: {checker: isDate, required: true},
    durationTo: {checker: isDate, required: true},
    status: {checker: isValidStatus, required: true}
};


var UPDATE_CHANGE_CHECKER = exports.UPDATE_CHANGE_CHECKER = {
    title: {checker: notEmpty, required: false},
    hull: {checker: isHull, required: false},
    teacherId: {checker: alwaysFalse, required: false},
    groups: {checker: alwaysFalse, required: false},
    auditory: {checker: isAuditory, required: false},
    dayOfWeek: {checker: alwaysFalse, required: false},
    time: {checker: alwaysFalse, required: false},
    week: {checker: alwaysFalse, required: false},
    type: {checker: isValidType, required: false},
    durationFrom: {checker: alwaysFalse, required: false},
    durationTo: {checker: alwaysFalse, required: false},
    status: {checker: isValidStatus,  required: false}
};

var CHANGE_LESSON_CHECKER = exports.CHANGE_LESSON_CHECKER = {
    title: {checker: notEmpty, required: false},
    hull: {checker: isHull, required: false},
    teacherId: {checker: alwaysFalse, required: false},
    groups: {checker: alwaysFalse, required: false},
    auditory: {checker: isAuditory, required: false},
    dayOfWeek: {checker: alwaysFalse, required: false},
    time: {checker: isValidLessonTime, required: false},
    week: {checker: alwaysFalse, required: false},
    type: {checker: isValidType, required: false},
    durationFrom: {checker: isDate, required: true},
    durationTo: {checker: isDate, required: true},
    status: {checker: isValidStatus,  required: false}
};

function alwaysFalse() {
    return false;
}

function alwaysTrue() {
    return true;
}

function isValidGroupsIds(groups) {
    return (Array.isArray(groups) && groups.length > 0);
}

function isSingleGroup(groups) {
    return (Array.isArray(groups) && groups.length == 1);
}

function notEmpty(data) {
    return data && data.length > 0;
}

function isValidLessonTime(time) {
    return time >= 1 && time <= 7;
}

function isValidWeekType(week) {
    return week && (
        week == domainLesson.W_TYPE_BLUE ||
        week == domainLesson.W_TYPE_RED ||
        week == domainLesson.W_TYPE_BOTH)
}

function isHull(hull) {
    var hulls = ["I", "II", "III", "IV", "V"];
    return hulls.indexOf(hull) > -1;
}

function isAuditory(auditory) {
    return auditory && auditory.length > 0 && auditory.length <= 4;
}

function isDayOfWeek(day) {
    return day >= 0 && day <= 6;
}

function isDate(time) {
    var d = moment(time);
    return (d != null || d.isValid());
}

function isValidStatus(status) {
    return (status == domainChange.S_TYPE_NORMAL || domainChange.S_TYPE_CANCELED == status);
}

function isValidType(type) {
    return type && (domainLesson.L_TYPE_LECTURE == type || domainLesson.L_TYPE_PRACTICE == type);
}

exports.getLessonFieldsFromReq = function(req, fieldChecker) {
    var body = req.body;
    var fields = {};

    for (var key in fieldChecker) {
        if (!fieldChecker.hasOwnProperty(key)) continue;
        var checkerData = fieldChecker[key];
        var value = body[key];

        if (value == undefined && checkerData.required) {
            logger.info("Required field " + key + " :: " + value);
            fields = null;
            break;
        }

        if (value != undefined) {
            if (checkerData.checker(value)) {
                fields[key] = value;
            } else {
                logger.info("Invalid field " + key + " :: " + value);
                fields = null;
                break;
            }
        }
    }
    return fields;
};