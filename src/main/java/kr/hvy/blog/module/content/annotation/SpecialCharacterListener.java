package kr.hvy.blog.module.content.annotation;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpecialCharacterListener {
	@PostLoad
	void postLoad(Object object) throws IllegalArgumentException, IllegalAccessException {
		// 이런 식으로 Object 를 파라미터로 지정한 경우 모든 엔티티가 적용됩니다.
		// https://gs.saro.me/dev?tn=515

		for (Field field : object.getClass().getDeclaredFields()) {
			
			// 모든 스트링 클래스에 인젝션 처리를 한다
			if(field.getType() == String.class && !Modifier.toString(field.getModifiers()).contains("final")) {
				field.setAccessible(true);
				String testCase = (String) field.get(object);
				if (testCase != null) {
					field.set(object, removeSomeTagCharacter(testCase));
				}				
			}
			
		}
	}
	
	@PrePersist
	@PreUpdate
	void original(Object object) throws IllegalArgumentException, IllegalAccessException {

		for (Field field : object.getClass().getDeclaredFields()) {
			OriginalTextCharacter setHtmlTag = field.getAnnotation(OriginalTextCharacter.class);
			if (setHtmlTag != null) {
				field.setAccessible(true);
				String text = (String) field.get(object);
				if (text != null) {
					field.set(object, changeSpecialCharacter(text));
				}
			}
		}
	}
	
	public static String injectionCheck(String str) {

		if (str == null)
			return null;

		String escapeStr = str;
		escapeStr = escapeStr.replace("<", "&lt;");
		escapeStr = escapeStr.replace(">", "&gt;");
		escapeStr = escapeStr.replace("|", "&#124;");
		escapeStr = escapeStr.replace("$", "&#36;");
		escapeStr = escapeStr.replace("%", "&#37;");
		escapeStr = escapeStr.replace("'", "&#39;");
		escapeStr = escapeStr.replace("&quot;", "\"");
		escapeStr = escapeStr.replace("(", "&#40;");
		escapeStr = escapeStr.replace(")", "&#41;");
		
		return escapeStr;
	}
	
	public static String[] injectionCheck(String[] str) {

		if (str == null)
			return null;

		String escapeStr = str[0];
		escapeStr = escapeStr.replace("<", "&lt;");
		escapeStr = escapeStr.replace(">", "&gt;");
		escapeStr = escapeStr.replace("|", "&#124;");
		escapeStr = escapeStr.replace("$", "&#36;");
		escapeStr = escapeStr.replace("%", "&#37;");
		escapeStr = escapeStr.replace("'", "&#39;");
		escapeStr = escapeStr.replace("&quot;", "\"");
		escapeStr = escapeStr.replace("(", "&#40;");
		escapeStr = escapeStr.replace(")", "&#41;");
		
		String[] rtn = Arrays.copyOf(str,  str.length);
		rtn[0] = escapeStr;
		return rtn;
	}
	
	public static String changeSpecialCharacter(String str) {
		if (str == null)
			return null;
		String escapeStr = str;
		escapeStr = escapeStr.replace("&lt;", "＜");
		escapeStr = escapeStr.replace("&gt;", "＞");
		escapeStr = escapeStr.replace("&#124;", "|");
		escapeStr = escapeStr.replace("&#36;", "$");
		escapeStr = escapeStr.replace("&#37;", "%");
		escapeStr = escapeStr.replace("&amp;", "&");
		escapeStr = escapeStr.replace("&#39;", "'");
		escapeStr = escapeStr.replace("&quot;", "\"");
		escapeStr = escapeStr.replace("&#40;", "(");
		escapeStr = escapeStr.replace("&#41;", ")");
		return escapeStr;
	}
	
	public static String removeSomeTagCharacter(String str) {
		if (str == null)
			return null;
		String escapeStr = str;
		escapeStr = escapeStr.replace("&lt;", "<");
		escapeStr = escapeStr.replace("&gt;", ">");
		escapeStr = escapeStr.replace("&#124;", "|");
		escapeStr = escapeStr.replace("&#36;", "$");
		escapeStr = escapeStr.replace("&#37;", "%");
		escapeStr = escapeStr.replace("&amp;", "&");
		escapeStr = escapeStr.replace("&#39;", "'");
		escapeStr = escapeStr.replace("&quot;", "\"");
		escapeStr = escapeStr.replace("&#40;", "(");
		escapeStr = escapeStr.replace("&#41;", ")");
		
		escapeStr = HtmlSanitizer.sanitize(escapeStr);
		
		return escapeStr;
	}

}
