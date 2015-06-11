var mongoose = require('../libs/mongoose.js');
var bcrypt = require('bcrypt-nodejs');
var Schema = mongoose.Schema;

var U_TYPE_TEACHER = exports.U_TYPE_TEACHER = 'teacher';
var U_TYPE_CLASS_LEADER = exports.U_TYPE_CLASS_LEADER = 'classLeader';
var U_TYPE_STUDENT = exports.U_TYPE_STUDENT = 'student';
var U_TYPE_ADMIN = exports.U_TYPE_ADMIN = 'admin';

var UserSchema = new Schema({
    'name': String,
    'password': String,
    'salt': String,
    'username': {type: String, unique: true},
    'groupId': String,
    'type': String
});

UserSchema.method('toClient', function () {
    var obj = this.toObject();
    delete obj['password'];
    delete obj['__v'];
    return obj;
});

UserSchema.method('isTeacher', function () {
    return this.type == U_TYPE_TEACHER;
});


var User = mongoose.model('user', UserSchema);

exports.save = function (user, done) {
    var salt = bcrypt.genSaltSync(10);
    var password = bcrypt.hashSync(user.password, salt);
    var type = user.type || (groupId && U_TYPE_STUDENT) || U_TYPE_TEACHER;

    User({
        name: user.name,
        password: password,
        username: user.username.toLowerCase(),
        groupId: user.groupId,
        type: type
    }).save(done);
};

exports.getUserById = function(userId, cb) {
    User.findById(userId, cb);
};

exports.getAllClassLeaders = function(cb) {
    User.find({type: U_TYPE_CLASS_LEADER}).exec(cb);
};

exports.getTeacher = function(teacherId, cb) {
    User.findById(teacherId, function(err, user){
        cb(err, !err && user.isTeacher() && user);
    });
};

exports.groupToUser = function(groupId) {
    return {
        name: groupId,
        groupId: groupId,
        type: U_TYPE_STUDENT
    }
};

exports.getAllTeachers = function(cb) {
    return User.find({type: U_TYPE_TEACHER}, "-__v -password").exec(cb);
};

exports.findByUsername = function(username, cb) {
    User.findOne({username: username}, cb);
};

exports.comparePassword = function(password, hashed) {
    return bcrypt.compareSync(password, hashed);
};

exports.User = User;