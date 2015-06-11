var mongoose = require('../libs/mongoose.js');
var Schema = mongoose.Schema;

var GroupSchema = new Schema({
    'title': String,
    'code': String,
    'faculty': String,
    'speciality': String,
    'course': Number,
    'group': Number,
    'educationForm': String
});

GroupSchema.method('toClient', function () {
    var obj = this.toObject();
    delete obj['__v'];
    return obj;
});

var Group = mongoose.model('group', GroupSchema);

exports.findByQuery = function(query, cb) {
    Group.find(query).exec(cb);
};

exports.save = function (group, done) {
    Group(group).save(done);
};

exports.getAll = function(cb) {
    return Group.find({}, "-__v").exec(cb);
};

exports.Group = Group;