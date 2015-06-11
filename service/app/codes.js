var codes = {
    badRequest: {code: 100, message: "Bad request"},
    userNotFound: {code: 101, message: "User not found"},
    wrongCredentials: {code: 102, message: "Wrong credentials"},
    notAllowed: {code:103, message: "Not allowed"},
    internalError: {code:104, message: "Internal error"},
    invalidGroupId: {code: 105, message: "Invalid group id"},
    notFound: {code: 106, message: "Element with id not found"},
    changeAlreadyExists: {code: 107, message: "Change already exists"},
    alreadyInitializaed: {code: 108, message: "Already initialized"}
};

module.exports = codes;
