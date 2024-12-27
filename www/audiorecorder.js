var exec = require('cordova/exec');

var AudioRecorder = {
    startRecording: function(success, error) {
        exec(success, error, 'AudioRecorder', 'startRecording', []);
    },
    stopRecording: function(success, error) {
        exec(success, error, 'AudioRecorder', 'stopRecording', []);
    },
    getFile: function(success, error) {
        exec(success, error, 'AudioRecorder', 'getFile', []);
    }
};

module.exports = AudioRecorder;
