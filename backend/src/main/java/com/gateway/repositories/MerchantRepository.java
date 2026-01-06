package com.gateway.repositories;

import com.gateway.models.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, String> {
    // Custom method to find a merchant by their API Key
    Optional<Merchant> findByApiKey(String apiKey);
}