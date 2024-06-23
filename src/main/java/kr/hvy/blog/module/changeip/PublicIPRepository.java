package kr.hvy.blog.module.changeip;

import kr.hvy.blog.module.changeip.dto.PublicIP;
import org.springframework.data.repository.CrudRepository;


public interface PublicIPRepository extends CrudRepository<PublicIP, String> {

}
