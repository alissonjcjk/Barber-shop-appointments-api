package br.com.barberdev.api.repository;

import br.com.barberdev.api.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClientRepository extends JpaRepository<ClientEntity, Long> {

    boolean existsByEmail(final String email);

    boolean existsByPhone(final String phone);

    Optional<ClientEntity> findByEmail(final String email);

    Optional<ClientEntity> findByPhone(final String phone);

    boolean existsByEmailAndIdNot(final String email, final Long id);

    boolean existsByPhoneAndIdNot(final String phone, final Long id);
}
