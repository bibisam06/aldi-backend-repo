package com.bibisam06.aldi.common.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistTokenRepository  extends CrudRepository<BlacklistToken, String> {
}
