var mongoose = require('../libs/mongoose.js');
var Schema = mongoose.Schema;
var ObjectId = mongoose.Schema.Types.ObjectId;

var L_TYPE_LECTURE = exports.L_TYPE_LECTURE = 'lecture';
var L_TYPE_PRACTICE = exports.L_TYPE_PRACTICE = 'practice';

var W_TYPE_BLUE = exports.W_TYPE_BLUE = 'red';
var W_TYPE_RED = exports.W_TYPE_RED = 'blue';
var W_TYPE_BOTH = exports.W_TYPE_BOTH = 'both';

var LessonSchema = new Schema({
    'title': String,
    'hull': String,
    'auditory': String,
    'teacherId': ObjectId,
    'teacherName': String,
    'dayOfWeek': Number,
    'time': Number,
    'week': String,
    'groups': [String],
    'type': String
});

LessonSchema.method('toClient', function () {
    var obj = this.toObject();
    delete obj['__v'];
    return obj;
});

var Lesson = mongoose.model('lesson', LessonSchema);

exports.save = function (lesson, done) {
    Lesson(lesson).save(done);
};

exports.getLessonIdsByGroup = function(groupId, cb) {
  exports.getByGroup(groupId, function(err, groups) {
      if (err) {
          cb(err);
      } else {
          var ids = groupsToIds(groups);
            cb(null, ids);
      }
  }, '_id');
};

exports.getLessonIdsByTeacher = function(teacherId, cb) {
    exports.getByTeacher(teacherId, function(err, groups) {
        if (err) {
            cb(err);
        } else {
            var ids = groupsToIds(groups);
            cb(null, ids);
        }
    }, '_id');
};

exports.getAll = function(cb) {
  return Lesson.find({}).exec(cb);
};

exports.deleteById = function(lessonId, cb) {
    return Lesson.find({_id: lessonId}).remove().exec(cb);
};

exports.getById = function(lessonId, cb) {
    return Lesson.findById(lessonId, cb);
};

exports.getByGroup = function(groupId, cb, field) {
    return Lesson.find({'groups': groupId}, field).exec(cb);
};

exports.getByTeacher = function(teacherId, cb, field) {
    return Lesson.find({'teacherId': teacherId}, field).exec(cb);
};

function groupsToIds(groups) {
    var ids = [];
    for (var i = 0, l = groups.length; i < l; i++) {
        var group = groups[i];
        group._id && ids.push(group._id);
    }
    return ids;
}

exports.Lesson = Lesson;