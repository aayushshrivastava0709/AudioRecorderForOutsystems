var exec = require('cordova/exec');

var AudioRecorder = {
    start: function(success, error) {
        exec(success, error, "AudioRecorder", "startRecording", []);
    },
    stop: function(success, error) {
        exec(success, error, "AudioRecorder", "stopRecording", []);
    },
    play: function(filePath, success, error) {
        exec(success, error, "AudioRecorder", "playAudio", [filePath]);
    }
};

module.exports = AudioRecorder;
