package com.claudiowork.cloud.ibmwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.claudiowork.cloud.ibmwatson.model.Speech;
import com.claudiowork.cloud.ibmwatson.service.SpeechService;
import com.ibm.cloud.sdk.core.http.HttpMediaType;

@RestController
@RequestMapping("/speech")
public class SpeechController {
	@Autowired
	private SpeechService service;

	@GetMapping("/transcript")
	public String transcript(@RequestBody Speech speech) throws InterruptedException {
		System.out.println("Transcript Controller - Starting");
		// Speech speech = new Speech();
		// speech.setAudio("D:\\WORKSPACE\\CLOUD_IBM_WATSON\\SpeechToText\\src\\main\\resources\\audio-file.flac");
		// speech.setAudio("D:\\1 - Projetos\\cloud\\ibm\\SpeechToText\\texto-em-portugues.flac");
		if (null != speech) {
			switch (speech.getAudioType()) {
			case "wav":
				speech.setAudioType(HttpMediaType.AUDIO_WAV);
				break;
			case "flac":
				speech.setAudioType(HttpMediaType.AUDIO_FLAC);
				break;
			case "mp3":
				speech.setAudioType(HttpMediaType.AUDIO_MP3);
				break;
			default:
				speech.setAudioType(HttpMediaType.AUDIO_BASIC);
			}
			speech.setAudioType(HttpMediaType.AUDIO_FLAC);
			System.out.println("Transcript Controller - Before call transcript");
			return service.transcript(speech);
		} else {
			return "Informações devem ser enviadas corretamente no corpo da requisição";
		}
	}

}
