package com.claudiowork.cloud.ibmwatson.service;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.claudiowork.cloud.ibmwatson.model.Speech;
import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionAlternative;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
@Component
public abstract class IbmWatson {

	@Value("${ibm.apiKey}")
	private String apiKey;
	@Value("${ibm.url}")
	private String apiUrl;
	private String result;
	private IamAuthenticator authenticator;
	private SpeechToText speechToText;
	private HttpConfigOptions configOptions;
	private Response<SpeechRecognitionResults> speechResponseResults;
	private SpeechRecognitionResults speechResults;

	private Boolean connect() {
		try {
			System.out.println("Connect IbmWatson Service");
			authenticator = new IamAuthenticator(apiKey);
			speechToText = new SpeechToText(authenticator);
			speechToText.setServiceUrl(apiUrl);
			// disable ssl
			configOptions = new HttpConfigOptions.Builder().disableSslVerification(true).build();
			speechToText.configureClient(configOptions);
		} catch (Exception e) {
			System.out.println("Erro na instancia IBMW");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public Boolean execute(Speech speech) throws InterruptedException {
		Boolean ret = Boolean.FALSE;
		try {
			if (connect()) {
				System.out.println("Connected");
				System.out.println("Audio e type" + speech.getAudioSource() +">>>" +speech.getAudioType());
				RecognizeOptions recognizeOptions = new RecognizeOptions.Builder()
						.audio(new FileInputStream(speech.getAudioSource())).contentType(speech.getAudioType())
						//.model("en-US_BroadbandModel")
						.model(speech.getAudioLanguage())
						.build();
				System.out.println("Options ok");

				System.out.println("Vai executar");

				speechResponseResults = speechToText.recognize(recognizeOptions).execute();
				speechResults = speechResponseResults.getResult();
				String resultTemp = new String("");
				for (SpeechRecognitionResult srr: speechResults.getResults()) {
					for(SpeechRecognitionAlternative sra: srr.getAlternatives()) {
						if (null != sra.getConfidence() && sra.getConfidence() > 0.80)
							result = sra.getTranscript();
						else
							resultTemp =  sra.getTranscript();
					}
				}
				if (null == result || result.isEmpty())
					result = resultTemp;
				ret = Boolean.TRUE;	
		//		System.out.println("Results:"+speechResults);
		//		System.out.println("Result:"+result);
				System.out.println("End Service");
			}
		} catch (NotFoundException e) {
			System.out.println("Erro 1:" + e.getMessage());
			ret = Boolean.FALSE;
		} catch (RequestTooLargeException e) {
			System.out.println("Erro 2:" + e.getMessage());
			ret = Boolean.FALSE;
		} catch (ServiceResponseException e) {
			// Base class for all exceptions caused by error responses from the service
			System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
			ret = Boolean.FALSE;
		} catch (IOException e) {
			System.out.println("Erro 5:" + e.getMessage());
			ret = Boolean.FALSE;
		}
		return ret;
	}

	public IamAuthenticator getAuthenticator() {
		return authenticator;
	}

	public SpeechToText getSpeechToText() {
		return speechToText;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getResult() {
		return result;
	}

}
