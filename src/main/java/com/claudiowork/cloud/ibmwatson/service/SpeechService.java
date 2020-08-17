package com.claudiowork.cloud.ibmwatson.service;

import org.springframework.stereotype.Service;

import com.claudiowork.cloud.ibmwatson.model.Speech;

@Service
public class SpeechService extends IbmWatson {

	public String transcript(Speech speech) throws InterruptedException {
		System.out.println("Speech service");
		if (execute(speech))
			return getResult();
		else
			return "não foi possivel realizar o transcript";
	}
}
