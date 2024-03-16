package kr.hvy.blog.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AuthorityNameConverter implements AttributeConverter<AuthorityName, String> {

  @Override
  public String convertToDatabaseColumn(AuthorityName authorityName) {
    if (authorityName == null) {
      return null;
    }
    return authorityName.getCode();
  }

  @Override
  public AuthorityName convertToEntityAttribute(String s) {
    return AuthorityName.valueOf(s);
  }
}
