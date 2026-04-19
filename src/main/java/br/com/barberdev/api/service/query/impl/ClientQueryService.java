package br.com.barberdev.api.service.query.impl;

import br.com.barberdev.api.entity.ClientEntity;
import br.com.barberdev.api.exception.EmailInUseException;
import br.com.barberdev.api.exception.NotFoundException;
import br.com.barberdev.api.exception.PhoneInUseException;
import br.com.barberdev.api.repository.IClientRepository;
import br.com.barberdev.api.service.query.IClientQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientQueryService implements IClientQueryService {

    private final IClientRepository repository;

    @Override
    public ClientEntity findById(final long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Não foi encontrado o cliente com id " + id)
        );
    }

    @Override
    public List<ClientEntity> list() {
        return repository.findAll();
    }

    // ── Verificações para CREATE (sem ID) ──────────────────────────────────

    @Override
    public void verifyEmailAvailable(final String email) {
        if (repository.existsByEmail(email)) {
            throw new EmailInUseException(email);
        }
    }

    @Override
    public void verifyPhoneAvailable(final String phone) {
        if (repository.existsByPhone(phone)) {
            throw new PhoneInUseException(phone);
        }
    }

    // ── Verificações para UPDATE (ignora o próprio registro) ───────────────

    /**
     * Verifica se o e-mail está disponível para UPDATE.
     * Usa 'existsByEmailAndIdNot' para não conflitar com o próprio registro.
     * CORREÇÃO do bug do projeto base que comparava 'phone' em vez de 'email'.
     */
    @Override
    public void verifyEmailAvailable(final long id, final String email) {
        if (repository.existsByEmailAndIdNot(email, id)) {
            throw new EmailInUseException(email);
        }
    }

    @Override
    public void verifyPhoneAvailable(final long id, final String phone) {
        if (repository.existsByPhoneAndIdNot(phone, id)) {
            throw new PhoneInUseException(phone);
        }
    }
}
