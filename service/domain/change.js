var mongoose = require('../libs/mongoose.js');
var Schema = mongoose.Schema;
var ObjectId = mongoose.Schema.Types.ObjectId;
var lessonDomain = require('./lesson');
var moment = require('moment');

var DEFAULT_BEFORE_DAY = exports.DEFAULT_BEFORE_DAY = 90;

var S_TYPE_CANCELED = exports.S_TYPE_CANCELED = 'canceled';
var S_TYPE_NORMAL = exports.S_TYPE_NORMAL = 'normal';
var S_TYPE_DELETED = exports.S_TYPE_DELETED = 'deleted';

var ChangeSchema = new Schema({
    'title': String,
    'hull': String,
    'auditory': String,
    'teacherId': ObjectId,
    'teacherName': String,
    'dayOfWeek': Number,
    'groups': [String],
    'time': Number,
    'week': String,
    'type': String,
    'lessonId': ObjectId,
    'durationFrom': Date,
    'durationTo': Date,
    'status': String
});

ChangeSchema.method('toClient', function () {
    var obj = this.toObject();
    delete obj['__v'];
    return obj;
});

ChangeSchema.pre('save', function (next) {
    if (this.isNew && 0 === this.groups.length) {
        this.groups = undefined;
    }
    next();
});

var Change = mongoose.model('change', ChangeSchema);

exports.save = function (change, done) {
    Change(change).save(done);
};

exports.getById = function(changeId, cb) {
    Change.findById(changeId, cb);
};

exports.getChanges = function(params, cb, field) {
    var getLessonIds = (params.groupId && lessonDomain.getLessonIdsByGroup) || lessonDomain.getLessonIdsByTeacher;
    var id = params.groupId || params.teacherId;
    getLessonIds(id, function(err, lessons){
       if (err) {
           cb(err);
       } else {
           exports.getChangesByLessonIds(params, lessons, cb, field);
       }
    });
};

exports.getChangeByLesson = function(lessonId, from, to, cb) {
    var query = {$and: [
        {lessonId: lessonId},
        {durationTo: {'$gt': from}},
        {durationFrom: {'$lt': to}},
        {status: {'$ne': S_TYPE_DELETED}}
    ]};
    Change.findOne(query, cb);
};

exports.deleteById = function(changeId, cb) {
    var findQuery = {"_id": changeId};
    var setQuery = { $set: {"status" : S_TYPE_DELETED}};
    Change.update(findQuery, setQuery).exec(cb);
};

exports.update = function(changeId, fields, cb) {
    return Change.update({_id: changeId}, fields).exec(cb);
};

exports.getChangesByLessonIds = function(params, lessonIds, cb, field) {
    var userQuery = (params.groupId && {'groups': params.groupId}) || {'teacherId': params.teacherId};
    var query = {'$and': [
        {'$or': [
            userQuery,
            {lessonId: {'$in': lessonIds}}
        ]},
        {durationTo: {'$gt': params.durationFrom || moment().add(-DEFAULT_BEFORE_DAY, 'days').toDate()}},
        {durationFrom: {'$lt': params.durationTo || moment().add(DEFAULT_BEFORE_DAY, 'days').toDate()}}]};

    if (!params.withDeleted) {
        query.$and.push({status: {'$ne': S_TYPE_DELETED}});
    }

    return Change.find(query, field).sort({'_id': -1}).exec(cb);
};

exports.getChangeIdsByGroup = function(groupId, fromDate, cb) {
    var params = {
        groupId: groupId,
        durationFrom: fromDate,
        withDeleted: true
    };
    getChangesIds(params, cb);
};

exports.getChangeIdsByTeacher = function(teacherId, fromDate, cb) {
    var params = {
        teacherId: teacherId,
        durationFrom: fromDate,
        withDeleted: true
    };
    getChangesIds(params, cb);
};

exports.getChangeIdsByTeacherAndLessonIds = function(teacherId, fromDate, lessonIds, cb) {
    var params = {
        teacherId: teacherId,
        durationFrom: fromDate,
        withDeleted: true

    };
    getChangeIdsByLessonIds(params, lessonIds, cb);
};

exports.getChangesIdsByGroupAndLessonIds = function(groupId, fromDate, lessonIds, cb) {
    var params = {
        groupId: groupId,
        durationFrom: fromDate,
        withDeleted: true
    };
    getChangeIdsByLessonIds(params, lessonIds, cb);
};

function getChangesIds(params, cb) {
    exports.getChanges(params, function(err, changes){
        if (err) {
            cb(err);
        } else {
            var ids = changesToIds(changes);
            cb(null, ids);
        }
    }, _id);
}

function getChangeIdsByLessonIds(params, lessonIds, cb) {
    exports.getChangesByLessonIds(params, lessonIds, function(err, changes) {
        if (err) {
            cb(err);
        } else {
            var ids = changesToIds(changes);
            cb(null, ids);
        }
    }, '_id');
}

function changesToIds(changes) {
    var ids = [];
    for (var i = 0, l = changes.length; i < l; i++) {
        var change = changes[i];
        change._id && ids.push(change._id);
    }
    return ids;
}

exports.Change = Change;