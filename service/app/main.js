var compression = require('compression');
var express = require('express');
var cookieParser = require('cookie-parser');
var session = require('express-session');
var MongoStore = require('connect-mongo')(session);
var bodyParser = require('body-parser');
var passport = require('passport');
var uuid = require('node-uuid');
var mongoose = require('../libs/mongoose.js');
var CORS = require('./CORS.js');

var app = module.exports = express();

var sessionKey = 'scheduleSecret';
var mongoStore = new MongoStore({mongooseConnection: mongoose.connection});


app.use(compression());
app.use(CORS.allowCrossDomain);
app.use(cookieParser());
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

var sessionStore = session({
    genid: function (req) {
        return uuid.v4();
    },
    secret: sessionKey,
    store: mongoStore,
    ttl: 30 * 24 * 60 * 60,
    resave: true,
    saveUninitialized: true
});

app.use(sessionStore);

app.use(passport.initialize());
app.use(passport.session());