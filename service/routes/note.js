var request = require('../libs/request');
var codes = require('../app/codes');
var moment = require('moment');
var domainNote = require('../domain/note');
var domainUser = require('../domain/user');

var done = request.done;
var fail = request.fail;


exports.getNotes = function(req, res) {
    var user = req.user;
    var dateFrom = req.query.dateFrom && moment(req.query.dateFrom);

    var params = {
        dateFrom: dateFrom && dateFrom.toDate(),
        lastNoteId: req.query.lastNoteId
    };

    if (user.type == domainUser.U_TYPE_TEACHER) {
        params.teacherId = user._id;
    } else {
        params.groupId = user.groupId;
    }

    domainNote.getNotes(params, request.fromMongo(res));
};

exports.createNote = function(req, res) {
    var user = req.user;
    var text = req.body.text;

    var lessonId = req.params.lessonId;
    var changeId = req.params.changeId;

    var date = req.body.date && moment(req.body.date);
    if (!date || !date.isValid()) {
        date = null
    } else {
        date = date.toDate();
    }

    if (!text || text.length == 0 || (!lessonId && !changeId) || !date) {
        return res.send(fail(codes.badRequest));
    }

    var note = {
        lessonId: lessonId,
        changeId: changeId,
        date: date,
        text: text,
        ownerId: user._id,
        ownerType: user.type,
        ownerExtraData: user.type == domainUser.U_TYPE_TEACHER ? user.name : user.groupId
    };

    domainNote.save(note, request.fromMongo(res))
};
