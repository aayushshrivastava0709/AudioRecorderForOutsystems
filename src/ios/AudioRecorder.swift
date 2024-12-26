import AVFoundation
import Cordova

@objc(AudioRecorder) class AudioRecorder: CDVPlugin {
    var audioRecorder: AVAudioRecorder?
    var outputFilePath: String?

    @objc(startRecording:)
    func startRecording(command: CDVInvokedUrlCommand) {
        let callbackId = command.callbackId

        let fileName = "recording.m4a"
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        outputFilePath = paths[0].appendingPathComponent(fileName).path

        let settings = [
            AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
            AVSampleRateKey: 12000,
            AVNumberOfChannelsKey: 1,
            AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
        ]

        do {
            audioRecorder = try AVAudioRecorder(url: URL(fileURLWithPath: outputFilePath!), settings: settings)
            audioRecorder?.record()
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Recording started")
            self.commandDelegate.send(pluginResult, callbackId: callbackId)
        } catch {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Failed to start recording: \(error.localizedDescription)")
            self.commandDelegate.send(pluginResult, callbackId: callbackId)
        }
    }

    @objc(stopRecording:)
    func stopRecording(command: CDVInvokedUrlCommand) {
        let callbackId = command.callbackId

        if let recorder = audioRecorder, recorder.isRecording {
            recorder.stop()
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Recording saved at \(outputFilePath!)")
            self.commandDelegate.send(pluginResult, callbackId: callbackId)
        } else {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "No recording in progress")
            self.commandDelegate.send(pluginResult, callbackId: callbackId)
        }
    }
}
