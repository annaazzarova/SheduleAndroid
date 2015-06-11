var config = require('./config.js');
var logger = require('./libs/logger');
var app = require('./app/main');
var auth = require('./app/auth');

var rootRoute = require('./routes/root');
var userRoute = require('./routes/user');
var groupRoute = require('./routes/group');
var lessonRoute = require('./routes/lesson');
var changeRoute = require('./routes/change');
var noteRoute = require('./routes/note');


// routes

app.get('/ping', rootRoute.ping);

app.get('/me', userRoute.me);
app.post('/login', auth.login, userRoute.me);
app.post('/logout', auth.logout);

app.get('/lesson/', auth.requiresLogin, lessonRoute.getLessons);
app.get('/lesson/group/:groupId', groupRoute.requiresGroup, lessonRoute.getLessons);

app.get('/change/', auth.requiresLogin, changeRoute.getChanges);
app.get('/change/group/:groupId', groupRoute.requiresGroup, changeRoute.getChanges);

app.post('/change/', auth.requiresClassLeader, changeRoute.createChange);
app.post('/change/:lessonId', auth.requiresClassLeader, changeRoute.createChange);
app.put('/change/:changeId', auth.requiresClassLeader, changeRoute.updateChange);
app.delete('/change/:changeId', auth.requiresClassLeader, changeRoute.deleteChange);

app.get('/note', auth.requiresLogin, noteRoute.getNotes);
app.get('/note/group/:groupId', groupRoute.requiresGroup, noteRoute.getNotes);
app.post('/note/lesson/:lessonId', auth.requiresLogin, noteRoute.createNote);
app.post('/note/change/:changeId', auth.requiresLogin, noteRoute.createNote);

app.get('/group', groupRoute.getAll);
app.get('/teacher', userRoute.getAllTeachers);

app.all('/service/*', auth.requiresAdmin);
app.post('/service/register', userRoute.register);
app.get('/service/group/initialize', groupRoute.init);
app.get('/service/class-leader/initialize', groupRoute.initClassLeaders);
app.get('/service/lesson/initialize', lessonRoute.init);
app.post('/service/lesson', lessonRoute.createLesson);
app.delete('/service/lesson/:lessonId', lessonRoute.deleteLesson);


// run application

var server = app.listen(config.port, function () {
    var host = server.address().address;
    var port = server.address().port;
    logger.info("Server started http://%s:%d", host, port);
});
