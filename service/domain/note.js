var mongoose = require('../libs/mongoose.js');
var Schema = mongoose.Schema;
var ObjectId = mongoose.Schema.Types.ObjectId;
var domainLesson = require('./lesson');
var domainChange = require('./change');
var moment = require('moment');

var CONST_NOTE_LIMIT = 300;
var DEFAULT_BEFORE_DAY = 90;

var NoteSchema = new Schema({
    'lessonId': ObjectId,
    'changeId': ObjectId,
    'date': {type: Date, required: true},
    'dateCreate': {type: Date, default: Date.now},
    'text': String,
    'ownerId': String,
    'ownerType': String,
    'ownerExtraData': String
}).index({lessonId: 1, changeId: 1}, {unique: false});

NoteSchema.method('toClient', function () {
    var obj = this.toObject();
    delete obj['__v'];
    return obj;
});

var Note = mongoose.model('note', NoteSchema);

exports.save = function (note, done) {
    Note(note).save(done);
};

// params = {fromDate, lastNoteId, teacherId, groupId}
exports.getNotes = function(params, cb) {
    var id = (params.teacherId || params.groupId);
    var getLessonIds = (params.teacherId && domainLesson.getLessonIdsByTeacher) || domainLesson.getLessonIdsByGroup;
    var getChangeIds = (params.teacherId && domainChange.getChangeIdsByTeacherAndLessonIds) || domainChange.getChangesIdsByGroupAndLessonIds;
    var fromDate = params.fromDate || moment().add(-DEFAULT_BEFORE_DAY, 'days').toDate();

    getLessonIds(id, function(err, lessonIds){
        if (err) {
            cb(err);
        } else {
            getChangeIds(id, fromDate, lessonIds, function(err, changeIds){
                if (err) {
                    cb(err);
                } else {
                    var data = {
                        lessonIds: lessonIds,
                        changeIds: changeIds,
                        fromDate: fromDate,
                        lastNoteId: params.lastNoteId
                    };
                    exports.getNotesByLessonsAndChanges(data, cb, "-__v");
                }
            });
        }
    });
};

exports.getNotesByLessonsAndChanges = function(params, cb, field) {
    var query = {'$and': [
        {'$or': [
            {lessonId: {'$in': params.lessonIds || []}},
            {changeId: {'$in': params.changeIds || []}}
        ]}
    ]};

    (params.lastNoteId) && query.$and.push({_id: {'$gt': params.lastNoteId}});
    (params.fromDate) && query.$and.push({date: {'$gt': params.fromDate}});

    return Note
        .find(query, field)
        .sort({'_id': -1})
        .limit(CONST_NOTE_LIMIT)
        .exec(cb);
};

exports.Note = Note;