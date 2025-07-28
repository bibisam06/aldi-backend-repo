package com.bibisam06.aldi.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class BlacklistTokenService {

    private final BlacklistTokenRepository blacklistRepo;

    public BlacklistTokenService(BlacklistTokenRepository blacklistRepo) {
        this.blacklistRepo = blacklistRepo;
    }

    public void blacklistToken(String token) {
        blacklistRepo.save(new BlacklistToken(token));
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistRepo.existsById(token);
    }
}
