package com.claudiowork.cloud.ibmwatson.model;

public class Speech {

	
	private String audioSource;
	private String audioType;
	private String audioLanguage;
	private String transcript;
	public String getAudioSource() {
		return audioSource;
	}
	public void setAudioSource(String audioSource) {
		this.audioSource = audioSource;
	}
	public String getAudioType() {
		return audioType;
	}
	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}
	public String getAudioLanguage() {
		return audioLanguage;
	}
	public void setAudioLanguage(String audioLanguage) {
		this.audioLanguage = audioLanguage;
	}
	public String getTranscript() {
		return transcript;
	}
	public void setTranscript(String transcript) {
		this.transcript = transcript;
	}

	
}
