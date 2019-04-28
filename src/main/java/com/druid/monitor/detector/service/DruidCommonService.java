package com.druid.monitor.detector.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;


@Service
public class DruidCommonService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DruidCommonService.class);
	
	private static final String URL_LOGIN="/submitLogin";
	
	private static final String URL_RESET="/reset-all.json";
	@Autowired
	private RestTemplate restTemplate;

	public String getLoginCookieToDruid(String prefixUrl, String username, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		param.add("loginUsername", username);
		param.add("loginPassword", password);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(param,
				headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(prefixUrl.concat(URL_LOGIN), HttpMethod.POST, requestEntity,
				String.class);
		String result = "";
		if ("success".equalsIgnoreCase(responseEntity.getBody())){
			result = responseEntity.getHeaders().get("Set-Cookie").toString();
			LOGGER.debug("login auth header Login={}", result);
			if (!StringUtils.isEmpty(result)){
				result = result.substring(result.indexOf("JSESSIONID"), result.indexOf(";"));
			}
			LOGGER.debug("login auth header Login={}", result);
		}
		return result;
	}
	
	public String resetDruid(String prefixUrl, String cookie) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Cookie", cookie);
		
		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(param,
				headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(prefixUrl.concat(URL_RESET), HttpMethod.POST, requestEntity,
				String.class);
		String result = responseEntity.getBody();
		return result;
	}
	
	public String druidService(String prefixUrl, String serviceUrl, String cookie) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Cookie", cookie);
		
		MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(param,
				headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(prefixUrl.concat(serviceUrl), HttpMethod.POST, requestEntity,
				String.class);
		String result = responseEntity.getBody();
		return result;
	}
	
	
//	public String loginToDruid2(String httpUrl, String username, String password) {
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("loginUsername", username);
//		param.put("loginPassword", password);
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		
//		HttpPost post = new HttpPost(httpUrl);
//		post.addHeader("Content-Type: ", "application/x-www-form-urlencoded; charset=UTF-8");
//		
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("loginUsername", username));
//		params.add(new BasicNameValuePair("loginPassword", password));
//		String result = "fail";
//		try {
//			HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//			post.setEntity(entity);
//			HttpResponse resultTemp = httpclient.execute(post, getResponseHandler());
//			if (resultTemp!=null && resultTemp.getEntity()!=null){
//				LOGGER.debug("loginToDruid-response={}", EntityUtils.toString(resultTemp.getEntity(), "UTF-8"));
//				LOGGER.debug("login auth header Login={}", resultTemp.getHeaders("Set-Cookie").toString());
//				LOGGER.debug("login auth header cookie={}", resultTemp.getHeaders("Cookie").toString());
//				result = resultTemp.getHeaders("Cookie").toString();
//			}
//		} catch (IOException e) {
//		}
//		return result;
//	} 
	

//    private static ResponseHandler<HttpResponse> getResponseHandler() {
//        return new ResponseHandler<HttpResponse>() {
//            @Override
//            public HttpResponse handleResponse(final HttpResponse response) throws IOException {
//            	return response;
//            }
//        };
//    }
	
}
